package org.justserve.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import org.justserve.model.graph.GraphFields;

import java.util.List;
import java.util.UUID;

import static java.lang.Boolean.TRUE;

/**
 * <h2>JustServe Project Recurring Time</h2>
 * Valid to use with
 * <ul>
 *     <li>{@link EventType#Recurring}</li>
 * </ul>
 *
 * <h4>Creating New Recurring Events</h4>
 * Use {@code ProjectRecurringTime.}{@link #builder()} when adding a new recurring event to ensure all
 * needed fields are included and validated.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(buildMethodName = "buildInternal")
@Serdeable
@Introspected
public class ProjectRecurringTime extends GraphFields {

    @Nullable
    @Email
    private String contactEmail;

    @Nullable
    @Size(max = 139)
    private String contactName;

    @Nullable
    private String contactPhone;

    @Nullable
    private String startTime;

    @Nullable
    private String endTime;

    @Builder.Default
    private Boolean firstWeek = false;

    @Builder.Default
    private Boolean secondWeek = false;

    @Builder.Default
    private Boolean thirdWeek = false;

    @Builder.Default
    private Boolean fourthWeek = false;

    @Builder.Default
    private Boolean fifthWeek = false;

    @Builder.Default
    private Boolean lastWeek = false;

    @Nullable
    private Integer groupLimit;


    @Nullable
    private UUID id;

    @Nullable
    private RecurringType recurringType;

    @Nullable
    private List<Integer> daysOfMonth;

    /**
     * <h4>The days of the months a recurring event lands on.</h4>
     * For example, a monthly recurring event landing on the 16th of the month:
     *
     */
    @Nullable
    private List<Integer> recurringDaysOfMonths;

    @Nullable
    private UUID projectRecurringId;

    @Nullable
    private String specialDirections;

    @Builder.Default
    private Boolean volunteersCapped = false;

    @Nullable
    private Integer totalVolunteersNeeded;

    @Nullable
    private Integer volunteersNeeded;

    @Builder.Default
    private Boolean monday = false;

    @Builder.Default
    private Boolean tuesday = false;

    @Builder.Default
    private Boolean wednesday = false;

    @Builder.Default
    private Boolean thursday = false;

    @Builder.Default
    private Boolean friday = false;

    @Builder.Default
    private Boolean saturday = false;

    @Builder.Default
    private Boolean sunday = false;


    public static class ProjectRecurringTimeBuilder {
        public ProjectRecurringTime build() {
            ProjectRecurringTime event = this.buildInternal();
            if (TRUE.equals(event.getVolunteersCapped()) && event.getVolunteersNeeded() == null) {
                throw new IllegalStateException("volunteersNeeded cannot be null when volunteersCapped is true");
            }

            return event;
        }
    }
}
