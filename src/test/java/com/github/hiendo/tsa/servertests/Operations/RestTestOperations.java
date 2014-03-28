
package com.github.hiendo.tsa.servertests.operations;

import com.github.hiendo.tsa.web.entities.TestEntity;

import javax.ws.rs.client.WebTarget;

public class RestTestOperations {

    private WebTarget webTarget;

    public RestTestOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public TestEntity getEntityFromController() {
        return webTarget.path("/testController").request().get(TestEntity.class);
    }
}
