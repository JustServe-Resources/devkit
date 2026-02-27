package org.justserve

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.client.DynamicRoutingClient
import org.justserve.model.DynamicRoutingDataResponse
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
        realOrgSlug = authOrgClient.searchByLocation(createSearchRequestForElkGrove()).body().getOrganizations().url.first().toString()
    }

    def "get orgId for #url"() {
        when:
        HttpResponse<DynamicRoutingDataResponse> response = client.getOrgIdFromSlug(url)
        then:
        response.status() == expectedStatus
        if (expectedStatus == HttpStatus.OK) {
            response.body().id != null
        }

        where:
        url         | expectedStatus       | client
        realOrgSlug | HttpStatus.OK        | authClient
        realOrgSlug | HttpStatus.OK        | noAuthClient
        "1234"      | HttpStatus.NOT_FOUND | authClient
        "1234"      | HttpStatus.NOT_FOUND | noAuthClient
    }
}
