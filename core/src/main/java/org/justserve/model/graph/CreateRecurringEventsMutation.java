package org.justserve.model.graph;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.micronaut.serde.annotation.Serdeable;
import org.justserve.model.ProjectRecurringTime;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * Data Transfer Object for the {@code createRecurringEvents} GraphQL mutation.
 * This class dynamically constructs the mutation query based on the
 * events provided in the {@link CreateRecurringEventsVariables}.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@Serdeable
@JsonPropertyOrder({"query", "variables"})
public class CreateRecurringEventsMutation extends GraphMutation {

    public CreateRecurringEventsMutation(UUID projectId, ProjectRecurringTime... events) {
        this(new CreateRecurringEventsVariables(projectId, events));
    }

    public CreateRecurringEventsMutation(CreateRecurringEventsVariables variables) {
        if (variables.getProjectEvents() == null || variables.getProjectEvents().isEmpty()) {
            throw new IllegalArgumentException("At least one event must be provided");
        }
        this.query = buildQuery(variables);
        this.variables = variables;
    }

    private String buildQuery(CreateRecurringEventsVariables variables) {
        StringBuilder args = new StringBuilder("$projectId: ID!");
        StringBuilder body = new StringBuilder();

        List<String> sortedKeys = variables.getProjectEvents().keySet().stream().sorted().toList();

        String template = """
                      %s: addRecurringTime(
                        projectId: $projectId
                        recurringTime: $%s
                      ) {
                        %%s
                      }
                """;

        IntStream.range(0, sortedKeys.size()).forEach(i -> {
            String key = sortedKeys.get(i);
            args.append(", $").append(key).append(": CreateProjectRecurringTimeInput!");
            body.append(String.format(template, "event" + i, key));
        });

        return String.format("""
                mutation createRecurringEvents(%s) {
                %s}
                """, args, body);
    }

    @Override
    public CreateRecurringEventsVariables getVariables() {
        return (CreateRecurringEventsVariables) super.getVariables();
    }

    @Override
    public String getQuery() {
        CreateRecurringEventsVariables vars = getVariables();

        List<String> sortedKeys = vars.getProjectEvents().keySet().stream().sorted().toList();

        Object[] fieldsArray = sortedKeys.stream().map(sortedKey -> vars.getProjectEvents().get(sortedKey)
                .getMutationFields()).toArray();

        return String.format(query, fieldsArray);
    }
}
