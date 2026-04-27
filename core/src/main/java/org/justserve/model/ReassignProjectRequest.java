package org.justserve.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.core.annotation.Nullable;
import jakarta.annotation.Generated;

/**
 * ReassignProjectRequest
 */
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data

@Serdeable
@Generated("io.micronaut.openapi.generator.JavaMicronautClientCodegen")
public class ReassignProjectRequest {

    public static final String JSON_PROPERTY_ASSIGN_ID = "assignId";
    public static final String JSON_PROPERTY_DELETE_ID = "deleteId";

    /**
     * UUID of the new owner of the project
     */
    @Nullable
    @JsonProperty(JSON_PROPERTY_ASSIGN_ID)
    @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
    private UUID assignId;

    /**
     * UUID of the previous owner of the project
     */
    @Nullable
    @JsonProperty(JSON_PROPERTY_DELETE_ID)
    @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
    private UUID deleteId;

}