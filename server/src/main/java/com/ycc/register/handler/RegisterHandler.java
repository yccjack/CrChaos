package com.ycc.register.handler;

import com.alibaba.fastjson.JSON;
import com.ycc.register.info.DataInfo;
import info.ServiceInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MysticalYcc
 * @date 2020/5/28
 */
public class RegisterHandler extends SimpleChannelInboundHandler<String> {

    DataInfo dataInfo = DataInfo.getDataInfo();

    private Logger log = LoggerFactory.getLogger(RegisterHandler.class);

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static Map<Channel, ServiceInfo> channelServiceInfoMap = new ConcurrentHashMap<>();

    /**
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    /**
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        notifyChatListRemove(channel, 1);
    }

    /**
     * 掉线或者客户端异常通知其他客户端掉线信息
     *
     * @param channel
     */
    private void notifyChatListRemove(Channel channel, int status) {
        ServiceInfo serviceInfo = channelServiceInfoMap.get(channel);
        dataInfo.removeService(serviceInfo, status);
    }

    /**
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        String addr = channel.remoteAddress().toString();
        //删除
        notifyChatListRemove(channel, 2);
        log.error("ChatServerHandler" + addr + "异常!");
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 处理连接事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.debug(channel.remoteAddress().toString() + "：连接");
        channels.add(channel);
    }

    /**
     * 处理离开事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.debug(channel.remoteAddress().toString() + "：断开");
        channels.remove(ctx.channel());
    }


    /**
     * google浏览器第一次请求会请求网站的缩略图，忽略此次请求
     */
    static String icoQuery = "favicon.ico";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        ServiceInfo serviceInfo = JSON.parseObject(msg, ServiceInfo.class);
        dataInfo.serviceRegistration(serviceInfo);
        channelServiceInfoMap.put(ctx.channel(), serviceInfo);
        Map<String, Set<String>>  serviceInfos = dataInfo.obtainServices();

        ctx.writeAndFlush((JSON.toJSONString(serviceInfos) + ServiceInfo.delimiter));
    }

//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
//        SocketAddress socketAddress = channelHandlerContext.channel().remoteAddress();
//        log.debug(socketAddress.toString());
//
//
//        String uri = fullHttpRequest.uri();
//        String name = fullHttpRequest.method().name();
//        log.debug("请求方法名称:" + name);
//
//        log.debug("uri:" + uri);
//        if (uri.contains(icoQuery)) {
//            return;
//        }
//        ByteBuf buf = fullHttpRequest.content();
//        log.debug(buf.toString(CharsetUtil.UTF_8));
//        ServiceInfo serviceInfo = JSON.parseObject(buf.toString(CharsetUtil.UTF_8), ServiceInfo.class);
//        if(socketAddress.toString().contains("0.0.0.0")){
//            serviceInfo.getAddrTree().add("localhost:");
//        }
//
//        dataInfo.serviceRegistration(serviceInfo);
//
//        List<ServiceInfo> serviceInfos = dataInfo.obtainServices();
//        ByteBuf byteBuf = Unpooled.copiedBuffer(JSON.toJSONString(serviceInfos), CharsetUtil.UTF_8);
//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
//        response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//        response.headers().add(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
//
//        channelHandlerContext.writeAndFlush(response);
//    }
}
