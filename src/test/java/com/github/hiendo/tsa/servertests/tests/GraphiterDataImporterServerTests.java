/*
 * Project Horizon
 * (c) 2013 VMware, Inc. All rights reserved.
 * VMware Confidential.
 */

package com.github.hiendo.tsa.servertests.tests;

import com.codahale.metrics.graphite.Graphite;
import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;

/**
 *
 */
@Test
public class GraphiterDataImporterServerTests extends AbstractServerTests {

    @Test
    public void canSendGraphiteData() throws Exception {
        String topic = "topic-" + UUID.randomUUID();

        graphite.send(topic, "44444", 44);
        graphite.send(topic, "33333", 33);
        graphite.send(topic, "55555", 55);

        DataPoints dataPoints = topicDataPointOperations.getDataForTopic(topic);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), equalTo(3));
        assertThat("Data points time", dataPoints.getY(0), closeTo(33, .01));
        assertThat("Data points time", dataPoints.getY(1), closeTo(44, .01));
        assertThat("Data points time", dataPoints.getY(2), closeTo(55, .01));
        assertThat("Data points value", dataPoints.getX(0), closeTo(33333, .01));
        assertThat("Data points value", dataPoints.getX(1), closeTo(44444, .01));
        assertThat("Data points value", dataPoints.getX(2), closeTo(55555, .01));
    }
}
