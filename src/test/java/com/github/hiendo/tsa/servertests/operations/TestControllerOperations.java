
package com.github.hiendo.tsa.servertests.operations;

import com.github.hiendo.tsa.entities.TestEntity;

import javax.ws.rs.client.WebTarget;

public class TestControllerOperations {

    private WebTarget webTarget;

    public TestControllerOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public TestEntity getTestEntity() {
        return webTarget.path("/testEntity").request().get(TestEntity.class);
    }
}
