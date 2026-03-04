package org.justserve.cli.command

import io.micronaut.context.ApplicationContext
import spock.lang.Shared

//@Execution(SAME_THREAD)
class MakeOrgAdminSpec extends BaseCommandSpec {

    @Shared
    UUID sharedUserID

    @Shared
    List<UUID> sharedOrgs

    def setupSpec() {
        sharedUserID = createUser().body().id
        sharedOrgs = createTestOrgs(3)
    }

    def "can make a user an admin to #orgCount org(s) using the #orgFlag and #userFlag flags #title"() {
        given:
        def orgs = sharedOrgs.take(orgCount).join(",")

        when:
        def (outputStream, errorStream) = executeCommand(context as ApplicationContext, ["makeOrgAdmin", orgFlag, orgs, userFlag, sharedUserID] as String[])

        then:
        if (context == noAuthCtx) {
            verifyAll {
                errorStream.matches(tokenNotSetRegex)
                outputStream.matches(blankRegex)
            }
            return
        }
        verifyAll {
            (outputStream as String).contains("successfully reassigned ${orgCount} orgs to user ${sharedUserID}")
            errorStream.matches(blankRegex)
        }

        where:
        orgFlag | orgCount | userFlag | context   | title                                                         | _
        "-o"    | 3        | "-u"     | ctx       | "as an authorized user successfully makes the changes"        | _
        "--org" | 3        | "-u"     | ctx       | "as an authorized user successfully makes the changes"        | _
        "-o"    | 3        | "--user" | ctx       | "as an authorized user successfully makes the changes"        | _
        "-o"    | 3        | "-u"     | noAuthCtx | "as unauthorized user and fails prior to making any api call" | _
        "--org" | 3        | "-u"     | noAuthCtx | "as unauthorized user and fails prior to making any api call" | _
        "-o"    | 3        | "--user" | noAuthCtx | "as unauthorized user and fails prior to making any api call" | _
        "-o"    | 1        | "-u"     | noAuthCtx | "as unauthorized user and fails prior to making any api call" | _
        "--org" | 1        | "-u"     | noAuthCtx | "as unauthorized user and fails prior to making any api call" | _
        "-o"    | 1        | "--user" | noAuthCtx | "as unauthorized user and fails prior to making any api call" | _
        "-o"    | 1        | "-u"     | ctx       | "as an authorized user successfully makes the changes"        | _
        "--org" | 1        | "-u"     | ctx       | "as an authorized user successfully makes the changes"        | _
        "-o"    | 1        | "--user" | ctx       | "as an authorized user successfully makes the changes"        | _
    }

    def "can make a user an admin to #orgCount where at least one org ID does not exist on JustServe"() {
        given:
        String orgs
        def fakeId = faker.internet().uuid().toString()
        if (orgCount == 1) {
            orgs = fakeId
        } else {
            orgs = sharedOrgs.take(orgCount - 1).join(",") + "," + fakeId
        }

        when:
        def (outputStream, errorStream) = executeCommand(context as ApplicationContext, ["makeOrgAdmin", orgFlag, orgs, userFlag, sharedUserID] as String[])

        then:
        verifyAll {
            (outputStream as String).contains("successfully reassigned ${orgCount - 1} orgs to user ${sharedUserID}")
            (errorStream as String).contains("Failed to make user ${sharedUserID} an admin for organization ${fakeId}.")
        }

        where:
        orgFlag | orgCount | userFlag | context | title                                                  | _
        "-o"    | 3        | "-u"     | ctx     | "as an authorized user successfully makes the changes" | _
        "--org" | 3        | "-u"     | ctx     | "as an authorized user successfully makes the changes" | _
        "-o"    | 3        | "--user" | ctx     | "as an authorized user successfully makes the changes" | _
        "-o"    | 1        | "-u"     | ctx     | "as an authorized user successfully makes the changes" | _
        "--org" | 1        | "-u"     | ctx     | "as an authorized user successfully makes the changes" | _
        "-o"    | 1        | "--user" | ctx     | "as an authorized user successfully makes the changes" | _
    }


    def "can make a user an admin to #orgCount where at least one org Slug does not exist on JustServe"() {
        given:
        String orgs
        def fakeSlug = faker.internet().slug().toString()
        if (orgCount == 1) {
            orgs = fakeSlug
        } else {
            // We can't easily get slugs for the sharedOrgs since they are just UUIDs in the list.
            // However, the original test was searching for orgs by location to get slugs.
            // To keep it fast, we can just fetch one set of slugs in setupSpec if needed,
            // or just do the search once here if we really need slugs.
            // But wait, createTestOrgs returns UUIDs.
            // The original test used: authOrgClient.searchByLocation(...).getOrganizations().url
            // Let's just do that search once in the test method, but limit it.
            // Actually, better yet, let's just use the search here but only call it if orgCount > 1.
            // Since we are optimizing, let's try to reuse the search result if possible, but for now
            // let's stick to the original logic for the slug part but reuse the user.
            orgs = authOrgClient.searchByLocation(createSearchRequestForElkGrove()).body().getOrganizations().url.take(orgCount - 1).join(",") + "," + fakeSlug
        }

        when:
        def (outputStream, errorStream) = executeCommand(context as ApplicationContext, ["makeOrgAdmin", orgFlag, orgs, userFlag, sharedUserID] as String[])

        then:
        verifyAll {
            (outputStream as String).contains("successfully reassigned ${orgCount - 1} orgs to user ${sharedUserID}")
            (errorStream as String).contains("Error The org '${fakeSlug}' is not found on JustServe")
        }

        where:
        orgFlag | orgCount | userFlag | context | title                                                  | _
        "-o"    | 3        | "-u"     | ctx     | "as an authorized user successfully makes the changes" | _
        "--org" | 3        | "-u"     | ctx     | "as an authorized user successfully makes the changes" | _
        "-o"    | 3        | "--user" | ctx     | "as an authorized user successfully makes the changes" | _
        "-o"    | 1        | "-u"     | ctx     | "as an authorized user successfully makes the changes" | _
        "--org" | 1        | "-u"     | ctx     | "as an authorized user successfully makes the changes" | _
        "-o"    | 1        | "--user" | ctx     | "as an authorized user successfully makes the changes" | _
    }
}
