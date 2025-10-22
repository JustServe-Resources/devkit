package org.justserve

import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import net.datafaker.Faker
import org.justserve.client.ImageClient
import org.justserve.client.UserClient
import spock.lang.Shared
import spock.lang.Specification

@MicronautTest()
class JustServeSpec extends Specification {

    @Shared
    Faker faker

    @Shared
    UserClient noAuthUserClient

    @Shared
    ImageClient authImageClient

    @Shared
    ApplicationContext ctx, noAuthCtx

    @Shared
    TestUser[] users


    def setupSpec() {
        faker = new Faker()
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
        noAuthUserClient = noAuthCtx.getBean(UserClient)
        users = new TestUser[]{new TestUser(new Faker(Locale.of("en-us")))}
        authImageClient = ctx.getBean(ImageClient)
    }

    void cleanupSpec() {
        noAuthCtx.stop()
        ctx.stop()
    }

    def createUser(UserClient client = noAuthUserClient, TestUser user) {
        return client.createUser(
                user.firstName,
                user.lastName,
                user.email,
                user.password,
                user.zipcode,
                user.locale,
                user.country,
                user.countryCode
        )
    }
}
