package org.justserve.model;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Serdeable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphQLRequest {

    private String query;
    
    // This handles dynamic variable maps/objects
    private Object variables;
}
