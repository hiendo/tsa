
package com.github.hiendo.tsa.servertests.operations;

import javax.ws.rs.client.WebTarget;

/**
 * Operations to get static file location against web server
 */
public class StaticFileOperations {

    private WebTarget webTarget;

    public StaticFileOperations(WebTarget webTarget) {
        this.webTarget = webTarget;
    }

    /**
     * Get file on web server.
     *
     * @param filePath file path
     * @return file content
     */
    public String getFile(String filePath) {
        return webTarget.path(filePath).request().get(String.class);
    }

    public String getHomePage() {
        return getFile("");
    }
}
