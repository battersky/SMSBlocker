package com.batter.smsblocker.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;

public class SmsBlockerDataService extends IntentService {

    public SmsBlockerDataService() {
        super("sms-blocker-service");
    }

    static ArrayList<String> mArrayList = new ArrayList<String>();
    static boolean mStarted = false;

    public static boolean isStarted() {
        return mStarted;
    }

    public void onDestroy() {
        super.onDestroy();
        mStarted = false;
    }

    public static boolean isBlockedPhoneNumber(String number) {
        if (mArrayList.contains(number)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
        }
    }
}
