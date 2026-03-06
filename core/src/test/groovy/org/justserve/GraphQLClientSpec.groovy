package org.justserve

import jakarta.inject.Inject
import org.justserve.client.GraphQLClient
import org.justserve.client.GraphQLRequest
import org.justserve.model.GraphQLCreateProjectRequest
import spock.lang.Shared
import spock.lang.Specification

class GraphQLClientSpec extends Specification{

    @Shared
    @Inject
    private GraphQLClient client;

    void "simple test"(){
        GraphQLRequest request = new GraphQLCreateProjectRequest()
        client.createProject()
    }
}
