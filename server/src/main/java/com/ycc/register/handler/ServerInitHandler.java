package com.ycc.register.handler;

import com.ycc.register.info.ServiceInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                .addLast(new IdleStateHandler(0, 0, 30, TimeUnit.SECONDS))
                .addLast(new HeartbeatHandler())
                .addLast("dataRegister",new RegisterHandler());
        log.info("ChatServerInitializer:" + socketChannel.remoteAddress() + "连接上");
    }
}
