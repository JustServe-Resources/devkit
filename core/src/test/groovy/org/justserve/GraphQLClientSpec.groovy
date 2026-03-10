package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.justserve.client.GraphQLClient
import org.justserve.model.EventType
import org.justserve.model.GraphQLCreateProjectVariables
import org.justserve.model.ProjectLocationType
import spock.lang.Shared
import spock.lang.Specification

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
//        given:
//        GraphQLCreate event = new GraphQLCreateEventVariablesProjectEvent()
//        GraphQLCreateEventVariables args = new GraphQLCreateEventVariables()
//            .setProjectEvent()
//
//
//        when:
//        client.createEvent()
//
    }
}
