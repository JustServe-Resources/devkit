package org.justserve.command

import io.micronaut.context.ApplicationContext
import spock.lang.Execution

import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD

@Execution(SAME_THREAD)
class MakeOrgAdminSpec extends BaseCommandSpec {

    def "can make a user an admin to #orgCount org(s) using the #orgFlag and #userFlag flags #title"() {
        given:
        UUID userID = createUser().body().id
        def orgs = createOrgs(orgCount).join(",")

        when:
        def (outputStream, errorStream) = executeCommand(context as ApplicationContext, ["makeOrgAdmin", orgFlag, orgs, userFlag, userID] as String[])

        then:
        if (context == noAuthCtx) {
            verifyAll {
                errorStream.matches(tokenNotSetRegex)
                outputStream.matches(blankRegex)
            }
            return
        }
        verifyAll {
            (outputStream as String).contains("successfully reassigned ${orgCount} orgs to user ${userID}")
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
        UUID userID = createUser().body().id
        String orgs
        def fakeId = faker.internet().uuid().toString()
        if (orgCount == 1) {
            orgs = fakeId
        } else {
            orgs = createOrgs(orgCount - 1).join(",") + "," + fakeId
        }

        when:
        def (outputStream, errorStream) = executeCommand(context as ApplicationContext, ["makeOrgAdmin", orgFlag, orgs, userFlag, userID] as String[])

        then:
        verifyAll {
            (outputStream as String).contains("successfully reassigned ${orgCount - 1 } orgs to user ${userID}")
            (errorStream as String).contains("Failed to make user ${userID} an admin for organization ${fakeId}.")
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
