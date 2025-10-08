package org.justserve.client

import io.micronaut.http.HttpStatus
import org.justserve.JustServeSpec
import org.justserve.model.OrganizationSearch
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
        def search = new OrganizationSearch().setLocation("95758")
        def response = client.searchByLocation(search)

        then:
        response.status() == expectedStatus
        if (expectedStatus == HttpStatus.OK) {
            response.body() != null
        }

        where:
        expectedStatus | client       | title
        HttpStatus.OK  | authClient   | "auth client"
        HttpStatus.OK  | noAuthClient | "no auth client"
    }


}
