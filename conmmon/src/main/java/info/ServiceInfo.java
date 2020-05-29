package info;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

/**
 * @author MysticalYcc
 * @date 2020/5/29
 */
public class ServiceInfo {

    public static String delimiter = "&_&";

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务集群地址
     */
    private List<String> addrTree;

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
            return serviceInfo.getAddr() != null && serviceInfo.getAddr().equals(this.getAddr());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = serviceName != null ? serviceName.hashCode() : 0;
        result = 31 * result + (addr != null ? addr.hashCode() : 0);
        return result;

    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<String> getAddrTree() {
        return addrTree;
    }

    public void setAddrTree(List<String> addrTree) {
        this.addrTree = addrTree;
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
