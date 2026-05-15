package org.justserve

import io.micronaut.http.client.HttpClient
import io.micronaut.http.HttpRequest
import io.micronaut.core.type.Argument
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared

/**
 * Test API endpoints under /api/v1/locations/*
 *
 * @author Peter Madsen
 */
@MicronautTest()
class LocationSpec extends JustServeSpec {

    /** List of language codes currently supported */
    @Shared
    List langCodes

    @Shared
    HttpClient httpClient

    def setupSpec() {
        // initialize client
        // FIXME temporary fix, figure out why an injected HttpClient doesn't work
        httpClient = HttpClient.create(new URI("https://stage.justserve.org").toURL())

        // Fetch and extract language codes currently in use
        // TODO filter for duplicates, for instance "eng" is given for both en-us and en-gb
        langCodes = httpClient
                .toBlocking()
                .retrieve(HttpRequest.GET("/api/v1/languages"), Argument.listOf(Map))
                *.lang3char
    }

    def cleanupSpec() {
        httpClient?.close()
    }

    /**
     * Helper method to resolve nested paths
     *
     * @param obj JSON parent object
     * @param path path in the form abc.value
     * @return the value at the designated path
     */
    def getNestedValue(obj, String path) {
        path.split("\\.").inject(obj) { current, key ->
            current?.get(key)
        }
    }

    def "endpoint returns valid data types for language #langCode"(String langCode) {
        when:
        // List of CountryStatePair objects from response
        // TODO Add extra checks, retrieve() will not check that the status is 200 and that the body is non-null
        def locations = httpClient
                .toBlocking()
                .retrieve(HttpRequest.GET("/api/v1/locations/${langCode}"), Argument.listOf(Map))

        def rules = [
                [field: "countryInfo.twoCharCode",   type: String],
                [field: "countryInfo.threeCharCode", type: String],
                [field: "countryInfo.countryPhone",  type: String],
                [field: "country",                   type: String],
                [field: "state",                     type: String],
                [field: "county",                    type: String],
        ]

        then:
        verifyAll {
            // Validate each property
            locations.each { loc ->
                rules.each { rule ->
                    def value = getNestedValue(loc, (String) rule.field)
                    rule.type.isInstance(value)
                }
            }

            // TODO test if data can be serialized into a Location object
        }

        where:
        langCode << langCodes
    }
}
