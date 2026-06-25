package net.mineshaft;

import org.apache.logging.log4j.Level;

public class MineshaftLogger {

    public static void log(org.apache.logging.log4j.Level level, String message) {
        org.apache.logging.log4j.LogManager.getLogger().log(level, message);
    }

    public static void logInfo(String text) {
        log(org.apache.logging.log4j.Level.INFO, text);
    }

    public static void logDebug(String text) {
        log(Level.DEBUG, text);
    }

    public static void logWarning(String text) {
        log(Level.WARN, text);
    }

    public static void logTrace(String text) {
        log(Level.TRACE, text);
    }

    public static void logError(String text) {
        log(Level.FATAL, text);
    }

}
