package org.justserve.client

import io.micronaut.http.HttpResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.justserve.JustServeSpec
import org.justserve.model.UserHashRequestByEmail
import spock.lang.Shared

import static io.micronaut.http.HttpStatus.*

class UserClientSpec extends JustServeSpec {

    @Shared
    UserClient noAuthUserClient, userClient


    def setupSpec() {
        noAuthUserClient = noAuthCtx.getBean(UserClient)
        userClient = ctx.getBean(UserClient)
    }

    def "get admin context for #description"() {
        when:
        def response = null
        def thrownException = null
        try {
            response = client.getAdminContext(UUID.fromString(uid))
        } catch (HttpClientResponseException e) {
            thrownException = e
        }

        then:
        if (thrownException) {
            thrownException.status == expectedStatus
        } else {
            response.status == expectedStatus
            response.body() != null
            if (expectedStatus == OK) {
                response.body().userId == UUID.fromString(uid)
            }
        }

        where:
        description            | uid                                    | client           | expectedStatus
        "a valid user"         | "e23d029c-25f6-4c93-aada-dcbdc6d50c2c" | userClient       | OK
        "an invalid user"      | faker.internet().uuid()                | userClient       | NOT_FOUND
        "a valid user no-auth" | "e23d029c-25f6-4c93-aada-dcbdc6d50c2c" | noAuthUserClient | UNAUTHORIZED
    }

    def "get tempPassword for #email"() {
        when:
        HttpResponse<String> response = null
        def caughtException = null
        def message = null
        try {
            response = client.getTempPassword(new UserHashRequestByEmail(email))
        } catch (e) {
            caughtException = e.class
            message = e.message
        }

        then:
        if (null != caughtException) {
            verifyAll {
                caughtException == expectedException
                message.contains(expectedMessage)
            }
        } else {
            verifyAll {
                expectedResponse == response.status()
                response.body() != null
            }
        }

        where:
        expectedResponse | email                 | expectedException           | expectedMessage  | client           | _
        OK               | userEmail             | null                        | null             | userClient       | _
        null             | "notanemail@mail.moc" | HttpClientResponseException | "\"status\":500" | userClient       | _
        null             | userEmail             | HttpClientResponseException | "\"status\":401" | noAuthUserClient | _
        null             | "notanemail@mail.moc" | HttpClientResponseException | "\"status\":401" | noAuthUserClient | _
    }
}
