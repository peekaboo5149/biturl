package org.nerds.biturl.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class Utility {
    private Utility() {
    }

    static {
        synchronized (Utility.class) {
            if (isStringEmpty(System.getProperty("server.address"))) {
                System.setProperty("server.address", getServerAddress());
            }
        }
    }

    public static String getRedirectionUrl() {
        return getServerAddress() + Constants.FULL_REDIRECTION_ROUTE;
    }

    private static String getServerAddress() {
        String _serverAddress;
        if (isStringNonEmpty(_serverAddress = System.getProperty("server.address"))) return _serverAddress;
        String ip;
        String port = System.getProperty("server.port");
        if (isStringEmpty(port)) {
            log.warn("FOUND EMPTY PORT for property \"server.port\"");
            port = "9000";
        }

        try {
            ip = InetAddress.getLocalHost().getHostName();
            if (isStringEmpty(ip)) ip = InetAddress.getLocalHost().getHostAddress();
            else ip = "localhost";
        } catch (UnknownHostException e) {
            log.error("unknown host", e);
            ip = "localhost";
        }

        return "http://" + ip + ":" + port;
    }

    public static boolean isStringNonEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static boolean isStringEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
