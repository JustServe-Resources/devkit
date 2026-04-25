package org.justserve

import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.client.BoundaryPermissionClient
import spock.lang.Shared

import static io.micronaut.http.HttpStatus.INTERNAL_SERVER_ERROR

@MicronautTest
class BoundaryPermissionSpec extends JustServeSpec {

    @Shared
    BoundaryPermissionClient noAuthBoundaryPermissionClient, authBoundaryPermissionClient

    def setupSpec() {
        noAuthBoundaryPermissionClient = noAuthCtx.getBean(BoundaryPermissionClient)
        authBoundaryPermissionClient = ctx.getBean(BoundaryPermissionClient)
    }

    def "can reassign organizations #title"() {
        given:
        UUID userID = createUser().getId()

        when:
        authBoundaryPermissionClient.makeAdminForOrg(orgID, userID).block()

        then:
        if (!expectedError) {
            verifyAll {
                authOrgClient.getOrgOwners(orgID).block().stream().anyMatch { user -> (user.id == userID) }
            }
            return
        }
        def exception = thrown(HttpClientResponseException)
        exception.status == expectedError

        where:
        client                         | expectedError         | title                                                    | orgID                                               | _
        authBoundaryPermissionClient   | null                  | "with a good OrgID as an admin and the request succeeds" | createOrg()                                         | _
        noAuthBoundaryPermissionClient | INTERNAL_SERVER_ERROR | "with a bad OrgID as an admin and the request fails"     | UUID.fromString(faker.internet().uuid().toString()) | _
//        noAuthBoundaryPermissionClient | UNAUTHORIZED  | "as an unauthorized user and the request fails" | createOrg() | _
    }


}
