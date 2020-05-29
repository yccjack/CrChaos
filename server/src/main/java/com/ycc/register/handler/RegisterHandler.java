package com.ycc.register.handler;

import com.alibaba.fastjson.JSON;
import com.ycc.register.info.DataInfo;
import info.ServiceInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.List;
import java.util.Set;

/**
 * @author MysticalYcc
 * @date 2020/5/28
 */
public class RegisterHandler extends SimpleChannelInboundHandler<String> {

    DataInfo dataInfo = DataInfo.getDataInfo();

    private Logger log = LoggerFactory.getLogger(RegisterHandler.class);

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

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
        notifyChatListRemove(channel);
    }

    /**
     * 掉线或者客户端异常通知其他客户端掉线信息
     *
     * @param channel
     */
    private void notifyChatListRemove(Channel channel) {

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
        notifyChatListRemove(channel);
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
        channels.add(ctx.channel());
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

        channels.remove(ctx.channel());
    }

    HttpRequest request;

    //    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject msg) throws Exception {
//        if (msg instanceof HttpRequest) {
//            request = (HttpRequest) msg;
//            request.method();
//            String uri = request.uri();
//            System.out.println("Uri:" + uri);
//        }
//        if (msg instanceof HttpContent) {
//
//            HttpContent content = (HttpContent) msg;
//            ByteBuf buf = content.content();
//            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
//
//            ByteBuf byteBuf = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);
//            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
//            response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//            response.headers().add(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
//
//            channelHandlerContext.writeAndFlush(response);
//
//        }
//    }
    /**
     * google浏览器第一次请求会请求网站的缩略图，忽略此次请求
     */
    static String icoQuery = "favicon.ico";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        ServiceInfo serviceInfo = JSON.parseObject(msg, ServiceInfo.class);
        dataInfo.put(serviceInfo);
        Set<ServiceInfo> serviceInfos = dataInfo.obtainServices();

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