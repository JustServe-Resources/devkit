package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.client.LocationClient
import org.justserve.model.CountryStatePair
import spock.lang.Shared

/**
 * Test API endpoints under /api/v1/locations/*
 *
 * @author Peter Madsen
 */
@MicronautTest()
class LocationSpec extends JustServeSpec {

    @Shared
    LocationClient locationClient

    def setupSpec() {
        locationClient = noAuthCtx.getBean(LocationClient)
    }

    def ".../locations/{language} endpoint returns without errors"() {
        given:
        def lang = "eng"

        when:
        locationClient.getLanguage(lang).block()

        then:
        noExceptionThrown()
    }

    def "result from .../locations/{language} can be deserialized"() {
        given:
        def lang = "eng"

        when:
        def locations = locationClient.getLanguage(lang)

        then:
        verifyAll {
            locations.each { loc ->
                loc instanceof CountryStatePair
            }
        }
    }

    def "result data from .../locations/{language} matches expected format"() {
        given:
        def lang = "eng"

        when:
        def location = locationClient.getLanguage(lang).block().first

        then:
        verifyAll {
            location != null
            location.getCountryInfo() != null

            location.getCountryInfo().getTwoCharCode() == null || location.getCountryInfo().getTwoCharCode() ==~ /[a-z]{2}/
            location.getCountryInfo().getThreeCharCode() == null || location.getCountryInfo().getThreeCharCode() ==~ /[a-z]{3}/
            location.getCountryInfo().getCountryPhone() == null || location.getCountryInfo().getCountryPhone() ==~ /\d+/
            location.getCountry() == null || location.getCountry() ==~ /.+/
            location.getState() == null || location.getState() ==~ /.+/
            location.getCounty() == null || location.getCounty() ==~ /.+/
        }
    }
}
