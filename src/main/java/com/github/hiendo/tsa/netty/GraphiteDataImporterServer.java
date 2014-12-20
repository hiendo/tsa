package com.github.hiendo.tsa.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 */
@Component
public class GraphiteDataImporterServer {
    private final int port = 2003;
    private GraphiteDataHandler graphiteDataHandler;

    @Autowired
    public GraphiteDataImporterServer(GraphiteDataHandler graphiteDataHandler) {
        this.graphiteDataHandler = graphiteDataHandler;
    }

    @PostConstruct
    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // @todo shutdown threads above

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("lineDelimiterDecoder",
                                new DelimiterBasedFrameDecoder(80, Delimiters.lineDelimiter()));
                        ch.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast("graphiteDataProcessor", graphiteDataHandler);
                    }
                });

        final ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

        Future<Void> future =
                Executors.newSingleThreadExecutor().submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        channelFuture.channel().closeFuture().sync();
                        return null;
                    }
                });
    }
}
