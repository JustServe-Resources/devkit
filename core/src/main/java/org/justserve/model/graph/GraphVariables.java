package org.justserve.model.graph;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanIntrospector;
import io.micronaut.core.naming.Named;
import io.micronaut.serde.annotation.Serdeable;

import java.util.stream.Collectors;

/**
 * <h4>Array of variables</h4>
 * These are the variables used in {@link GraphQuery#query}. Non-null values are added into the query as well as the variables json object.
 */
@Introspected
@Serdeable
public class GraphVariables {

    String getMutationFields() {
        return getFieldsForBean(this);
    }

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
