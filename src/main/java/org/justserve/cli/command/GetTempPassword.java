package org.justserve.cli.command;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.validation.constraints.Email;
import org.justserve.client.UserClient;
import org.justserve.model.UserHashRequestByEmail;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "getTempPassword", description = "get a temporary password for a user")
public class GetTempPassword extends BaseCommand implements Runnable {

    @Email
    @Option(names = {"-e", "--email"}, description = "email for the user whose temporary password will be generated")
    String email;

    @Inject
    Provider<UserClient> userClientProvider;


    @Override
    public void run() {
        if (isTokenInvalid()) {
            return;
        }
        HttpResponse<String> response;
        try {
            UserClient userClient = userClientProvider.get();
            response = userClient.getTempPassword(new UserHashRequestByEmail(email));
        } catch (HttpClientResponseException e) {
            String errorMessage = "Received an unexpected response from JustServe:" +
                    String.format("%n%d (%s)", e.getResponse().status().getCode(), e.reason());
            err(errorMessage);
            return;
        }
        if (response != null) {
            out(response.body().replace("\"", "").trim());
        } else {
            err("An unexpected error occurred. Response from JustServe was null.");
        }
    }
}
