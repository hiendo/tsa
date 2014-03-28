
package com.github.hiendo.tsa.servertests.operations;

import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

public class TopicDataPointOperations {

    private WebTarget webTarget;

    public TopicDataPointOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public void addData(String topic, DataPoint dataPoint) {
        webTarget.path("/api/topics/" + topic + "/datapoints").request().post(Entity.json(dataPoint));
    }

    public DataPoints getDataForTopic(String topic) {
        return webTarget.path("/api/topics/" + topic + "/datapoints").request().get(DataPoints.class);
    }

    public DataPoints getDataForTopicInRange(String topic, double start, double end) {
        return webTarget.path("/api/topics/" + topic + "/datapoints").queryParam("start", start).queryParam("end", end)
                .request().get(DataPoints.class);
    }
}
