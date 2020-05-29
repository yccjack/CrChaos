package com.ycc.register;

import com.alibaba.fastjson.JSON;
import com.ycc.register.handler.RegisterClientInitializer;
import com.ycc.register.listener.FutureListener;
import info.ServiceInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author MysticalYcc
 * @date 2020/5/29
 */
public class client {
    private final int port = 8082;
    private final String host = "localhost";
    Channel channel;
    EventLoopGroup group;

    public void start() {
        group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new RegisterClientInitializer());
        ChannelFuture sync = null;
        try {
            sync = b.connect(host, port).sync();
            sync.addListener(new FutureListener());
            channel = sync.channel();
            register();
        } catch (InterruptedException e) {
            destroyClient();
            e.printStackTrace();
        }
    }

    /**
     * 中断与服务器的连接
     *
     * @throws Exception
     */
    public void destroyClient() {
        channel.closeFuture();
        channel.close();
        try {
            group.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册服务
     *
     * @throws Exception
     */
    public void register() {

        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceName("client1");
        serviceInfo.setAddr(host + ":" + port);
        channel.writeAndFlush(JSON.toJSONString(serviceInfo) + ServiceInfo.delimiter);
    }

    public static void main(String[] args) {
        client client = new client();
        client.start();
    }
}
