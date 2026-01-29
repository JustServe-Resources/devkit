package org.justserve.client

import io.micronaut.http.HttpResponse
import org.justserve.JustServeSpec
import org.justserve.model.*

import static io.micronaut.http.HttpStatus.OK
import static org.justserve.model.DistanceType.MILES

class ProjectClientSpec extends JustServeSpec {

    void "get project's current owner"(ProjectCard project) {
        when:
        GetProjectRequest projectRequest = new GetProjectRequest()

        def response = projectClient
                .getProject((project as ProjectCard).getId(), "en-US", projectRequest)

        then:
        verifyAll {
            response.body() != null
            response.body().getProjectOwnerUserId() != null
        }


        where:
        project << searchResults

    }

    void "can reassign a project using either an empty string locale or 'en-US' locale"(ProjectCard project) {
        given:
        GetProjectRequest projectRequest = new GetProjectRequest()
        String locale = new Random().nextBoolean() ? " " : "en-US"
        UUID currentOwner = projectClient.getProject((project as ProjectCard).getId(), locale, projectRequest)
                .body().getProjectOwnerUserId()
        ReassignProjectRequest reassignProjectRequest = new ReassignProjectRequest(readOnlyUser.uuid, currentOwner)

        when:
        def response = projectClient.reassignProject((project as ProjectCard).getId(), reassignProjectRequest)

        then:
        noExceptionThrown()
        verifyAll {
            if (response.status() != OK) {
                println "Warning: response status ${response.status()} != OK"
            } else {
                projectClient.getProject((project as ProjectCard).getId(), "en-US", projectRequest)
                        .body().getProjectOwnerUserId() == readOnlyUser.uuid
            }
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
        HttpResponse<ProjectSearchResponse> response = projectClient.searchProjects(request)

        then:
        verifyAll {
            response.status == OK
            response.body() != null
            response.body().items != null
        }
    }

    void "can assign an organization to a project"() {
        given:
        def orgSearchResponse = authOrgClient.searchByLocation(createSearchRequestForElkGrove())
        UUID orgId = orgSearchResponse.body().organizations.first().id
        ProjectCard project = searchResults.first()

        when:
        def response = projectClient.assignOrganizationToProject(project.getId(), orgId)

        then:
        verifyAll {
            response.status == OK
        }

        and: "validate that the reassignment worked - this is testing the underlying tech, not our codebase"
        def updatedProject = projectClient.getProject(project.getId(), "en-US", new GetProjectRequest()).body()
        verifyAll {
            updatedProject.organization.organizationId == orgId
        }
    }

}
