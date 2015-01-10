package com.github.hiendo.tsa.servertests.operations;

import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

/**
 * Operations to act on a data point within a topic.
 */
public class TopicDataPointOperations {

    private WebTarget webTarget;

    public TopicDataPointOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    /**
     * Add the datapoint to the topic.
     *
     * @param topic topic
     * @param dataPoint data point for the topic
     */
    public void addData(String topic, DataPoint dataPoint) {
        webTarget.path("/api/topics/" + topic + "/datapoints").request().post(Entity.json(dataPoint));
    }

    /**
     * Get all the datapoint for the specified topic.
     *
     * @param topic topic to add
     * @return all the data points.
     */
    public DataPoints getDataForTopic(String topic) {
        return webTarget.path("/api/topics/" + topic + "/datapoints").request().get(DataPoints.class);
    }

    /**
     * Get the data points within a specified range.
     *
     * @param topic topic to get data points for
     * @param start start range
     * @param end end range
     * @return all the data points within the range.
     */
    public DataPoints getDataForTopicInRange(String topic, long start, long end) {
        return webTarget.path("/api/topics/" + topic + "/datapoints").queryParam("start", start).queryParam(
                "end", end).request().get(DataPoints.class);
    }
}
