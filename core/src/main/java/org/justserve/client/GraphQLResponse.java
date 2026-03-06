package org.justserve.client;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
