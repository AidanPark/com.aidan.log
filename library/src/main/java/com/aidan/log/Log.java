package com.aidan.log;

import android.content.Context;


/**
 * <p>Logger.
 * <p>메소드네임, 라인넘버, 메시지를 출력한다.
 *
 * @author Aidan
 * @version : 1.0
 */
public class Log {

    private static boolean DEBUG = false;

    public static void init(Context context, boolean debug) {
        Log.DEBUG = debug;
//        if(DEBUG) {
            //Database.open(context);
//        }
    }

    protected static String getLogMessage(LogTag TAG, String msg) {
        if (TAG != null && TAG.thread != null) {
            try {
                StackTraceElement[] stackTrace = TAG.thread.getStackTrace();
                for (StackTraceElement ste : stackTrace) {
                    String className = ste.getClassName();
                    if (className.contains(TAG.simpleName)) {
                        return ste.getMethodName() + "() [" + ste.getLineNumber() + "]  " + msg;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    public static void d(LogTag TAG, String msg) {
        insertLog(android.util.Log.DEBUG, TAG.simpleName, getLogMessage(TAG, msg));
        android.util.Log.d(TAG.simpleName, getLogMessage(TAG, msg));
    }

    public static void i(LogTag TAG, String msg) {
        insertLog(android.util.Log.INFO, TAG.simpleName, getLogMessage(TAG, msg));
        android.util.Log.i(TAG.simpleName, getLogMessage(TAG, msg));
    }

    public static void v(LogTag TAG, String msg) {
        insertLog(android.util.Log.VERBOSE, TAG.simpleName, getLogMessage(TAG, msg));
        android.util.Log.v(TAG.simpleName, getLogMessage(TAG, msg));
    }

    public static void w(LogTag TAG, String msg) {
        insertLog(android.util.Log.WARN, TAG.simpleName, getLogMessage(TAG, msg));
        android.util.Log.w(TAG.simpleName, getLogMessage(TAG, msg));
    }

    public static void e(LogTag TAG, String msg) {
        insertLog(android.util.Log.ERROR, TAG.simpleName, getLogMessage(TAG, msg));
        android.util.Log.e(TAG.simpleName, getLogMessage(TAG, msg));
    }

    public static void e(LogTag TAG, String msg, Throwable t) {
        insertLog(android.util.Log.ERROR, TAG.simpleName, getLogMessage(TAG, msg) + "\n" + t.toString());
        android.util.Log.e(TAG.simpleName, getLogMessage(TAG, msg), t);
    }

    public static void printStackTrace(Throwable t) {
        t.printStackTrace();
    }

    private static void insertLog(int level, String tag, String msg) {
//        if(DEBUG) {
            //Database.insertLog(level, tag, msg);
//        }
    }
}
