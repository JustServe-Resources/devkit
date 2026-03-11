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

import java.util.Date;
import java.util.UUID;

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
    private Boolean deleted;

    @Nullable
    private UUID deletedBy;

    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date deletedOn;

    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date end;

    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date endDateTimeOffset;

    @Nullable
    private Boolean eventCapReached;

    @Nullable
    private Integer groupCap;

    @Nullable
    private Integer groupLimit;

    @Nullable
    private UUID id;

    @Nullable
    private String locationLink;

    @Nullable
    @Size(max = 139)
    private String locationName;

    @Nullable
    private UUID projectEventLocationId;

    @Nullable
    private UUID projectRecurringTimeId;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date startDateTimeOffset;

    @Nullable
    private String status;

    @Nullable
    private UUID statusId;

    @Nullable
    private String timezone;

    @Nullable
    private UUID timeZoneEnumId;

    @Nullable
    private Integer totalVolunteersNeeded;

    @Nullable
    private Integer volunteerCap;

    @Nullable
    private Integer volunteersNeeded;
}
