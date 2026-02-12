package org.justserve.client

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.JustServeSpec
import org.justserve.model.UserResultBounded

@MicronautTest
class UserBoundedClientSpec extends JustServeSpec {

    def "Should be able to get details for a specific user without facing errors"(UserClient client) {
        when:
        UserResultBounded result = client.apiV1UsersUserIdBoundedGet(readOnlyUser.uuid)

        then:
        noExceptionThrown()

        where:
        client     | _
        userClient | _
//        noAuthUserClient | _
    }
}
