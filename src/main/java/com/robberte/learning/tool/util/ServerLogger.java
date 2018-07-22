package com.robberte.learning.tool.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 服务端日志服务
 * @author robberte
 * @date 2018/7/21 下午2:57
 */
public class ServerLogger {

    private static final Logger logger = LoggerFactory.getLogger(ServerLogger.class);

    private static final Logger EVENT_LOG = LoggerFactory.getLogger("EVENT");

    private static ScheduledThreadPoolExecutor writer = new ScheduledThreadPoolExecutor(5);

    private static ThreadLocal<Long> currUid = new ThreadLocal<Long>();

    public static Map<Long, Boolean> colorUids = new ConcurrentHashMap<Long, Boolean>();

    private static Map<String, Boolean> eventEnabled = new ConcurrentHashMap<String, Boolean>();

    private static Boolean isDebugEnable = null;

    public static void setCurrUid(Long uid) {
        if(uid != null) {
            currUid.set(uid);
        } else {
            currUid.set(0L);
        }
    }


    public static boolean isDebugEnable() {
        if(isDebugEnable == null) {
            return logger.isDebugEnabled();
        }
        return isDebugEnable;
    }

    public static boolean isColor() {
        if(currUid.get() != null && currUid.get() != 0L) {
            return isEventEnable("colorLog") && colorUids.containsKey(currUid.get());
        }
        return false;
    }

    public static boolean isEventEnable(String logger) {
        if(!eventEnabled.containsKey(logger)) {
            // TODO 判断是否需要记录事件日志
            eventEnabled.put(logger, true);
        }
        return eventEnabled.get(logger);
    }

    public static String EVENT = "event";
    public static String DEBUG = "debug";
    public static String INFO = "info";
    public static String WARN = "warn";
    public static String ERROR = "error";


    /**
     * Log with  INFO EVEN level, and specify a custom logger.
     * @param logger
     * @param message
     * @param args
     */
    public static void event(String logger, String message, Object... args) {
        submitWriteLog(EVENT, null, logger, message, args);
        if(isColor()) {
            submitWriteLog(EVENT, null, "colorLog", message, args);
        }
    }


    /**
     * Log with DEBUG level
     * @param e
     * @param message
     * @param args
     */
    public static void debug(Throwable e, String message, Object... args) {
        if(isDebugEnable()) {
            submitWriteLog(DEBUG, e, null, message, args);
        }
        if(isColor()) {
            submitWriteLog(EVENT, null, "colorLog", message, args);
        }
    }

    /**
     * Log with DEBUG level
     * @param message
     * @param args
     */
    public static void debug(String message, Object... args) {
        if(isDebugEnable()) {
            submitWriteLog(DEBUG, null, null, message, args);
        }
        if(isColor()) {
            submitWriteLog(EVENT, null, "coloLog", message, args);
        }
    }



    /**
     * Log with INFO level
     * @param e
     * @param message
     * @param args
     */
    public static void info(Throwable e, String message, Object... args) {
        submitWriteLog(INFO, e, null, message, args);
        if(isColor()) {
            submitWriteLog(EVENT, null, "colorLog", message, args);
        }
    }

    /**
     * Log with INFO level
     * @param message
     * @param args
     */
    public static void info(String message, Object... args) {
        submitWriteLog(INFO, null, null, message, args);
        if(isColor()) {
            submitWriteLog(EVENT, null, "coloLog", message, args);
        }
    }


    /**
     * Log with WARN level
     * @param e
     * @param message
     * @param args
     */
    public static void warn(Throwable e, String message, Object... args) {
        submitWriteLog(WARN, e, null, message, args);
        if(isColor()) {
            submitWriteLog(EVENT, null, "colorLog", message, args);
        }
    }

    /**
     * Log with WARN level
     * @param message
     * @param args
     */
    public static void warn(String message, Object... args) {
        submitWriteLog(WARN, null, null, message, args);
        if(isColor()) {
            submitWriteLog(EVENT, null, "coloLog", message, args);
        }
    }



    /**
     * Log with ERROR level
     * @param e
     * @param message
     * @param args
     */
    public static void error(Throwable e, String message, Object... args) {
        submitWriteLog(ERROR, e, null, message, args);
        if(isColor()) {
            submitWriteLog(EVENT, null, "colorLog", message, args);
        }
    }

    /**
     * Log with ERROR level
     * @param message
     * @param args
     */
    public static void error(String message, Object... args) {
        submitWriteLog(ERROR, null, null, message, args);
        if(isColor()) {
            submitWriteLog(EVENT, null, "coloLog", message, args);
        }
    }

    private  static void submitWriteLog(String level, Throwable t, String appender, String message, Object... args) {
        try {
            if (EVENT.equals(level) && ("messageTrace".equals(appender)
                        || "tcpAccessLog".equals(appender)
                        || "bizStatLog".equals(appender)
                        || "clientWaLog".equals(appender))) {
                asyncWriteLog(level, t, appender, message, args);
            } else if(INFO.equals(level)){
                asyncWriteLog(level, t, appender, message, args);
            } else {
                writerLog(level, t, appender, message, args);
            }
        } catch(Exception e) {
            logger.error("submit Write Log: msg - {}", message, e);
        }
    }

    private static void asyncWriteLog(String level, Throwable t, String appender, String message, Object... args) {
        WriteLogTask task = new WriteLogTask(level, t, appender, message, args);
        writer.submit(task);
    }

    private static void writerLog(String level, Throwable t, String appender, String message, Object... args) {
        if(EVENT.equals(level)) {
            EVENT_LOG.info(message, args);
        } else if (DEBUG.equals(level)) {
            logger.debug(message, args);
        } else if (INFO.equals(level)) {
            logger.info(message, args);
        } else if (WARN.equals(level)) {
            logger.warn(message, args);
        } else if (ERROR.equals(level)) {
            logger.error(message, args);
        } else {
            logger.error("unknow log level : {}", level);
        }
    }

    /**
     * 异步写日志任务
     */
    static class WriteLogTask implements Runnable {
        private String level;
        private Throwable t;
        private String appender;
        private String message;
        private Object[] args;

        public WriteLogTask(String level, Throwable t, String appender, String message, Object... args) {
            this.level = level;
            this.t = t;
            this.appender = appender;
            this.message = message;
            this.args = args;
        }

        @Override
        public void run() {
            writerLog(level, t, appender, message, args);
        }
    }
}
