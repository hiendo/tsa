
package com.github.hiendo.tsa.servertests.operations;

import com.github.hiendo.tsa.entities.TestEntity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class RestTestOperations {

    private WebTarget webTarget;

    public RestTestOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public TestEntity getTestEntity() {
        return webTarget.path("/testEntity").request().get(TestEntity.class);
    }

    public TestEntity getTestEntityFromResource() {
        return webTarget.path("/rest/testresource").request().accept(MediaType.APPLICATION_JSON_TYPE).get(
                TestEntity.class);
    }
}
