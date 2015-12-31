package com.icbms.appserver.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;


public class L {

    static int depth = 2;
    private static final DateFormat df = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.KOREA);
    static Logger logger = Logger.getLogger("ServerSide");
    static String version = "[Server]";
    static boolean debug = true;
    
    public synchronized static void i(String message) {
        if (debug) {
            depth = 2;
            final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            logger.info(version + getTrace(ste) + " " + message);
        }
    }

    public static void i(String message, String tag) {
    }

    public synchronized static void d(String message) {
        if (debug) {
            depth = 2;
            final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            logger.debug(version + getTrace(ste) + " " + message);
        }
    }
    
    public synchronized static void d2(String msg) {
        if (debug) {
            depth = 3;
            final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            logger.debug(version + getTrace(ste) + " " + msg);
            depth = 2;
        }
    }

    public static void d(String message, String tag) {
    }

    public synchronized static void e(String message) {
        if (debug) {
            depth = 2;
            final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            logger.error(version + getTrace(ste) + " " + message);
        }
    }

    public static void e(String message, String tag) {
    }

    public synchronized static void w(String message) {
        if (debug) {
            depth = 2;
            final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            logger.warn(version + getTrace(ste) + " " + message);
        }
    }

    public static void w(String message, String tag) {
    }

    public static void v(String message) {
    }

    public static void v(String message, String tag) {
    }

    public static String getTrace(StackTraceElement[] ste) {
        return "[" + getClassName(ste) + "][" + getMethodName(ste) + "][" + getLineNumber(ste) + "] ";
    }

    public static String getClassPackage(StackTraceElement[] ste) {
        return ste[depth].getClassName();
    }

    public static String getClassName(StackTraceElement[] ste) {
        String[] temp = ste[depth].getClassName().split("\\.");
        return temp[temp.length - 1];
    }

    public static String getMethodName(StackTraceElement[] ste) {
        return ste[depth].getMethodName();
    }

    public static int getLineNumber(StackTraceElement[] ste) {
        return ste[depth].getLineNumber();
    }
}
