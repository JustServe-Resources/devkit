package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.client.LocationClient
import org.justserve.model.CountryContext
import spock.lang.Shared

/**
 * Test API endpoints under /api/v1/locations/*
 *
 * @author Peter Madsen
 */
@MicronautTest()
class LocationSpec extends JustServeSpec {

    @Shared
    LocationClient noAuthLocationClient

    @Shared
    LocationClient adminLocationClient

    def setupSpec() {
        noAuthLocationClient = noAuthCtx.getBean(LocationClient)
        adminLocationClient = ctx.getBean(LocationClient)
    }

    def "Can query LocationClient.getLocation(String) with no error as #userType"(LocationClient client, String userType, String lang){

        when:
        CountryContext[] locations = client.getLocation(lang).block()

        then:
        noExceptionThrown()

        where:
        client                  | userType      | lang
        noAuthLocationClient    | "no auth"     | "eng"
        adminLocationClient     | "admin"       | "eng"

    }

    def "Data from LocationClient.getLocation(String) matches expected format when queried as #userType"(LocationClient client, String userType, String lang) {
        when:
        CountryContext location = client.getLocation(lang).block().first

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

        where:
        client                  | userType      | lang
        noAuthLocationClient    | "no auth"     | "eng"
        adminLocationClient     | "admin"       | "eng"
    }
}
