package org.justserve.cli.command;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.justserve.client.BoundaryPermissionClient;
import org.justserve.client.DynamicRoutingClient;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Command(name = "makeOrgAdmin", description = "make a user an admin for the provided organization(s). " +
        "Makes no changes to the user's boundaries.", mixinStandardHelpOptions = true)
public class MakeOrgAdmin extends BaseCommand implements Runnable {
    
    @Option(names = {"--user", "-u"}, description = "the user who will be made org admin")
    private UUID user;

    @Option(names = {"-o", "--org"}, description = "The organization(s) specified by URL slug or UID.",
            converter = OrgConverter.class, arity = "1..*", split = ",")
    private Org[] orgs;

    @Inject
    Provider<DynamicRoutingClient> dynamicRoutingClientProvider;

    @Inject
    Provider<BoundaryPermissionClient> boundaryPermissionClientProvider;

    @Override
    public void run() {
        if (isTokenInvalid()) {
            return;
        }
        DynamicRoutingClient dynamicRoutingClient = dynamicRoutingClientProvider.get();
        // since we allow submitting orgId's and orgUrl, convert any slugs to orgId's
        Map<Org, @Nullable UUID> orgUuidMap = Arrays.stream(orgs).distinct().parallel()
                .collect(Collectors.toMap(
                        org -> org,
                        org -> org instanceof OrgId ? ((OrgId) org).getId() : dynamicRoutingClient
                                .getOrgIdFromSlug(((OrgSlug) org).getSlug()).body().getId()
                ));
        if (orgUuidMap.values().stream().anyMatch(Objects::isNull)) {
            // provide a list of all invalid org slugs, not just the first failure
            List<OrgSlug> invalidOrgSlugs = new ArrayList<>();
            orgUuidMap.entrySet().stream()
                    .filter(entry -> entry.getValue() == null)
                    .map(entry -> (OrgSlug) entry.getKey())
                    .forEach(invalidOrgSlugs::add);
            err("The following organization slugs are invalid: " + invalidOrgSlugs.stream()
                    .map(OrgSlug::getSlug)
                    .collect(Collectors.joining(", ")));
            return;
        }
        log.atTrace().log("Finished converting any slugs to org id's.");
        BoundaryPermissionClient boundaryPermissionClient = boundaryPermissionClientProvider.get();
        Map<Org, @Nullable UUID> successfulReassignments = new HashMap<>();
        orgUuidMap.entrySet().stream().parallel().forEach(entry -> {
            Org org = entry.getKey();
            UUID orgId = entry.getValue();
            String orgIdentifier = org instanceof OrgSlug ? ((OrgSlug) org).getSlug() + " (" + orgId + ")" : orgId.toString();

            log.atTrace().log("Making user {} an admin for org {}", user, orgIdentifier);
            try {
                log.atTrace().log("sending request to make user {} an admin for org {}.", user, orgIdentifier);
                HttpResponse<Object> response = boundaryPermissionClient.makeAdminForOrg(orgId, user);
                log.atTrace().log("received api response status: {}", response.status());
                log.atDebug().log("Successfully made user {} an admin for org {}.", user, orgIdentifier);
                successfulReassignments.put(org, orgId);
            } catch (HttpClientResponseException e) {
                err("Failed to make user " + user + " an admin for organization " + orgIdentifier + ".");
                log.atError().setCause(e).log("Error response from API: {}", e.getResponse().body());
            }
        });
        out("successfully reassigned %d orgs to user %s", successfulReassignments.size(), user);
    }

    /**
     * abstract class whose sole purpose is to allow the user to pass a list of orgID's and org Slugs
     */
    private abstract static class Org {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    private static class OrgSlug extends Org {
        private final String slug;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    private static class OrgId extends Org {
        private final UUID id;
    }

    private static class OrgConverter implements CommandLine.ITypeConverter<Org> {
        @Override
        public Org convert(String value) {
            try {
                return new OrgId(UUID.fromString(value));
            } catch (IllegalArgumentException e) {
                if (value.matches("[^\\s/]+")) {
                    return new OrgSlug(value);
                } else {
                    throw new CommandLine.TypeConversionException("'" + value + "' is not a valid organization UID or URL slug.");
                }
            }
        }
    }
}
