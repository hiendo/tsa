package com.github.hiendo.tsa.servertests.operations;

import com.github.hiendo.tsa.web.entities.AggregatedStatsSet;

import javax.ws.rs.client.WebTarget;

/**
 * Operations to get aggregated stats for a topic.
 */
public class MetricsIntervalOperations {

    private WebTarget webTarget;

    public MetricsIntervalOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    /**
     * Get the aggregated stats for the topic all within the time range.
     *
     * @param topic topic to get aggregated stats
     * @param start start time
     * @param end end time
     * @return aggregated stats all in within the time range.
     */
    public AggregatedStatsSet aggregateTopicInTimeRange(String topic, long start, long end) {
        return webTarget.path("/api/topics/" + topic + "/metrics/interval").queryParam("start", start)
                .queryParam("end", end).request()
                .get(AggregatedStatsSet.class);
    }

    /**
     * Get the aggregated stats for the topic for each interval with the specified start time.
     *
     * @param topic topic to get aggregated stats
     * @param start start time
     * @param interval interval from the start time
     * @return aggregated stats for each time interval.
     */
    public AggregatedStatsSet aggregateTopicByTimeInterval(String topic, long start, long interval) {
        return webTarget.path("/api/topics/" + topic + "/metrics/interval").queryParam("interval", interval)
                .queryParam("start", start).request()
                .get(AggregatedStatsSet.class);
    }

    /**
     * Get the aggregated stats for the topic for all time.
     *
     * @param topic topic to get aggregated stats
     * @return aggregated stats for all time.
     */
    public AggregatedStatsSet aggregateTopicForAllTime(String topic) {
        return webTarget.path("/api/topics/" + topic + "/metrics/interval").request()
                .get(AggregatedStatsSet.class);
    }
}
