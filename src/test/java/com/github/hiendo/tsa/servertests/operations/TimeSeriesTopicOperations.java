
package com.github.hiendo.tsa.servertests.operations;

import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

public class TimeSeriesTopicOperations {

    private WebTarget webTarget;

    public TimeSeriesTopicOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public void addData(String topic, double value) {
        webTarget.path("/rest/topic/" + topic).request().post(Entity.json(new DataPoint(value)));
    }

    public DataPoints getDataForTopic(String topic) {
        return webTarget.path("/rest/topic/" + topic).request().get(DataPoints.class);
    }
}
