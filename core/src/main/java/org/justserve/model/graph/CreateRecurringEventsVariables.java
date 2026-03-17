package org.justserve.model.graph;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.justserve.model.ProjectRecurringTime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Pojo to serialize the variables object passed with a createRecurringEvents mutation.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@EqualsAndHashCode(callSuper = true)
@Serdeable
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CreateRecurringEventsVariables extends GraphVariables {

    private UUID projectId;

    @JsonIgnore
    private Map<String, ProjectRecurringTime> projectEvents = new HashMap<>();

    public CreateRecurringEventsVariables(UUID projectId, @NonNull ProjectRecurringTime... events) {
        this.projectId = projectId;
        for (int i = 0; i < events.length; i++) {
            this.projectEvents.put("projectEvent" + i, events[i]);
        }
    }

    @JsonAnyGetter
    public Map<String, ProjectRecurringTime> getProjectEvents() {
        return projectEvents;
    }
}
