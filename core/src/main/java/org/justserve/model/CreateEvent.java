package org.justserve.model;

public class CreateEvent {
    String query = """
            mutation createEvent($projectId: ID!, $projectEvent: UpdateProjectEventInput!) {
                  createEvent(
                    projectId: $projectId
                    projectEvent: $projectEvent
                  ) {
                    %s
                  }
                }
            """;
}
