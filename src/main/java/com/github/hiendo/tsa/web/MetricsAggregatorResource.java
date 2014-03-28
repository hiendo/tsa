package com.github.hiendo.tsa.web;

import com.github.hiendo.tsa.chart.DataPointsSet;
import com.github.hiendo.tsa.chart.TimeIntervalDatapointsSplitter;
import com.github.hiendo.tsa.db.DataPointRepository;
import com.github.hiendo.tsa.db.DataPointsEntity;
import com.github.hiendo.tsa.web.entities.AggregatedStats;
import com.github.hiendo.tsa.web.entities.AggregatedStatsSet;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Resource to add a data point against a topic
 */
@Path("topics/{topic}/aggregator")
@Component
public class MetricsAggregatorResource {
    final static Logger logger = LoggerFactory.getLogger(MetricsAggregatorResource.class);

    @Autowired
    private TimeIntervalDatapointsSplitter timeIntervalDatapointsSplitter;

    @Autowired
    private DataPointRepository dataPointRepository;

    /**
     * Get a set of aggregate stats where each item in the set is for the specified time window and interval;
     *
     * @param topic topic to get stats for
     * @param interval interval to aggregate the stats for
     * @param startX start position of the x value
     * @param endX end position of the x value
     * @return set of aggregate stats
     */
    @GET
    @Path("interval")
    @Produces(MediaType.APPLICATION_JSON)
    public AggregatedStatsSet getAggregatedStatsSetByInterval(@PathParam("topic") String topic,
            @QueryParam("start") Double startX, @QueryParam("end") Double endX,
            @QueryParam("interval") Double interval) {
        DataPointsEntity allDatapoints = dataPointRepository.getDataPointsForTopic(topic, startX, endX);
        DataPointsSet dataPointsSet =
                timeIntervalDatapointsSplitter.splitDatapoints(allDatapoints, startX, interval);

        List<AggregatedStats> aggregatedStatsList = new ArrayList<>();
        for (int i = 0; i < dataPointsSet.getSize(); i++) {
            DataPointsEntity dataPoints = dataPointsSet.getDataPoints(i);

            DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics(dataPoints.getYValues());
            aggregatedStatsList
                    .add(new AggregatedStats(dataPoints.getFirstX(), dataPoints.getLastX(), dataPoints.size(),
                            descriptiveStatistics.getMean(), descriptiveStatistics.getMean(),
                            descriptiveStatistics.getMin(), descriptiveStatistics.getMax()));
        }

        return new AggregatedStatsSet(aggregatedStatsList);
    }
}
