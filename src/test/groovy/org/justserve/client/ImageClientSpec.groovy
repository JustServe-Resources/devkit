package org.justserve.client

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.justserve.JustServeSpec
import org.justserve.model.ImageUploadRequest
import spock.lang.Shared

class ImageClientSpec extends JustServeSpec {

    @Shared
    ImageClient noAuthImageClient

    def setupSpec() {
        noAuthImageClient = noAuthCtx.getBean(ImageClient)
        //authImageClient created in JustServeSpec
    }

    void "can upload an image with #title with no errors"() {
        given:
        // data faker gives full metadata before the base64 string
        def imageUpload = new ImageUploadRequest(faker.image().base64JPG().split(",")[1], 256, 256, false, 0, 0)

        when:
        client.uploadImage(imageUpload)

        then:
        noExceptionThrown()

        where:
        client          | title
        authImageClient | "auth client"
    }

    void "can upload an image with #title with expected status"() {
        given:
        // data faker gives full metadata before the base64 string
        def imageUpload = new ImageUploadRequest(faker.image().base64JPG().split(",")[1], 256, 256, false, 0, 0)

        when:
        def response = client.uploadImage(imageUpload)

        then:
        if (!expectedStatus) {
            response.status() == HttpStatus.CREATED
            response.body() != null
            return
        }
        def exception = thrown(HttpClientResponseException)
        exception.status == expectedStatus


        where:
        expectedStatus          | client            | title
        null                    | authImageClient   | "auth client"
        HttpStatus.UNAUTHORIZED | noAuthImageClient | "no auth client"
    }


}
