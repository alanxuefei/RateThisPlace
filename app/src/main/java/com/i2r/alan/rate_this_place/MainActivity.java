package com.i2r.alan.rate_this_place;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.i2r.alan.rate_this_place.database.DBContract;
import com.i2r.alan.rate_this_place.database.DBHelper;
import com.i2r.alan.rate_this_place.feedback.FeedbackDialogFragment;
import com.i2r.alan.rate_this_place.mapview.MapsActivity;
import com.i2r.alan.rate_this_place.myrewards.AsyncTaskGetDataToMyReward;
import com.i2r.alan.rate_this_place.myrewards.MyRewardActivity;
import com.i2r.alan.rate_this_place.pasivedatacollection.PassiveDataToFTPIntentService;
import com.i2r.alan.rate_this_place.pasivedatacollection.SensorListenerService;
import com.i2r.alan.rate_this_place.ratethisplace.RateThisPlaceActivity;
import com.i2r.alan.rate_this_place.usersetting.UserAgreementDialogFragment;
import com.i2r.alan.rate_this_place.usersetting.UserProfileActivity;
import com.i2r.alan.rate_this_place.utility.Constants;
import com.i2r.alan.rate_this_place.utility.DataLogger;
import com.i2r.alan.rate_this_place.visitedplace.GeofencingService;
import com.i2r.alan.rate_this_place.visitedplace.VisitedPlacesActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Xue Fei on 19/5/2015.
 */

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener   {

    protected static final String FirstRun_TAG = "FirstRun";
    protected static final String ActionBar_TAG = "ActionBar";
    protected static final String GoogleSignIn_TAG = "GoogleSignIn";
    protected static final String Toolbar_TAG = "Toolbar";
    protected static final String GPS_Internet_Check_TAG = "GPS_Internet_Check";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        startService(new Intent(this, GeofencingService.class));

        checkNetworkandGPS();
        checkFirstRun();

        DataLogger.CheckAndCreateFolder(String.valueOf("RateThisPlace"));
        DataLogger.CheckAndCreateFolder(String.valueOf("RateThisPlace" + "/" + "PassiveData"));
        DataLogger.CheckAndCreateFolder(String.valueOf("RateThisPlace" + "/" + "ActiveData"));
        DataLogger.CheckAndCreateFolder(String.valueOf("RateThisPlace" + "/" + "PendingToSend"));


        addDB("I2ROffice");


    }


    private void addDB(String LocationName) {
                        /*my code*/

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        String currentDate = sdf.format(new Date());
        sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(new Date());
        DBHelper mDbHelper = new DBHelper(this);
        LatLng detectedlocation_LatLng = Constants.BAY_AREA_LANDMARKS.get(LocationName);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBContract.FeedEntry.COLUMN_NAME_DATE, currentDate);
        values.put(DBContract.FeedEntry.COLUMN_NAME_TIME, currentTime);
        values.put(DBContract.FeedEntry.COLUMN_LOCATION_NAME, LocationName);
        values.put(DBContract.FeedEntry.COLUMN_LOCATION_LATITUDE, detectedlocation_LatLng.latitude);
        values.put(DBContract.FeedEntry.COLUMN_LOCATION_LONGITUDE,detectedlocation_LatLng.longitude);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DBContract.FeedEntry.TABLE_NAME,
                null,
                values);
        Log.e("test1", "geo insert"+newRowId);
    }



    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) this);
        popup.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkNetworkandGPS();


       // Intent intent = new Intent(this, SensorListenerService.class);
       // startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncTaskGetDataToMyReward(this,(LinearLayout)findViewById(R.id.linearLayout_rewardbar)).execute();
       // Intent intent = new Intent(this, SensorListenerService.class);
       // startService(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Intent intent = new Intent(this, SensorListenerService.class);
       // stopService(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();

        //Intent intent = new Intent(this, SensorListenerService.class);
       // stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

      //  Intent intent = new Intent(this, SensorListenerService.class);
       // stopService(intent);
    }






    public void clickImage_activity_log(View view) {
      /*  Toast.makeText(this, "Activity_log", Toast.LENGTH_SHORT).show();*/
        //DataLogger.writeTolog("_________________________________start_a_new_test____________________________" + "\n");
        Intent intent = new Intent(this, VisitedPlacesActivity.class);
        startActivity(intent);

    }


    public void clickImage_myreward(View view) {

        Intent intent = new Intent(this, MyRewardActivity.class);
        startActivity(intent);

    }


    public void clickImage_rate_this_place(View view) {

        Intent intent = new Intent(this, RateThisPlaceActivity.class);
        intent.putExtra("From","MainActivity");
        startActivity(intent);

    }



    public void checkFirstRun() {
        boolean DoesUserAgree = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("DoesUserAgree", false);


        if (DoesUserAgree){
            // Place your dialog code here to display the dialog

            Log.i(FirstRun_TAG, "User  agree");
            ReadGoogleAccount();
            Intent intent = new Intent(this, SensorListenerService.class);
            startService(intent);
        }
        else{
            Log.i(FirstRun_TAG, "User have not agree yet");
            UserAgreementDialogFragment UserAgreement = new UserAgreementDialogFragment();;
            UserAgreement.show(getSupportFragmentManager(), "NoticeDialogFragment");
        }
    }

    public void ReadGoogleAccount() {

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        String possibleEmail = null;
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
                Log.i("GoogleAccount", possibleEmail);
            }
        }

        possibleEmail=possibleEmail+"_"+getUniqueHardwareID();

        this.getSharedPreferences("UserInfo", this.MODE_PRIVATE)
                .edit()
                .putString("UserID",possibleEmail)
                .apply();

        ((TextView)findViewById(R.id.textView_UserID)).setText("UserID: "+possibleEmail);

    }

    public String getUniqueHardwareID(){
        WifiManager wifiMan = (WifiManager) this.getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        String wifimacAddr = wifiInf.getMacAddress();
        if (wifimacAddr != null){
            return "wifi_"+wifimacAddr;
        }
        else {
            BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (myBluetoothAdapter!=null){
                String bluetoothmacAddr = myBluetoothAdapter.getAddress();
                if (bluetoothmacAddr!=null){
                    return "Blue_"+bluetoothmacAddr;
                }
                else{
                    String android_id = Secure.getString(this.getContentResolver(),
                            Secure.ANDROID_ID);
                    if (android_id!=null){
                        return "Aid_"+android_id;
                    }
                    else{
                        return "no_available_hardwareID";

                    }
                }
            }
            else{
                String android_id = Secure.getString(this.getContentResolver(),
                        Secure.ANDROID_ID);
                if (android_id!=null){
                    return "Aid_"+android_id;
                }
                else{
                    return "no_available_hardwareID";

                }
            }
        }
    }


    public void checkNetworkandGPS()
    {

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Log.i(GPS_Internet_Check_TAG, "GPS Yes");
            if (isConnectingToInternet())
            {
                ((TextView)findViewById(R.id.textView8)).setText("");
                Log.i(GPS_Internet_Check_TAG, "internet Yes");
            }
            else{
                ((TextView)findViewById(R.id.textView8)).setText("Internet is not available");

                Log.i(GPS_Internet_Check_TAG, "internet No");
            }
        }
        else{

            Log.i(GPS_Internet_Check_TAG, "GPS No");
            if (isConnectingToInternet())
            {
                ((TextView)findViewById(R.id.textView8)).setText("GPS is off");
                Log.i(GPS_Internet_Check_TAG, "internet Yes");
            }
            else{
                ((TextView)findViewById(R.id.textView8)).setText("Internet is not available and GPS is off");

                Log.i(GPS_Internet_Check_TAG, "internet No");
            }

        }


    }

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_Menu:
                if (isConnectingToInternet()){
                    startActivity(new Intent(this, MapsActivity.class));
                }
                else{
                    Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.action_manualupload:
                if (isConnectingToInternet()){
                    startService(new Intent(getBaseContext(), PassiveDataToFTPIntentService.class));
                }
                else{
                    Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.action_userprofile:
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case R.id.action_aboutus:

                break;
            case R.id.action_feedback:
                new FeedbackDialogFragment().show(getSupportFragmentManager(), "FeedbackDialog");
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}
