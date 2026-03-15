package org.justserve.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Generated;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <h4>Supported Location Types for Projects</h4>
 *
 */
@RequiredArgsConstructor
@Serdeable
public enum ProjectLocationType {
    SINGLE_LOCATION(1, "SINGLE_LOCATION"),
    REGIONAL(3, "REGIONAL"),
    REMOTE(4, "REMOTE");

    public static final Map<Integer, ProjectLocationType> VALUE_MAPPING = Map.copyOf(Arrays.stream(values())
            .collect(Collectors.toMap(v -> v.intValue, Function.identity())));

    private final Integer intValue;
    private final String stringValue;

    @Override
    public String toString() {
        return String.valueOf(intValue);
    }

    @JsonValue
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Parses the incoming value to either the string or integer value, whichever the server is using.
     *
     * @param value the incoming value from the server
     * @return the event type that matches the incoming value
     */
    @Generated //manually placed annotation to tell jacoco coverage report to ignore this
    @JsonCreator
    public static ProjectLocationType fromValue(Object value) {
        if (value instanceof Number) {
            int intVal = ((Number) value).intValue();
            for (ProjectLocationType type : values()) {
                if (type.intValue == intVal) return type;
            }
        } else if (value instanceof String strVal) {
            for (ProjectLocationType type : values()) {
                if (type.stringValue.equalsIgnoreCase(strVal)) return type;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "' for ProjectLocationType");
    }
}
