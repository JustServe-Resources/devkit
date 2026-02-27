package org.justserve.client;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Client("justserve")
public interface BoundaryPermissionClient {

    /**
     * Make User an admin to a given organization. returns a null response. Makes no changes to a user&#39;s boundaries,
     * all boundary changes to be handled outside of this api call
     *
     * @since 0.0.7
     * @author Jonathan Zollinger
     *
     * @param orgId (required)
     * @param userId (required)
     * @return OK (status code 200)
     */
    @Retryable
    @Put("/api/v1/boundaries/rep/{userId}/org/{orgId}")
    HttpResponse<Object> makeAdminForOrg(
        @PathVariable("orgId") @NotNull UUID orgId,
        @PathVariable("userId") @NotNull UUID userId
    );

}