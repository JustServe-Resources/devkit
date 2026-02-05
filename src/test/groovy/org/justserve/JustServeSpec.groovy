package org.justserve

import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import net.datafaker.Faker
import org.apache.commons.lang3.RandomStringUtils
import org.justserve.client.*
import org.justserve.model.*
import spock.lang.Shared
import spock.lang.Specification

import static org.justserve.model.DistanceType.MILES

@MicronautTest()
class JustServeSpec extends Specification {


    // ---------- fields ----------
    @Shared
    String knownWorkingLocation = "Latitude, Longitude : 32.75338, -96.80831,  Dallas,  Dallas County,  Texas,  75203,  United States"

    @Shared
    TestUser[] users

    @Shared
    List<ProjectCard> searchResults

    @Shared
    Faker faker

    // ---------- clients ----------
    @Shared
    ApplicationContext ctx, noAuthCtx

    @Shared
    UserClient noAuthUserClient

    @Shared
    BoundaryPermissionClient authBoundaryPermissionClient

    @Shared
    DynamicRoutingClient authDynamicRoutingClient

    @Shared
    ImageClient authImageClient

    @Shared
    OrganizationClient authOrgClient

    @Shared
    UserClient userClient

    @Shared
    TestUser readOnlyUser

    @Shared
    ProjectClient projectClient


    def setupSpec() {
        faker = new Faker()
        // if (null != System.getenv("JUSTSERVE_TOKEN")) {
        //     throw new IllegalStateException("JUSTSERVE_TOKEN is set. Do not define this variable in testing.")
        // }
        ctx = ApplicationContext.builder()
                .environments(Environment.CLI, Environment.TEST)
                .properties([
                        "justserve.token": System.getenv("TEST_TOKEN")
                ])
                .build()
                .start()
        noAuthCtx = ApplicationContext
                .builder()
                .environments(Environment.CLI, Environment.TEST)
                .environmentVariableExcludes("JUSTSERVE_TOKEN")
                .build()
                .start()
        noAuthUserClient = noAuthCtx.getBean(UserClient)
        users = new TestUser[]{new TestUser(new Faker(Locale.of("en-us")))}
        authImageClient = ctx.getBean(ImageClient)
        authOrgClient = ctx.getBean(OrganizationClient)
        authBoundaryPermissionClient = ctx.getBean(BoundaryPermissionClient)
        authDynamicRoutingClient = ctx.getBean(DynamicRoutingClient)
        userClient = ctx.getBean(UserClient)
        readOnlyUser = new TestUser(new Faker(Locale.of("en-us")))
        projectClient = ctx.getBean(ProjectClient)

        //        TODO: validate the user does not already exist (use the admin client user search)
        String customRandomEmail=RandomStringUtils.insecure().nextAlphanumeric(20)+ "@fake.com"
        readOnlyUser.uuid = createUserFromFaker(noAuthUserClient, readOnlyUser, customRandomEmail).body().getId()
        searchResults = getProjectsByLocation(faker.location().toString())
    }

    void cleanupSpec() {
        noAuthCtx.stop()
        ctx.stop()
    }

    def createUser(UserClient client = noAuthUserClient) {
        HttpResponse response = null
        def tries = 0
        while ((null == response || HttpStatus.OK != response.status()) && tries < 5) {
            try {
                // A new user is generated on each loop iteration to avoid collisions
                response = createUserFromFaker(client, new TestUser(new Faker(Locale.of("en-us"))))
            } catch (HttpClientResponseException ignored) {
                tries++
                // This user likely already exists, so we'll loop and try a new one.
            }
        }
        return response
    }

    private static def createUserFromFaker(UserClient client, TestUser user, String uniqueEmailInput=null) {
        return client.createUser(
                user.firstName,
                user.lastName,
                (uniqueEmailInput ?: user.email) as String, //in the case that we provide our own custom email, to avoid the same email being repeated
                user.password,
                user.zipcode,
                user.locale,
                user.country,
                user.countryCode)
    }

    List<ProjectCard> getProjectsByLocation(String location) {
        return getProjects(" ", 1, 10, location, "en-US")
    }

    List<ProjectCard> getProjects(String keyword = " ", int page = 1, int size = 10, String location = " ", String locale = "en-US") {
        return projectClient.searchProjects(new ProjectSearchRequest().setPage(Integer.valueOf(page))
                .setSize(size).setKeywords(keyword).setLocation(location).setRadiusType(MILES).setVolunteerFromAnywhere(false)
                .setIncludeOrgInfo(true).setLanguage(locale).setBrowserLocale(locale).setPublishedOnly(false)
                .setIncludeFilledProjects(true).setDisasterRecoveryProjectsOnly(false).setTimesOfDay(null)).body().getItems()

    }

    /**
     * creates a random org for testing
     * @return
     */
    UUID createOrg() {
        def orgRequest = new OrganizationCreateRequest()
                .setContactEmail(faker.internet().emailAddress())
                .setContactName(faker.zelda().character())
                .setContactPhone(faker.phoneNumber().phoneNumber())
                .setDescription(faker.zelda().game())
                .setLocationString(knownWorkingLocation)
                .setLogo(getUploadedImageFileName())
                .setName(faker.zelda().character())
                .set_public(null)
                .setUrl(getUniqueSlug())
                .setVolunteerCenterInfo(null)
                .setWebsite(faker.internet().url())
        authOrgClient.createOrganization(orgRequest)
        return authDynamicRoutingClient.getOrgIdFromSlug(orgRequest.url).body().id
    }

    /**
     * Creates a specified number of random organizations for testing.
     * @param count The number of organizations to create.
     * @return A list of UUIDs for the created organizations.
     */
    List<UUID> createTestOrgs(int count) {
        return (1..count).collect {
            createOrg()
        }
    }


    /**
     * Creates a default organization search request for testing.
     * @return A pre-configured {@link org.justserve.model.OrganizationSearchRequest}.
     */
    static OrganizationSearchRequest createSearchRequestForElkGrove() {
        return new OrganizationSearchRequest()
                .setLocation("Elk Grove, CA 95758, USA")
                .setSortBy("az")
    }

    /**
     * Uploads a fake JPG image and returns the display file name.
     * @return The file name of the uploaded image.
     */
    String getUploadedImageFileName() {
        HttpResponse<ImageUploadResponse> profileImage = authImageClient.uploadImage(
                new ImageUploadRequest(faker.image().base64JPG().split(",")[1], 256, 256, false, 0, 0)
        )
        return profileImage.body().displayFileName
    }

    /**
     * Generates a unique URL slug for an organization by checking for its existence.
     * @return A unique string to be used as a URL slug.
     */
    String getUniqueSlug() {
        String urlSlug = null
        while (null == urlSlug) {
            def potentialSlug = faker.word().noun()
            def response = authDynamicRoutingClient.getOrgIdFromSlug(potentialSlug)
            if (response.status() == HttpStatus.NOT_FOUND) {
                urlSlug = potentialSlug
            }
        }
        return urlSlug
    }
}
