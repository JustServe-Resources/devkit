package org.justserve;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.justserve.client.UserClient;
import org.justserve.model.UserHashRequestByEmail;
import org.justserve.util.VersionProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.jansi.graalvm.AnsiConsole;

import static org.justserve.util.JustServePrinter.printError;
import static org.justserve.util.JustServePrinter.printNormal;

@Command(name = "justserve", versionProvider = VersionProvider.class,
        description = "justserve-cli is a terminal tool to help specialists and admin using JustServe")
public class BaseCommand implements Runnable {

    @Option(names = {"-e", "--email"}, description = "email for the user whose temporary password will be generated")
    String email;

    @Option(names = {"version", "--version", "-v"}, versionHelp = true, description = "print version info and exit")
    boolean version = false;

    @Inject
    Provider<UserClient> userClientProvider;

    @Value("${justserve.token}")
    String token;

    public static void main(String[] args) {
        try (AnsiConsole ignored = AnsiConsole.windowsInstall()) {
            PicocliRunner.run(BaseCommand.class, args);
        }

    }

    public void run() {
        HttpResponse<String> response;
        if ("i-need-to-be-defined".equals(token) || null == token) {
            printError(("NO AUTHENTICATION PROVIDED" + System.lineSeparator() +
                    "The Authentication token is not assigned as an environment variable." + System.lineSeparator() +
                    "Please define the environment variable \"JUSTSERVE_TOKEN\" and try again."));
            return;
        }
        try {
            UserClient userClient = userClientProvider.get();
            response = userClient.getTempPassword(new UserHashRequestByEmail(email));
        } catch (HttpClientResponseException e) {
            String errorMessage = "Received an unexpected response from JustServe:" +
                    String.format("%n%d (%s)", e.getResponse().status().getCode(), e.reason());
            printError(errorMessage);
            return;
        }
        if (response != null) {
            printNormal(response.body().replace("\"", "").trim());
        } else {
            printError("An unexpected error occurred. Response from JustServe was null.");
        }
    }
}
