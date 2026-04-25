package org.justserve

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.client.DynamicRoutingClient
import spock.lang.Shared

@MicronautTest
class DynamicRoutingClientSpec extends JustServeSpec {

    @Shared
    DynamicRoutingClient noAuthClient, authClient

    @Shared
    String realOrgSlug


    def setupSpec() {
        noAuthClient = noAuthCtx.getBean(DynamicRoutingClient)
        authClient = ctx.getBean(DynamicRoutingClient)
        realOrgSlug = authOrgClient.searchByLocation(createSearchRequestForElkGrove()).block().getOrganizations().url.first().toString()
    }

    def "get orgId for #url"() {
        when:
        def response = client.getOrgIdFromSlug(url).block()

        then:
        if (expectedStatus == HttpStatus.OK) {
            response.id != null
        } else {
            thrown(HttpClientResponseException)
        }


        where:
        url         | expectedStatus       | client
        realOrgSlug | HttpStatus.OK        | authClient
        realOrgSlug | HttpStatus.OK        | noAuthClient
        "1234"      | HttpStatus.NOT_FOUND | authClient
        "1234"      | HttpStatus.NOT_FOUND | noAuthClient
    }
}
