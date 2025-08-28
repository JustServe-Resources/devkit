package org.justserve.cli;

import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;
import picocli.jansi.graalvm.AnsiConsole;
import picocli.shell.jline3.PicocliJLineCompleter;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import static picocli.CommandLine.Help.Ansi.AUTO;

public class InteractiveShell {

    private static final String DEFAULT_PROMPT = "@|blue JustServe>|@ ";

    private final CommandLine commandLine;
    private final Consumer<String[]> executor;
    private final BiFunction<Throwable, CommandLine, Integer> onError;
    private final String prompt;

    public InteractiveShell(CommandLine commandLine,
                            Consumer<String[]> executor,
                            BiFunction<Throwable, CommandLine, Integer> onError) {
        this(commandLine, executor, onError, DEFAULT_PROMPT);
    }

    public InteractiveShell(CommandLine commandLine,
                            Consumer<String[]> executor,
                            BiFunction<Throwable, CommandLine, Integer> onError,
                            String prompt) {
        this.commandLine = commandLine;
        this.executor = executor;
        this.onError = onError;
        this.prompt = prompt;
    }

    public void start() {
        try (AnsiConsole ignored = AnsiConsole.windowsInstall()) {
            PicocliJLineCompleter picocliCommands = new PicocliJLineCompleter(commandLine.getCommandSpec());
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(picocliCommands)
                    .parser(new DefaultParser())
                    .variable(LineReader.LIST_MAX, 50)   // max tab completion candidates
                    .build();

            String ansiPrompt = AUTO.string(prompt);
            String rightPrompt = null;

            // start the shell and process input until the user quits with Ctl-D
            String line;
            while (true) {
                try {
                    line = reader.readLine(ansiPrompt, rightPrompt, (MaskingCallback) null, null);
                    if (line.matches("^\\s*#.*")) {
                        continue;
                    }
                    if ("exit".equals(line)) {
                        return;
                    }
                    ParsedLine pl = reader.getParser().parse(line, 0);
                    String[] arguments = pl.words().toArray(new String[0]);
                    executor.accept(arguments);
                } catch (UserInterruptException | EndOfFileException e) {
                    return;
                }
            }
        } catch (Throwable t) {
            onError.apply(t, commandLine);
        } finally {
            AnsiConsole.systemUninstall();
        }
    }
}
