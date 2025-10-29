package org.justserve.client

import io.micronaut.http.HttpResponse
import org.justserve.JustServeSpec
import spock.lang.Shared

import static io.micronaut.http.HttpStatus.UNAUTHORIZED

class BoundaryPermissionSpec extends JustServeSpec {

    @Shared
    BoundaryPermissionClient noAuthBoundaryPermissionClient, authBoundaryPermissionClient

    def setupSpec() {
        noAuthBoundaryPermissionClient = noAuthCtx.getBean(BoundaryPermissionClient)
        authBoundaryPermissionClient = ctx.getBean(BoundaryPermissionClient)
    }

    def "can reassign org with no errors"() {
        given:
        UUID orgID = createOrg()
        UUID userID = createUser().body().id

        when:
        HttpResponse<Object> response = authBoundaryPermissionClient.makeAdminForOrg(orgID, userID)

        then:
        if (!expectedError) {
            verifyAll {
                null == response.body()
                authOrgClient.getOrgOwners(orgID).body().stream().anyMatch { user -> (user.id == userID) }
            }
            return
        }
        noExceptionThrown()

        where:
        client                         | expectedError | title                                           | _
        authBoundaryPermissionClient   | null          | "as an admin and the assignment takes hold"     | _
        noAuthBoundaryPermissionClient | UNAUTHORIZED  | "as an unauthorized user and the request fails" | _
    }


}
