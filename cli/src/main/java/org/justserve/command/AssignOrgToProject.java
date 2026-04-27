package org.justserve.command;

import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import org.justserve.client.ProjectClient;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.UUID;

import static org.justserve.util.JustServePrinter.printError;
import static org.justserve.util.JustServePrinter.printNormal;


@Slf4j
@Command(name = "assignOrgToProject", description = "Assigns an organization to a project", mixinStandardHelpOptions = true)
public class AssignOrgToProject extends BaseCommand implements Runnable {

    @Option(names = {"--project", "-p"}, description = "the project ID", required = true)
    private UUID projectId;

    @Option(names = {"--org", "-o"}, description = "the organization ID", required = true)
    private UUID orgId;

    @Inject
    Provider<ProjectClient> projectClientProvider;

    @Override
    public void run() {
        if (isTokenInvalid()) {
            return;
        }

        ProjectClient client = projectClientProvider.get();

        try {
            log.atTrace().log("Assigning organization {} to project {}", orgId, projectId);
            client.assignOrganizationToProject(projectId, orgId).block();
            printNormal("Successfully assigned organization %s to project %s", orgId, projectId);
            log.atTrace().log("Successfully assigned organization to project");
        } catch (HttpClientResponseException e) {
            printError("Failed to assign organization %s to project %s. (%s: %s)",
                    orgId, projectId, e.getStatus().getCode(), e.getMessage());
            log.atError().setCause(e).log("Error response from API: {}", e.getResponse().body());
        }
    }
}
