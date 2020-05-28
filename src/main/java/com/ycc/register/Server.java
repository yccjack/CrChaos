package com.ycc.register;

import com.ycc.register.handler.ServerInitHandler;
import com.ycc.register.lstener.ServerBoundListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MysticalYcc
 * @date 2020/5/28
 */
public class Server {

    int port = 8082;
    private Logger log = LoggerFactory.getLogger(Server.class);

    public void init() throws InterruptedException {
        ServerBootstrap b = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        b.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerInitHandler())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        serverStart(b);
    }

    private void serverStart(ServerBootstrap b) throws InterruptedException {
        log.info("服务启动");
        ChannelFuture f = b.bind(port).sync();
        f.addListener(new ServerBoundListener(port));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> f.channel().close()));
        f.channel().closeFuture().syncUninterruptibly();
    }
}
