package org.justserve

import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import net.datafaker.Faker
import spock.lang.Shared
import spock.lang.Specification

@MicronautTest()
class JustServeSpec extends Specification {

    @Shared
    Faker faker

    @Shared
    ApplicationContext ctx, noAuthCtx

    @Shared
    String userEmail

    def setupSpec() {
        faker = new Faker()
        userEmail = "jimmy@justserve.org"

        if (null != System.getenv("JUSTSERVE_TOKEN")) {
            throw new IllegalStateException("JUSTSERVE_TOKEN is set. Do not define this variable in testing.")
        }
        ctx = ApplicationContext.builder()
                    .environments(Environment.CLI, Environment.TEST)
                    .properties([
                            "justserve.token": System.getenv("TEST_TOKEN")
                    ])
                    .build()
                    .start()
        noAuthCtx = ApplicationContext
                    .builder()
                    .environments(Environment.CLI, Environment.TEST)
                    .environmentVariableExcludes("JUSTSERVE_TOKEN")
                    .build()
                    .start()
        }

    void cleanupSpec() {
        noAuthCtx.stop()
        ctx.stop()
    }
}
