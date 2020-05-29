package com.ycc.register.handler;

import info.ServiceInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MysticalYcc
 * @date 2020/5/29
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    private Logger log = LoggerFactory.getLogger(HeartbeatHandler.class);
    private static final ByteBuf HEARTBEAT_SEQUENCE;

    static {
        HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(ServiceInfo.delimiter, CharsetUtil.UTF_8));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            log.info("未续约;即将终止此服务");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
