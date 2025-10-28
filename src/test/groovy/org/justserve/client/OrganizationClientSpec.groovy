package org.justserve.client

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.justserve.JustServeSpec
import org.justserve.model.OrganizationCreateRequest
import org.justserve.model.OrganizationSearchRequest
import spock.lang.Shared

class OrganizationClientSpec extends JustServeSpec {
    @Shared
    OrganizationClient noAuthClient, authClient

    @Shared
    DynamicRoutingClient dynamicRoutingClient


    def setupSpec() {
        noAuthClient = noAuthCtx.getBean(OrganizationClient)
        authClient = ctx.getBean(OrganizationClient)
        dynamicRoutingClient = ctx.getBean(DynamicRoutingClient)
    }

    def "using searchByLocation() should work when using #title"() {
        when:
        def search = new OrganizationSearchRequest()
                .setLocation("Elk Grove, CA 95758, USA")
                .setSortBy("az")
        def response = client.searchByLocation(search)

        then:
        response.status() == expectedStatus
        if (expectedStatus == HttpStatus.OK) {
            response.body() != null
            response.body().organizations.size() > 0
        }

        where:
        expectedStatus | client       | title
        HttpStatus.OK  | authClient   | "auth client"
        HttpStatus.OK  | noAuthClient | "no auth client"
    }

    def "get admins for a given org with no error"() {
        given:
        def search = new OrganizationSearchRequest()
                .setLocation("Elk Grove, CA 95758, USA")
                .setSortBy("az")
        UUID orgID = client.searchByLocation(search).body().organizations.first.id

        when:
        client.getOrgOwners(orgID)

        then:
        noExceptionThrown()

        where:
        expectedStatus | client       | title
        HttpStatus.OK  | authClient   | "auth client"
        HttpStatus.OK  | noAuthClient | "no auth client"
    }

    def "can add org using #title"() {
        when:
        def response = null
        def exceptionStatusCode = null
        def orgRequest = new OrganizationCreateRequest()
                .setContactEmail(faker.internet().emailAddress())
                .setContactName(faker.zelda().character())
                .setContactPhone(faker.phoneNumber().phoneNumber())
                .setDescription(faker.zelda().game())
                .setLocationString("Latitude, Longitude : 40.55930, 15.30702,  Sicignano degli Alburni,  Provincia di Salerno,  Campania,  84029,  Italy")
                .setLogo("f7cd7609-7ba3-4e00-95ba-626265ddb218.png")
                .setName(faker.zelda().character())
                .set_public(null)
                .setUrl(faker.zelda().character())
                .setVolunteerCenterInfo(null)
                .setWebsite(faker.internet().url())
        try {
            response = client.createOrganization(orgRequest)
        } catch (HttpClientResponseException e) {
            exceptionStatusCode = e.status
        }


        then:
        exceptionStatusCode == expectedStatus || response.status() == expectedStatus


        where:
        expectedStatus          | client       | title
        HttpStatus.CREATED      | authClient   | "auth client"
        HttpStatus.UNAUTHORIZED | noAuthClient | "no auth client"

    }
}
