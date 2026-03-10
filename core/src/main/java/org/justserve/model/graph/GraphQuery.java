package org.justserve.model.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GraphQuery {
    /**
     * <h4>Mutation Query String</h4>formatted to receive a \n-delimited string of variable names.<br>
     * The query is to include the mutation's signature, as well as its opening and closing curly braces.<br>
     * <h5>Example:</h5> {@code "mutation ($projectId: ID!, $attachmentId: ID!) {\\n %s\\n" }
     */
    //"^mutation\\b[\\s\\S]*\\{[\\s\\S]*%s[\\s\\S]*\\}$"
    @Pattern(regexp = "^mutation.*\\{.*%s.*}.*", message = "Query must begin with 'mutation' and include a '%s' placeholder")
    String query;


    @JsonProperty("variables")
    GraphVariables variables;


    String getQuery() {
        if (query == null) {
            log.warn("Query nor signature can be null in {}: query template is {} and query must contain '%s' placeholder.", this.getClass().getSimpleName(), query);
            return "";
        }
        return String.format(query, variables.getMutationFields());
    }
}
