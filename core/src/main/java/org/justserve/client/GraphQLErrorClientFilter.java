package org.justserve.client;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

/**
 * GraphQL calls return 200 even if errors occur. This filter marks the response as a failure and maps the server's
 * error message to an{@link  HttpClientResponseException}. This does not change the status code.
 *
 * @author jonathan zollinger
 * @since 0.1.0
 */
@Filter("/**/graphql")
@Slf4j
public class GraphQLErrorClientFilter implements HttpClientFilter {

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {

        return Mono.from(chain.proceed(request)).map(response -> {
            Optional<Map> bodyOpt = response.getBody(Map.class);

            if (bodyOpt.isPresent()) {
                Map<?, ?> body = bodyOpt.get();
                String err = "errors";
                if (body.containsKey(err) && body.get(err) != null) {
                    Object errors = body.get(err);
                    String errorMessage = "GraphQL returned errors: " + errors.toString();
                    throw new HttpClientResponseException(errorMessage, response);
                }
                log.debug("GraphQL request contains no errors");
            }
            return response;
        });
    }
}