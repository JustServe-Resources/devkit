package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.client.LanguageClient
import spock.lang.Shared
import spock.lang.Unroll

@MicronautTest
class LanguageSpec extends JustServeSpec {

    @Shared
    LanguageClient languageClient, noAuthLanguageClient

    def setupSpec() {
        languageClient = ctx.getBean(LanguageClient)
        noAuthLanguageClient = noAuthCtx.getBean(LanguageClient)
    }

    @Unroll ("Can query LanguageClient.getLanguages() with no error as #userType")
    def "can query languages with no error"(LanguageClient client, String userType){
        when:
        client.getLanguages().block()

        then:
        noExceptionThrown()

        where:
        [client, userType] << [[languageClient, "no auth"], [noAuthLanguageClient, "standard"]]
    }

}
