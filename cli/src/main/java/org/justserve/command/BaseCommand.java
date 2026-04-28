package org.justserve.command;

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

/**
 * Commonly used values and methods for commands.
 *
 * @author Jonathan Zollinger
 * @since 0.0.1
 */
@Command
public class BaseCommand{

    public Ansi blue = ansi().fgRgb(0, 158, 185);
    public Ansi orange = ansi().fgRgb(255, 140, 0); // OG color is 239, 94, 57
    public Ansi red = ansi().fgRgb(233, 59, 84); // I definitely eyeballed this one
    public Ansi yellow = ansi().fgRgb(225, 188, 33);

    public Ansi normalStyle = ansi().reset();
    public Ansi warningStyle = yellow;
    public Ansi errorTitleStyle = red.bold();
    public Ansi errorInfoStyle = ansi().reset();

    @Spec
    @ReflectiveAccess
    protected CommandSpec spec;

    @Value("${justserve.token}")
    String token;

    boolean isTokenInvalid() {
        if ("i-need-to-be-defined".equals(token) || null == token || token.isEmpty()) {
            err(("NO AUTHENTICATION PROVIDED" + System.lineSeparator() +
                    "The Authentication token is not assigned as an environment variable." + System.lineSeparator() +
                    "Please define the environment variable \"JUSTSERVE_TOKEN\" and try again."));
            return true;
        }
        return false;
    }

    public void out(String message, Object... args) {
        outWriter().ifPresent(writer -> writer.println(applyStyle(String.format(message, args), normalStyle)));
    }

    public void err(String message, Object... args) {
        errWriter().ifPresent(writer -> writer.println(applyErrorStyle(message, args)));
    }

    public void warning(String message, Object... args) {
        outWriter().ifPresent(writer -> writer.println(applyStyle(String.format(message, args), warningStyle)));
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
    
    protected String applyStyle(String message, Ansi... style) {
        Ansi styledMessage = stream(style).reduce(ansi(), Ansi::a, Ansi::a);
        return styledMessage.a(message).reset().toString();
    }    

    /**
     * Returns a formatted error message.
     *
     * @param message The error message to print, which can be a format string.
     * @param args    Arguments referenced by the format specifiers in the format string.
     */
    String applyErrorStyle(String message, Object... args) {
        String fullMessage = (args == null || args.length == 0) ? message : String.format(message, args);
        String[] lines = fullMessage.split("(\\r\\n|\\r|\\n)");
        StringBuilder output = new StringBuilder();
        output.append(applyStyle(lines[0], errorTitleStyle));
        if (lines.length > 1) {
            stream(lines).skip(1).forEach(line -> output.append(applyStyle(line, errorInfoStyle)));
        }
        return output.toString();
    }
}
