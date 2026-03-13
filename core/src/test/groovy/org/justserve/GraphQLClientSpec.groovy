package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import net.datafaker.Faker
import org.justserve.client.GraphQLClient
import org.justserve.model.*
import org.justserve.model.graph.CreateEventQuery
import org.justserve.model.graph.CreateEventVariables
import org.justserve.model.graph.ProjectEvent
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

    def setupSpec() {
        def project = client.createProject(new GraphQLCreateProjectVariables()
                .setTitle("this is a test")
                .setEventType(EventType.Ongoing)
                .setLocationType(ProjectLocationType.SINGLE_LOCATION)
        )
        projectId = project
                .getData()
                .getCreateProject().getId()
    }

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

    def "can set contactEmail for ongoing event"() {
        given:
        def event = new ProjectEvent().setContactEmail(faker.internet().emailAddress())
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set contactName for ongoing event"() {
        given:
        def event = new ProjectEvent().setContactName(faker.name().fullName())
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set contactPhone for ongoing event"() {
        given:
        def event = new ProjectEvent().setContactPhone(faker.phoneNumber().phoneNumber())
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set end for ongoing event"() {
        given:
        def event = new ProjectEvent().setEnd(Date.from(faker.timeAndDate().future(365, TimeUnit.DAYS)))
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set groupCap for ongoing event"() {
        given:
        def event = new ProjectEvent().setGroupCap(true)
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set groupLimit for ongoing event"() {
        given:
        def event = new ProjectEvent().setGroupLimit(10)
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set locationLink for ongoing event"() {
        given:
        def event = new ProjectEvent().setLocationLink(faker.internet().url())
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set locationName for ongoing event"() {
        given:
        def event = new ProjectEvent().setLocationName(faker.address().streetAddress())
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set qrCodeImageLocation for ongoing event"() {
        given:
        def event = new ProjectEvent().setQrCodeImageLocation(faker.internet().url())
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set renewDate for ongoing event"() {
        given:
        def event = new ProjectEvent().setRenewDate(Date.from(faker.timeAndDate().future(730, TimeUnit.DAYS)))
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set schedule for ongoing event"() {
        given:
        def event = new ProjectEvent().setSchedule(faker.lorem().sentence())
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set shiftTitle for ongoing event"() {
        given:
        def event = new ProjectEvent().setShiftTitle(faker.lorem().word())
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set specialDirections for ongoing event"() {
        given:
        def event = new ProjectEvent().setSpecialDirections(faker.lorem().paragraph())
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set start for ongoing event"() {
        given:
        def event = new ProjectEvent().setStart(Date.from(faker.timeAndDate().future(180, TimeUnit.DAYS)))
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set status for ongoing event"() {
        given:
        def event = new ProjectEvent().setStatus(ProjectEventStatus.ACTIVE)
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set timezone for ongoing event"() {
        given:
        def event = new ProjectEvent().setTimezone(TimeZone.ARIZONA)
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set totalVolunteersNeeded for ongoing event"() {
        given:
        def event = new ProjectEvent().setTotalVolunteersNeeded(20)
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "can set volunteerCap for ongoing event"() {
        given:
        def event = new ProjectEvent().setVolunteerCap(true)
        def vars = new CreateEventVariables().setProjectId(projectId).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventQuery(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()
    }
}
