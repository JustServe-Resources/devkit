package org.justserve.cli.command

import io.micronaut.core.io.ResourceResolver
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import net.datafaker.Faker
import org.justserve.model.ProjectCard
import org.justserve.util.TestEmailGenerator
import spock.lang.Execution
import spock.lang.Shared

import java.nio.file.Files

import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD

@Execution(SAME_THREAD)
@MicronautTest
class UnReassignProjectsSpec extends BaseCommandSpec {

    @Inject
    ResourceResolver resourceResolver

    @Shared
    File tempEmlFile

    def setup() {
        // Create a temporary file for the test
        tempEmlFile = File.createTempFile("test-reassign", ".eml")
        println "Temp file created at: " + tempEmlFile.absolutePath
    }

    def cleanup() {
        if (tempEmlFile != null && tempEmlFile.exists()) {
             tempEmlFile.delete()
        }
    }

    def "can make reassignments from #emlFile to #user"() {
        given:
        List<ProjectCard> projects = getProjectsByLocation(new Faker().address().fullAddress())
        assert projects.size() > 0

        String emlContent = TestEmailGenerator.generateMockEmlContent(projects, readOnlyUser)
        Files.writeString(tempEmlFile.toPath(), emlContent)

        def args = ["unReassignProjects", "-u", readOnlyUser.uuid.toString(), "-f", tempEmlFile.absolutePath]

        when:
        def (outputStream, errorStream) = executeCommand(ctx, args as String[])

        then:
        errorStream.matches(blankRegex)
        projects.each { project ->
            outputStream.contains(project.id.toString())
        }
        outputStream.contains("Successfully reassigned ${projects.size()} projects to user ${readOnlyUser.uuid}")
    }
}
