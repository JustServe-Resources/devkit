package org.justserve.cli.command

import io.micronaut.core.io.ResourceResolver
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.justserve.util.EmailParser
import spock.lang.Execution
import spock.lang.Shared

import java.util.stream.Collectors
import java.util.stream.Stream

import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD

@Execution(SAME_THREAD)
@MicronautTest
class UnReassignProjectsSpec extends BaseCommandSpec {

    @Inject
    @Shared
    ResourceResolver resourceResolver

    @Shared
    File tempEmlFile

    @Shared
    Map<String, String> testEmails

    def setupSpec() {
        testEmails = new HashMap<>()
        Stream.of("sara-anderson-email.eml", "test-with-automated-email.eml", "test-without-automated-email.eml").forEach { file ->
            def resource = resourceResolver.getResourceAsStream("classpath:$file")
            resource.ifPresent { stream ->
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                    testEmails.put(file.replace(".eml", ""), reader.lines().collect(Collectors.joining(System.lineSeparator())))
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read test file: $file", e)
                }
            }
        }
    }

    def "can make reassignments from #title to a user"(String title, String fileContent) {
        given:
        if (title.contains("without")) {
            return
        }
        String testFile = new File(resourceResolver.getResource("classpath:${title}.eml").get().toURI()).absolutePath
        def args = ["unReassignProjects", "-u", readOnlyUser.uuid.toString(), "-f", testFile]
        def projectCount = EmailParser.getProjects(fileContent).values().flatten().size()

        when:
        def (outputStream, errorStream) = executeCommand(ctx, args as String[])

        then:
        errorStream.matches(blankRegex)
        projects.each { project ->
            outputStream.contains(project.id.toString())
        }
        outputStream.contains("Successfully reassigned ${projectCount} projects to user ${readOnlyUser.uuid}")

        where:
        [title, fileContent] << testEmails.collect { key, value -> [key, value] }
    }
}
