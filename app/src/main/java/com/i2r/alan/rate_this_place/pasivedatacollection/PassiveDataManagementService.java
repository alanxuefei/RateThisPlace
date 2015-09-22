package com.i2r.alan.rate_this_place.pasivedatacollection;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;

public class PassiveDataManagementService extends Service {
    public PassiveDataManagementService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Calendar c = Calendar.getInstance();
        int HOUR_OF_DAY = c.get(Calendar.HOUR_OF_DAY);
        if ((HOUR_OF_DAY>12)&&(HOUR_OF_DAY<5)){
            Intent startServiceIntent = new Intent(this, SensorListenerService.class);
            this.startService(startServiceIntent);
        }
        else {
            Intent startServiceIntent = new Intent(this, SensorListenerService.class);
            this.stopService(startServiceIntent);
        }
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
