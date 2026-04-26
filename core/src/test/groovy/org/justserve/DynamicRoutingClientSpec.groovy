package org.justserve


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

    def "can query orgId for #url"(DynamicRoutingClient client, String url) {
        when:
        client.getOrgIdFromSlug(url).block()

        then:
        noExceptionThrown()

        where:
        [url, client] << [realOrgSlug, [authClient, noAuthClient]].combinations()
    }

    def "attempting to query the orgId for #url fails as expected"(DynamicRoutingClient client, String url) {
        when:
        client.getOrgIdFromSlug(url).block()

        then:
        thrown(HttpClientResponseException)

        where:
        [url, client] <<  ["thisisafakeurl", [authClient, noAuthClient]].combinations()
    }
}
