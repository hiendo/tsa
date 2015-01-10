
package com.github.hiendo.tsa.netty;

import com.datastax.driver.core.ResultSetFuture;
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
        ResultSetFuture resultSetFuture =
                dataPointRepository.saveDataPointAsync(split[0], Long.valueOf(split[2]), Double.valueOf(split[1]));
        // @todo: what should we do about this failure?
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
