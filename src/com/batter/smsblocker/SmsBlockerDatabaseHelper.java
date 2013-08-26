package com.batter.smsblocker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class SmsBlockerDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sms-blocker.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE_NAME_SMS_BLOCKER_LIST = "sms_blocker_list";
    private static final String DATABASE_TABLE_NAME_SMS_BLOCKER_HISTORY = "sms_blocker_history";
    private static final String SMS_BLOCKER_HISTORY_DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE_NAME_SMS_BLOCKER_HISTORY + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY, " + "adddress TEXT, name TEXT, time LONG);";

    private static final String SMS_BLOCKER_LIST_DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE_NAME_SMS_BLOCKER_LIST + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY, " + "adddress TEXT);";

    public SmsBlockerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SMS_BLOCKER_LIST_DATABASE_CREATE);
        db.execSQL(SMS_BLOCKER_HISTORY_DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE_NAME_SMS_BLOCKER_LIST);
        db.execSQL("drop table if exists " + DATABASE_TABLE_NAME_SMS_BLOCKER_HISTORY);
        onCreate(db);
    }

}
