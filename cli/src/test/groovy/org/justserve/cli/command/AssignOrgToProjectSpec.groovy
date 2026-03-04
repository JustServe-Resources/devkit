package org.justserve.cli.command

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.model.ProjectCard
import spock.lang.Execution

import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD

@Execution(SAME_THREAD)
@MicronautTest
class AssignOrgToProjectSpec extends BaseCommandSpec {

    def "can assign an organization to a project"() {
        given:
        def orgSearchResponse = authOrgClient.searchByLocation(createSearchRequestForElkGrove())
        UUID orgId = orgSearchResponse.body().organizations.first().id
        ProjectCard project = searchResults.first()
        def args = ["assignOrgToProject", "-p", project.getId().toString(), "-o", orgId.toString()]

        when:
        def (outputStream, errorStream) = executeCommand(ctx, args as String[])

        then:
        errorStream.matches(blankRegex)
        outputStream.contains("Successfully assigned organization ${orgId} to project ${project.getId()}")
    }

    def "fails gracefully when project or org does not exist"() {
        given:
        UUID fakeId = UUID.randomUUID()
        def args = ["assignOrgToProject", "-p", fakeId.toString(), "-o", fakeId.toString()]

        when:
        def (outputStream, errorStream) = executeCommand(ctx, args as String[])

        then:
        outputStream.matches(blankRegex)
        errorStream.contains("Failed to assign organization ${fakeId} to project ${fakeId}. (400: Client 'justserve': Bad Request)")
    }
}
