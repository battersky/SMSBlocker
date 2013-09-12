package com.batter.smsblocker.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceItent = new Intent(intent);
            serviceItent.setClass(context, SmsBlockerDataService.class);
            context.startService(serviceItent);
        } else {
            Bundle extras = intent.getExtras();
            if (extras == null) {
                return;
            }

            Object[] pdus = (Object[]) extras.get("pdus");
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                if (SmsBlockerDataService.isBlockedPhoneNumber(message.getDisplayOriginatingAddress())) {
                    Log.d("Batter", "try to block the received sms");
                    setResultCode(Activity.RESULT_OK);
                    abortBroadcast();
                }
            }
        }
    }

}
