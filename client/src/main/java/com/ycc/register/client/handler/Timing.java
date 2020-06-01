package com.ycc.register.client.handler;

import com.ycc.register.Client;
import io.netty.channel.Channel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @date
 */
public class Timing {
    /**
     * 服务续约时间
     */
    protected int renewalPeriod = 30;

    public void renewal(){
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(() -> {
            Channel channel = Client.getClient().getChannel();
            channel.writeAndFlush(Client.delimiter);
        },0,renewalPeriod, TimeUnit.SECONDS);
    }
}
