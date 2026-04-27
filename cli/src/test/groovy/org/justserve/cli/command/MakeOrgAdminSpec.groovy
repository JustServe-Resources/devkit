package org.justserve.cli.command

import io.micronaut.context.ApplicationContext
import spock.lang.Shared

class MakeOrgAdminSpec extends BaseCommandSpec {

    @Shared
    UUID sharedUserID

    @Shared
    List<UUID> sharedOrgs

    def setupSpec() {
        sharedUserID = createUser().id
        sharedOrgs = createTestOrgs(3)
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "can make a user an admin to #orgCount org(s) using the #orgFlag and #userFlag flags as an authorized user"(String orgFlag, Integer orgCount, String userFlag) {
        given:
        def orgs = sharedOrgs.take(orgCount).join(",")

        when:
        def (outputStream, errorStream) = executeCommand(ctx as ApplicationContext, ["makeOrgAdmin", orgFlag, orgs, userFlag, sharedUserID] as String[])

        then:
        verifyAll {
            (outputStream as String).contains("successfully reassigned ${orgCount} orgs to user ${sharedUserID}")
            errorStream.matches(blankRegex)
        }

        where:
        [orgFlag, orgCount, userFlag] << [["-o", "--org"], [3, 1], ["-u", "--user"]].combinations()
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "fails to make a user an admin to #orgCount org(s) when unauthorized"(String orgFlag, Integer orgCount, String userFlag) {
        given:
        def orgs = sharedOrgs.take(orgCount).join(",")

        when:
        def (outputStream, errorStream) = executeCommand(noAuthCtx as ApplicationContext, ["makeOrgAdmin", orgFlag, orgs, userFlag, sharedUserID] as String[])

        then:
        verifyAll {
            errorStream.matches(tokenNotSetRegex)
            outputStream.matches(blankRegex)
        }

        where:
        [orgFlag, orgCount, userFlag] << [["-o", "--org"], [3, 1], ["-u", "--user"]].combinations()
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def "can make a user an admin to #orgCount where at least one org ID does not exist on JustServe using the #orgFlag and #userFlag flags"(
            String orgFlag, Integer orgCount, String userFlag) {
        given:
        String orgs
        def fakeId = faker.internet().uuid().toString()
        if (orgCount == 1) {
            orgs = fakeId
        } else {
            orgs = sharedOrgs.take(orgCount - 1).join(",") + "," + fakeId
        }

        when:
        def (outputStream, errorStream) = executeCommand(ctx as ApplicationContext, ["makeOrgAdmin", orgFlag, orgs, userFlag, sharedUserID] as String[])

        then:
        verifyAll {
            (outputStream as String).contains("successfully reassigned ${orgCount - 1} orgs to user ${sharedUserID}")
            (errorStream as String).contains("Failed to make user ${sharedUserID} an admin for organization ${fakeId}.")
        }

        where:
        [orgFlag, orgCount, userFlag] << [["-o", "--org"], [3, 1], ["-u", "--user"]].combinations()
    }


    @SuppressWarnings("GroovyAssignabilityCheck")
    def "can make a user an admin to #orgCount where at least one org Slug does not exist on JustServe using the #orgFlag and #userFlag flags"(String orgFlag, Integer orgCount, String userFlag) {
        given:
        String orgs
        def fakeSlug = faker.internet().slug().toString()
        if (orgCount == 1) {
            orgs = fakeSlug
        } else {
            orgs = authOrgClient.searchByLocation(createSearchRequestForElkGrove()).block().getOrganizations().url.take(orgCount - 1).join(",") + "," + fakeSlug
        }

        when:
        def (outputStream, errorStream) = executeCommand(ctx as ApplicationContext, ["makeOrgAdmin", orgFlag, orgs, userFlag, sharedUserID] as String[])

        then:
        verifyAll {
            (outputStream as String).contains("successfully reassigned ${orgCount - 1} orgs to user ${sharedUserID}")
            (errorStream as String).contains("Error The org '${fakeSlug}' is not found on JustServe")
        }

        where:
        [orgFlag, orgCount, userFlag] << [["-o", "--org"], [3, 1], ["-u", "--user"]].combinations()
    }
}
