package com.ycc.register.common.utils;

import java.net.*;
import java.util.Enumeration;

/**
 * @author MysticalYcc
 * @date 2020/6/1
 */
public class IpUtil {
    /**
     * Fond LocalHost ipv4
     *
     * @return java.lang.String
     */
    public static String getIP() {
        try {
            // 根据 hostname 找 ip
            InetAddress address = InetAddress.getLocalHost();
            if (address.isLoopbackAddress()) {
                Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                while (allNetInterfaces.hasMoreElements()) {
                    NetworkInterface netInterface = allNetInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress ip = addresses.nextElement();
                        if (!ip.isLinkLocalAddress() && !ip.isLoopbackAddress() && ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
            return address.getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find all network interfaces
     */
    public static void printAllIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();

                // Remove loopback interface, sub-interface, unrun and interface
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip != null) {

                        System.out.println("ip = " + ip.getHostAddress());
                        // ipv4
                        if (ip instanceof Inet4Address) {
                            System.out.println("ipv4 = " + ip.getHostAddress());

                            if (!ip.getHostAddress().startsWith("192") && !ip.getHostAddress().startsWith("10") && !ip.getHostAddress().startsWith("172")) {
                                // Intranet
                                ip.getHostAddress();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("[Error] can't get host ip address" + e.getMessage());
        }
    }
}
