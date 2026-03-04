package org.justserve

import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.client.multipart.MultipartBody
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import net.datafaker.Faker
import org.apache.commons.lang3.RandomStringUtils
import org.justserve.client.UserClient
import org.justserve.model.UserHashRequestByEmail

import static io.micronaut.http.HttpStatus.UNAUTHORIZED

@MicronautTest
class UserClientSpec extends JustServeSpec {

    def setupSpec() {

    }

    def "create user #{user.firstName} #{user.lastName} #{user.email} #{user.password} #{user.zipcode} #{user.locale} #{user.country} #{user.countryCode}"() {
        given:
        TestUser user = new TestUser(new Faker(Locale.of("en-us")))
        MultipartBody requestBody = MultipartBody.builder()
                .addPart("firstName", user.firstName)
                .addPart("lastName", user.lastName)
                .addPart("email", RandomStringUtils.insecure().nextAlphanumeric(20) + "@fake.com")
                .addPart("password", "JustServe2026")
                .addPart("postal", user.zipcode)
                .addPart("language", user.locale)
                .addPart("countryCode", user.countryCode)
                .addPart("country", user.country)
                .addPart("termsChecked", "true")
                .build()
        when:
        client.createUser(requestBody)

        then:
        noExceptionThrown()

        where:
        client           | _
        adminUserClient  | _
        noAuthUserClient | _
    }

    def "can get admin context for a user as an admin"(UserClient client) {
        when:
        def response = client.getAdminContext(readOnlyUser.uuid)

        then:
        response.body() != null

        where:
        client          | _
        adminUserClient | _
    }

    def "cannot get admin context for a user if not an admin"(UserClient client) {
        when:
        client.getAdminContext(readOnlyUser.uuid)

        then:
        def exception = thrown(HttpClientResponseException)
        exception.status == UNAUTHORIZED

        where:
        client           | _
        noAuthUserClient | _
    }

    def "can get tempPassword for a user as an admin"(UserClient client) {
        when:
        client.getTempPassword(new UserHashRequestByEmail(readOnlyUser.email))

        then:
        noExceptionThrown()

        where:
        client          | _
        adminUserClient | _
    }

    def "cannot get tempPassword for a user if not an admin"(UserClient client) {
        when:
        client.getTempPassword(new UserHashRequestByEmail(readOnlyUser.email))

        then:
        def exception = thrown(HttpClientResponseException)
        exception.status == UNAUTHORIZED

        where:
        client           | _
        noAuthUserClient | _
    }

    def "can get all user information for a user as an admin"(UserClient client) {
        when:
        client.getAllUserInformation(readOnlyUser.uuid, true, true, 1)

        then:
        noExceptionThrown()

        where:
        client          | _
        adminUserClient | _
    }
}
