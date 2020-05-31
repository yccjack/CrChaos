package com.ycc.register.client.handler;

import com.ycc.register.Client;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;



/**
 * @author MysticalYcc
 * @date 2020/5/29
 */
public class RegisterClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        ByteBuf delimiter = Unpooled.wrappedBuffer(Client.delimiter.getBytes());
        pipeline
                .addLast("framer", new DelimiterBasedFrameDecoder(8192, delimiter))
                .addLast("decoder", new StringDecoder())
                .addLast("encoder", new StringEncoder())
//                .addLast("httpServerCodec", new HttpServerCodec())
//                .addLast("aggregator", new HttpObjectAggregator(65536))
                .addLast("handler", new RegisterClientHandler());
    }
}
