package org.justserve.model.graph;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;
import org.justserve.model.ProjectRecurringTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses the dynamic response from the createRecurringEvents GraphQL mutation.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@Serdeable
@Data
public class CreateRecurringEventsData {

    @JsonIgnore
    private Map<String, ProjectRecurringTime> events = new HashMap<>();

    @JsonAnySetter
    public void addEvent(String key, ProjectRecurringTime event) {
        if (key != null && key.startsWith("event")) {
            events.put(key, event);
        }
    }

    /**
     * Helper to return all the dynamically parsed events as a single list.
     *
     * @return List of newly created recurring events
     */
    @JsonIgnore
    public List<ProjectRecurringTime> getEventList() {
        return new ArrayList<>(events.values());
    }
}
