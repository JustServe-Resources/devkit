package org.justserve.command

import io.micronaut.context.ApplicationContext
import net.datafaker.Faker
import org.justserve.TestUser
import org.justserve.client.UserClient
import spock.lang.Shared
import spock.lang.Unroll

class GetTempPasswordSpec extends BaseCommandSpec {
    @Shared
    TestUser readOnlyUser

    def setupSpec() {
        noAuthCtx.getBean(UserClient)
        readOnlyUser = new TestUser(new Faker(Locale.of("en-us")))
        readOnlyUser.uuid = UUID.fromString(createUser(readOnlyUser).body().getId())
    }

    @Unroll("getting temp password with '#flag' and '#email' returns ")
    def "commands to query temporary password should behave as expected with or without authentication"() {
        when:
        def (outputStream, errorStream) = executeCommand(context as ApplicationContext, ["getTempPassword", flag, email] as String[])

        then:
        verifyAll {
            outputStream.matches(expectedOutput)
            errorStream.matches(expectedError)
        }
        where:
        flag      | email                 | context   | expectedOutput | expectedError    | expectation                                           | _
        '-e'      | 'notanemail@mail.moc' | ctx       | blankRegex     | errorRegex       | "should provide error from api call in printout"      | _
        '-e'      | 'notanemail@mail.moc' | noAuthCtx | blankRegex     | tokenNotSetRegex | "should provide error that user is not authenticated" | _
        '-e'      | user.email            | ctx       | successRegex   | blankRegex       | "should print out the temp password"                  | _
        '-e'      | user.email            | noAuthCtx | blankRegex     | tokenNotSetRegex | "should provide error that user is not authenticated" | _
        '--email' | 'notanemail@mail.moc' | ctx       | blankRegex     | errorRegex       | "should provide error from api call in printout"      | _
        '--email' | 'notanemail@mail.moc' | noAuthCtx | blankRegex     | tokenNotSetRegex | "should provide error that user is not authenticated" | _
        '--email' | user.email            | ctx       | successRegex   | blankRegex       | "should print out the temp password"                  | _
        '--email' | user.email            | noAuthCtx | blankRegex     | tokenNotSetRegex | "should provide error that user is not authenticated" | _
    }
}
