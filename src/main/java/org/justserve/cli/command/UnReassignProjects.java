package org.justserve.cli.command;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.justserve.cli.util.JustServeEmailParserError;
import org.justserve.client.ProjectClient;
import org.justserve.model.GetProjectRequest;
import org.justserve.model.Project;
import org.justserve.model.ReassignProjectRequest;
import org.justserve.util.EmailParser;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

import static org.justserve.cli.util.JustServePrinter.printError;
import static org.justserve.cli.util.JustServePrinter.printNormal;

@Slf4j
@Command(name = "unReassignProjects", description = "Reassigns projects from an email file to a user", mixinStandardHelpOptions = true)
public class UnReassignProjects extends BaseCommand implements Runnable {

    @Option(names = {"--user", "-u"}, description = "the user who will be assigned the projects", required = true)
    private UUID userID;

    @Option(names = {"--file", "-f"}, description = "the .eml file containing the projects to reassign", required = true)
    private File emlFile;

    @Inject
    Provider<ProjectClient> projectClientProvider;

    @Override
    public void run() {
        if (isTokenInvalid()) {
            return;
        }

        if (!emlFile.exists() || !emlFile.isFile()) {
            printError("The provided file does not exist or is not a file: " + emlFile.getAbsolutePath());
            return;
        }

        String emlContent;
        try {
            emlContent = Files.readString(emlFile.toPath());
        } catch (IOException e) {
            printError("Failed to read file: " + emlFile.getAbsolutePath());
            log.atError().setCause(e).log("Error reading file");
            return;
        }

        Map<String, UUID> projects;
        try {
            projects = EmailParser.getProjects(emlContent);
        } catch (MessagingException | IOException | JustServeEmailParserError e) {
            printError("Failed to parse email file: " + e.getMessage());
            log.atError().setCause(e).log("Error parsing email file");
            return;
        }

        if (projects.isEmpty()) {
            printNormal("No projects found in the email file.");
            return;
        }


        ProjectClient client = projectClientProvider.get();
        int successCount = 0;

        for (Map.Entry<String, UUID> entry : projects.entrySet()) {
            String projectName = entry.getKey();
            UUID projectId = entry.getValue();
            GetProjectRequest getProjectRequest = new GetProjectRequest();
            Project project;
            try {
                project = client.getProject(projectId, " ", getProjectRequest).body();
            } catch (HttpClientResponseException | NullPointerException e) {
                printError("Failed to get project " + projectName + " (" + projectId + ")");
                log.atError().setCause(e).log("Error getting project");
                continue;
            }
            if (null == project.getProjectOwnerUserId()) {
                warning(String.format("Project %s (%s) has no owner", projectName, projectId));
                log.warn("Project {} ({}) has no owner", projectName, projectId);
                continue;
            }
            if (project.getProjectOwnerUserId().equals(userID)) {
                log.warn("Project {} ({}) is already assigned to user {}", projectName, projectId, userID);
                continue;
            }

            try {
                ReassignProjectRequest reassignProjectRequest = new ReassignProjectRequest(userID, project.getProjectOwnerUserId());
                log.atTrace().log("Reassigning project {} ({}) to user {}", projectName, projectId, userID);
                HttpResponse<Object> reassignResponse = client.reassignProject(projectId, reassignProjectRequest);
                if (reassignResponse.status() == HttpStatus.OK) {
                    printNormal("Successfully reassigned project %s (%s) to user %s", projectName, projectId, userID);
                    log.atTrace().log("received api response status: {}", reassignResponse.status());
                    successCount++;
                    continue;
                }
                printError("Failed to reassign project " + projectName + " (" + projectId + ") to user " + userID +
                        ". Expected HTTP Status 'OK', but got " + reassignResponse.status());
                log.atError().log("Failed to reassign project {} ({}) to user {}. Expected HTTP Status 'OK', but got {}",
                        projectName, projectId, userID, reassignResponse.status());
            } catch (HttpClientResponseException e) {
                printError("Failed to reassign project " + projectName + " (" + projectId + ") to user " + userID);
                log.atError().setCause(e).log("Error response from API: {}", e.getResponse().body());
            }
        }
        printNormal("Successfully reassigned %d projects to user %s", successCount, userID);
        log.atTrace().log("Finished reassigning projects to user {}", userID);
    }
}
