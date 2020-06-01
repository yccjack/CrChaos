package com.ycc.register.client.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ycc.register.client.banlance.RestRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author MysticalYcc
 * @date 2020/5/29
 */
public class RegisterClientHandler extends SimpleChannelInboundHandler<String> {

    private Logger log = LoggerFactory.getLogger(RegisterClientHandler.class);
    RestRequest restRequest = new RestRequest();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        if (StringUtil.isNullOrEmpty(msg)) {
            log.info("心跳返回");
        } else {
            Map<String, Set<String>> services = JSON.parseObject(msg, new TypeReference<Map<String, Set<String>>>() {
            });
            restRequest.setServices(services);
        }

        log.debug(msg);
    }

}
