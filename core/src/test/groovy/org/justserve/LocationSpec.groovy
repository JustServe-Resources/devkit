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

    def "result from language endpoint can be deserialized"() {
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
}
