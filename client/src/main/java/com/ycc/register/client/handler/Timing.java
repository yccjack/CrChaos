package com.ycc.register.client.handler;

import com.alibaba.fastjson.JSON;
import com.ycc.register.Client;
import com.ycc.register.client.utils.ClientThreadFactory;
import com.ycc.register.common.info.ServiceInfo;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
    private int renewalPeriod = 30;

    /**
     * 0:初始状态；1：正在执行定时任务；2：续约时间需要修改；3：注册中心不可用
     */
    public int status = 0;

    public void renewal() {
        if (status == 2) {

        }
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1, new ClientThreadFactory());
        log.debug("服务续约时间为" + renewalPeriod + "s,开始执行定时任务");
        ScheduledFuture<?> scheduledFuture = ses.scheduleAtFixedRate(new ClientTiming(), renewalPeriod, renewalPeriod, TimeUnit.SECONDS);
        if (status == 2) {
            scheduledFuture.cancel(true);
            ses.scheduleAtFixedRate(new ClientTiming(), renewalPeriod, renewalPeriod, TimeUnit.SECONDS);
            if (scheduledFuture.isCancelled()) {
                scheduledFuture = null;
            }
        }


        status = 1;
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


    public void setRenewalPeriod(int time) {
        if (time != renewalPeriod && status != 0) {
            status = 2;
            renewalPeriod = time;
        } else if (time != renewalPeriod) {
            renewalPeriod = time;
        }
    }
}
