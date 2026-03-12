package org.justserve.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Serdeable
public enum ProjectEventStatus {
    ACTIVE(1, "ACTIVE"),
    CANCELLED(2, "CANCELLED"),
    ON_HOLD(3, "ON_HOLD");

    public static final Map<Integer, ProjectEventStatus> VALUE_MAPPING = Map.copyOf(Arrays.stream(values())
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
    public static ProjectEventStatus fromValue(Object value) {
        if (value instanceof Number) {
            int intVal = ((Number) value).intValue();
            for (ProjectEventStatus type : values()) {
                if (type.intValue == intVal) return type;
            }
        } else if (value instanceof String strVal) {
            for (ProjectEventStatus type : values()) {
                if (type.stringValue.equalsIgnoreCase(strVal)) return type;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "' for ProjectEventStatus");
    }
}
