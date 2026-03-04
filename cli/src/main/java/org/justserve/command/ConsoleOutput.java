package org.justserve.command;

public interface ConsoleOutput {
    ConsoleOutput NOOP = new ConsoleOutput() {
        @Override
        public void out(String message) { }

        @Override
        public void err(String message) { }

        @Override
        public void warning(String message) { }

        @Override
        public boolean showStacktrace() {
            return false;
        }

        @Override
        public boolean verbose() {
            return false;
        }

    };

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
