package com.github.hiendo.tsa.web;

import com.github.hiendo.tsa.db.TimeSeriesRepository;
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
import javax.ws.rs.core.MediaType;


@Path("topic/{topic}")
@Component
public class TimeSeriesTopicResource {
    final static Logger logger = LoggerFactory.getLogger(TimeSeriesTopicResource.class);

    @Autowired
    private TimeSeriesRepository timeSeriesRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addDataPoints(@PathParam("topic") String topic, DataPoint dataPoint) throws Exception {
        timeSeriesRepository.saveTime(topic, dataPoint.getValue());
	}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DataPoints getDataPoints(@PathParam("topic") String topic) throws Exception {
        return timeSeriesRepository.getAllDataPointsForTopic(topic).toApiEntity();
	}
}
