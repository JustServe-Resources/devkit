package org.justserve

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import spock.lang.Shared
import spock.lang.Unroll

import java.util.regex.Pattern

class BaseCommandSpec extends JustServeSpec {

    @Shared
    Pattern cliVersion, ansiRegex, blankRegex, successRegex, errorRegex, tokenNotSetRegex

    def setupSpec() {
        def props = new Properties()
        new File('gradle.properties').withInputStream { stream ->
            props.load(stream)
        }
        def ansi = "\\u001B\\[[;\\d]*m"
        def staticText = """     _           _    ____
    | |         | |  / ___|
    | |_   _ ___| |_| (___   ___ _ ____   _____
    | | | | / __| __|\\___ \\ / _ \\ '__\\ \\ / / _ \\
 ___| | |_| \\__ \\ |_ ____) |  __/ |   \\ V /  __/
|____/ \\__,_|___/\\__|_____/ \\___|_|    \\_/ \\___|""";
        cliVersion = Pattern.compile("^${ansi}${Pattern.quote(staticText)}${ansi}\\s*${ansi}" +
                Pattern.quote(props.getProperty('justserveCliVersion')) + "${ansi}\\s*\$")
        blankRegex = Pattern.compile "^\\s*\$"
        successRegex = Pattern.compile("^${ansi}\\w+${ansi}\\s*\$")
        errorRegex = Pattern.compile("(?is)^${ansi}received an unexpected response from JustServe:${ansi}.*\\d+ \\(.*?\\)${ansi}\\s*\$")
        tokenNotSetRegex = Pattern.compile("(?s)^${ansi}NO AUTHENTICATION PROVIDED${ansi}.*" +
                "${ansi}The Authentication token is not assigned as an environment variable\\.${ansi}.*" +
                "${ansi}Please define the environment variable \"JUSTSERVE_TOKEN\" and try again\\.${ansi}\\s*\$")
    }

    @Unroll("command with args: calling 'justserve #flag #email' works as expected")
    def "commands to query temporary password should behave as expected with or without authentication"() {
        when:
        def (outputStream, errorStream) = executeCommand(context as ApplicationContext, [flag, email] as String[])

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

    @Unroll
    def "querying version returns current version, with or without authentication"() {
        when:
        def (outputStream, errorStream) = executeCommand(context as ApplicationContext, args as String[])

        then:
        verifyAll {
            outputStream.matches(cliVersion)
            errorStream.matches(blankRegex)
        }

        where:
        [args, context] << [[['-v'], ['--version'], ['version']], [noAuthCtx, ctx]].combinations()
    }

    /**
     * Executes a command with the given arguments and a specific application context,
     * capturing stdout and stderr.
     *
     * @param ctx The application context to use for running the command.
     * @param args The arguments to pass to the command.
     * @return An array containing the captured stdout (at index 0) and stderr (at index 1)
     *         as String representations of the output streams.
     */
    String[] executeCommand(ApplicationContext ctx, String... args) {
        OutputStream out = new ByteArrayOutputStream()
        OutputStream err = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        System.setErr(new PrintStream(err))
        PicocliRunner.run(BaseCommand.class, ctx, args)
        return new String[]{out.toString(), err.toString()}
    }

    String stripColor(String string) {
        return ansiRegex.matcher(string).replaceAll("")
    }

}
