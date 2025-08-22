package org.justserve.auth;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.ClientFilter;
import io.micronaut.http.annotation.RequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A Micronaut client filter that adds a bearer token to outgoing requests.
 * This filter is applied to all requests ("/**") and requires the
 * `justserve.token` property to be set.
 */
@SuppressWarnings("unused")
@ClientFilter("/**")
@Requires(property = "justserve.token")
public class JustServeClientFilter {
    private final String token;

    private final Logger log = LoggerFactory.getLogger(JustServeClientFilter.class);

    /**
     * Constructs a new JustServeClientFilter.
     *
     * @param token The bearer token to be added to requests, injected from the
     *              `justserve.token` property.
     */
    public JustServeClientFilter(@Property(name = "justserve.token") String token) {
        this.token = token;
    }

    /**
     * Filters outgoing requests to add a bearer token to the Authorization header.
     *
     * @param request The mutable HTTP request to be filtered.
     */
    @RequestFilter
    public void doFilter(MutableHttpRequest<?> request) {
        log.debug("adding bearer token to request ({})", request.getMethod() + " " + request.getUri());
        request.bearerAuth(token);
    }
}
