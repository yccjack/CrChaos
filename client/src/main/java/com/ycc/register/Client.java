package com.ycc.register;

import com.alibaba.fastjson.JSON;
import com.ycc.register.client.handler.RegisterClientInitializer;
import com.ycc.register.client.listener.FutureListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MysticalYcc
 * @date 2020/5/29
 */
public class Client {
    private final int port = 8082;
    private final String host = "localhost";
    Channel channel;
    EventLoopGroup group;
    public static String delimiter = "&_&";


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
        Map<String,String> map = new HashMap<>();
        map.put("serviceName","client1");
        map.put("addr","localhost:8088");
        channel.writeAndFlush(JSON.toJSONString(map) + delimiter);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
