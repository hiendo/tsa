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
import javax.annotation.PreDestroy;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Netty server which simulate graphite's format for data import.
 */
@Component
public class GraphiteDataImporterServer {
    private final int port = 2003;
    private GraphiteDataHandler graphiteDataHandler;
    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public GraphiteDataImporterServer(GraphiteDataHandler graphiteDataHandler) {
        this.graphiteDataHandler = graphiteDataHandler;
    }

    @PostConstruct
    public void run() throws Exception {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("lineDelimiterDecoder",
                                new DelimiterBasedFrameDecoder(160, Delimiters.lineDelimiter()));
                        ch.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast("graphiteDataProcessor", graphiteDataHandler);
                    }
                });

        serverBootstrap.bind(port).sync();
    }

    @PreDestroy
    public void stop() throws Exception {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }

        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }

        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
