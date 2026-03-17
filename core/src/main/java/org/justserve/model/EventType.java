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
 * Defines the scheduling model for a JustServe project, determining how its
 * {@link ProjectEvent}s are structured and displayed.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@RequiredArgsConstructor
@Serdeable
public enum EventType {
    /**
     * <h4>Date, Time, and Location</h4>
     * A standard event that occurs at a specific time and place.
     */
    DTL(1, "DTL"),

    /**
     * <h4>Ongoing</h4>
     * An event with no specific time. The start and end dates determine visibility on JustServe
     */
    Ongoing(2, "ONGOING"),

    /**
     * <h4>Recurring</h4>
     * An event that repeats on a regular schedule, such as weekly or monthly.
     * <p><b>Example:</b> An evening opportunity that occurs every Monday, Wednesday,
     * and Friday for three months.
     */
    Recurring(3, "RECURRING"),

    /**
     * <h4>Multiple Date, Time, and Location</h4>
     * A complex event that has multiple, distinct shifts or occurrences.
     * <p><b>Example:</b> A project with multiple shifts on each Saturday for several weeks.
     */
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

    /**
     * Parses the incoming value to either the string or integer value, whichever the server is using.
     *
     * @param value the incoming value from the server
     * @return the event type that matches the incoming value
     */
    @Generated //manually placed annotation to tell jacoco coverage report to ignore this
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
