package org.justserve.model.graph;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.micronaut.serde.annotation.Serdeable;

/**
 * Data Transfer Object for the {@code createEvent} GraphQL mutation.
 * This class dynamically constructs the mutation query based on the non-null fields
 * provided in the {@link CreateEventVariables}.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@Serdeable
@JsonPropertyOrder({"query", "variables"})
public class CreateEventMutation extends GraphMutation {

    public CreateEventMutation(CreateEventVariables variables) {
        this.query = """
            mutation createEvent($projectId: ID!, $projectEvent: UpdateProjectEventInput!) {
                  createEvent(
                    projectId: $projectId
                    projectEvent: $projectEvent
                  ) {
                    %s
                  }
                }
            """;
        this.variables = variables;
    }

    @Override
    public CreateEventVariables getVariables() {
        return (CreateEventVariables) super.getVariables();
    }

    @Override
    public String getQuery() {
        return String.format(query, getVariables().getProjectEvent().getMutationFields());
    }
}
