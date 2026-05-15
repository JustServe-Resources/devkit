package org.justserve

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.client.LocationsClient
import org.justserve.model.CountryStatePair
import spock.lang.Shared

/**
 * Test API endpoints under /api/v1/locations/*
 *
 * @author Peter Madsen
 */
@MicronautTest()
class LocationsSpec extends JustServeSpec {

    /** List of language codes currently supported */
    @Shared
    String[] langCodes

    @Shared
    LocationsClient locationsClient

    def setupSpec() {
        locationsClient = noAuthCtx.getBean(LocationsClient)
        langCodes = ["eng"] // TODO something to get supported codes
    }

    def "validate location can be serialized for code #lang"(String lang) {
        when:
        def locations = locationsClient.getLanguage(lang)

        then:
        verifyAll {
            locations.each { loc ->
                loc instanceof CountryStatePair
            }
        }

        where:
        lang << langCodes
    }
}
