package com.ycc.register.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author MysticalYcc
 * @date 2020/5/28
 */
public class ServerInitHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        ByteBuf delimiter = Unpooled.copiedBuffer("$_$".getBytes());
        pipeline
                //.addLast("framer", new DelimiterBasedFrameDecoder(8192, true, true, delimiter))
                .addLast("httpServerCodec", new HttpServerCodec())
                .addLast("aggregator", new HttpObjectAggregator(65536))
                .addLast("dataDis", new RegisterHandler());
        ;
    }
}
