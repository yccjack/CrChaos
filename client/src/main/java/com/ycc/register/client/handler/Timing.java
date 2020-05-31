package com.ycc.register.client.handler;

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
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

            }
        },0,renewalPeriod, TimeUnit.SECONDS);
    }
}
