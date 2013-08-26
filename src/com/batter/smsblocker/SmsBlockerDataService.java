package com.batter.smsblocker;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SmsBlockerDataService extends Service {

    static ArrayList<String> mArrayList = new ArrayList<String>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
	    handleCommand(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    handleCommand(intent);
	    // We want this service to continue running until it is explicitly
	    // stopped, so return sticky.
	    return START_STICKY;
	}

	private void handleCommand(Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
		}
	}

	public static boolean isBlockedPhoneNumber(String number) {
		if (mArrayList.contains(number)) {
			return true;
		}
		return false;
	}

}
