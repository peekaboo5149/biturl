package org.nerds.biturl.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

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

    public static void loadProperties() {
        log.info("Loading variables");
        try {
            ClassLoader loader = Utility.class.getClassLoader();
            try (InputStream in = loader.getResourceAsStream("server.properties")) {
                if (in != null) {
                    Properties properties = new Properties();
                    properties.load(in);
                    log.info("Loading all properties into system variables ...");
                    if (!properties.isEmpty()) {
                        System.setProperties(properties);
                        log.info("All server properties loaded successfully");
                    } else log.warn("Server properties are empty");
                } else log.warn("Could not find any system variables");
            }
        } catch (Exception e) {
            log.error("Could not load any file", e);
            throw new RuntimeException(e);
        }
    }

    public static void clearProperties() {
        try {
            ClassLoader loader = Utility.class.getClassLoader();
            try (InputStream in = loader.getResourceAsStream("server.properties")) {
                if (in != null) {
                    Properties properties = new Properties();
                    properties.load(in);
                    var iterator = properties.keys().asIterator();
                    while (iterator.hasNext()) {
                        var key = (String) iterator.next();
                        System.clearProperty(key);
                    }
                }
            }
            log.info("All keys cleared successfully");
        } catch (Exception e) {
            log.error("Could not clear all properties on server stop", e);
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
