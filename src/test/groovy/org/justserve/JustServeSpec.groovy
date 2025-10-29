package org.justserve

import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import net.datafaker.Faker
import org.justserve.client.*
import org.justserve.model.ImageUploadRequest
import org.justserve.model.ImageUploadResponse
import org.justserve.model.OrganizationCreateRequest
import org.justserve.model.OrganizationSearchRequest
import spock.lang.Shared
import spock.lang.Specification

@MicronautTest()
class JustServeSpec extends Specification {


    // ---------- fields ----------
    @Shared
    String knownWorkingLocation = "Latitude, Longitude : 32.75338, -96.80831,  Dallas,  Dallas County,  Texas,  75203,  United States"

    @Shared
    TestUser[] users

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


    def setupSpec() {
        faker = new Faker()
        if (null != System.getenv("JUSTSERVE_TOKEN")) {
            throw new IllegalStateException("JUSTSERVE_TOKEN is set. Do not define this variable in testing.")
        }
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
    }

    void cleanupSpec() {
        noAuthCtx.stop()
        ctx.stop()
    }

    def createUser() {
        return createUser(new TestUser(new Faker(Locale.of("en-us"))))
    }

    def createUser(UserClient client = noAuthUserClient, TestUser user) {
        return client.createUser(
                user.firstName,
                user.lastName,
                user.email,
                user.password,
                user.zipcode,
                user.locale,
                user.country,
                user.countryCode
        )
    }

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
