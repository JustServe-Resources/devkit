package org.justserve.cli.command;

import io.micronaut.core.annotation.ReflectiveAccess;
import org.justserve.cli.util.JustServeVersionProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Mixin that adds help, version and other common options to a command. Example usage:
 * <pre>
 * &#064;Command(name = "command")
 * class App {
 *     &#064;Mixin
 *     CommonOptionsMixin commonOptions // adds common options to the command
 *
 *     // ...
 * }
 * </pre>
 *
 * @author Remko Popma
 * @version 1.0
 */
@Command(mixinStandardHelpOptions = true, versionProvider = JustServeVersionProvider.class)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class CommonOptionsMixin {

    @Option(names = {"-x", "--stacktrace"}, defaultValue = "false", description = "Show full stack trace when exceptions occur.")
    @ReflectiveAccess
    public boolean showStacktrace;

    @Option(names = {"-v", "--verbose"}, defaultValue = "false", description = "Create verbose output.")
    @ReflectiveAccess
    public boolean verbose;
}