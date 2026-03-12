package org.justserve.model.graph;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.justserve.model.ProjectEventStatus;

import java.util.Date;

/**
 * <h4>All potential variables to use with {@link CreateEventQuery}</h4>
 * Null values will be omitted from the generated query.
 *
 * @since 0.1.0
 * @author Jonathan Zollinger
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Serdeable
@Introspected
public class CreateEventFields extends GraphFields {
    @Nullable
    @Email
    private String contactEmail;

    @Nullable
    @Size(max = 139)
    private String contactName;

    @Nullable
    private String contactPhone;

    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date end;

    /**
     * whether a group cap is set for this event.
     */
    @Nullable
    private Boolean groupCap;

    @Nullable
    private Integer groupLimit;

    @Nullable
    private String locationLink;

    @Nullable
    @Size(max = 139)
    private String locationName;

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
    private String timezone;

    @Nullable
    private Integer totalVolunteersNeeded;

    /**
     * whether a volunteer cap is set for this event.
     */
    @Nullable
    private Boolean volunteerCap;
}
