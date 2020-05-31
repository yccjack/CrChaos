package com.ycc.register.info;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.ycc.register.utils.YmlUtil.getValue;

/**
 * @author MysticalYcc
 * @date 2020/5/28
 */
public class DataInfo {

    private static DataInfo dataInfo = new DataInfo();

    /**
     * {@link RegisterInfo}
     */
    private static RegisterInfo registerInfo;
    public static int port;
    /**
     * 服务续约时间
     */
    static int renewalPeriod;
    /**
     * 开启时，服务不可用不会立马删除服务，会等待一定的时间删除服务
     */
    static boolean safeguard = false;

    static {
        port = Integer.parseInt(getValue("app.yml", "server.port"));
        renewalPeriod = Integer.parseInt(getValue("app.yml", "server.renewalPeriod"));
        safeguard = getValue("app.yml", "server.safeguard") != null && Boolean.parseBoolean(getValue("app.yml", "server.safeguard"));
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
        String serviceName = serviceInfo.getServiceName();
        if (registerInfo.serviceInfoMap.containsKey(serviceName)) {
            registerInfo.serviceInfoMap.get(serviceName).add(serviceInfo);
            registerInfo.serviceAddrInfos.get(serviceName).add(serviceInfo.getAddr());
        } else {
            Set<ServiceInfo> serviceInfoSet = new HashSet<>();
            Set<String> serviceAddrSet = new HashSet<>();
            serviceInfoSet.add(serviceInfo);
            serviceAddrSet.add(serviceInfo.getAddr());
            registerInfo.serviceInfoMap.put(serviceInfo.getServiceName(), serviceInfoSet);
            registerInfo.serviceAddrInfos.put(serviceInfo.getServiceName(), serviceAddrSet);
        }

    }

    /**
     * 获取服务列表
     *
     * @return List<ServiceInfo> 服务列表
     */
    public Map<String, Set<String>> obtainServices() {
        return registerInfo.serviceAddrInfos;
    }


    /**
     * 删除离线的服务
     */
    public void removeService(ServiceInfo serviceInfo, int status) {
        if (safeguard) {
            serviceInfo.setStatus(status);
        } else {
            registerInfo.serviceInfoMap.remove(serviceInfo.getServiceName());
            registerInfo.serviceAddrInfos.remove(serviceInfo.getServiceName());
        }
    }

    /**
     * 注册的服务
     */
    private static class RegisterInfo {
        /**
         * 注册服务的具体信息
         */
        private Map<String, Set<ServiceInfo>> serviceInfoMap = new ConcurrentHashMap<>();
        /**
         * 注册服务的集群地址
         */
        private Map<String, Set<String>> serviceAddrInfos = new ConcurrentHashMap<>();
    }


    private static void init() {

    }

    private DataInfo() {

    }
}
