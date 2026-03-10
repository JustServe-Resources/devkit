package org.justserve.model.graph;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.beans.BeanIntrospector;

import java.util.stream.Collectors;

/**
 * <h4>Array of variables</h4>
 * These are the variables used in the {@link GraphQuery#query}. Non-null values are added into the query as well as the variables json object.
 */
@Introspected
public class GraphVariables {

    String getMutationFields() {
        return BeanIntrospector.SHARED.getIntrospection(this.getClass()).getBeanProperties().stream()
                .map(prop -> prop.getName() + ": $" + prop.getName())
                .collect(Collectors.joining("\n"));
    }
}
