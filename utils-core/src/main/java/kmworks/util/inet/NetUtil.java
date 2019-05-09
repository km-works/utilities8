package kmworks.util.inet;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public final class NetUtil {
    
    private static byte[] cachedMac = null;
    
    private NetUtil() {}
    
    /** Find any MAC address
     *  Once found the MAC address will be cached and always returned by subsequent calls.
     *  In case no MAC address can be determinded a RuntimeException will be thrown.
    */
    public static synchronized byte[] getMacAddress() throws RuntimeException {
        
        if (cachedMac != null) {
            return cachedMac;
        }

        final Enumeration<NetworkInterface> nets;
        try {
            nets = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        }

        for (NetworkInterface netIf : Collections.list(nets)) {

            try {
                if (!netIf.isUp() || netIf.isVirtual() || netIf.isLoopback()) continue;
            } catch (SocketException ex) {
                continue;
            }
            
            byte[] mac = null;

            try {
                mac = netIf.getHardwareAddress();
            } catch (SocketException ex) {}

            if (mac != null) {
                cachedMac = mac;
                return mac;
            }
        }
        
        throw new RuntimeException("Unable to determine MAC address");
    }

    public static String formatMacAddress(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        return sb.toString();
    }

}
