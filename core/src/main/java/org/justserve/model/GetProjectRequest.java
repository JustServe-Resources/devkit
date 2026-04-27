package org.justserve.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.core.annotation.Nullable;

/**
 * GetProjectRequest
 */
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Serdeable
public class GetProjectRequest {

    @Nullable
    @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
    private String latitude;

    @Nullable
    @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
    private String longitude;

    @Nullable
    @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
    private String postalCode;

    @Nullable
    @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
    private String lang;

}