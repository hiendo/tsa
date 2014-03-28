package com.github.hiendo.tsa.web;

import com.github.hiendo.tsa.chart.BasicBoxAndWhiskerChart;
import com.github.hiendo.tsa.chart.BasicXyLineChart;
import com.github.hiendo.tsa.chart.BoxAndWhisperMetricAggregationIntervalChartOption;
import com.github.hiendo.tsa.chart.DataPointsSet;
import com.github.hiendo.tsa.chart.TimeIntervalDatapointsSplitter;
import com.github.hiendo.tsa.chart.XyChartOptions;
import com.github.hiendo.tsa.db.DataPointsEntity;
import com.github.hiendo.tsa.db.DataPointRepository;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Charts for for a particular topic or list of topics
 */
@Path("charts")
@Component
public class TopicChartResource {
    final static Logger logger = LoggerFactory.getLogger(TopicChartResource.class);

    @Autowired
    private TimeIntervalDatapointsSplitter timeIntervalDatapointsSplitter;

    @Autowired
    private DataPointRepository dataPointRepository;

    /**
     * Get a basic line chart drawn for a number of topics.
     *
     * @param topics list of topics to draw chart
     * @param xyChartOptions display chart options
     * @return Response with jpeg chart image
     */
    @GET
    @Path("xyline")
    @Produces({"image/jpeg", MediaType.APPLICATION_OCTET_STREAM + "; qs=0.9"}) // Stream used for testing
    public Response getXyLineChart(@QueryParam(value = "topic") List<String> topics, @BeanParam XyChartOptions xyChartOptions)
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

    /**
     * Get a set of box and whisker charts where each item is stats aggregated metrics by some time interval.
     *
     * @param topics list of topics to draw chart
     * @param chartOptions display chart options
     * @return Response with jpeg chart image
     */
    @GET
    @Path("metricAggregatorInterval")
    @Produces({"image/jpeg", MediaType.APPLICATION_OCTET_STREAM + "; qs=0.9"}) // Stream used for testing
    public Response getBoxWhiskerMetricAggregatorInterval(@QueryParam(value = "topic") List<String> topics,
            @BeanParam final BoxAndWhisperMetricAggregationIntervalChartOption chartOptions) throws Exception {

        DataPointsEntity dataPoints = dataPointRepository
                .getDataPointsForTopic(topics.get(0), chartOptions.getStartX(), chartOptions.getEndX());
        final DataPointsSet dataPointsSet = timeIntervalDatapointsSplitter
                .splitDatapoints(dataPoints, chartOptions.getStartX(), chartOptions.getInterval());

        final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        for (int dataPointsSetIndex = 0; dataPointsSetIndex < dataPointsSet.getSize(); dataPointsSetIndex++ ) {
            DataPointsEntity dataPointsEntity = dataPointsSet.getDataPoints(dataPointsSetIndex);
            List<Double> values = new ArrayList<Double>();
            for(int dataPointIndex = 0; dataPointIndex < dataPointsEntity.size(); dataPointIndex++) {
                values.add(dataPointsEntity.getX(dataPointIndex));
            }

            String dateRange = chartOptions.getDateFormat().format(new Date((long) dataPointsEntity.getFirstX()))
            +" to " + chartOptions.getDateFormat().format(new Date((long) dataPointsEntity.getLastX()));

            dataset.add(values, dateRange, dateRange);
        }

        StreamingOutput stream = new StreamingOutput() {
             public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                 try {
                     BasicBoxAndWhiskerChart basicBoxAndWhiskerChart = new BasicBoxAndWhiskerChart(chartOptions);
                     basicBoxAndWhiskerChart.writeChart(outputStream, dataset);
                 } catch (Exception e) {
                     throw new WebApplicationException(e);
                 }
             }
         };

        return Response.ok(stream).build();
	}
}
