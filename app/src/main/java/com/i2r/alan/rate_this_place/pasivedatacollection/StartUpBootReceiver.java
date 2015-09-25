package com.i2r.alan.rate_this_place.pasivedatacollection;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Xue Fei on 3/6/2015.
 */
public class StartUpBootReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            Intent startServiceIntent = new Intent(context, SensorListenerService.class);
            context.startService(startServiceIntent);

        }
    }

}