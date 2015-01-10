package com.github.hiendo.tsa.web;

import com.github.hiendo.tsa.db.DataPointRepository;
import com.github.hiendo.tsa.db.DataPointsEntity;
import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Resource to add a data point against a topic
 */
@Path("topics/{topic}/datapoints")
@Component
public class TopicDataPointResource {
    final static Logger logger = LoggerFactory.getLogger(TopicDataPointResource.class);

    @Autowired
    private DataPointRepository dataPointRepository;

    /**
     * Add data point against topic.  If topic doesn't exist, it is created.
     *
     * @param topic topic to add point for
     * @param dataPoint data point to add
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addDataPoints(@PathParam("topic") String topic, DataPoint dataPoint) {
        dataPointRepository.saveDataPoint(topic, dataPoint);
	}

    /**
     * Get all data points of the specified topic between a certain x value range.
     *
     * @param topic topic to get points for
     * @param startTime start time range; null means to get all points up to end x range
     * @param endTime end time range; null means to get all points from start x range to infinity
     * @return all data points of topic in the range
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DataPoints getDataPoints(@PathParam("topic") String topic, @QueryParam("start") Long startTime,
            @QueryParam("end") Long endTime) {
        DataPointsEntity dataPointsForTopic = dataPointRepository.getDataPointsForTopic(topic, startTime, endTime);
        return dataPointsForTopic.toApiEntity();
	}
}
