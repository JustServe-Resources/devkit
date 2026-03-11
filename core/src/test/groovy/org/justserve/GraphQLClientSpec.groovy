package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import net.datafaker.Faker
import org.justserve.client.GraphQLClient
import org.justserve.model.EventType
import org.justserve.model.GraphQLCreateProjectData
import org.justserve.model.GraphQLCreateProjectVariables
import org.justserve.model.GraphQLResponse
import org.justserve.model.GraphQLSetProjectLocationVariables
import org.justserve.model.GraphQLSetProjectLocationVariablesLocationData
import org.justserve.model.ProjectLocationType
import spock.lang.Shared
import spock.lang.Specification

@MicronautTest
class GraphQLClientSpec extends Specification {

    @Shared
    @Inject
    GraphQLClient client

    @Shared
    GraphQLResponse<GraphQLCreateProjectData> projectData

    def setupSpec() {
        GraphQLCreateProjectVariables createProjectArgs = new GraphQLCreateProjectVariables()
                .setEventType(EventType.DTL)
                .setLocationType(ProjectLocationType.SINGLE_LOCATION)
                .setTitle("this is my title")
                .setRedirect("https://google.com")
        projectData = client.createProject(createProjectArgs)
    }

    void "can create Project with EventType: #eventType, LocationType: #locationType, and Redirect: #redirect"(EventType eventType, ProjectLocationType locationType, String redirect) {
        given:
        GraphQLCreateProjectVariables args = new GraphQLCreateProjectVariables()
                .setEventType(eventType)
                .setLocationType(locationType)
                .setTitle("this is my title")
                .setRedirect(redirect)

        when:
        client.createProject(args)
        then:
        noExceptionThrown()

        where:
        [eventType, locationType, redirect] << [EventType.values(), ProjectLocationType.values(), ["", null, "https://google.com"]].combinations()
    }

    void "can set project location using Project Id: #projectId, Location: #location, Location Data: #locationData"(UUID projectId, String location, GraphQLSetProjectLocationVariablesLocationData locationData) {
        given:
        GraphQLSetProjectLocationVariables addAttachmentArgs = new GraphQLSetProjectLocationVariables()
                .setProjectId(projectId)
                .setLocation(location)
                .setLocationData(locationData)

        when:

        client.setProjectLocation(addAttachmentArgs)

        then:
        noExceptionThrown()

        where:
//        TODO: Replace hardcoded values to make this a bit more dynamic
        [projectId, location, locationData] << [projectData.data.createProject.id, ["20 West 6th Street, Tempe, Arizona, United States", null, ""], [null, new GraphQLSetProjectLocationVariablesLocationData()
                .setAddress("20 West 6th Street")
                .setAreaId("790052")
                .setCcId("543306")
                .setCity("Tempe")
                .setCountry("United States")
                .setCountryCode("us")
                .setCounty("Maricopa County")
                .setLocationDetails("")
                .setLocationName("20 West 6th Street, Tempe, Arizona, United States")
                .setLatitude(33.4245772)
                .setLongitude(-111.9410241)
                .setMissionId("2011050")
                .setPostal("85281")
                .setStakeId("504297")
                .setState("Arizona")
        ]].combinations()
    }
}
