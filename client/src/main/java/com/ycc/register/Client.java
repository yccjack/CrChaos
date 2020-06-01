package com.ycc.register;

import com.alibaba.fastjson.JSON;
import com.ycc.register.client.handler.RegisterClientInitializer;
import com.ycc.register.client.handler.Timing;
import com.ycc.register.client.listener.FutureListener;
import com.ycc.register.common.info.ServiceInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;
import java.util.Map;

import static com.ycc.register.common.utils.IpUtil.getIP;
import static com.ycc.register.common.utils.YmlUtil.getValue;

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


    String localIp = getIP();
    private ServiceInfo clientInfo = new ServiceInfo();
    static Client client = new Client();
    Timing timing = new Timing();

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
            timing.renewal();
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

        Object port = getValue("server.port");
        Object serviceName =getValue("server.client.serviceName");
        clientInfo.setServiceName( String.valueOf(serviceName));
        clientInfo.setAddr(localIp + ":" + port);
        channel.writeAndFlush(JSON.toJSONString(clientInfo) + delimiter);
    }

    public Channel getChannel() {
        return channel;
    }

    public static Client getClient() {
        return client;
    }

    public ServiceInfo getClientInfo() {
        return clientInfo;
    }

    public static void main(String[] args) {
        client.start();
    }
}
