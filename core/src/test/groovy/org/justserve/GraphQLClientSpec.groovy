package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.justserve.client.GraphQLClient
import org.justserve.model.*
import org.justserve.model.graph.CreateEventQuery
import org.justserve.model.graph.Event
import org.justserve.model.graph.GraphQLResponse
import spock.lang.Shared
import spock.lang.Specification

import java.time.ZonedDateTime

@MicronautTest
class GraphQLClientSpec extends Specification {

    @Shared
    @Inject
    GraphQLClient client

    void "can create Project with EventType: #eventType, LocationType: #locationType, and Redirect: #redirect"(EventType eventType, ProjectLocationType locationType, String redirect) {
        given:
        GraphQLCreateProjectVariables args = new GraphQLCreateProjectVariables()
                .setEventType(eventType)
                .setLocationType(locationType)
                .setTitle("this is my title")
                .setRedirect(redirect)

        when:
        def response = client.createProject(args)

        then:
        noExceptionThrown()

        where:
        [eventType, locationType, redirect] << [EventType.values(), ProjectLocationType.values(), ["", null, "https://google.com"]].combinations()
    }

    void "can use createOngoingEvent()"() {
        given:
        GraphQLResponse<GraphQLCreateProjectData> createProjectResponse = client.createProject(new GraphQLCreateProjectVariables()
                .setTitle("this is a test")
                .setEventType(EventType.Ongoing)
                .setLocationType(ProjectLocationType.SINGLE_LOCATION)
        )
        Event event = new Event()
                .setProjectId(createProjectResponse.getData().getCreateProject().getId())
                .setEnd(ZonedDateTime.now().plusMonths(6))
                .setStart(ZonedDateTime.now().plusMonths(1))

        when:
        GraphQLResponse<GraphQLCreateEventData> response = client.createEvent(new CreateEventQuery(event))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }
}
