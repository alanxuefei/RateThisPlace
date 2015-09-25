package com.i2r.alan.rate_this_place.pasivedatacollection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Alan on 25/9/2015.
 */
public class WifiBroadcastReceiver extends BroadcastReceiver {
    protected static final String TAG = "activity-detection-response-receiver";
    Context from;

    public WifiBroadcastReceiver(Context context){
        from=context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("wifi", "wifistart");

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            if (isWiFi){
                Log.i("wifi", "wifi-upload-start");
                File filepath= new File(Environment.getExternalStorageDirectory()+"/"+ "RateThisPlace"+"/PassiveData");
                if (folderSize(filepath)>10000){
                    Intent mServiceIntent = new Intent(from, PassiveDataToFTPIntentService.class);
                    from.startService(mServiceIntent);
                }

            }
        }
    }

    public static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }
}
