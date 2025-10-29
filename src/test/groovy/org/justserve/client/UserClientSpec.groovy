package org.justserve.client

import io.micronaut.http.client.exceptions.HttpClientResponseException
import net.datafaker.Faker
import org.justserve.JustServeSpec
import org.justserve.TestUser
import org.justserve.model.UserHashRequestByEmail
import spock.lang.Shared

import static io.micronaut.http.HttpStatus.UNAUTHORIZED

class UserClientSpec extends JustServeSpec {

    @Shared
    UserClient userClient

    @Shared
    TestUser readOnlyUser


    def setupSpec() {
        userClient = ctx.getBean(UserClient)
        readOnlyUser = new TestUser(new Faker(Locale.of("en-us")))
        readOnlyUser.uuid = createUser(readOnlyUser).body().getId()
    }

    def "create user #{user.firstname} #{user.lastname} #{user.email} #{user.password} #{user.postal} #{user.locale} #{user.country} #{user.countryCode}"() {
        when:
        TestUser user = new TestUser(new Faker(Locale.of("en-us")))
        then:
        createUser(client, user)

        where:
        client           | _
        userClient       | _
        noAuthUserClient | _
    }

    def "get admin context for a generated user with as an admin"() {
        //todo: add user with admin context to testing
        when:
        def response = client.getAdminContext(readOnlyUser.uuid)

        then:
        if (!expectedError) {
            response.body() != null
            return
        }
        def exception = thrown(HttpClientResponseException)
        exception.status == expectedError

        where:
        expectedError | client           | _
        null          | userClient       | _
        UNAUTHORIZED  | noAuthUserClient | _

    }

    def "get tempPassword for a previously created user"() {
        given:
        when:
        def response = client.getTempPassword(new UserHashRequestByEmail(readOnlyUser.email))

        then:
        if (!expectedError) {
            response.body() != null
            return
        }
        def exception = thrown(HttpClientResponseException)
        exception.status == expectedError

        where:
        expectedError         | client           | _
        null                  | userClient       | _
        UNAUTHORIZED          | noAuthUserClient | _
    }
}
