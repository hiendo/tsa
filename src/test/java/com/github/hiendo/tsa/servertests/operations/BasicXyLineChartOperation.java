
package com.github.hiendo.tsa.servertests.operations;

import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;

public class BasicXyLineChartOperation {

    private WebTarget webTarget;

    public BasicXyLineChartOperation(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    public byte[] downloadDefaultLineChart(String topic) {
        return webTarget.path("/api/topic/" + topic + "/xychart").request().accept(APPLICATION_OCTET_STREAM)
                .get(byte[].class);
    }
}
