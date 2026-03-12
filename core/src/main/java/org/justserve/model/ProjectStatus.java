package org.justserve.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Gets or Sets ProjectStatus
 */
@RequiredArgsConstructor
@Serdeable
public enum ProjectStatus {
    PUBLISHED(1, "PUBLISHED"),
    SUBMITTED(2, "SUBMITTED"),
    DRAFT(3, "DRAFT"),
    TEMPLATE(4, "TEMPLATE"),
    ON_HOLD(5, "ON_HOLD"),
    CANCELLED(6, "CANCELLED"),
    DECLINED(7, "DECLINED");

    public static final Map<Integer, ProjectStatus> VALUE_MAPPING = Map.copyOf(Arrays.stream(values())
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
     * 2. RECEIVING (Response): This catches the incoming data.
     * It can handle the Integer '1' from GraphQL, or even a String if a REST endpoint sends one.
     */
    @JsonCreator
    public static ProjectStatus fromValue(Object value) {
        if (value instanceof Number) {
            int intVal = ((Number) value).intValue();
            for (ProjectStatus type : values()) {
                if (type.intValue == intVal) return type;
            }
        } else if (value instanceof String strVal) {
            for (ProjectStatus type : values()) {
                if (type.stringValue.equalsIgnoreCase(strVal)) return type;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "' for ProjectStatus");
    }
}
