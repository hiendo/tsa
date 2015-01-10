
package com.github.hiendo.tsa.servertests.operations;

import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;

/**
 * Operations to retrieve a jpg chart for list of topics.
 */
public class TopicChartOperations {

    private WebTarget webTarget;

    public TopicChartOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    /**
     * Download time-line plot chart for the specified topics.
     *
     * @param topics topics to plot
     * @return jpg chart
     */
    public byte[] downloadXYLineChart(String... topics) {
        WebTarget target = webTarget.path("/api/charts/xyline");

        for (String topic : topics) {
            target = target.queryParam("topic", topic);
        }

        return target.request().accept(APPLICATION_OCTET_STREAM).get(byte[].class);
    }

    /**
     * Download box-whisker plot chart for the specified topics.
     *
     * @param topics topics to plot
     * @return jpg chart
     */
    public byte[] downloadBoxWhiskerChart(String... topics) {
        WebTarget target = webTarget.path("/api/charts/boxwhisker");

        for (String topic : topics) {
            target = target.queryParam("topic", topic);
        }

        return target.request().accept(APPLICATION_OCTET_STREAM).get(byte[].class);
    }
}
