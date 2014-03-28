
package com.github.hiendo.tsa.servertests.operations;

import com.github.hiendo.tsa.web.entities.AggregatedStatsSet;

import javax.ws.rs.client.WebTarget;

public class MetricsAggregatorOperations {

    private WebTarget webTarget;

    public MetricsAggregatorOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public AggregatedStatsSet aggregateMetricByTimeInterval(String topic, double start, double interval) {
        return webTarget.path("/api/topics/" + topic + "/aggregator/interval").queryParam("interval", interval)
                .queryParam("start", start).request()
                .get(AggregatedStatsSet.class);
    }
}
