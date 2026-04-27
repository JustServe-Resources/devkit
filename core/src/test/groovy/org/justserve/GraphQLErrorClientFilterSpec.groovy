package org.justserve

import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import net.datafaker.Faker
import org.justserve.client.GraphQLClient
import org.justserve.model.EventType
import org.justserve.model.GraphQLCreateProjectVariables
import org.justserve.model.ProjectLocationType
import org.justserve.model.ProjectRecurringTime
import org.justserve.model.graph.CreateRecurringEventsMutation
import org.justserve.model.graph.CreateRecurringEventsVariables
import spock.lang.Shared
import spock.lang.Specification

@MicronautTest
class GraphQLErrorClientFilterSpec extends Specification {

    @Shared
    @Inject
    GraphQLClient client

    @SuppressWarnings("GroovyAssignabilityCheck")
    void "can create Project with EventType: #eventType, LocationType: #locationType, and Redirect: #redirect"(EventType eventType, ProjectLocationType locationType, String redirect) {
        given:
        GraphQLCreateProjectVariables args = new GraphQLCreateProjectVariables()
                .setEventType(eventType)
                .setLocationType(locationType)
                .setTitle("this is my title")
                .setRedirect(redirect)

        when:
        def response = client.createProject(args).block()

        then:
        noExceptionThrown()

        when: "create an incompatible event"
        response.getData().getCreateProject()
        client.createRecurringEvents(
                new CreateRecurringEventsMutation(
                        new CreateRecurringEventsVariables(
                                response.getData().getCreateProject().getId(),
                                new ProjectRecurringTime().setEndTime("whenever"))))
                .block()

        then:
        HttpClientResponseException error = thrown(HttpClientResponseException)

        and:
        verifyAll {
            error.getMessage().length() > 0
            error.getClass() == HttpClientResponseException.class
        }


        where:
        [eventType, locationType, redirect] << [EventType.DTL, ProjectLocationType.values(), ["", null, "https://google.com"]].combinations()
    }
}