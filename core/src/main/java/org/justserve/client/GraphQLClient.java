package org.justserve.client;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;
import org.justserve.model.*;

@Produces("application/json")
@Consumes("application/graphql-response+json; charset=utf-8")
@Retryable
@Client("justserve")
public interface GraphQLClient {

    @Post("/graphql")
    GraphQLResponse<GraphQLAddProjectAttachmentData> executeAddProjectAttachment(@Body GraphQLAddProjectAttachmentRequest request);

    @Post("/graphql")
    GraphQLResponse<GraphQLAddProjectOrganizationData> executeAddProjectOrganization(@Body GraphQLAddProjectOrganizationRequest request);

    @Post("/graphql")
    GraphQLResponse<GraphQLCombinedMutationUpdateProjectAddProjectTagData> executeCombinedMutationUpdateProjectAddProjectTag(@Body GraphQLCombinedMutationUpdateProjectAddProjectTagRequest request);

    @Post("/graphql")
    GraphQLResponse<GraphQLCreateEventData> executeCreateEvent(@Body GraphQLCreateEventRequest request);

    @Post("/graphql")
    GraphQLResponse<GraphQLCreateProjectData> executeCreateProject(@Body GraphQLCreateProjectRequest request);

    @Post("/graphql")
    GraphQLResponse<GraphQLPublishProjectData> executePublishProject(@Body GraphQLPublishProjectRequest request);

    @Post("/graphql")
    GraphQLResponse<GraphQLSearchOrganizationData> executeSearchOrganization(@Body GraphQLSearchOrganizationRequest request);

    @Post("/graphql")
    GraphQLResponse<GraphQLSetProjectLocationData> executeSetProjectLocation(@Body GraphQLSetProjectLocationRequest request);

    @Post("/graphql")
    GraphQLResponse<GraphQLUpdateProjectAttachmentData> executeUpdateProjectAttachment(@Body GraphQLUpdateProjectAttachmentRequest request);

    @Post("/graphql")
    GraphQLResponse<GraphQLUpdateProjectListingData> executeUpdateProjectListing(@Body GraphQLUpdateProjectListingRequest request);

    @Post("/graphql")
    GraphQLResponse<GraphQLUpdateProjectData> executeUpdateProject(@Body GraphQLUpdateProjectRequest request);

    default GraphQLResponse<GraphQLAddProjectAttachmentData> addProjectAttachment(GraphQLAddProjectAttachmentVariables variables) {
        String fixedQuery = "mutation ($projectId: ID!, $attachmentId: ID!) {\n        addProjectAttachment(projectId: $projectId, attachmentId: $attachmentId) {\n          attachmentId\n        }\n      }";
        GraphQLAddProjectAttachmentRequest request = new GraphQLAddProjectAttachmentRequest();
        request.setQuery(fixedQuery);
        request.setVariables(variables);
        return this.executeAddProjectAttachment(request);
    }

    default GraphQLResponse<GraphQLAddProjectOrganizationData> addProjectOrganization(GraphQLAddProjectOrganizationVariables variables) {
        String fixedQuery = "mutation addProjectOrganization($organizationId: ID!, $projectId: ID!) {\n      addProjectOrganization(organizationId: $organizationId, projectId: $projectId) {\n        id\n        organizations {\n          id\n          name\n        }\n      }\n    }";
        GraphQLAddProjectOrganizationRequest request = new GraphQLAddProjectOrganizationRequest();
        request.setQuery(fixedQuery);
        request.setVariables(variables);
        return this.executeAddProjectOrganization(request);
    }

    default GraphQLResponse<GraphQLCombinedMutationUpdateProjectAddProjectTagData> combinedMutationUpdateProjectAddProjectTag(GraphQLCombinedMutationUpdateProjectAddProjectTagVariables variables) {
        String fixedQuery = "mutation combinedMutation($projectId: ID!, $modify: UpdateProjectInput!) {\n      updateProject(\n        id: $projectId,\n        modify: $modify\n      ) {\n        id\n        wheelchairAccessible\n        itemDonations\n        indoors\n        longDescription\n        shortDescription\n        sponsorUserId\n        groupProjects\n      }\n\n    skill0: addProjectTag(\n            projectId: $projectId\n            tagId: 31\n          ) {\n            id\n            tags {\n              id\n              tagType\n              tagTypeId\n              translations(languageId: 1) {\n                description\n                label\n                languageId\n              }\n            }\n          }\nskill1: addProjectTag(\n            projectId: $projectId\n            tagId: 46\n          ) {\n            id\n            tags {\n              id\n              tagType\n              tagTypeId\n              translations(languageId: 1) {\n                description\n                label\n                languageId\n              }\n            }\n          }\n\n    interest0: addProjectTag(\n            projectId: $projectId\n            tagId: 11\n          ) {\n            id\n            tags {\n              id\n              tagType\n              tagTypeId\n              translations(languageId: 1) {\n                description\n                label\n                languageId\n              }\n            }\n          }\ninterest1: addProjectTag(\n            projectId: $projectId\n            tagId: 26\n          ) {\n            id\n            tags {\n              id\n              tagType\n              tagTypeId\n              translations(languageId: 1) {\n                description\n                label\n                languageId\n              }\n            }\n          }\n    }";
        GraphQLCombinedMutationUpdateProjectAddProjectTagRequest request = new GraphQLCombinedMutationUpdateProjectAddProjectTagRequest();
        request.setQuery(fixedQuery);
        request.setVariables(variables);
        return this.executeCombinedMutationUpdateProjectAddProjectTag(request);
    }

