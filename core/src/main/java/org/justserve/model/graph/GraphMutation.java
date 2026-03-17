package org.justserve.model.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.justserve.client.GraphQLClient;

/**
 * Abstract base class used to serialize the JSON payload sent via the{@link GraphQLClient}
 * for GraphQL operations.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@Serdeable
@Getter
@JsonPropertyOrder({"query", "variables"})
public abstract class GraphMutation {
    /**
     * <h4>Mutation Query String</h4>formatted to receive a (@code \n) delimited string of variable names.<br>
     * The query is to include the mutation's signature, as well as its opening and closing curly braces.<br>
     * <h5>Example:</h5> {@code "mutation ($projectId: ID!, $attachmentId: ID!) {\\n %s\\n}" }
     */
    //"^mutation\\b[\\s\\S]*\\{[\\s\\S]*%s[\\s\\S]*\\}$"
    @Pattern(regexp = "^mutation.*\\{.*%s.*}.*", message = "Query must begin with 'mutation' and include a '%s' placeholder")
    String query;

    @JsonProperty("variables")
    GraphVariables variables;

    /**
     * <h4>{@summary Gets the query string used in this mutation object.}</h4>
     * Produces the dynamic string used for the mutation. Fields with null values are not included in the query.
     * <h5>Example:</h5>
     * {@code getQuery} would return this string if the variables include non-null values for id, projectId, contactEmail,
     * contactName, contactPhone, start and end. <br>
     * <pre>
     * mutation createEvent($projectId: ID!, $projectEvent: UpdateProjectEventInput!) {
     *      createEvent(
     *          projectId: $projectId
     *          projectEvent: $projectEvent
     *      ) {
     *          id
     *          projectId
     *          contactEmail
     *          contactName
     *          contactPhone
     *          start
     *          end
     *      }
     * }
     * </pre>
     * <p>
     * {@code getQuery} would return this string if the variables did not include non-null values for the contact
     * information:
     *
     * <pre>
     * mutation createEvent($projectId: ID!, $projectEvent: UpdateProjectEventInput!) {
     *      createEvent(
     *          projectId: $projectId
     *          projectEvent: $projectEvent
     *      ) {
     *          id
     *          projectId
     *          start
     *          end
     *      }
     * }
     * </pre>
     *
     * <h4>NOTE</h4>
     * Only the value property names values are provided in this part of the query. See{@link  GraphFields#getMutationFields()}
     *
     * @return The entire graphql mutation string.
     *
     */
    public abstract String getQuery();
}
