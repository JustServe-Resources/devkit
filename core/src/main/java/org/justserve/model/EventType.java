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

/**
 * Gets or Sets EventType
 */
@RequiredArgsConstructor
@Serdeable
@Generated("io.micronaut.openapi.generator.JavaMicronautClientCodegen")
public enum EventType {
//    None(0, "None"),
    DTL(1, "DTL"),
    Ongoing(2, "ONGOING"),
    Recurring(3, "RECURRING"),
    MultipleDTL(4, "MULTIPLE_DTL");

    public static final Map<Integer, EventType> VALUE_MAPPING = Map.copyOf(Arrays.stream(values())
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

    // 2. RECEIVING (Response): This catches the incoming data.
    // It can handle the Integer '1' from GraphQL, or even a String if a REST endpoint sends one.
    @JsonCreator
    public static EventType fromValue(Object value) {
        if (value instanceof Number) {
            int intVal = ((Number) value).intValue();
            for (EventType type : values()) {
                if (type.intValue == intVal) return type;
            }
        } else if (value instanceof String strVal) {
            for (EventType type : values()) {
                if (type.stringValue.equalsIgnoreCase(strVal)) return type;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "' for EventType");
    }
}