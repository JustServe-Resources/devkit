package org.justserve.model.graph;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class CreateEventQuery extends GraphQuery {

    public CreateEventQuery(Event variables) {
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

}
