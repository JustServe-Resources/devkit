package org.justserve.client

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import org.justserve.JustServeSpec
import org.justserve.model.DynamicRoutingDataResponse
import spock.lang.Shared

class DynamicRoutingClientSpec extends JustServeSpec {

    @Shared
    DynamicRoutingClient noAuthClient, authClient


    def setupSpec() {
        noAuthClient = noAuthCtx.getBean(DynamicRoutingClient)
        authClient = ctx.getBean(DynamicRoutingClient)
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
        url            | expectedStatus       | client
        "blankparkzoo" | HttpStatus.OK        | authClient
        "blankparkzoo" | HttpStatus.OK        | noAuthClient
        "1234"         | HttpStatus.NOT_FOUND | authClient
        "1234"         | HttpStatus.NOT_FOUND | noAuthClient
    }
}
