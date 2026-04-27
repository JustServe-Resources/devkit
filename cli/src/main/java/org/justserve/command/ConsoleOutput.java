package org.justserve.command;

public interface ConsoleOutput {

    void out(String message);

    void err(String message);

    void warning(String message);

    boolean showStacktrace();

    boolean verbose();

    default void green(String message) {
        out(message);
    }

    default void red(String message) {
        out(message);
    }
}
