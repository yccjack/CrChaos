package com.ycc.register.handler;

import com.google.common.util.concurrent.*;
import com.ycc.register.common.info.ServiceInfo;
import com.ycc.register.info.DataInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author MysticalYcc
 * @date 2020/6/1
 */
public class ServerInitHandler  extends ChannelInitializer<SocketChannel> {
    private Logger log = LoggerFactory.getLogger(ServerInitHandler.class);

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        ByteBuf delimiter = Unpooled.wrappedBuffer(ServiceInfo.delimiter.getBytes());
        pipeline
                .addLast("framer", new DelimiterBasedFrameDecoder(8192, delimiter))
                .addLast("decoder", new StringDecoder())
                .addLast("encoder", new StringEncoder())
//                .addLast("httpServerCodec", new HttpServerCodec())
//                .addLast("aggregator", new HttpObjectAggregator(65536))
                .addLast(new IdleStateHandler(0, 0, DataInfo.renewalPeriod, TimeUnit.SECONDS))
                .addLast(new HeartbeatHandler())
                .addLast("dataRegister",new RegisterHandler());
        log.info("ChatServerInitializer:" + socketChannel.remoteAddress() + "连接上");
    }

    public static void main(String[] args) {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<String> submit = service.submit(() -> {
            Thread.sleep(2000);
            throw new RuntimeException("异常");
           // return "返回结果";
        });

        Futures.addCallback(submit, new FutureCallback<String>() {
            @Override
            public void onSuccess(@Nullable String s) {
                System.out.println(s);
            }
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("执行失败，"+throwable.getLocalizedMessage());
            }
        },service);
    }
}
