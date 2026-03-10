package org.justserve.model.graph;

public class CreateEventQuery extends GraphQuery{

    private static class eventVariables extends GraphVariables {
        String contactEmail;
        String contactName;
        String contactPhone;
        Boolean deleted;
        String deletedBy;
        String deletedOn;
        String end;
        String endDateTimeOffset;
        Boolean eventCapReached;
        Integer groupCap;
        Integer groupLimit;
        String id;
        String locationLink;
        String locationName;
        String projectEventLocationId;
        String projectId;
        String projectRecurringTimeId;
        String qrCodeImageLocation;
        String renewDate;
        String schedule;
        String shiftTitle;
        String specialDirections;
        String start;
        String startDateTimeOffset;
        String status;
        String statusId;
        String timezone;
        String timeZoneEnumId;
        Integer totalVolunteersNeeded;
        Integer volunteerCap;
        Integer volunteersNeeded;
    }

    public CreateEventQuery(GraphVariables variables) {
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
