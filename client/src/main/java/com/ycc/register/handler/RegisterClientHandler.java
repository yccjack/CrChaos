package com.ycc.register.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MysticalYcc
 * @date 2020/5/29
 */
public class RegisterClientHandler extends SimpleChannelInboundHandler<String> {

    private Logger log = LoggerFactory.getLogger(RegisterClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        log.debug(msg);
    }

}
