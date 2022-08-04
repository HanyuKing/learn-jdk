package net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetUtil {

    public static void main(String[] args) throws Exception {
        String iPv4 = privateIPv4();
        System.out.println(iPv4);
    }

    public static String privateIPv4() throws SocketException {
        try {
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (isPrivateIPv4(inetAddr.getHostAddress())) {
                            return inetAddr.getHostAddress();
                        }
                    }
                }
            }

        } catch (SocketException e) {
            throw e;
        }
        throw new RuntimeException("no private ip address");
    }

    private static boolean isPrivateIPv4(String ip) {
        if (ip == null || "".equals(ip)) {
            return false;
        }
        String[] ips = ip.split("\\.");
        return (ips[0].equals("10")
                || ips[0].equals("172") && (Integer.valueOf(ips[1]) >= 16 && Integer.valueOf(ips[1]) < 32)
                || ips[0].equals("192") && ips[1].equals("168"));
    }
}
