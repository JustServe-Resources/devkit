package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.justserve.client.GraphQLClient
import org.justserve.model.EventType
import org.justserve.model.GraphQLCreateProjectVariables
import org.justserve.model.ProjectLocationType
import org.justserve.model.graph.CreateEventFields
import org.justserve.model.graph.CreateEventQuery
import org.justserve.model.graph.CreateEventVariables
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

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
        !response.hasErrors()

        where:
        [eventType, locationType, redirect] << [EventType.values(), ProjectLocationType.values(), ["", null, "https://google.com"]].combinations()
    }

    @Unroll
    void "can use createOngoingEvent() with combination of fields"(String contactEmail, String contactName, String contactPhone, Boolean deleted, Date end, Boolean eventCapReached, Integer groupCap, Integer groupLimit, String locationLink, String locationName, String qrCodeImageLocation, Date renewDate, String schedule, String shiftTitle, String specialDirections, Date start, String status, String timezone, Integer totalVolunteersNeeded, Integer volunteerCap, Integer volunteersNeeded) {
        given:
        def projectId = client.createProject(new GraphQLCreateProjectVariables()
                .setTitle("this is a test")
                .setEventType(EventType.Ongoing)
                .setLocationType(ProjectLocationType.SINGLE_location)
        ).data.createProject.id

        def event = new CreateEventFields()
                .setContactEmail(contactEmail)
                .setContactName(contactName)
                .setContactPhone(contactPhone)
                .setDeleted(deleted)
                .setEnd(end)
                .setEventCapReached(eventCapReached)
                .setGroupCap(groupCap)
                .setGroupLimit(groupLimit)
                .setLocationLink(locationLink)
                .setLocationName(locationName)
                .setQrCodeImageLocation(qrCodeImageLocation)
                .setRenewDate(renewDate)
                .setSchedule(schedule)
                .setShiftTitle(shiftTitle)
                .setSpecialDirections(specialDirections)
                .setStart(start)
                .setStatus(status)
                .setTimezone(timezone)
                .setTotalVolunteersNeeded(totalVolunteersNeeded)
                .setVolunteerCap(volunteerCap)
                .setVolunteersNeeded(volunteersNeeded)

        def vars = new CreateEventVariables()
                .setProjectId(projectId)
                .setProjectEvent(event)

        when:
        def query = new CreateEventQuery(vars)
        def response = client.createEvent(query)

        then:
        noExceptionThrown()
        !response.hasErrors()

        where:
        [contactEmail, contactName, contactPhone, deleted, end, eventCapReached, groupCap, groupLimit, locationLink, locationName, qrCodeImageLocation, renewDate, schedule, shiftTitle, specialDirections, start, status, timezone, totalVolunteersNeeded, volunteerCap, volunteersNeeded] << [
                ["test@test.com", "a@a.com", null],
                ["name", "another name", null],
                ["123-456-7890", "9876543210", null],
                [true, false, null],
                [Date.from(ZonedDateTime.now().plusMonths(6).toInstant()), Date.from(ZonedDateTime.now().plusYears(1).toInstant()), null],
                [true, false, null],
                [10, 20, null],
                [5, 10, null],
                ["https://google.com", "https://bing.com", null],
                ["location", "another location", null],
                ["/qr/code.png", "/another/qr.png", null],
                [Date.from(ZonedDateTime.now().plusYears(1).toInstant()), Date.from(ZonedDateTime.now().plusYears(2).toInstant()), null],
                ["schedule", "another schedule", null],
                ["shift", "another shift", null],
                ["directions", "more directions", null],
                [Date.from(ZonedDateTime.now().plusMonths(1).toInstant()), Date.from(ZonedDateTime.now().plusDays(15)), null],
                ["active", "inactive", null],
                ["UTC", "America/New_York", null],
                [100, 200, null],
                [50, 100, null],
                [25, 50, null]
        ].combinations()
    }
}
