package org.justserve.util;

import org.fusesource.jansi.Ansi;

import static java.lang.System.lineSeparator;
import static java.util.Arrays.stream;
import static org.fusesource.jansi.Ansi.ansi;


/**
 * A utility class for printing to the console using the JustServe color scheme.
 * <a href="https://justserve.zendesk.com/hc/en-us/articles/30949071567387-Style-Guides">See style guide</a>
 */
public final class JustServePrinter {

    private final static Ansi blue = ansi().fgRgb(0, 158, 185);
    private final static Ansi orange = ansi().fgRgb(239, 94, 57);
    private final static Ansi red = ansi().fgRgb(233, 59, 84); // I definitely eyeballed this one
    private final static Ansi yellow = ansi().fgRgb(225, 188, 33);

    private final static Ansi normalStyle = ansi().reset();
    private final static Ansi titleStyle = blue.bold();
    private final static Ansi emphasisStyle = orange;
    private final static Ansi warningStyle = yellow;
    private final static Ansi errorTitleStyle = red.bold();
    private final static Ansi errorInfoStyle = ansi().reset();


    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private JustServePrinter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static String applyStyle(String message, Ansi... style) {
        Ansi styledMessage = stream(style).reduce(ansi(), Ansi::a, Ansi::a);
        String[] lines = message.split(lineSeparator());
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(styledMessage.a(line).reset());
        }
        return sb.toString();
    }

    private static void jsPrint(String message, Ansi... style) {
        System.out.println(applyStyle(message, style));
    }


    private static void jsPrintErr(String message, Ansi... style) {
        System.err.println(applyStyle(message, style));
    }

    /**
     * Prints a standard message using the primary brand color (JustServe Blue).
     *
     * @param message The message to print.
     */
    public static void printNormal(String message) {
        jsPrint(message, normalStyle);
    }

    /**
     * Returns a String stylized in Orange.
     *
     * @param message The message to print.
     */
    public static String styleTitle(String message) {
       return applyStyle(message, titleStyle);
    }

    /**
     * Returns a String stylized in Blue.
     *
     * @param message The message to print.
     */
    public static String styleOrange(String message) {
        return applyStyle(message, orange);
    }

    /**
     * Prints an error message to the standard error stream in red.
     *
     * @param message The error message to print.
     */
    public static void printError(String message) {
        printError(message, (Object[]) null);
    }

    /**
     * Prints a formatted error message to the standard error stream in red.
     *
     * @param message The error message to print, which can be a format string.
     * @param args    Arguments referenced by the format specifiers in the format string.
     */
    public static void printError(String message, Object... args) {
        String fullMessage = (args == null || args.length == 0) ? message : String.format(message, args);
        String[] lines = fullMessage.split("(\\r\\n|\\r|\\n)");
        jsPrintErr(lines[0], errorTitleStyle);
        if (lines.length > 1) {
            stream(lines).skip(1).forEach(line -> jsPrintErr(line, errorInfoStyle));
        }
    }
}