package com.ycc.register.handler;

import com.alibaba.fastjson.JSON;
import com.ycc.register.info.DataInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    private Logger log = LoggerFactory.getLogger(HeartbeatHandler.class);
    private static final ByteBuf HEARTBEAT_SEQUENCE;

    static {
        DataInfo dataInfo = DataInfo.getDataInfo();
        HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(JSON.toJSONString(dataInfo) + "\n", CharsetUtil.UTF_8));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            log.info("连接超时未发送任何内容;");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
