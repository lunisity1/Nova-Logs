package dev.lunisity.novalogs.util;

import java.util.logging.Logger;

public class LogUtils {

    public static Logger logger = Logger.getLogger("NovaLogs");

    public static void info(final String args) {
        logger.info(args);
    }

    public static void warning(final String args) {
        logger.warning(args);
    }

    public static void severe(final String args) {
        logger.severe(args);
    }

}
