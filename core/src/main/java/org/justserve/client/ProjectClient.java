package org.justserve.client;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.core.annotation.*;
import io.micronaut.http.client.annotation.Client;
import org.justserve.model.*;
import reactor.core.publisher.Mono;

import java.util.UUID;
import io.micronaut.retry.annotation.Retryable;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Generated("io.micronaut.openapi.generator.JavaMicronautClientCodegen")
@Retryable
@Client("justserve")
public interface ProjectClient {

    /**
     * Assigns an organization to a project.
     *
     * @param id ID of the project (required)
     * @param organizationId ID of the organization to assign (required)
     *
     * @return OK (status code 200)
     */
    @Put("/api/v1/projects/{id}/organization/{organizationId}/assign")
    Mono<HttpResponse<Object>> assignOrganizationToProject(
            @PathVariable("id") @NotNull UUID id,
            @PathVariable("organizationId") @NotNull UUID organizationId
    );

    /**
     * Get project details for a given project
     *
     * @param id (required)
     * @param locale (required)
     * @param getProjectRequest (optional)
     *
     * @return OK (status code 200)
     */
    @Post("/api/v1/projects/{id}/{locale}")
    Mono<@Valid Project> getProject(
            @PathVariable("id") @NotNull UUID id,
            @PathVariable("locale") @NotNull String locale,
            @Body @Nullable @Valid GetProjectRequest getProjectRequest
    );

    /**
     * Reassigns multiple projects to a new user.
     *
     * @param projectId ID of the project to be reassigned (required)
     * @param reassignProjectRequest (optional)
     *
     * @return OK (status code 200)
     */
    @Put("/api/v1/projects/{projectId}/users/reassignAndDelete")
    Mono<HttpResponse<Object>> reassignProject(
            @PathVariable("projectId") @NotNull UUID projectId,
            @Body @Nullable @Valid ReassignProjectRequest reassignProjectRequest
    );

    /**
     * search active projects
     *
     * @param projectSearchRequest (optional)
     *
     * @return OK (status code 200)
     */
    @Post("/api/v2/projects/search")
    Mono<@Valid ProjectSearchResponse> searchProjects(
            @Body @Nullable @Valid ProjectSearchRequest projectSearchRequest
    );
}