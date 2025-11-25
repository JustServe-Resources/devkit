package org.justserve.cli.command;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.ReflectiveAccess;
import org.fusesource.jansi.Ansi;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.io.PrintWriter;
import java.util.Optional;

import static java.util.Arrays.stream;
import static org.fusesource.jansi.Ansi.ansi;
import static org.justserve.cli.util.JustServeStyle.*;

/**
 * Base class for terminal output with the JustServe color scheme
 * <a href="https://justserve.zendesk.com/hc/en-us/articles/30949071567387-Style-Guides">See style guide</a>
 */
@Command
public class BaseCommand implements ConsoleOutput {

    @Spec
    @ReflectiveAccess
    protected CommandSpec spec;

    @Value("${justserve.token}")
    String token;

    boolean validateToken() {
        if ("i-need-to-be-defined".equals(token) || null == token) {
            err(("NO AUTHENTICATION PROVIDED" + System.lineSeparator() +
                    "The Authentication token is not assigned as an environment variable." + System.lineSeparator() +
                    "Please define the environment variable \"JUSTSERVE_TOKEN\" and try again."));
            return false;
        }
        return true;
    }

    /**
     * Prints a formatted message in the normalStyle (whatever that style may be)
     * @param message the message to print
     */
    @Override
    public void out(String message) {
        out(message, (Object[]) null);
    }

    /**
     * Prints a formatted message in the normalStyle (whatever that style may be)
     * @param message the message to print
     */
    public void out(String message, Object... args) {
        outWriter().ifPresent(writer -> writer.println(applyStyle(String.format(message, args), normalStyle)));
    }

    /**
     * Prints a formatted error message to the standard error stream in red.
     * <p>
     * First line is formatted as a titled error message and remaining lines are formatted as a normal error style.
     *
     * @param message The error message to print.
     */
    public void err(String message) {
        err(message, (Object[]) null);
    }

    /**
     * Prints a formatted error message to the standard error stream in red.
     * <p>
     * First line is formatted as a titled error message and remaining lines are formatted as a normal error style.
     *
     * @param message The error message to print, which can be a format string.
     * @param args    Arguments referenced by the format specifiers in the format string.
     */
    public void err(String message, Object... args) {
        //splits the message into lines and prints the first line in the errorTitleStyle, the rest in errorInfoStyle
        String fullMessage = (args == null || args.length == 0) ? message : String.format(message, args);
        String[] lines = fullMessage.split("(\\r\\n|\\r|\\n)");
        errWriter().ifPresent(writer -> writer.println(applyStyle(" Error|" + lines[0], errorTitleStyle)));
        if (lines.length > 1) {
            stream(lines).skip(1).forEach(line -> {
                errWriter().ifPresent(writer -> writer.println(applyStyle(line, errorInfoStyle)));
            });
        }
    }

    public void warning(String message) {
        outWriter().ifPresent(writer -> writer.println(applyStyle(message, warningStyle)
        ));
    }

    @Override
    public boolean showStacktrace() {
        return false;
    }

    @Override
    public boolean verbose() {
        return false;
    }


    @NonNull
    public Optional<PrintWriter> outWriter() {
        return getSpec().map(spec -> spec.commandLine().getOut());
    }

    @NonNull
    public Optional<PrintWriter> errWriter() {
        return getSpec().map(spec -> spec.commandLine().getErr());
    }

    @NonNull
    public Optional<CommandSpec> getSpec() {
        return Optional.ofNullable(spec);
    }

    /**
     * Returns a stylized string in a given set of styles.
     * <p>
     * combines the styles provided into one style (ie bold and red) and adds an ANSI reset at the end of the string.
     *
     * @param message text to be returned in a given style
     * @param style   one or several styles to be used against the given message
     * @return string containing the combined ANSI escape sequences, the message, and the reset code.
     */
    private static String applyStyle(String message, Ansi... style) {
        Ansi styledMessage = stream(style).reduce(ansi(), Ansi::a, Ansi::a);
        return styledMessage.a(message).reset().toString();
    }
}
