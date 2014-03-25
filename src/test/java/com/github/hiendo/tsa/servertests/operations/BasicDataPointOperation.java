
package com.github.hiendo.tsa.servertests.operations;

import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.internal.util.collection.ByteBufferInputStream;
import org.joda.time.MutableDateTime;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;

public class BasicDataPointOperation {

    private WebTarget webTarget;

    public BasicDataPointOperation(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public void addData(String topic, DataPoint dataPoint) {
        webTarget.path("/rest/topic/" + topic).request().post(Entity.json(dataPoint));
    }

    public DataPoints getDataForTopic(String topic) {
        return webTarget.path("/rest/topic/" + topic).request().get(DataPoints.class);
    }

    public byte[] downloadGraph(String topic) {
        return webTarget.path("/rest/topic/" + topic + "/chart").request().accept(APPLICATION_OCTET_STREAM)
                .get(byte[].class);
    }
}
