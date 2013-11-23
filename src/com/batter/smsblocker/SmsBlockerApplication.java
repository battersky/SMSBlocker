package com.batter.smsblocker;

import com.batter.smsblocker.service.SmsBlockerDataService;
import com.batter.smsblocker.util.Constant;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class SmsBlockerApplication extends Application {
	
	public void onCreate() {
		super.onCreate();
		startDataService(this);
	}

	private void startDataService(Context context) {
        Intent serviceItent = new Intent(Constant.ACTION_START_SERVICE);
        serviceItent.setClass(context, SmsBlockerDataService.class);
        context.startService(serviceItent);
    }
}
