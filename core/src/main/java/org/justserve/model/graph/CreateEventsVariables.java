package org.justserve.model.graph;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.justserve.model.ProjectEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Pojo to serialize the variables object passed with a createEvents mutation.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@EqualsAndHashCode(callSuper = true)
@Serdeable
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CreateEventsVariables extends GraphVariables {

    private UUID projectId;

    @JsonIgnore
    private Map<String, ProjectEvent> projectEvents = new HashMap<>();

    public CreateEventsVariables(UUID projectId, @NonNull ProjectEvent... events) {
        this.projectId = projectId;
        for (int i = 0; i < events.length; i++) {
            this.projectEvents.put("projectEvent" + i, events[i]);
        }
    }

    @JsonAnyGetter
    public Map<String, ProjectEvent> getProjectEvents() {
        return projectEvents;
    }
}
