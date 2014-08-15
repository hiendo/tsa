
package com.github.hiendo.tsa.netty;

import com.github.hiendo.tsa.db.DataPointRepository;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.jboss.netty.channel.Channels.fireMessageReceived;

/**
 * Netty handler to save graphite data.
 */
@Component
public class GraphiteDataHandler extends SimpleChannelUpstreamHandler {
    private static final String WHITESPACE = "[\\s]+";

    final static Logger logger = LoggerFactory.getLogger(GraphiteDataHandler.class);

    private DataPointRepository dataPointRepository;

    @Autowired
    public GraphiteDataHandler(DataPointRepository dataPointRepository) {
        this.dataPointRepository = dataPointRepository;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String msg = (String) e.getMessage();
        String[] split = msg.split(WHITESPACE);
        if (split.length != 3) {
            throw new IllegalArgumentException("Incorrect format: " + msg);
        }
        dataPointRepository.saveDataPoint(split[0], new DataPoint(Double.valueOf(split[2]), Double.valueOf(split[1])));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
