package org.justserve.command

import io.micronaut.context.ApplicationContext
import spock.lang.Unroll

class GetTempPasswordSpec extends BaseCommandSpec {

    @Unroll("command with args: calling 'justserve #flag #email' works as expected")
    def "commands to query temporary password should behave as expected with or without authentication"() {
        when:
        def (outputStream, errorStream) = executeCommand(context as ApplicationContext, ["getTempPassword", flag, email] as String[])

        then:
        if (context == noAuthCtx) {
            verifyAll {
                outputStream.matches(blankRegex)
                errorStream.matches(tokenNotSetRegex)
            }
        } else if (userEmail.equalsIgnoreCase(email as String)) {
            verifyAll {
                outputStream.matches(successRegex)
                errorStream.matches(blankRegex)
            }
        } else {
            verifyAll {
                outputStream.matches(blankRegex)
                errorStream.matches(errorRegex)
            }
        }
        where:
        [flag, email, context] << [['-e', '--email'], [userEmail, "notanemail@mail.moc"], [noAuthCtx, ctx]].combinations()
    }
}
