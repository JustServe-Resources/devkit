package org.justserve.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.justserve.cli.command.BaseCommand;
import org.justserve.cli.util.JustServeVersionProvider;
import org.justserve.client.UserClient;
import org.justserve.model.UserHashRequestByEmail;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.jansi.graalvm.AnsiConsole;

import java.awt.datatransfer.StringSelection;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

import static java.awt.Toolkit.getDefaultToolkit;
import static org.justserve.cli.util.JustServePrinter.printError;

@Command(name = "justserve", versionProvider = JustServeVersionProvider.class,
        description = "justserve-cli is a terminal tool to help specialists and admin using JustServe")
public class JustServeCommand extends BaseCommand implements Callable<Integer> {

    private static Boolean interactiveShell = false;

    private static final BiFunction<Throwable, CommandLine, Integer> exHandler = (e, commandLine) -> {
        BaseCommand command = commandLine.getCommand();
        command.err(e.getMessage());
        if (command.showStacktrace()) {
            e.printStackTrace(commandLine.getErr());
        }
        return 1;
    };


    public static void main(String[] args) {
        //if the command is called with no args, enter the interactive shell
        if (args.length == 0) {
            CommandLine commandLine = createCommandLine();
            JustServeCommand.interactiveShell = true;
            new InteractiveShell(commandLine, JustServeCommand::execute, exHandler).start();
        } else {
            System.exit(execute(args));
        }

    }

    static int execute(String[] args) {
        boolean noOpConsole = args.length > 0 && args[0].startsWith("update-cli-config");
        try (BeanContext beanContext = ApplicationContext.builder().deduceEnvironment(false).start()) {
            return createCommandLine(beanContext, noOpConsole).execute(args);
        }
    }

    static CommandLine createCommandLine() {
        boolean noOpConsole = JustServeCommand.interactiveShell;
        try (BeanContext beanContext = ApplicationContext.builder().deduceEnvironment(false).start()) {
            return createCommandLine(beanContext, noOpConsole);
        }
    }

    private static CommandLine createCommandLine(BeanContext beanContext, boolean noOpConsole) {
        JustServeCommand starter = beanContext.getBean(JustServeCommand.class);
        CommandLine commandLine = new CommandLine(starter, new JustServeFactory(beanContext));
        commandLine.setExecutionExceptionHandler(
                (ex, thisCmd, parseResult) -> exHandler.apply(ex, thisCmd)
        );
        commandLine.setUsageHelpWidth(100);
        return commandLine;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Integer call() throws Exception {
        throw new ParameterException(spec.commandLine(), "No command specified");
    }
}
