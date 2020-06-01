package com.ycc.register.client.handler;

import com.alibaba.fastjson.JSON;
import com.ycc.register.Client;
import com.ycc.register.client.utils.ClientThreadFactory;
import com.ycc.register.common.info.ServiceInfo;
import io.netty.channel.Channel;

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
    /**
     * 服务续约时间
     */
    protected int renewalPeriod = 30;

    public void renewal() {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1,new ClientThreadFactory());
        ses.scheduleAtFixedRate(new ClientTiming(), renewalPeriod, renewalPeriod, TimeUnit.SECONDS);
    }

   static class ClientTiming implements Runnable{

       @Override
       public void run() {
           Client client = Client.getClient();
           Channel channel = client.getChannel();
           ServiceInfo clientInfo = client.getClientInfo();
           clientInfo.setType(1);
           channel.writeAndFlush(JSON.toJSONString(clientInfo)+Client.delimiter);
       }
   }
}
