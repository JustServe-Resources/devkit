package org.justserve.client;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * Adds a retry to any 500 errors. The micronaut generator doesn't support this yet, so I'm adding it here instead
 * <br>
 * You can control the retry behavior by setting the {@code micronaut.http.client.retry-attempts}
 * and {@code micronaut.http.client.retry-timeout} environment variables or configuration properties.
 * The default retry count is 3 and the default retry timeout is 1 second.
 *
 * @since 0.1.0
 * @author jonathan zollinger
 */
@Filter("/**")
public class RetryClientFilter implements HttpClientFilter {

    @Value("${micronaut.http.client.retry-attempts:3}")
    int retryAttempts;

    @Value("${micronaut.http.client.retry-timeout:1s}")
    Duration retryTimeout;

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {

        return Mono.from(chain.proceed(request))
                .retryWhen(Retry.backoff(retryAttempts, retryTimeout)
                        .filter(throwable -> {
                            if (throwable instanceof HttpClientResponseException e) {
                                return 500 == e.getStatus().getCode();
                            }
                            return true;
                        })
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                retrySignal.failure()
                        )
                );
    }
}