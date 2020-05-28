package com.ycc.register.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author MysticalYcc
 * @date 2020/5/28
 */
public class RegisterHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
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

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        channelHandlerContext.channel().remoteAddress();

        FullHttpRequest request = fullHttpRequest;

        System.out.println("请求方法名称:" + request.method().name());

        System.out.println("uri:" + request.uri());
        ByteBuf buf = request.content();
        System.out.print(buf.toString(CharsetUtil.UTF_8));


        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().add(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

        channelHandlerContext.writeAndFlush(response);
    }
}
