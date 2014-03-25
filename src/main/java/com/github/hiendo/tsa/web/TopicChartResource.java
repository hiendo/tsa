package com.github.hiendo.tsa.web;

import com.github.hiendo.tsa.chart.BasicXyLineChart;
import com.github.hiendo.tsa.chart.XyChartOptions;
import com.github.hiendo.tsa.db.DataPointsEntity;
import com.github.hiendo.tsa.db.DataPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Basic charts for for a particular topic
 */
@Path("topics/{topic}/charts")
@Component
public class TopicChartResource {
    final static Logger logger = LoggerFactory.getLogger(TopicChartResource.class);

    @Autowired
    private DataPointRepository dataPointRepository;

    @GET
    @Produces({"image/jpeg", MediaType.APPLICATION_OCTET_STREAM + "; qs=0.9"}) // Stream used for testing
    public Response queryImage(@PathParam("topic") String topic, @BeanParam XyChartOptions xyChartOptions)
            throws Exception {

        final DataPointsEntity dataPointsEntity =
                dataPointRepository.getDataPointsForTopic(topic, xyChartOptions.getStartX(), xyChartOptions.getEndX());

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
