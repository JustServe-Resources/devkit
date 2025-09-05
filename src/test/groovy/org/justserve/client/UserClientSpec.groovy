package org.justserve.client


import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.justserve.JustServeSpec
import org.justserve.model.UserHashRequestByEmail
import spock.lang.Shared

class UserClientSpec extends JustServeSpec {

    @Shared
    UserClient noAuthUserClient, userClient


    def setupSpec() {
        noAuthUserClient = noAuthCtx.getBean(UserClient)
        userClient = ctx.getBean(UserClient)
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
        HttpStatus.OK    | userEmail             | null                        | null             | userClient       | _
        null             | "notanemail@mail.moc" | HttpClientResponseException | "\"status\":500" | userClient       | _
        null             | userEmail             | HttpClientResponseException | "\"status\":401" | noAuthUserClient | _
        null             | "notanemail@mail.moc" | HttpClientResponseException | "\"status\":401" | noAuthUserClient | _
    }
}
