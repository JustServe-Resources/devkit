package org.justserve.model.graph;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import org.justserve.model.EventType;
import org.justserve.model.Project;
import org.justserve.model.ProjectEventStatus;
import org.justserve.model.TimeZone;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.lang.Boolean.TRUE;

/**
 * <h2>JustServe Project Event</h2>
 * Valid to use with
 * <ul><li>{@link EventType#Ongoing}
 * <li>{@link EventType#DTL}
 * <li>{@link EventType#MultipleDTL}</li>
 * <li>{@link EventType#Recurring}</li> </ul>
 *
 * <h4>Creating New Events</h4>
 * Use {@code ProjectEvent.}{@link #builder()} when adding a new event to ensure all
 * needed fields are included. This not only checks for required fields, but double checks
 * contradictions or invalid combinations are being submitted.
 * <h6>Example</h6>
 * <pre>{@code
 * ProjectEvent newEvent = ProjectEvent.builder()
 *     .start(startDate)
 *     .end(endDate)
 *     .shiftTitle("Morning Shift")
 *     .build();
 * }</pre>
 *
 * <h4>Updating Existing Events</h4>
 * Use {@code new ProjectEvent()} (without the builder) when <i>updating</i> an event. This
 * skips the builder's checks and lets you send partial updates to existing events.
 * <h6>Example</h6>
 * <pre>{@code
 * ProjectEvent partialUpdate = new ProjectEvent()
 *     .setShiftTitle("Afternoon Shift");
 * }</pre>
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
public class ProjectEvent extends GraphFields {
    @Nullable
    @Email
    private String contactEmail;

    @Nullable
    @Size(max = 139)
    private String contactName;

    @Nullable
    private String contactPhone;

    /**
     * <h4>Whether the project event has been deleted.</h4>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    private Boolean deleted;

    /**
     * <h4>The user who deleted the project event.</h4>
     * See{@link #deletedByNavigation}<br>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    private UUID deletedBy;

    /**
     * <h4>User who deleted the event.</h4>
     * See{@link #deletedBy}<br>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    private User deletedByNavigation;

    /**
     * <h4>The date and time the project event was deleted.</h4>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date deletedOn;

    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date end;

    /**
     * <h4>The end date and time of the event with timezone offset.</h4>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date endDateTimeOffset;

    /**
     * <h4>Indicates if the event capacity has been reached.</h4>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    private Boolean eventCapReached;

    /**
     * Whether a group cap is set for this event.
     */
    @NonNull
    @Builder.Default
    private Boolean groupCap = false;

    /**
     * The max number of people which can sign up by one person
     */
    @Nullable
    private Integer groupLimit;

    /**
     * <h4>The unique identifier for the project event.</h4>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    private UUID id;

    @Nullable
    private String locationLink;

    @Nullable
    @Size(max = 139)
    private String locationName;

    /**
     * <h4>The project this event belongs to.</h4>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    private Project project;

    /**
     * <h4>The location of the project event.</h4>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    private ProjectEventLocation projectEventLocation;

    /**
     * <h4>The ID of the project event location.</h4>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    private UUID projectEventLocationId;

    /**
     * <h4>The regions associated with the project event.</h4>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    private List<CivicGeography> projectEventRegions;

    /**
     * <h4>The ID of the project this event belongs to.</h4>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    private UUID projectId;

    /**
     * <h4>Information about the recurring schedule of the project event.</h4>
     * Not Usable In:
     * <ul>
     *     <li>{@link CreateEventQuery}</li>
     * </ul>
     */
    @Nullable
    private ProjectRecurringTime projectRecurringTime;

    @Nullable
    private String qrCodeImageLocation;

    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date renewDate;

    @Nullable
    @Size(max = 300)
    private String schedule;

    @Nullable
    @Size(max = 300)
    private String shiftTitle;

    @Nullable
    private String specialDirections;

    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date start;

    @Nullable
    private ProjectEventStatus status;

    @Nullable
    private TimeZone timezone;

    @Nullable
    private Integer totalVolunteersNeeded;

    /**
     * whether a volunteer cap is set for this event.
     */
    @Nullable
    private Boolean volunteerCap;

    public static class ProjectEventBuilder {
        public ProjectEvent build() {
            ProjectEvent event = this.buildInternal();

            if (event.getEnd() == null || event.getStart() == null) {
                throw new IllegalStateException("Events created with the builder must have a start and end date");
            }
            if ((TRUE.equals(event.getGroupCap()) && event.getGroupLimit() == null)) {
                throw new IllegalStateException("groupLimit cannot be null when groupCap is true");
            }
            if (TRUE.equals(event.getVolunteerCap()) && event.getTotalVolunteersNeeded() == null) {
                throw new IllegalStateException("totalVolunteersNeeded cannot be null when volunteerCap is true");
            }

            return event;
        }
    }
}
