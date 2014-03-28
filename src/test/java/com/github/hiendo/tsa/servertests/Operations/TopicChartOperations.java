
package com.github.hiendo.tsa.servertests.operations;

import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;

public class TopicChartOperations {

    private WebTarget webTarget;

    public TopicChartOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public byte[] downloadDefaultLineChart(String... topics) {
        WebTarget target = webTarget.path("/api/charts/xyline");

        for (String topic : topics) {
            target.queryParam("topic", topic);
        }

        return target.request().accept(APPLICATION_OCTET_STREAM).get(byte[].class);
    }
}
