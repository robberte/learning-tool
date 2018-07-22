package com.robberte.learning.tool.util;

import io.netty.channel.Channel;

import java.net.*;
import java.util.Enumeration;

/**
 * @author robberte
 * @date 2018/7/22 下午10:08
 */
public class IpUtil {

    public static final String DEFAULT_LOCAL_IP = "127.0.0.1";

    public static String getRemoteIpAddress(Channel channel) {
        if(channel == null) {
            return null;
        }
        return getIp(channel.remoteAddress()) + ":" + getPort(channel.remoteAddress());
    }

    public static String getLocalIpAddress(Channel channel) {
        if(channel == null) {
            return null;
        }
        return getIp(channel.localAddress()) + ":" + getPort(channel.localAddress());
    }

    public static String getIp(SocketAddress address) {
        String fullAddress = ((InetSocketAddress) address).getAddress().getHostAddress();
        if(fullAddress.matches("/[0-9]+[.][0-9]+[.][0-9]+[.][0-9]+[:][0-9]+")) {
            fullAddress = fullAddress.substring(1);
            fullAddress = fullAddress.substring(0, fullAddress.indexOf(":"));
        } else if(fullAddress.matches(".*[%].*")) {
            fullAddress = fullAddress.substring(0, fullAddress.indexOf("%"));
        }
        return fullAddress;
    }

    public static Integer getPort(SocketAddress address) {
        return ((InetSocketAddress) address).getPort();
    }

    /**
     * 获取本地IP
     * @return
     */
    public static String getLocalIp() {
        Enumeration allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while(allNetInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = networkInterface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if(ip != null && ip instanceof Inet4Address && !DEFAULT_LOCAL_IP.equals(ip.getHostAddress())) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            // 忽略异常
        }
        return DEFAULT_LOCAL_IP;
    }
}
