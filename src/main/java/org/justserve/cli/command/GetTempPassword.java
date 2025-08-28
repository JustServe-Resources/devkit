package org.justserve.cli.command;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.justserve.client.UserClient;
import org.justserve.model.UserHashRequestByEmail;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.awt.datatransfer.StringSelection;
import java.util.concurrent.Callable;

import static java.awt.Toolkit.getDefaultToolkit;
import static org.justserve.cli.util.JustServePrinter.printError;

@Command(name = "getTempPassword", description = "get a temporary password for a user")
public class GetTempPassword extends BaseCommand implements Callable<Integer> {

    @ReflectiveAccess
    @Option(names = {"-e", "--email"}, description = "email for the user whose temporary password will be generated")
    String email;
    
    @Inject
    @ReflectiveAccess
    Provider<UserClient> userClientProvider;

    @Value("${justserve.token}")
    String token;

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Integer call() throws Exception {
        HttpResponse<String> response;
        if ("i-need-to-be-defined".equals(token) || null == token) {
            printError(("NO AUTHENTICATION PROVIDED" + System.lineSeparator() +
                    "The Authentication token is not assigned as an environment variable." + System.lineSeparator() +
                    "Please define the environment variable \"JUSTSERVE_TOKEN\" and try again."));
            return 1;
        }
        try {
            UserClient userClient = userClientProvider.get();
            response = userClient.getTempPassword(new UserHashRequestByEmail(email));
        } catch (HttpClientResponseException e) {
            String errorMessage = "Received an unexpected response from JustServe:" +
                    String.format("%n%d (%s)", e.getResponse().status().getCode(), e.reason());
            printError(errorMessage);
            return 1;
        }
        if (response != null) {
            getDefaultToolkit().getSystemClipboard().setContents(
                    new StringSelection(response.body().replace("\"", "").trim()), null);
            return 0;
        } else {
            printError("An unexpected error occurred. Response from JustServe was null.");
            return 1;
        }
    }
}
