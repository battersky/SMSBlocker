package com.batter.smsblocker.service;

import com.batter.smsblocker.util.Constant;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            startDataService(context);
        } else {
            if (SmsBlockerDataService.getServiceState() !=
                    SmsBlockerDataService.SERVICE_STATE_STARTED &&
                    SmsBlockerDataService.getServiceState() !=
                    SmsBlockerDataService.SERVICE_STATE_STARTING) {
                startDataService(context);
                Toast.makeText(context,
                        "sms blocker is instailing, so can't block this message now",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle extras = intent.getExtras();
            if (extras == null) {
                return;
            }

            Object[] pdus = (Object[]) extras.get("pdus");
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                if (SmsBlockerDataService.isBlockedPhoneNumber(message.getOriginatingAddress())) {
                    Log.d("Batter", "try to block the received sms");
                    setResultCode(Activity.RESULT_OK);
                    abortBroadcast();
                }
            }
        }
    }

    private void startDataService(Context context) {
        Intent serviceItent = new Intent(Constant.ACTION_START_SERVICE);
        serviceItent.setClass(context, SmsBlockerDataService.class);
        context.startService(serviceItent);
    }

}
