package org.justserve.model.graph;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Data Transfer Object for the {@code createEvents} GraphQL mutation.
 * This class dynamically constructs the mutation query based on the
 * events provided in the {@link CreateEventsVariables}.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@Serdeable
@JsonPropertyOrder({"query", "variables"})
public class CreateEventsMutation extends GraphMutation {

    public CreateEventsMutation(CreateEventsVariables variables) {
        if (variables.getProjectEvents().isEmpty()) {
            throw new IllegalArgumentException("At least one event must be provided");
        }
        this.query = buildQuery(variables);
        this.variables = variables;
    }

    private String buildQuery(CreateEventsVariables variables) {
        StringBuilder args = new StringBuilder("$projectId: ID!");
        StringBuilder body = new StringBuilder();

        List<String> sortedKeys = variables.getProjectEvents().keySet().stream().sorted().toList();

        IntStream.range(0, sortedKeys.size()).forEach(i -> {
            String key = sortedKeys.get(i);
            args.append(", $").append(key).append(": UpdateProjectEventInput!");
            body.append(String.format("""
                          %s: createEvent(
                            projectId: $projectId
                            projectEvent: $%s
                          ) {
                            %%s
                          }
                    """, "event" + i, key));
        });

        return String.format("""
                mutation createEvents(%s) {
                %s}
                """, args, body);
    }

    @Override
    public CreateEventsVariables getVariables() {
        return (CreateEventsVariables) super.getVariables();
    }

    @Override
    public String getQuery() {
        CreateEventsVariables vars = getVariables();

        List<String> sortedKeys = vars.getProjectEvents().keySet().stream().sorted().toList();

        Object[] fieldsArray = sortedKeys.stream().map(sortedKey -> vars.getProjectEvents().get(sortedKey)
                .getMutationFields()).toArray();

        return String.format(query, fieldsArray);
    }
}
