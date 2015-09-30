package com.i2r.alan.rate_this_place.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.i2r.alan.rate_this_place.pasivedatacollection.SensorListenerService;
import com.i2r.alan.rate_this_place.visitedplace.GeofencingService;

import java.util.Calendar;

/**
 * Created by Alan on 25/9/2015.
 */
public class Commonfunctions {

    public static void setSensingAlarm(Context context) {
        Intent startServiceIntent = new Intent(context, SensorListenerService.class);
        context.startService(startServiceIntent);
        context.startService(new Intent(context, GeofencingService.class));

      /*  AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SensorListenerService.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
       // am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 15 * 1, pi); // Millisec * Second * Minute*/


        // Calendar cal = Calendar.getInstance();

        //   Intent intent = new Intent(context, SensorListenerService.class);
        //  PendingIntent pintent = PendingIntent.getService(context, 0, intent, 0);

        //   AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
// schedule for every 30 seconds
        //  alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000*60*10, pintent);
    }
}



