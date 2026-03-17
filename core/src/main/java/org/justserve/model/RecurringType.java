package org.justserve.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

/**
 * JustServe Project Recurring Type.
 *
 * @author Jonathan Zollinger
 * @since 0.1.0
 */
@Serdeable
@Introspected
public enum RecurringType {
    WEEKLY,
    MONTHLY
}
