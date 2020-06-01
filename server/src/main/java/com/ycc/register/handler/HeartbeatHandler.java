package com.ycc.register.handler;

import com.ycc.register.common.info.ServiceInfo;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MysticalYcc
 * @date 2020/6/1
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    private Logger log = LoggerFactory.getLogger(HeartbeatHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.writeAndFlush(ServiceInfo.delimiter).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            log.info("连接超时即将下线此服务;");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
