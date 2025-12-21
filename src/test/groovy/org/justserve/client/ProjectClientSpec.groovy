package org.justserve.client

import io.micronaut.http.HttpResponse
import org.justserve.JustServeSpec
import org.justserve.model.ProjectSearchRequest
import org.justserve.model.ProjectSearchResponse
import spock.lang.Shared

import static io.micronaut.http.HttpStatus.OK
import static org.justserve.model.DistanceType.MILES

class ProjectClientSpec extends JustServeSpec {

    @Shared
    ProjectClient projectClient

    def setupSpec() {
        projectClient = ctx.getBean(ProjectClient)
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

}
