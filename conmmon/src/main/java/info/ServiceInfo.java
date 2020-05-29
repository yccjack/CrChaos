package info;

import java.util.Set;

/**
 * @author MysticalYcc
 * @date 2020/5/29
 */
public class ServiceInfo {

    /**
     * 分隔符
     */
    public static String delimiter = "&_&";

    /**
     * 服务名称
     */
    private String serviceName;


    private String addr;
    /**
     * 服务续约时间
     */
    int renewalPeriod;

    /**
     * 服务状态 0：正常，1：即将下线； 2：异常待恢复；
     */
    private int status = 0;


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof ServiceInfo) {
            ServiceInfo serviceInfo = (ServiceInfo) obj;
            int count = 0;
            if (serviceInfo.getServiceName() != null && serviceInfo.getServiceName().equals(this.getServiceName())) {
                count++;
            }
            if (serviceInfo.getAddr() != null && serviceInfo.getAddr().equals(this.getAddr())) {
                count++;
            }
            return count == 2;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = serviceName != null ? serviceName.hashCode() : 0;
        result = 31 * result;
        result = 31 * result + (addr != null ? addr.hashCode() : 0);

        return result;

    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
