package org.justserve

import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.client.BoundaryPermissionClient
import spock.lang.Shared

import static io.micronaut.http.HttpStatus.FORBIDDEN
import static io.micronaut.http.HttpStatus.UNAUTHORIZED

@MicronautTest
class BoundaryPermissionSpec extends JustServeSpec {

    @Shared
    BoundaryPermissionClient noAuthBoundaryPermissionClient, authBoundaryPermissionClient

    def setupSpec() {
        noAuthBoundaryPermissionClient = noAuthCtx.getBean(BoundaryPermissionClient)
        authBoundaryPermissionClient = ctx.getBean(BoundaryPermissionClient)
    }

    def "successfully reassign organization admin"() {
        given:
        UUID userID = createUser().getId()
        UUID orgID = createOrg()

        when:
        authBoundaryPermissionClient.makeAdminForOrg(orgID, userID).block()

        then:
        authOrgClient.getOrgOwners(orgID).block().stream().anyMatch { user -> (user.id == userID) }
    }

    def "fail to reassign organization admin #title"() {
        given:
        UUID userID = createUser().getId()

        when:
        client.makeAdminForOrg(orgID, userID).block()

        then:
        def exception = thrown(HttpClientResponseException)
        exception.status == expectedError

        where:
        client                         | expectedError | orgID             | title
        noAuthBoundaryPermissionClient | UNAUTHORIZED  | createOrg()       | "as an unauthorized user"
        authBoundaryPermissionClient   | FORBIDDEN     | UUID.randomUUID() | "with a non-existent OrgID"
    }


}
