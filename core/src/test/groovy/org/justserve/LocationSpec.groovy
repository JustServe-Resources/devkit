package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.client.LanguageClient
import org.justserve.client.LocationClient
import spock.lang.Shared
import spock.lang.Unroll

@MicronautTest()
class LocationSpec extends JustServeSpec {

    @Shared
    LocationClient noAuthLocationClient, locationClient

    @Shared
    LanguageClient languageClient

    @Shared
    List<String> threeCharLocales

    def setupSpec() {
        noAuthLocationClient = noAuthCtx.getBean(LocationClient)
        locationClient = ctx.getBean(LocationClient)
        languageClient = ctx.getBean(LanguageClient)
        threeCharLocales = languageClient.getLanguages().block().collect { locale -> locale.getLang3char() }
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Unroll ("Can query LocationClient.getLocation(#lang) with no error as #userType")
    def "can query location with by a given language"(LocationClient client, String userType, String lang){
        when:
        client.getLocation(lang).block()

        then:
        noExceptionThrown()

        where:
        [[client, userType], lang] << [[[noAuthLocationClient, "no auth"], [locationClient, "standard"]], threeCharLocales].combinations()
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Unroll("Can query LocationClient.getAllCountryIdentifiers(#lang) with no error as #userType")
    def "can query all country identifiers for a given language"(LocationClient client, String userType, String lang) {
        when:
        client.getAllCountryIdentifiers(lang).block()

        then:
        noExceptionThrown()

        where:
        [[client, userType], lang] << [[[noAuthLocationClient, "no auth"], [locationClient, "standard"]], threeCharLocales].combinations()
    }

}
