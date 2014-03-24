package com.github.hiendo.tsa.web;

import com.github.hiendo.tsa.chart.BasicXyLineChart;
import com.github.hiendo.tsa.chart.XyChartOptions;
import com.github.hiendo.tsa.db.DataPointsEntity;
import com.github.hiendo.tsa.db.TimeSeriesRepository;
import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;


@Path("topic/{topic}")
@Component
public class TimeSeriesTopicResource {
    final static Logger logger = LoggerFactory.getLogger(TimeSeriesTopicResource.class);

    @Autowired
    private TimeSeriesRepository timeSeriesRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addDataPoints(@PathParam("topic") String topic, DataPoint dataPoint) throws Exception {
        timeSeriesRepository.saveTime(topic, dataPoint);
	}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DataPoints getDataPoints(@PathParam("topic") String topic) throws Exception {
        return timeSeriesRepository.getAllDataPointsForTopic(topic).toApiEntity();
	}

    @GET
    @Path("chart")
    @Produces({"image/jpeg", MediaType.APPLICATION_OCTET_STREAM + "; qs=0.9"})
    public Response queryImage(@PathParam("topic") String topic, @BeanParam XyChartOptions xyChartOptions)
            throws Exception {

        final DataPointsEntity dataPointsEntity = timeSeriesRepository.getAllDataPointsForTopic(topic);

        final BasicXyLineChart basicXyPlot = new BasicXyLineChart(xyChartOptions);

        StreamingOutput stream = new StreamingOutput() {
             public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                 try {
                     basicXyPlot.writeChart(outputStream, dataPointsEntity);
                 } catch (Exception e) {
                     throw new WebApplicationException(e);
                 }
             }
         };

        return Response.ok(stream).build();
	}
}
