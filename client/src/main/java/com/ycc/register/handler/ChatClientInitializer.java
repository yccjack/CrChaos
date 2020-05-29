package com.ycc.register.handler;

import info.ServiceInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


/**
 * @author ycc
 * @version 1.0
 * @date 2019/1/22 9:27
 */
public class ChatClientInitializer extends ChannelInitializer<SocketChannel> {

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
                .addLast("handler", new ChatClientHandler());
    }
}
