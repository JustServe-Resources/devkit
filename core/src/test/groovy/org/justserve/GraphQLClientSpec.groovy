package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import net.datafaker.Faker
import org.justserve.client.GraphQLClient
import org.justserve.model.*
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
    void "can use createOngoingEvent() with combination of fields"(String contactEmail, String contactName, String contactPhone, Date end, Boolean hasGroupCap, Integer groupLimit, String locationLink, String locationName, String qrCodeImageLocation, Date renewDate, String schedule, String shiftTitle, String specialDirections, Date start, ProjectEventStatus status, String timezone, Integer totalVolunteersNeeded, Boolean hasVolunteerCap) {
        given:
        def project = client.createProject(new GraphQLCreateProjectVariables()
                .setTitle("this is a test")
                .setEventType(EventType.Ongoing)
                .setLocationType(ProjectLocationType.SINGLE_LOCATION)
        )
        def projectId = project
                .getData()
                .getCreateProject().getId()

        def event = new CreateEventFields()
                .setContactEmail(contactEmail)
                .setContactName(contactName)
                .setContactPhone(contactPhone)
                .setEnd(end)
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
            end: ${end}
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
            
            """
            new File('build/test-errors.log').append(errorLog)
        }
        noExceptionThrown()
        !response.hasErrors()

        where:
        [contactEmail, contactName, contactPhone, end, hasGroupCap, groupLimit, locationLink, locationName, qrCodeImageLocation, renewDate, schedule, shiftTitle, specialDirections, start, status, timezone, totalVolunteersNeeded, hasVolunteerCap] << [
                [
                        faker.internet().emailAddress(),
                        faker.name().fullName(),
                        faker.phoneNumber().phoneNumber(),
                        Date.from(faker.timeAndDate().future(365, TimeUnit.DAYS)),
                        true,
                        10,
                        faker.internet().url(),
                        faker.address().streetAddress(),
                        faker.internet().url(),
                        Date.from(faker.timeAndDate().future(730, TimeUnit.DAYS)),
                        faker.lorem().sentence(),
                        faker.lorem().word(),
                        faker.lorem().paragraph(),
                        Date.from(faker.timeAndDate().future(180, TimeUnit.DAYS)),
                        ProjectEventStatus.ACTIVE,
                        TimeZone.ARIZONA,
                        20,
                        true
                ]
        ]
    }
}
