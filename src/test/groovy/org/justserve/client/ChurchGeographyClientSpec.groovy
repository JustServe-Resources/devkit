package org.justserve.client

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.justserve.JustServeSpec
import spock.lang.Shared

@MicronautTest
class ChurchGeographyClientSpec extends JustServeSpec {

    @Shared
    ChurchGeographyClient churchGeographyClient

    def setupSpec() {
        churchGeographyClient = ctx.getBean(ChurchGeographyClient)
    }

    void "can update church units"() {
        when:
        churchGeographyClient.updateChurchUnit(churchUnit)

        then:
        noExceptionThrown()

        where:
        churchUnit | _
        "780588"   | _ // Coordinating Council
        "96725"    | _ // Ward
        "389854"   | _ // Stake
    }

    void "updating church units fail properly"() {
        when:
        churchGeographyClient.updateChurchUnit(churchUnit)

        then:
        noExceptionThrown() // This is a defect

        where:
        churchUnit | _
        "@@^&*%^^" | _ // Bad Data
    }

}