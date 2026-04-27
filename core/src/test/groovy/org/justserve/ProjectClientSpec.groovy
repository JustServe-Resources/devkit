package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.model.*

import static org.justserve.model.DistanceType.MILES

@MicronautTest
class ProjectClientSpec extends JustServeSpec {

    void "get project's current owner"(ProjectCard project) {
        when:
        GetProjectRequest projectRequest = new GetProjectRequest()

        def response = projectClient
                .getProject((project as ProjectCard).getId(), "en-US", projectRequest).block()

        then:
        verifyAll {
            response != null
            response.getProjectOwnerUserId() != null
        }


        where:
        project << searchResults

    }

    void "can reassign a project using either an empty string locale or 'en-US' locale"(ProjectCard project) {
        given:
        GetProjectRequest projectRequest = new GetProjectRequest()
        String locale = new Random().nextBoolean() ? " " : "en-US"
        UUID currentOwner = projectClient.getProject((project as ProjectCard).getId(), locale, projectRequest).block()
                .getProjectOwnerUserId()
        ReassignProjectRequest reassignProjectRequest = new ReassignProjectRequest(readOnlyUser.uuid, currentOwner)

        when:
        projectClient.reassignProject((project as ProjectCard).getId(), reassignProjectRequest).block()

        then:
        noExceptionThrown()
        verifyAll {
            projectClient.getProject((project as ProjectCard).getId(), "en-US", projectRequest).block()
                    .getProjectOwnerUserId() == readOnlyUser.uuid
        }

        where:
        project << searchResults
    }

    void "searchProjects should return results"() {
        given:
        def locale = "en-US"
        def request = new ProjectSearchRequest()
                .setPage(Integer.valueOf(1)).setSize(10).setKeywords(" ").setLocation(" ").setRadiusType(MILES)
                .setVolunteerFromAnywhere(false).setIncludeOrgInfo(true).setLanguage(locale).setBrowserLocale(locale)
                .setPublishedOnly(false).setIncludeFilledProjects(true).setDisasterRecoveryProjectsOnly(false).setTimesOfDay(null)

        when:
        ProjectSearchResponse response = projectClient.searchProjects(request).block()

        then:
        verifyAll {
            response != null
            response.items != null
        }
    }

    void "can assign an organization to a project"() {
        given:
        def orgSearchResponse = authOrgClient.searchByLocation(createSearchRequestForElkGrove()).block()
        UUID orgId = orgSearchResponse.organizations.first().id
        ProjectCard project = searchResults.first()

        when:
        projectClient.assignOrganizationToProject(project.getId(), orgId).block()

        then:
        noExceptionThrown()


        and: "validate that the reassignment worked - this is testing the underlying tech, not our codebase"
        def updatedProject = projectClient.getProject(project.getId(), "en-US", new GetProjectRequest()).block()
        verifyAll {
            updatedProject.organization.organizationId == orgId
        }
    }

}
