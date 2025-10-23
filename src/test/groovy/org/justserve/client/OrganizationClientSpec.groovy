package org.justserve.client

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.justserve.JustServeSpec
import org.justserve.model.OrganizationCreateRequest
import spock.lang.Shared

class OrganizationClientSpec extends JustServeSpec {
    @Shared
    OrganizationClient noAuthClient, authClient


    def setupSpec() {
        noAuthClient = noAuthCtx.getBean(OrganizationClient)
        authClient = ctx.getBean(OrganizationClient)
    }

    def "can add org using #title"() {
        when:
        def response = null
        def caughtException = null
        def exceptionStatusCode=null
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
                .setVolunteerCenter(null)
                .setWebsite(faker.internet().url())
        try {
            response = client.createOrganization(orgRequest)
        } catch (HttpClientResponseException e) {
            //in case we want to test we recieved a response in the correct schema
//            caughtException = e.class
            exceptionStatusCode=e.status
        }


        then:
        exceptionStatusCode==expectedStatus || response.status() == expectedStatus


        where:
        expectedStatus          | client       | title
        HttpStatus.CREATED      | authClient   | "auth client"
        HttpStatus.UNAUTHORIZED | noAuthClient | "no auth client"

    }
}
