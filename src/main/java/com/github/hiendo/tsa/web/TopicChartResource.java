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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Charts for for a particular topic or list of topics
 */
@Path("charts")
@Component
public class TopicChartResource {
    final static Logger logger = LoggerFactory.getLogger(TopicChartResource.class);

    @Autowired
    private DataPointRepository dataPointRepository;

    @GET
    @Path("xyline")
    @Produces({"image/jpeg", MediaType.APPLICATION_OCTET_STREAM + "; qs=0.9"}) // Stream used for testing
    public Response queryImage(@QueryParam(value = "topic") List<String> topics, @BeanParam XyChartOptions xyChartOptions)
            throws Exception {

        final List<DataPointsEntity> dataPointsEntities = new ArrayList<>();
        for(String topic : topics) {
            dataPointsEntities.add(dataPointRepository
                    .getDataPointsForTopic(topic, xyChartOptions.getStartX(), xyChartOptions.getEndX()));
        }

        final BasicXyLineChart basicXyPlot = new BasicXyLineChart(xyChartOptions);

        StreamingOutput stream = new StreamingOutput() {
             public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                 try {
                     basicXyPlot.writeChart(outputStream, dataPointsEntities.toArray(new DataPointsEntity[0]));
                 } catch (Exception e) {
                     throw new WebApplicationException(e);
                 }
             }
         };

        return Response.ok(stream).build();
	}
}
