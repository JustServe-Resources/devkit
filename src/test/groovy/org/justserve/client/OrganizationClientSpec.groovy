package org.justserve.client


import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.justserve.JustServeSpec
import org.justserve.model.ImageUploadRequest
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

    def "create an org with no error"() {
        given:
        def profileImage = authImageClient.uploadImage(
                new ImageUploadRequest(faker.image().base64JPG().split(",")[1], 256, 256, false, 0, 0)
        )
        String urlSlug = null
        while (null == urlSlug) {
            def potentialSlug = faker.word().noun()
            def response = dynamicRoutingClient.getOrgIdFromSlug(potentialSlug)
            if (response.status() == HttpStatus.NOT_FOUND) {
                urlSlug = potentialSlug
            }

        }
        def orgCreationRequest = new OrganizationCreateRequest()
                .setLogo(profileImage.body().displayFileName)
                .setContactEmail(faker.internet().emailAddress())
                .setDescription(faker.chuckNorris().fact())
                .setLocationString("Latitude, Longitude : 32.75338, -96.80831,  Dallas,  Dallas County,  Texas,  75203,  United States")
                .setName(faker.company().name())
                .set_public(true)
                .setUrl(urlSlug)

        when:
        authClient.createOrganization(orgCreationRequest)

        then:
        noExceptionThrown()
    }

    def "create an org with with #title and expected results"() {
        given:
        def profileImage = authImageClient.uploadImage(
                new ImageUploadRequest(faker.image().base64JPG().split(",")[1], 256, 256, false, 0, 0)
        )
        String urlSlug = null
        while (null == urlSlug) {
            def potentialSlug = faker.word().noun()
            def slugQueryResponse = dynamicRoutingClient.getOrgIdFromSlug(potentialSlug)
            if (slugQueryResponse.status() == HttpStatus.NOT_FOUND) {
                urlSlug = potentialSlug
            }

        }
        def orgCreationRequest = new OrganizationCreateRequest()
                .setLogo(profileImage.body().displayFileName)
                .setContactEmail(faker.internet().emailAddress())
                .setDescription(faker.chuckNorris().fact())
                .setLocationString("Latitude, Longitude : 32.75338, -96.80831,  Dallas,  Dallas County,  Texas,  75203,  United States")
                .setName(faker.company().name())
                .set_public(true)
                .setUrl(urlSlug)

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
        expectedStatus     | client       | title
        HttpStatus.CREATED | authClient   | "auth client"
        HttpStatus.CREATED | noAuthClient | "no auth client"
    }

}
