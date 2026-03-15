package org.justserve.model.graph;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.justserve.model.ProjectEvent;

import java.util.UUID;

/**
 * Pojo to serialize the variables object passed with a createEvent mutation.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@EqualsAndHashCode(callSuper = true)
@Serdeable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CreateEventVariables extends GraphVariables {
    private UUID projectId;
    private ProjectEvent projectEvent;
}
