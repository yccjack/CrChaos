package com.ycc.register.info;

import info.ServiceInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ycc.register.utils.YmlUtil.getValue;

/**
 * @author MysticalYcc
 * @date 2020/5/28
 */
public class DataInfo {

    private static DataInfo dataInfo = new DataInfo();

    private static RegisterInfo registerInfo;
    public static int port;

    static {
        port = Integer.parseInt(String.valueOf(getValue("app.yml", "server.port")));
        registerInfo = new RegisterInfo();
    }

    public static DataInfo getDataInfo() {
        init();
        return dataInfo;
    }


    /**
     * 注册服务
     *
     * @param serviceInfo {@link ServiceInfo}
     */
    public void serviceRegistration(ServiceInfo serviceInfo) {
        registerInfo.serviceInfo.add(serviceInfo);
    }

    /**
     * 获取服务列表
     *
     * @return List<ServiceInfo> 服务列表
     */
    public Set<ServiceInfo> obtainServices() {
        return registerInfo.serviceInfo;
    }

    public void put(ServiceInfo serviceInfo) {
        Set<ServiceInfo> serviceInfos = dataInfo.obtainServices();
        serviceInfos.add(serviceInfo);
    }

    /**
     * 注册的服务
     */
    private static class RegisterInfo {
        private Set<ServiceInfo> serviceInfo = new HashSet<>();
    }


    private static void init() {

    }

    private DataInfo() {

    }
}
