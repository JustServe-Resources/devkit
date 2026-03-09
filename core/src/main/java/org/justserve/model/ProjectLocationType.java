package org.justserve.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.Generated;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Serdeable
@Generated("io.micronaut.openapi.generator.JavaMicronautClientCodegen")
public enum ProjectLocationType {
//    NONE(0, "NONE"),
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
