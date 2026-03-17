package org.justserve.model.graph;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <h4>GraphQL Response</h4>
 * A generic wrapper for GraphQL API responses, containing the data payload and any execution errors.
 *
 * @param <T> Underlying response object
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@Serdeable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphQLResponse<T> {

    private T data;
    
    private List<Object> errors;
    
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
}
