
package com.github.hiendo.tsa.servertests.operations;

import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;

public class TopicChartOperations {

    private WebTarget webTarget;

    public TopicChartOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public byte[] downloadDefaultLineChart(String... topic) {
        return webTarget.path("/api/charts/xyline").queryParam("topic", topic).request()
                .accept(APPLICATION_OCTET_STREAM).get(byte[].class);
    }
}
