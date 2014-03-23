
package com.github.hiendo.tsa.servertests.operations;

import com.github.hiendo.tsa.db.DataPoints;
import com.github.hiendo.tsa.entities.DataPoint;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

public class TimeSeriesOperations {

    private WebTarget webTarget;

    public TimeSeriesOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public void addData(String topic, double value) {
        webTarget.path("/topic/" + topic).request().post(Entity.json(new DataPoint(value)));
    }

    public DataPoints getDataForTopic(String topic) {
        return webTarget.path("/topic/" + topic).request().get(DataPoints.class);
    }
}
