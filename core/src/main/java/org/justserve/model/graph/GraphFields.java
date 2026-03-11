package org.justserve.model.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanIntrospector;
import io.micronaut.core.naming.Named;
import io.micronaut.serde.annotation.Serdeable;

import java.util.stream.Collectors;

/**
 * <h4>Fields used in a graphql mutation.</h4>
 * These are the fields used in{@link GraphQuery#query} fields.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@Introspected
@Serdeable
public class GraphFields {

    @JsonIgnore
    String getMutationFields() {
        return getFieldsForBean(this);
    }

    /**
     * reflection free implementation of querying non-null fields for a bean.
     *
     * @param bean class which is being queried
     * @param <T>  class type
     * @return all fields that are not null
     */
    private static <T> String getFieldsForBean(T bean) {
        @SuppressWarnings("unchecked")
        Class<T> beanClass = (Class<T>) bean.getClass();
        BeanIntrospection<T> introspection = BeanIntrospector.SHARED.getIntrospection(beanClass);

        return introspection.getBeanProperties().stream()
                .filter(prop -> !prop.getName().equals("mutationFields"))
                .filter(prop -> {
                    try {
                        return prop.get(bean) != null;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(Named::getName)
                .collect(Collectors.joining("\n        "));
    }
}
