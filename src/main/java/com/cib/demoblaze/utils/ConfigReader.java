package com.cib.demoblaze.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads configuration values from config.properties.
 * Falls back to system properties so CI pipelines can override anything
 * without touching the file (e.g. -Dbrowser=firefox).
 */
public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("[ConfigReader] Could not load config.properties — using defaults.");
        }
    }

    /**
     * Returns the value for the given key.
     * System properties take priority over the file so the CI pipeline
     * can override values at runtime.
     */
    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return get("base.url");
    }

    public static String getUsername() {
        return get("test.username");
    }

    public static String getPassword() {
        return get("test.password");
    }

    public static String getBrowser() {
        String browser = get("browser");
        return (browser != null) ? browser : "chrome";
    }

    public static boolean isHeadless() {
        String headless = get("headless");
        return (headless != null) ? Boolean.parseBoolean(headless) : true;
    }

    public static int getImplicitWait() {
        String val = get("implicit.wait");
        return (val != null) ? Integer.parseInt(val) : 10;
    }

    public static int getExplicitWait() {
        String val = get("explicit.wait");
        return (val != null) ? Integer.parseInt(val) : 15;
    }

    public static int getPageLoadTimeout() {
        String val = get("page.load.timeout");
        return (val != null) ? Integer.parseInt(val) : 30;
    }
}
