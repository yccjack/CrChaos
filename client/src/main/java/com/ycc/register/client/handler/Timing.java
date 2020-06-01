package com.ycc.register.client.handler;

import com.alibaba.fastjson.JSON;
import com.ycc.register.Client;
import com.ycc.register.client.utils.ClientThreadFactory;
import com.ycc.register.common.info.ServiceInfo;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author MysticalYcc
 * @date 2020/6/1
 */
public class Timing {
    private static Logger log = LoggerFactory.getLogger(Timing.class);
    /**
     * 服务续约时间
     */
    public int renewalPeriod = 30;

    public void renewal() {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1, new ClientThreadFactory());
        log.debug("服务续约时间为" + renewalPeriod + "s,开始执行定时任务");
        ses.scheduleAtFixedRate(new ClientTiming(), renewalPeriod, renewalPeriod, TimeUnit.SECONDS);
    }

    static class ClientTiming implements Runnable {

        @Override
        public void run() {
            log.debug("执行续约！");
            Client client = Client.getClient();
            Channel channel = client.getChannel();
            ServiceInfo clientInfo = client.getClientInfo();
            clientInfo.setType(1);
            channel.writeAndFlush(JSON.toJSONString(clientInfo) + Client.delimiter);
        }
    }
}
