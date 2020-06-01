package com.ycc.register.common.info;

import com.alibaba.fastjson.annotation.JSONField;
import com.ycc.register.common.utils.YmlUtil;

import java.beans.Transient;

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
   private int renewalPeriod;

    /**
     * 服务状态 0：正常，1：即将下线； 2：异常待恢复；
     */
    @JSONField(serialize = false)
    private int status = 0;

    private int type=0;

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

    @Override
    public String toString() {
        return "ServiceInfo{" +
                "serviceName='" + serviceName + '\'' +
                ", addr='" + addr + '\'' +
                ", renewalPeriod=" + renewalPeriod +
                ", status=" + status +
                ", type=" + type +
                '}';
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRenewalPeriod() {
        return renewalPeriod;
    }

    public void setRenewalPeriod(int renewalPeriod) {
        this.renewalPeriod = renewalPeriod;
    }
}
