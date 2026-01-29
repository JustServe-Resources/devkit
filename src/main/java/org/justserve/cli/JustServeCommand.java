package org.justserve.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import org.justserve.cli.command.*;
import org.justserve.cli.util.JustServeVersionProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParameterException;
import picocli.jansi.graalvm.AnsiConsole;

@Command(subcommands = {GetTempPassword.class, MakeOrgAdmin.class, UnReassignProjects.class, AssignOrgToProject.class},
        mixinStandardHelpOptions = true,
        name = "justserve", versionProvider = JustServeVersionProvider.class,
        description = "justserve-cli is a terminal tool to help specialists and admin using JustServe")
public class JustServeCommand extends BaseCommand implements Runnable {

    public static void main(String[] args) {
        try (AnsiConsole ignored = AnsiConsole.windowsInstall()) {
            PicocliRunner.run(JustServeCommand.class, args);
        }

    }

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "No command specified");
    }
}