    default GraphQLResponse<GraphQLCreateEventData> createEvent(GraphQLCreateEventVariables variables) {
        String fixedQuery = "mutation createEvent($projectId: ID!, $projectEvent: UpdateProjectEventInput!) {\n      createEvent(\n        projectId: $projectId\n        projectEvent: $projectEvent\n      ) {\n        id\n        projectId\n        contactEmail\n        contactName\n        contactPhone\n        start\n        end\n        groupCap\n        groupLimit\n        timezone\n        totalVolunteersNeeded\n        volunteerCap\n      }\n    }";
        GraphQLCreateEventRequest request = new GraphQLCreateEventRequest();
        request.setQuery(fixedQuery);
        request.setVariables(variables);
        return this.executeCreateEvent(request);
    }

    default GraphQLResponse<GraphQLCreateProjectData> createProject(GraphQLCreateProjectVariables variables) {
        String fixedQuery = "mutation createProject($title: String!, $eventType: ProjectType!, $locationType: ProjectLocationType!, $redirect: String) {\n      createProject(\n        title: $title\n        eventType: $eventType\n        locationType: $locationType\n        redirect: $redirect\n      ) {\n          id\n          title\n          typeId\n          locationTypeId\n          externalVolunteerUrl\n          statusId\n        }\n      }";
        GraphQLCreateProjectRequest request = new GraphQLCreateProjectRequest();
        request.setQuery(fixedQuery);
        request.setVariables(variables);
        return this.executeCreateProject(request);
    }

    default GraphQLResponse<GraphQLPublishProjectData> publishProject(GraphQLPublishProjectVariables variables) {
        String fixedQuery = "mutation ($projectId: ID!){\n        publishProject(projectId: $projectId) {\n          id\n          statusId\n        }\n      }";
        GraphQLPublishProjectRequest request = new GraphQLPublishProjectRequest();
        request.setQuery(fixedQuery);
        request.setVariables(variables);
        return this.executePublishProject(request);
    }

    default GraphQLResponse<GraphQLSearchOrganizationData> searchOrganization(GraphQLSearchOrganizationVariables variables) {
        String fixedQuery = "\n      query organization(\n          $searchTerm: String!\n          $includeAll: Boolean\n          $activeOnly: Boolean\n        ) {\n          adminOrganizationSearchByTitle(\n            activeOnly: $activeOnly\n            includeAll: $includeAll\n            title: $searchTerm\n          ) {\n            id\n            name\n            logo\n            description\n            contactName\n            contactPhone\n            contactEmail\n            url\n            location {\n              displayCity\n              displayState\n            }\n          }\n        }\n      ";
        GraphQLSearchOrganizationRequest request = new GraphQLSearchOrganizationRequest();
        request.setQuery(fixedQuery);
        request.setVariables(variables);
        return this.executeSearchOrganization(request);
    }

    default GraphQLResponse<GraphQLSetProjectLocationData> setProjectLocation(GraphQLSetProjectLocationVariables variables) {
        String fixedQuery = "mutation setProjectLocation($projectId: ID!, $location: String, $locationData: LocationDataInput) {\n      setProjectLocation(\n        projectId: $projectId\n        location: $location\n        locationData: $locationData\n      ) {\n        displayAddress\n        displayAddress2\n        displayCity\n        displayCountry\n        displayCountryCode\n        displayCounty\n        displayNeighborhood\n        displayPostalCode\n        displayState\n        id\n        latitude\n        locationDetails\n        locationName\n        longitude\n        maxLatitude\n        maxLongitude\n        minLatitude\n        minLongitude\n        timezone\n        civicGeography {\n          state {\n            code\n          }\n        }\n        churchGeography {\n          areaUnitId\n          ccUnitId\n          missionUnitId\n          stakeUnitId\n        }\n      }\n    }";
        GraphQLSetProjectLocationRequest request = new GraphQLSetProjectLocationRequest();
        request.setQuery(fixedQuery);
        request.setVariables(variables);
        return this.executeSetProjectLocation(request);
    }

    default GraphQLResponse<GraphQLUpdateProjectAttachmentData> updateProjectAttachment(GraphQLUpdateProjectAttachmentVariables variables) {
        String fixedQuery = "mutation ($attachmentId: ID!, $title: String!, $description: String!) {\n        updateProjectAttachment(attachmentId: $attachmentId, title: $title, description: $description) {\n          attachmentId\n        }\n      }";
        GraphQLUpdateProjectAttachmentRequest request = new GraphQLUpdateProjectAttachmentRequest();
        request.setQuery(fixedQuery);
        request.setVariables(variables);
        return this.executeUpdateProjectAttachment(request);
    }

    default GraphQLResponse<GraphQLUpdateProjectListingData> updateProjectListing(GraphQLUpdateProjectListingVariables variables) {
        String fixedQuery = "mutation listing ($projectId: ID!, $unlisted: Boolean!) {\n      updateProjectListing(projectId: $projectId, unlisted: $unlisted) {\n        id\n        unlisted\n      }\n    }";
        GraphQLUpdateProjectListingRequest request = new GraphQLUpdateProjectListingRequest();
        request.setQuery(fixedQuery);
        request.setVariables(variables);
        return this.executeUpdateProjectListing(request);
    }

    default GraphQLResponse<GraphQLUpdateProjectData> updateProject(GraphQLUpdateProjectVariables variables) {
        String fixedQuery = "mutation ($projectId: ID!, $logo: String!) {\n      updateProject(id: $projectId, modify: { logo: $logo }) {\n        id\n        logo\n      }\n    }";
        GraphQLUpdateProjectRequest request = new GraphQLUpdateProjectRequest();
        request.setQuery(fixedQuery);
        request.setVariables(variables);
        return this.executeUpdateProject(request);
    }
}
