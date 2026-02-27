package org.justserve

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.client.DynamicRoutingClient
import org.justserve.client.OrganizationClient
import org.justserve.model.OrganizationCreateRequest
import spock.lang.Shared

@MicronautTest
class OrganizationClientSpec extends JustServeSpec {
    @Shared
    OrganizationClient noAuthClient

    @Shared
    DynamicRoutingClient dynamicRoutingClient


    def setupSpec() {
        noAuthClient = noAuthCtx.getBean(OrganizationClient)
        dynamicRoutingClient = ctx.getBean(DynamicRoutingClient)
    }

    def "using searchByLocation() should work when using #title"() {
        when:
        def search = createSearchRequestForElkGrove()
        def response = client.searchByLocation(search)

        then:
        response.status() == expectedStatus
        if (expectedStatus == HttpStatus.OK) {
            response.body() != null
            response.body().organizations.size() > 0
        }

        where:
        expectedStatus | client        | title
        HttpStatus.OK  | authOrgClient | "auth client"
        HttpStatus.OK  | noAuthClient  | "no auth client"
    }

    def "get admins for a given org with no error"() {
        given:
        def search = createSearchRequestForElkGrove()
        UUID orgID = client.searchByLocation(search).body().organizations.first.id

        when:
        client.getOrgOwners(orgID)

        then:
        noExceptionThrown()

        where:
        expectedStatus | client        | title
        HttpStatus.OK  | authOrgClient | "auth client"
        HttpStatus.OK  | noAuthClient  | "no auth client"
    }

    def "create an org with no error"() {
        given:
        def orgRequest = new OrganizationCreateRequest()
                .setContactEmail(faker.internet().emailAddress())
                .setContactName(faker.zelda().character())
                .setContactPhone(faker.phoneNumber().phoneNumber())
                .setDescription(faker.zelda().game())
                .setLocationString(knownWorkingLocation)
                .setLogo(getUploadedImageFileName())
                .setName(faker.zelda().character())
                .set_public(null)
                .setUrl(getUniqueSlug())
                .setVolunteerCenterInfo(null)
                .setWebsite(faker.internet().url())
        when:
        authOrgClient.createOrganization(orgRequest)

        then:
        noExceptionThrown()
    }

    def "create an org with with #title and expected results"() {
        given:
        def orgCreationRequest = new OrganizationCreateRequest()
                .setLogo(getUploadedImageFileName())
                .setContactEmail(faker.internet().emailAddress())
                .setDescription(faker.chuckNorris().fact())
                .setLocationString(knownWorkingLocation)
                .setName(faker.company().name())
                .set_public(true)
                .setUrl(getUniqueSlug())

        when:
        def response = client.createOrganization(orgCreationRequest)

        then:
        if (expectedStatus == HttpStatus.CREATED) {
            null == response
            return
        }
        def exception = thrown(HttpClientResponseException)
        exception.status == expectedStatus

        where:
        expectedStatus     | client        | title
        HttpStatus.CREATED | authOrgClient | "auth client"
        HttpStatus.CREATED | noAuthClient  | "no auth client"
    }

}
