package org.justserve.util;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.justserve.cli.util.JustServeEmailParserError;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class EmailParser {


    /**
     * Parses an EML format string, extracts the HTML body, verifies it's a JustServe generated email,
     * and then extracts project names and their corresponding UUIDs.
     *
     * @param emlFileContent The content of the EML file as a String.
     * @return A map where keys are project names (String) and values are project UUIDs.
     * @throws MessagingException        If there is an issue with parsing the MimeMessage.
     * @throws IOException               If an I/O error occurs during stream processing.
     * @throws JustServeEmailParserError If the email does not contain an HTML body, is not a JustServe generated email,
     *                                   or the HTML structure does not conform to the expected format for extracting projects.
     */
    public static Map<String, Set<UUID>> getProjects(String emlFileContent) throws MessagingException, IOException, JustServeEmailParserError {
        return getProjects(parse(emlFileContent));
    }

    /**
     * Parses an EML format string into a Jsoup Document, ensuring it's a JustServe generated email.
     *
     * @param emlFileContent The content of the EML file as a String.
     * @return A Jsoup Document representing the HTML body of the email.
     * @throws MessagingException        If there is an issue with parsing the MimeMessage.
     * @throws IOException               If an I/O error occurs during stream processing.
     * @throws JustServeEmailParserError If the email does not contain an HTML body or is not a JustServe generated email.
     */
    public static Document parse(String emlFileContent) throws MessagingException, IOException, JustServeEmailParserError {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try (InputStream is = new ByteArrayInputStream(emlFileContent.getBytes(StandardCharsets.UTF_8))) {
            MimeMessage message = new MimeMessage(session, is);
            String htmlBody = getHtmlBody(message);
            if (htmlBody == null) {
                throw new JustServeEmailParserError("Email does not contain an HTML body.");
            }
            Document doc = Jsoup.parse(htmlBody);
            if (!isJustServeGeneratedEmail(doc)) {
                throw new JustServeEmailParserError("Email is not a JustServe generated email.");
            }
            return doc;
        }
    }

    /**
     * Extracts project names and their corresponding UUIDs for project ids on JustServe.
     * The Document is expected to represent an HTML email body from an automated JustServe email regarding reassigned projects
     *
     * @param doc The Jsoup Document containing the HTML structure of the email.
     * @return A map where keys are project names (String) and values are project UUIDs.
     * @throws JustServeEmailParserError If the HTML structure does not conform to the expected format for extracting projects.
     */
    public static Map<String, Set<UUID>> getProjects(Document doc) throws JustServeEmailParserError {
        Map<String, Set<UUID>> projects = new HashMap<>();
        Collection<String> errors = new ArrayList<>();

        /*
        the original email from justserve's list element is :
        ```
        <li>
            ::marker
            <a href="ugly-url" rel="noopener" style="color:#45a2c4;text-decoration:none">Downtown Love Day - Alley Clean-up</a>
        </li
        ```
        some email clients manipulate this into something else, like :
        <li style="color:#64686C;">
            <span style="font-size:13.5pt;">
                <a href="ugly-url">
                <span style="color:#45A2C4;text-decoration:none">Food Distribution, We Care Wednesday at MCC</span>
                </a>
            </span>
        </li>

         */

        doc.select("li").forEach(element -> {
            Element link = element.selectFirst("a");
            if (link == null) {
                errors.add(String.format("Expected list element to contain a link, but found none. Element text: %s", element.text()));
            } else {
                UUID uuid = getProjectIDFromUglyUrl(link.attr("href"));
                if (uuid == null) {
                    errors.add(String.format("Expected link to contain a valid project ID, but found none. URL: %s", link.attr("href")));
                } else {
                    if (!projects.containsKey(link.text())) {
                        projects.put(link.text(), new HashSet<>());
                    }
                    projects.get(link.text()).add(uuid);
                }
            }
        });
        if (!errors.isEmpty()) {
            throw new JustServeEmailParserError(String.join("\n", errors));
        }
        return projects;
    }

    /**
     * Checks that the email contains an image whose source is the logo hosted in justserve's static assets.
     *
     * @param doc html content which may or may not have a justserve logo
     * @return true if the html contains the target image, false if not.
     */
    private static boolean isJustServeGeneratedEmail(Document doc) {
        Elements elements = doc.selectXpath("//img[@src='https://static-assets.justserve.org/images/static/email/justserve-logo-title.gif']");
        return !elements.isEmpty();
    }


    /**
     * Recursively extracts the HTML body from a given {@link Part} of an email.
     *
     * @param part The email part to process.
     * @return The HTML content as a String, or null if no HTML part is found.
     * @throws MessagingException If there is an issue with the messaging system.
     * @throws IOException        If an I/O error occurs.
     */
    private static String getHtmlBody(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/html")) {
            return (String) part.getContent();
        }

        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                Part bodyPart = multipart.getBodyPart(i);
                String html = getHtmlBody(bodyPart);
                if (html != null) {
                    return html;
                }
            }
        }
        return null;
    }

    /**
     * gets the project ID from a url, which url is wrapped in proofpoint's URL defense service, and thus looks ugly.
     *
     * @param uglyUrl long url with an encoded justserve project url.
     * @return the UUID of the project contained in the URL. returns null if the URL does
     * not contain the 'www.justserve.org*2Fprojects*2F' string.
     */
    static UUID getProjectIDFromUglyUrl(String uglyUrl) {
        String start = "www.justserve.org*2Fprojects*2F";
        if (!uglyUrl.contains(start)) {
            return null;
        }
        String uuid = uglyUrl.split(Pattern.quote(start))[1].split(Pattern.quote("/"))[0];
        return UUID.fromString(uuid);
    }
}
