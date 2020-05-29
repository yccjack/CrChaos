package com.ycc.register.lstener;

import com.ycc.register.info.DataInfo;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ycc.register.utils.YmlUtil.getValue;

/**
 * @author :MysticalYcc
 * @date :11:12 2019/2/20
 */
public class ServerBoundListener implements ChannelFutureListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            logger.info("服务器绑定成功,端口号：" + DataInfo.port);
            logger.info("访问地址：http://localhost:" + DataInfo.port);

        } else {
            Throwable cause = future.cause();
            if (cause == null) {
                logger.error("服务器绑定失败,原因未知");
            } else {
                cause.printStackTrace();
            }
        }
    }
}
