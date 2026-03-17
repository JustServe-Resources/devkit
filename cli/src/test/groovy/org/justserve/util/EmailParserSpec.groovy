package org.justserve.util

import io.micronaut.core.io.ResourceResolver
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.jsoup.nodes.Document
import spock.lang.Shared
import spock.lang.Specification

import java.util.stream.Collectors
import java.util.stream.Stream

@MicronautTest
class EmailParserSpec extends Specification {

    @Shared
    @Inject
    ResourceResolver resourceResolver

    @Shared
    Map<String, String> testEmails

    @Shared
    Map<String, String> testTrackingUrls


    def setupSpec() {
        testEmails = new HashMap<>()
        
        Faker faker = new Faker()
        TestUser recipient = new TestUser(faker)
        List<ProjectCard> mockProjects = [
            new ProjectCard(id: UUID.randomUUID(), title: faker.book().title()),
            new ProjectCard(id: UUID.randomUUID(), title: faker.book().title())
        ]
        
        testEmails.put("test-with-automated-email", TestEmailGenerator.generateMockValidEmlContent(mockProjects, recipient))
        testEmails.put("test-without-automated-email", TestEmailGenerator.generateInvalidMockEmlContent())

        testTrackingUrls = new HashMap<>()
        def yamlResource = resourceResolver.getResourceAsStream("classpath:projects.yaml")
        yamlResource.ifPresent { stream ->
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                testTrackingUrls = reader.lines()
                        .filter(line -> line.contains(':'))
                        .collect(Collectors.toMap(
                                { line -> ((line as String).split(':', 2)[0] as String).replaceAll('"', '').trim() },
                                { line -> ((line as String).split(':', 2)[1] as String).replaceAll('"', '').trim() }
                        ))
            } catch (IOException e) {
                throw new RuntimeException("Failed to read projects.yaml", e)
            }
        }
    }

    def "validate parse can read in an eml file and take in the html body of the email"() {
        when:
        def doc = EmailParser.parse(fileContent as String)

        then:
        if (!(title as String).toLowerCase().contains("without")) {
            null != doc
            return
        }
        def error = thrown(JustServeEmailParserError)
        error.message == "Email is not a JustServe generated email."


        where:
        [title, fileContent] << testEmails.collect { key, value -> [key, value] }
    }


    def "validate getProjectIdFromUglyUrl will properly get #projectName's id from the provided ugly url"() {
        when:
        UUID projectID = EmailParser.getProjectIDFromUglyUrl(uglyUrl as String)

        then:
        null != projectID
        noExceptionThrown()

        where:
        [projectName, uglyUrl] << testTrackingUrls.collect { key, value -> [key, value] }
    }

    def "Can use 'getProjects' to parse jsoup doc in #title file"() {
        given:
        Document doc = EmailParser.parse(fileContent as String)

        when:
        Map<String, Set<UUID>> projects = EmailParser.getProjects(doc)

        then:
        if (!(title as String).toLowerCase().contains("without")) {
            projects.size() > 0
            return
        }
        def error = thrown(JustServeEmailParserError)
        error.message == "Email does not contain an HTML body."

        where:
        [title, fileContent] << testEmails.findAll { key, _ -> !key.toLowerCase().contains("without") }.collect { key, value -> [key, value] }
    }

    def "Can use 'getProjects' to parse entire #title file"() {
        when:
        Map<String, Set<UUID>> projects = EmailParser.getProjects(fileContent as String)

        then:
        if (!(title as String).toLowerCase().contains("without")) {
            projects.size() > 0
            return
        }
        def error = thrown(JustServeEmailParserError)
        error.message == "Email is not a JustServe generated email."

        where:
        [title, fileContent] << testEmails.collect { key, value -> [key, value] }
    }
}
