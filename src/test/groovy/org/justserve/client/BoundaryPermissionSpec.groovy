package org.justserve.client


import org.justserve.JustServeSpec
import spock.lang.Shared

class BoundaryPermissionSpec extends JustServeSpec {

    @Shared
    BoundaryPermissionClient noAuthClient, authClient

    def setupSpec() {
        noAuthClient = noAuthCtx.getBean(BoundaryPermissionClient)
        authClient = ctx.getBean(BoundaryPermissionClient)
    }

//    def ""() {
//        when:
//        def request = new BoundaryUpdateRequest()
//
//        then:
//        response.status() == expectedStatus
//        if (expectedStatus == HttpStatus.OK) {
//            response.body() != null
//        }
//
//        where:
//        expectedStatus | client       | title
//        HttpStatus.OK  | authClient   | "auth client"
//        HttpStatus.OK  | noAuthClient | "no auth client"
//    }


}
