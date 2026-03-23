package org.justserve.cli.command


import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.TestUser
import org.justserve.model.ProjectCard
import org.justserve.util.EmailParser
import org.justserve.util.TestEmailGenerator
import spock.lang.Execution
import spock.lang.Shared

import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD

@Execution(SAME_THREAD)
@MicronautTest
class UnReassignProjectsSpec extends BaseCommandSpec {

    @Shared
    File tempEmlFile

    @Shared
    Map<String, String> testEmails

    @Shared
    List<ProjectCard> testProjects

    def setupSpec() {
        testProjects = getProjectsByLocation(faker.location().toString())
        def newReadOnlyUser = new TestUser(faker)
        newReadOnlyUser.uuid = createUser().body().id
        testEmails = new HashMap<>()
        testEmails.put("forwarded-reassignment-email", TestEmailGenerator.generateMockValidEmlContent(testProjects, readOnlyUser))
        testEmails.put("automated-reassignment-email", TestEmailGenerator.generateMockValidEmlContent(testProjects, newReadOnlyUser))
        testEmails.put("email-without-justserve-content", TestEmailGenerator.generateInvalidMockEmlContent())
    }

    def "can make reassignments from #title to a user"(String title, String fileContent) {
        given:
        if (title.contains("without")) {
            return
        }
        tempEmlFile = File.createTempFile(title, ".eml")
        tempEmlFile.write(fileContent)
        def args = ["unReassignProjects", "-u", readOnlyUser.uuid.toString(), "-f", tempEmlFile.absolutePath]
        def projectCount = EmailParser.getProjects(fileContent).values().flatten().size()

        when:
        def (outputStream, errorStream) = executeCommand(ctx, args as String[])

        then:
        errorStream.matches(blankRegex)
        testProjects.each { project ->
            outputStream.contains(project.id.toString())
        }
        outputStream.contains("Successfully reassigned ${projectCount} projects to user ${readOnlyUser.uuid}")

        cleanup:
        try {
            tempEmlFile.delete()
        } catch (Exception ignored) {
        }

        where:
        [title, fileContent] << testEmails.collect { key, value -> [key, value] }
    }

    def "shows error when project ID is invalid"() {
        given:
        def invalidProject = new ProjectCard().setId(UUID.randomUUID()).setTitle("Surprised by Joy")
        def emailContent = TestEmailGenerator.generateMockValidEmlContent([invalidProject], readOnlyUser)
        tempEmlFile = File.createTempFile("invalid-project", ".eml")
        tempEmlFile.write(emailContent)
        def args = ["unReassignProjects", "-u", readOnlyUser.uuid.toString(), "-f", tempEmlFile.absolutePath]

        when:
        def (outputStream, errorStream) = executeCommand(ctx, args as String[])

        then:
        errorStream.contains("Failed to get project 'Surprised by Joy' (${invalidProject.id})")
        outputStream.contains("Successfully reassigned 0 projects")

        cleanup:
        try {
            tempEmlFile.delete()
        } catch (Exception ignored) {
        }
    }
}
