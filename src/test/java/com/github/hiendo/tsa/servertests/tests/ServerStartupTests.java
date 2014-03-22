package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;


public class ServerStartupTests extends AbstractServerTests {

    @Test
    public void canGetHomePage() throws Exception {
        String homePage = staticFileOperations.getHomePage();
        assertThat(homePage, containsString("<!DOCTYPE html>"));
    }

    @Test
    public void canGetStaticContent() {
        String cssFile = staticFileOperations.getFile("/app.css");
        assertThat(cssFile, containsString("body {"));
    }

    @Test
    public void canConnectToSockJs() {
        String websocketMessage = staticFileOperations.getFile("/testwebsocket");
        assertThat(websocketMessage, containsString("Welcome to SockJS!"));
    }
}
