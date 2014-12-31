
package com.github.hiendo.tsa.netty;

import com.github.hiendo.tsa.db.DataPointRepository;
import com.github.hiendo.tsa.web.entities.DataPoint;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handler to handle request from graphite's format.
 */
@Component
@ChannelHandler.Sharable
public class GraphiteDataHandler extends SimpleChannelInboundHandler<String> {
    private static final String WHITESPACE = "[\\s]+";

    final static Logger logger = LoggerFactory.getLogger(GraphiteDataHandler.class);

    private DataPointRepository dataPointRepository;

    @Autowired
    public GraphiteDataHandler(DataPointRepository dataPointRepository) {
        this.dataPointRepository = dataPointRepository;
    }

    @Override protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String[] split = msg.split(WHITESPACE);
        if (split.length != 3) {
            throw new IllegalArgumentException("Incorrect format: " + msg);
        }
        dataPointRepository.saveDataPoint(split[0], new DataPoint(Double.valueOf(split[2]), Double.valueOf(split[1])));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("Unexpected exception in processing graphite's data.", cause);
    }
}
