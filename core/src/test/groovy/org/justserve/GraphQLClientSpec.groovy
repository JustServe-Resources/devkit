package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import net.datafaker.Faker
import org.justserve.client.GraphQLClient
import org.justserve.model.*
import org.justserve.model.graph.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

@MicronautTest
class GraphQLClientSpec extends Specification {

    @Shared
    @Inject
    GraphQLClient client

    @Shared
    Faker faker = new Faker()

    @Shared
    Map<EventType, UUID> projectIds = [:]


    def setupSpec() {
        EventType.values().each { type ->
            def project = client.createProject(new GraphQLCreateProjectVariables()
                    .setTitle("Test Project - ${type.name()}")
                    .setEventType(type)
                    .setLocationType(ProjectLocationType.SINGLE_LOCATION)
            )
            projectIds[type] = project.getData().getCreateProject().getId()
        }
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

    @Unroll("can set contact info for #eventType.name() event")
    def "can set contact info for #eventType event"() {
        given:
        def event = baseEventBuilder()
                .contactEmail(faker.internet().emailAddress())
                .contactName(faker.name().fullName())
                .contactPhone(faker.phoneNumber().phoneNumber())
                .build()
        def vars = new CreateEventVariables().setProjectId(projectIds[eventType]).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventMutation(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()

        where:
        eventType << [EventType.DTL, EventType.Ongoing, EventType.MultipleDTL]
    }

    @Unroll("can set dates for #eventType.name() event")
    def "can set dates for #eventType event"() {
        given:
        def event = baseEventBuilder()
                .renewDate(Date.from(faker.timeAndDate().future(730, TimeUnit.DAYS)))
                .build()
        def vars = new CreateEventVariables().setProjectId(projectIds[eventType]).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventMutation(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()

        where:
        eventType << [EventType.MultipleDTL]
    }

    @Unroll("can set group info for #eventType.name() event")
    def "can set group info for #eventType event"() {
        given:
        def event = baseEventBuilder()
                .groupCap(true)
                .groupLimit(10)
                .build()
        def vars = new CreateEventVariables().setProjectId(projectIds[eventType]).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventMutation(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()

        where: // error reads "Only a multiple DTL project can have more than one event"
        eventType << [/*EventType.DTL,*/ EventType.Ongoing, EventType.MultipleDTL]
    }

    @Unroll("can set location info for #eventType.name() event")
    def "can set location info for #eventType event"() {
        given:
        def event = baseEventBuilder()
                .locationLink(faker.internet().url())
                .locationName(faker.address().streetAddress())
                .build()
        def vars = new CreateEventVariables().setProjectId(projectIds[eventType]).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventMutation(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()

        where: //error reads "Only a multiple DTL project can have more than one event, StackTrace=   at JustServe.Mediators.ProjectEvents.ProjectEventMediator.InternalCreateEvent(Project project, UpdateProjectEvent updateProjectEvent, SecurityContext securityContext) in /src/src/JustServe.Mediators/ProjectEvents/ProjectEventMediator.cs:line 109"
        eventType << [EventType.DTL, /*EventType.Ongoing,*/ EventType.MultipleDTL]
    }

    @Unroll("can set schedule info for #eventType.name() event")
    def "can set schedule info for #eventType event"() {
        given:
        def schedule = faker.lorem().sentence()
        def event = baseEventBuilder()
                .schedule(schedule)
                .shiftTitle(schedule)
                .build()
        def vars = new CreateEventVariables().setProjectId(projectIds[eventType]).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventMutation(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()

        where:
        eventType << [EventType.DTL, EventType.Ongoing, EventType.MultipleDTL]
    }

    @Unroll("can set volunteer info for #eventType.name() event")
    def "can set volunteer info for #eventType event"() {
        given:
        def event = baseEventBuilder()
                .volunteerCap(true)
                .totalVolunteersNeeded(20)
                .build()
        def vars = new CreateEventVariables().setProjectId(projectIds[eventType]).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventMutation(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()

        where:
        eventType << [EventType.DTL, EventType.Ongoing, EventType.MultipleDTL]
    }

    @Unroll("can set miscellaneous info for #eventType.name() event")
    def "can set miscellaneous info for #eventType event"() {
        given:
        def event = baseEventBuilder()
                .qrCodeImageLocation(faker.internet().url())
                .specialDirections(faker.lorem().paragraph())
                .status(ProjectEventStatus.ACTIVE)
                .timezone(TimeZone.ARIZONA)
                .build()
        def vars = new CreateEventVariables().setProjectId(projectIds[eventType]).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventMutation(vars))

        then:
        noExceptionThrown()
        !response.hasErrors()

        where:
        eventType << [EventType.DTL, EventType.Ongoing, EventType.MultipleDTL]
    }

    @Unroll("cannot manually create event for #eventType.name() project")
    def "cannot manually create event for invalid project types"() {
        given:
        def event = baseEventBuilder().build()
        def vars = new CreateEventVariables().setProjectId(projectIds[eventType]).setProjectEvent(event)

        when:
        def response = client.createEvent(new CreateEventMutation(vars))

        then:
        response.hasErrors()

        where:
        eventType << [EventType.Recurring]
    }

    @Unroll("can add multiple events only for #eventType.name() projects (shouldFail: #shouldFail)")
    def "can add multiple events only for Multi-DTL projects"() {
        given:
        def firstEvent = baseEventBuilder().build()
        def secondEvent = baseEventBuilder().build()
        def firstVars = new CreateEventVariables().setProjectId(projectIds[eventType]).setProjectEvent(firstEvent)
        def secondVars = new CreateEventVariables().setProjectId(projectIds[eventType]).setProjectEvent(secondEvent)

        when:
        client.createEvent(new CreateEventMutation(firstVars))
        def secondResponse = client.createEvent(new CreateEventMutation(secondVars))

        then:
        secondResponse.hasErrors() == shouldFail

        where:
        eventType             | shouldFail
        EventType.DTL         | true
        EventType.Ongoing     | true
        EventType.Recurring   | true
        EventType.MultipleDTL | false
    }

    def "can create multiple events at once for Multi-DTL projects using createEvents mutation"() {
        given:
        def event1 = baseEventBuilder()
                .shiftTitle("Morning Shift")
                .build()
        def event2 = baseEventBuilder()
                .shiftTitle("Afternoon Shift")
                .build()
        def event3 = baseEventBuilder()
                .shiftTitle("Evening Shift")
                .build()
        def vars = new CreateEventsVariables(projectIds[EventType.MultipleDTL], event1, event2, event3)
        def mutation = new CreateEventsMutation(vars)

        when:
        def response = client.createEvents(mutation)

        then:
        noExceptionThrown()
        !response.hasErrors()

        and: "the response data should contain the newly created events"
        null != response.getData()
    }

    def "can create multiple recurring events at once for Recurring projects using createRecurringEvents mutation"() {
        given:
        def time1 = new ProjectRecurringTime()
                .setStartTime("10:00")
                .setEndTime("12:00")
                .setRecurringType(RecurringType.WEEKLY)
                .setMonday(true)
        def time2 = new ProjectRecurringTime()
                .setStartTime("14:00")
                .setEndTime("16:00")
                .setRecurringType(RecurringType.MONTHLY)
                .setThirdWeek(true)
        def vars = new CreateRecurringEventsVariables(projectIds[EventType.Recurring], time1, time2)
        def mutation = new CreateRecurringEventsMutation(vars)

        when:
        def response = client.createRecurringEvents(mutation)

        then:
        noExceptionThrown()
        !response.hasErrors()

        and: "the response data should contain the newly created recurring events"
        null != response.getData()
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

    def "cannot set volunteersCapped without volunteersNeeded on ProjectRecurringTime"() {
        when:
        ProjectRecurringTime.builder().volunteersCapped(true).build()

        then:
        thrown(IllegalStateException)
    }

    def "can set all info for ProjectRecurringTime"() {
        given:
        def time1 = ProjectRecurringTime.builder()
                .contactEmail(faker.internet().emailAddress())
                .contactName(faker.name().fullName())
                .contactPhone(faker.phoneNumber().phoneNumber())
                .startTime("10:00")
                .endTime("12:00")
                .firstWeek(true)
                .secondWeek(true)
                .thirdWeek(true)
                .fourthWeek(true)
                .fifthWeek(true)
                .lastWeek(true)
                .groupLimit(10)
                .recurringType(RecurringType.WEEKLY)
                .recurringDaysOfMonths([1, 15, 28])
                .specialDirections(faker.lorem().paragraph())
                .volunteersCapped(true)
                .volunteersNeeded(5)
                .monday(true)
                .tuesday(true)
                .wednesday(true)
                .thursday(true)
                .friday(true)
                .saturday(true)
                .sunday(true)
                .build()

        def vars = new CreateRecurringEventsVariables(projectIds[EventType.Recurring], time1)
        def mutation = new CreateRecurringEventsMutation(vars)

        when:
        def response = client.createRecurringEvents(mutation)

        then:
        noExceptionThrown()
        !response.hasErrors()
    }

    def "test searchOrganization parses the input correctly"(String searchTerm, Boolean includesAll) {
        given:

        GraphQLSearchOrganizationVariables inputData = new GraphQLSearchOrganizationVariables()
                .setSearchTerm(searchTerm)
                .setIncludeAll(includesAll)

        when:
        GraphQLResponse<GraphQLSearchOrganizationData> response = client.searchOrganization(inputData)

        then:
        noExceptionThrown()
        !response.hasErrors()

        where:
        [includesAll, searchTerm] << [[true, false], ["the"]].combinations()

    }
}
