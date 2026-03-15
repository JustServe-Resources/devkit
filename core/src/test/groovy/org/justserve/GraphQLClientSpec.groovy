package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import net.datafaker.Faker
import org.justserve.client.GraphQLClient
import org.justserve.model.*
import org.justserve.model.graph.CreateEventQuery
import org.justserve.model.graph.CreateEventVariables
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.TimeUnit

@MicronautTest
class GraphQLClientSpec extends Specification {

    @Shared
    @Inject
    GraphQLClient client

    @Shared
    Faker faker = new Faker()

    @Shared
    UUID projectId

    def setup() {
        def project = client.createProject(new GraphQLCreateProjectVariables()
                .setTitle("this is a test")
                .setEventType(EventType.Ongoing)
                .setLocationType(ProjectLocationType.SINGLE_LOCATION))
        projectId = project
                .getData()
                .getCreateProject().getId()
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
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
        !response.hasErrors()

        where:
        [eventType, locationType, redirect] << [EventType.values(), ProjectLocationType.values(), ["", null, "https://google.com"]].combinations()
    }

    private ProjectEvent.ProjectEventBuilder baseEventBuilder() {
        return ProjectEvent.builder()
                .start(Date.from(faker.timeAndDate().future(180, TimeUnit.DAYS)))
                .end(Date.from(faker.timeAndDate().future(365, TimeUnit.DAYS)))
    }

    def "can set contact info for ongoing event"() {
        given:
        def event = baseEventBuilder()
                .contactEmail(faker.internet().emailAddress())
                .contactName(faker.name().fullName())
                .contactPhone(faker.phoneNumber().phoneNumber())
                .build()
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set dates for ongoing event"() {
        given:
        def event = baseEventBuilder()
                .renewDate(Date.from(faker.timeAndDate().future(730, TimeUnit.DAYS)))
                .build()
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set group info for ongoing event"() {
        given:
        def event = baseEventBuilder()
                .groupCap(true)
                .groupLimit(10)
                .build()
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set location info for ongoing event"() {
        given:
        def event = baseEventBuilder()
                .locationLink(faker.internet().url())
                .locationName(faker.address().streetAddress())
                .build()
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set schedule info for ongoing event"() {
        given:
        def schedule = faker.lorem().sentence()
        def event = baseEventBuilder()
                .schedule(schedule)
                .shiftTitle(schedule)
                .build()
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set volunteer info for ongoing event"() {
        given:
        def event = baseEventBuilder()
                .volunteerCap(true)
                .totalVolunteersNeeded(20)
                .build()
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set miscellaneous info for ongoing event"() {
        given:
        def event = baseEventBuilder()
                .qrCodeImageLocation(faker.internet().url())
                .specialDirections(faker.lorem().paragraph())
                .status(ProjectEventStatus.ACTIVE)
                .timezone(TimeZone.ARIZONA)
                .build()
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "cannot create event without start and end dates"() {
        when:
        ProjectEvent.builder().build()

        then:
        thrown(IllegalStateException)
    }

    def "cannot set groupCap without groupLimit"() {
        when:
        baseEventBuilder().groupCap(true).build()

        then:
        thrown(IllegalStateException)
    }

    def "cannot set volunteerCap without totalVolunteersNeeded"() {
        when:
        baseEventBuilder().volunteerCap(true).build()

        then:
        thrown(IllegalStateException)
    }
}
