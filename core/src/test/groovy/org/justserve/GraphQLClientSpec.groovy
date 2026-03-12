package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import net.datafaker.Faker
import org.justserve.client.GraphQLClient
import org.justserve.model.EventType
import org.justserve.model.GraphQLCreateProjectVariables
import org.justserve.model.ProjectLocationType
import org.justserve.model.ProjectStatus
import org.justserve.model.graph.CreateEventFields
import org.justserve.model.graph.CreateEventQuery
import org.justserve.model.graph.CreateEventVariables
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
    void "can use createOngoingEvent() with combination of fields"(String contactEmail, String contactName, String contactPhone, Boolean deleted, Date end, Boolean eventCapReached, Boolean hasGroupCap, Integer groupLimit, String locationLink, String locationName, String qrCodeImageLocation, Date renewDate, String schedule, String shiftTitle, String specialDirections, Date start, ProjectStatus status, String timezone, Integer totalVolunteersNeeded, Boolean hasVolunteerCap, Integer volunteersNeeded) {
        given:
        def projectId = client.createProject(new GraphQLCreateProjectVariables()
                .setTitle("this is a test")
                .setEventType(EventType.Ongoing)
                .setLocationType(ProjectLocationType.SINGLE_LOCATION)
        ).data.createProject.id

        def event = new CreateEventFields()
                .setContactEmail(contactEmail)
                .setContactName(contactName)
                .setContactPhone(contactPhone)
                .setDeleted(deleted)
                .setEnd(end)
                .setEventCapReached(eventCapReached)
                .setGroupCap(hasGroupCap)
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
                .setVolunteerCap(hasVolunteerCap)
                .setVolunteersNeeded(volunteersNeeded)

        def vars = new CreateEventVariables()
                .setProjectId(projectId)
                .setProjectEvent(event)

        when:
        def query = new CreateEventQuery(vars)
        def response = client.createEvent(query)

        then:
        if (response.hasErrors()) {
            def errorLog = """
            ${response.errors}
            Errors for combination:
            contactEmail: ${contactEmail}
            contactName: ${contactName}
            contactPhone: ${contactPhone}
            deleted: ${deleted}
            end: ${end}
            eventCapReached: ${eventCapReached}
            hasGroupCap: ${hasGroupCap}
            groupLimit: ${groupLimit}
            locationLink: ${locationLink}
            locationName: ${locationName}
            qrCodeImageLocation: ${qrCodeImageLocation}
            renewDate: ${renewDate}
            schedule: ${schedule}
            shiftTitle: ${shiftTitle}
            specialDirections: ${specialDirections}
            start: ${start}
            status: ${status}
            timezone: ${timezone}
            totalVolunteersNeeded: ${totalVolunteersNeeded}
            hasVolunteerCap: ${hasVolunteerCap}
            volunteersNeeded: ${volunteersNeeded}
            
            """
            new File('build/test-errors.log').append(errorLog)
        }
        noExceptionThrown()
        !response.hasErrors()

        where:
        [contactEmail, contactName, contactPhone, deleted, end, eventCapReached, hasGroupCap, groupLimit, locationLink, locationName, qrCodeImageLocation, renewDate, schedule, shiftTitle, specialDirections, start, status, timezone, totalVolunteersNeeded, hasVolunteerCap, volunteersNeeded] << [
                [faker.internet().emailAddress(), null],
                [faker.lorem().characters(1, 139), null],
                [faker.phoneNumber().phoneNumber(), null],
                [true, false, null],
                [Date.from(faker.timeAndDate().future(365, TimeUnit.DAYS)), null],
                [true, false, null],
                [true, false, null],
                [(+faker.number()), null],
                [faker.internet().url(), null],
                [faker.lorem().characters(1, 139), null],
                [faker.internet().url(), null],
                [Date.from(faker.timeAndDate().future(730, TimeUnit.DAYS)), null],
                [faker.lorem().characters(1, 300), null],
                [faker.lorem().characters(1, 300), null],
                [faker.lorem().paragraph(), null],
                [Date.from(faker.timeAndDate().future(180, TimeUnit.DAYS)), null],
                [ProjectStatus.values(), null].flatten(),
                [faker.address().timeZone(), null],
                [(+faker.number()), null],
                [(+faker.number()), null],
                [(+faker.number()), null]
        ].combinations().getFirst()
    }
}
