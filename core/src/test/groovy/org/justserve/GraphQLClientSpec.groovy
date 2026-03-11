package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.justserve.client.GraphQLClient
import org.justserve.model.*
import org.justserve.model.graph.CreateEventQuery
import org.justserve.model.graph.CreateEventVariables
import org.justserve.model.graph.GraphQLResponse
import org.justserve.model.graph.ProjectEvent
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
        ProjectEvent event = new ProjectEvent()
                .setStart(Date.from(ZonedDateTime.now().plusMonths(1).toInstant()))
                .setEnd(Date.from(ZonedDateTime.now().plusMonths(6).toInstant()))
        CreateEventVariables vars = new CreateEventVariables()
                .setProjectId(createProjectResponse.getData().getCreateProject().getId())
                .setProjectEvent(event)

        when:
        CreateEventQuery query = new CreateEventQuery(vars)

        GraphQLResponse<GraphQLCreateEventData> response = client.createEvent(query)

        then:
        noExceptionThrown()
        !response.hasErrors()
    }
}
