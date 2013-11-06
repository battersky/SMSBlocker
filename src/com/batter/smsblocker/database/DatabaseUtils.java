package com.batter.smsblocker.database;

import com.batter.smsblocker.util.Contant;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

public class DatabaseUtils {

    private static class DeletePhoneNumberThread extends Thread {

        private long[] mIds;
        private Handler mhandler;
        public DeletePhoneNumberThread(Handler handler, long[] ids) {
            super("delete-block-phone-number");
            mIds = ids;
            mhandler = handler;
        }

        @Override
        public void run() {
            SQLiteDatabase database = sSmsBlockerDatabaseHelper.getWritableDatabase();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < mIds.length; i ++) {
                if (i >= 1) {
                    stringBuilder.append(" OR ");
                }
                stringBuilder.append("_id = " + mIds[i]);
            }

            database.delete(SmsBlockerDatabaseHelper.DATABASE_TABLE_NAME_SMS_BLOCKER_LIST,
                    stringBuilder.toString(), null);
            if (mhandler != null) {
                Message messge = mhandler.obtainMessage(Contant.MSG_DATABASE_CONTENT_CHANGE);
                mhandler.sendMessage(messge);
            }
        }
    }

    private static class NewPhoneNumberThread extends Thread {

        private CharSequence mPhoneNumber;
        private Handler mhandler;
        public NewPhoneNumberThread(Handler handler, CharSequence phoneNum) {
            super("insert-new-block-phone-number");
            mPhoneNumber = phoneNum;
            mhandler = handler;
        }

        @Override
        public void run() {
            SQLiteDatabase database = sSmsBlockerDatabaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("adddress", mPhoneNumber.toString());
            database.insert(SmsBlockerDatabaseHelper.DATABASE_TABLE_NAME_SMS_BLOCKER_LIST, null, values);
            if (mhandler != null) {
                Message messge = mhandler.obtainMessage(Contant.MSG_DATABASE_CONTENT_CHANGE);
                mhandler.sendMessage(messge);
            }
        }
    }

    private static SmsBlockerDatabaseHelper sSmsBlockerDatabaseHelper;

    public DatabaseUtils(Context context) {
        sSmsBlockerDatabaseHelper = new SmsBlockerDatabaseHelper(context);
    }

    public static void addNewBlockPhoneNumber(CharSequence text, Handler handler) {
        new NewPhoneNumberThread(handler, text).start();
    }

    public static void deleteBlockPhoneNumber(long[] ids, Handler handler) {
        new DeletePhoneNumberThread(handler, ids).start();
    }
}
