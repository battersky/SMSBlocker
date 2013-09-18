package com.batter.smsblocker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseUtils {

    private static class NewPhoneNumberThread extends Thread {

        private CharSequence mPhoneNumber;
        public NewPhoneNumberThread(CharSequence phoneNum) {
            super("insert-new-block-phone-number");
            mPhoneNumber = phoneNum;
        }

        @Override
        public void run() {
            SQLiteDatabase database = sSmsBlockerDatabaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("adddress", mPhoneNumber.toString());
            database.insert(SmsBlockerDatabaseHelper.DATABASE_TABLE_NAME_SMS_BLOCKER_LIST, null, values);
        }
    }

    private static SmsBlockerDatabaseHelper sSmsBlockerDatabaseHelper;

    public DatabaseUtils(Context context) {
        sSmsBlockerDatabaseHelper = new SmsBlockerDatabaseHelper(context);
    }

    public static void addNewBlockPhoneNumber(CharSequence text) {
        new NewPhoneNumberThread(text).start();
    }
}
