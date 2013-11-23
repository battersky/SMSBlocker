package com.batter.smsblocker.service;

import com.batter.smsblocker.database.DatabaseUtils;
import com.batter.smsblocker.database.SmsBlockerDatabaseHelper;
import com.batter.smsblocker.util.Constant;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

public class SmsBlockerDataService extends IntentService {

    public SmsBlockerDataService() {
        super("sms-blocker-service");
    }

    static ArrayList<String> mArrayList = new ArrayList<String>();

    public final static int SERVICE_STATE_STARTING = 0;
    public final static int SERVICE_STATE_STARTED = 1;
    public final static int SERVICE_STATE_SHUTTING_DOWN = 2;
    public final static int SERVICE_STATE_CLOSED = 3;

    private static int sState = SERVICE_STATE_CLOSED;

    public static int getServiceState() {
        return sState;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public static boolean isBlockedPhoneNumber(String number) {
        if (mArrayList.contains(PhoneNumberUtils.toCallerIDMinMatch(number))) {
            return true;
        }
        return false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sState = SERVICE_STATE_STARTING;
        if (intent != null) {
            if (intent.getAction().equals(Constant.ACTION_START_SERVICE)) {
                //To-do should use DatabaseUtils as singleton
                DatabaseUtils dataUtils = new DatabaseUtils(this);
                SmsBlockerDatabaseHelper databaseHelper = DatabaseUtils.getDatabaseHelper();
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                Cursor cursor = database.query(SmsBlockerDatabaseHelper.DATABASE_TABLE_NAME_SMS_BLOCKER_LIST,
                        new String[] {"_id", "adddress"}, null, null, null, null, null);
                if (cursor != null) {
                    int addressIndex = cursor.getColumnIndex("adddress");
                    if (addressIndex != -1) {
                        while(cursor.moveToNext()) {
                            Log.d("Batter", "get string number from db: " + cursor.getString(addressIndex));
                            mArrayList.add(PhoneNumberUtils.toCallerIDMinMatch(cursor.getString(addressIndex)));
                        }
                    }
                }
            }
        }
        sState = SERVICE_STATE_STARTED;
    }
}
