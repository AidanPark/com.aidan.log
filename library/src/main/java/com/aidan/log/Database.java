package com.aidan.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static String TAG = "log.Database";

    private static final int DATABASE_VERSION = 1;

    private static SQLiteDatabase db;
    private static AVDDBOpenHelper dbHelper;

    //------------------------------------------------------------------------------------------------
    // 공통
    //------------------------------------------------------------------------------------------------
    private static final String KEY_ID = "_id";
    private static final String KEY_KEY = "key";
    private static final String KEY_VALUE = "value";

    //------------------------------------------------------------------------------------------------
    // Log
    //------------------------------------------------------------------------------------------------
    private static final String TABLE_LOG = "log";

    private static final String KEY_LOG_LEVEL = "level";
    private static final String KEY_LOG_TIME = "time";
    private static final String KEY_LOG_TAG = "tag";
    private static final String KEY_LOG_TEXT = "text";

    private static final String CREATE_TABLE_LOG = "create table " + TABLE_LOG + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_LOG_LEVEL + " integer, " + KEY_LOG_TIME + " integer, " + KEY_LOG_TAG + " text, " + KEY_LOG_TEXT + " text " + ");";

    static long insertLog(int level, String tag, String msg) {
        try {
            if (db != null && db.isOpen()) {
                ContentValues cv = new ContentValues();
                cv.put(KEY_LOG_LEVEL, level);
                cv.put(KEY_LOG_TIME, System.currentTimeMillis());
                cv.put(KEY_LOG_TAG, tag);
                cv.put(KEY_LOG_TEXT, msg);
                return db.insert(TABLE_LOG, null, cv);
            }
        } catch (Throwable t) {
            //Log.printStackTrace(t);
        }
        return -1;
    }

    public static List<LogBean> getLogs() {
        try {
            if (db == null || !db.isOpen()) {
                return null;
            }

            List<LogBean> returnThis = new ArrayList<>();

            Cursor c = db.query(TABLE_LOG, new String[]{KEY_LOG_LEVEL, KEY_LOG_TIME, KEY_LOG_TAG, KEY_LOG_TEXT}, null, null, null, null, KEY_LOG_TIME + " ASC");
            while (c.moveToNext()) {
                LogBean lBean = new LogBean();
                lBean.level = c.getInt(0);
                lBean.time = c.getLong(1);
                lBean.tag = c.getString(2);
                lBean.text = c.getString(3);
                returnThis.add(lBean);
            }
            return returnThis;
        } finally {
        }
    }

    /**
     * Open the database
     */
    static void open(Context context) throws SQLiteException {
        if (db == null || !db.isOpen()) {
            String DATABASE_NAME = context.getPackageName() + ".log";
            dbHelper = new AVDDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

            try {
                db = dbHelper.getReadableDatabase();
            } catch (SQLiteException ex) {
                db = dbHelper.getWritableDatabase();
            }
        }
    }

    /**
     * Close the database
     */
    static synchronized void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    private static class AVDDBOpenHelper extends SQLiteOpenHelper { 

        public AVDDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            android.util.Log.w(TAG, "------- onCreate ------------");
            _db.execSQL(CREATE_TABLE_LOG);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
            android.util.Log.w(TAG, "------- onUpgrade ------------  " + _oldVersion + "   " + _newVersion);
        }

    }

}
