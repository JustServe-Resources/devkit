package org.justserve.client


import io.micronaut.http.HttpStatus
import org.justserve.JustServeSpec
import org.justserve.model.OrganizationSearchRequest
import spock.lang.Shared

class OrganizationClientSpec extends JustServeSpec {

    @Shared
    OrganizationClient noAuthClient, authClient

    def setupSpec() {
        noAuthClient = noAuthCtx.getBean(OrganizationClient)
        authClient = ctx.getBean(OrganizationClient)
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

    def "get admins for a given org"() {
        given:
        def search = new OrganizationSearchRequest()
                .setLocation("Elk Grove, CA 95758, USA")
                .setSortBy("az")
        UUID orgID = client.searchByLocation(search).body().organizations.first.id

        when:
        def response = client.getOrgOwners(orgID)

        then:
        noExceptionThrown()

        where:
        expectedStatus | client       | title
        HttpStatus.OK  | authClient   | "auth client"
        HttpStatus.OK  | noAuthClient | "no auth client"
    }

}
