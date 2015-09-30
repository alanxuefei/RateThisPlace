package com.i2r.alan.rate_this_place.visitedplace;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.i2r.alan.rate_this_place.ratethisplace.AsyncTaskUploadActivity;
import com.i2r.alan.rate_this_place.utility.Constants;
import com.i2r.alan.rate_this_place.utility.globalvariable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class RateThisPlaceActivityFromVisitedPlaceActivity extends AppCompatActivity  {

    private AutoCompleteTextView actv;
    String[] languages={"This place is not clean","This is a most crowded place on Earth","IOS","SQL","JDBC","Web services"};
    /*google activity detection*/
    protected GoogleApiClient mGoogleApiClient;
    public AddressResultReceiver mResultReceiver = new AddressResultReceiver(this);
    String mAddressOutput;
    Location mLastLocation= new Location("");

    private enum AloneGroupSet {NA, Alone,Group};
    private AloneGroupSet  AloneGroup = AloneGroupSet.NA;



    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Top Rated", "Games", "Movies" };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.i2r.alan.rate_this_place.R.layout.activity_activity);



        final EditText mEdit_Activity_Others= (EditText) findViewById(com.i2r.alan.rate_this_place.R.id.Edit_Activity_Others);

        mEdit_Activity_Others.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox10)).setChecked(true);

            }

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                final Button mbutton_editdone = (Button) findViewById(com.i2r.alan.rate_this_place.R.id.button_editdone);
                mbutton_editdone.setVisibility(View.VISIBLE);
                mbutton_editdone.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View view) {


                        mbutton_editdone.setVisibility(View.GONE);
                        //release the focus
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                });

            }


        });

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
       // ((ImageView) findViewById(R.id.imageView_picture)).setImageBitmap(null);
        //  Intent intent = new Intent(this, SensorListenerService.class);
        // stopService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(com.i2r.alan.rate_this_place.R.menu.rate_this_place_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.i2r.alan.rate_this_place.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    class AddressResultReceiver extends ResultReceiver {
        Activity mRateThisPlace;
        public AddressResultReceiver(Activity RateThisPlace) {
            super(null);
            mRateThisPlace=RateThisPlace;

        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
             mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            //displayAddressOutput(mAddressOutput);

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
               // showToast(getString(R.string.address_found));
            }
            Log.i("locationname", mAddressOutput);

            mRateThisPlace.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // This code will always run on the UI thread, therefore is safe to modify UI elements.


                    ProgressBar mprogressBar_locationname = (ProgressBar) findViewById(com.i2r.alan.rate_this_place.R.id.progressBar_locationname);
                    mprogressBar_locationname.setVisibility(View.GONE);

                }
            });



        }
    }

    public void clickImage_alone(View view) {
        ((RadioButton) findViewById(com.i2r.alan.rate_this_place.R.id.radioButton1)).setChecked(true);
        AloneGroup= AloneGroupSet.Alone;

    }

    public void clickImage_group(View view) {
        ((RadioButton) findViewById(com.i2r.alan.rate_this_place.R.id.radioButton2)).setChecked(true);
        AloneGroup= AloneGroupSet.Group;
    }

    public void clickImage_activity1(View view) {
        CheckBox mCheckBox1 =((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox1));
        if (!mCheckBox1.isChecked()){
            mCheckBox1.setChecked(true);
        }
        else{
            mCheckBox1.setChecked(false);
        }
    }


    public void clickImage_activity2(View view) {
        CheckBox mCheckBox2 =((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox2));
        if (!mCheckBox2.isChecked()){
            mCheckBox2.setChecked(true);
        }
        else{
            mCheckBox2.setChecked(false);
        }
    }


    public void clickImage_activity3(View view) {
        CheckBox mCheckBox3 =((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox3));
        if (!mCheckBox3.isChecked()){
            mCheckBox3.setChecked(true);
        }
        else{
            mCheckBox3.setChecked(false);
        }
    }


    public void clickImage_activity4(View view) {
        CheckBox mCheckBox4 =((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox4));
        if (!mCheckBox4.isChecked()){
            mCheckBox4.setChecked(true);
        }
        else{
            mCheckBox4.setChecked(false);
        }
    }

    public void clickImage_activity5(View view) {
        CheckBox mCheckBox5 =((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox5));
        if (!mCheckBox5.isChecked()){
            mCheckBox5.setChecked(true);
        }
        else{
            mCheckBox5.setChecked(false);
        }
    }

    public void clickImage_activity6(View view) {
        CheckBox mCheckBox6 =((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox6));
        if (!mCheckBox6.isChecked()){
            mCheckBox6.setChecked(true);
        }
        else{
            mCheckBox6.setChecked(false);
        }
    }


    public void clickImage_activity7(View view) {
        CheckBox mCheckBox7 =((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox7));
        if (!mCheckBox7.isChecked()){
            mCheckBox7.setChecked(true);
        }
        else{
            mCheckBox7.setChecked(false);
        }
    }


    public void clickImage_activity8(View view) {
        CheckBox mCheckBox8 =((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox8));
        if (!mCheckBox8.isChecked()){
            mCheckBox8.setChecked(true);
        }
        else{
            mCheckBox8.setChecked(false);
        }
    }


    public void clickImage_activity9(View view) {
        CheckBox mCheckBox9 =((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox9));
        if (!mCheckBox9.isChecked()){
            mCheckBox9.setChecked(true);
        }
        else{
            mCheckBox9.setChecked(false);
        }
        
    }







    public void clickButton_submit(View view) {

        SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = datetimeformat.format(new Date());
        JSONObject JsonGenerator_activity = new JSONObject();
        JSONObject JsonGenerator_basicrating_location = new JSONObject();

        try {
            JsonGenerator_activity.put("UserID", this.getSharedPreferences("UserInfo", this.MODE_PRIVATE).getString("UserID", null));
            JsonGenerator_activity.put("Nickname", PreferenceManager.getDefaultSharedPreferences(this).getString("display_name", ""));
            if (globalvariable.thelocation==null){JsonGenerator_basicrating_location=null;}
            else {
                JsonGenerator_basicrating_location.put("longitude", globalvariable.thelocation.getLongitude());
                JsonGenerator_basicrating_location.put("latitude",globalvariable.thelocation.getLatitude());
            }
            JsonGenerator_activity.put("Datatime", timestamp);
            JsonGenerator_activity.put("Location", JsonGenerator_basicrating_location);
            JsonGenerator_activity.put("AloneGroup", AloneGroup.toString());
            String Activities="";
            if (((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox1)).isChecked()) Activities=Activities+"Playing_";
            if (((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox2)).isChecked()) Activities=Activities+"Cycling_";
            if (((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox3)).isChecked()) Activities=Activities+"OnlineSocializing_";
            if (((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox4)).isChecked()) Activities=Activities+"Running_";
            if (((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox5)).isChecked()) Activities=Activities+"Sitting_";
            if (((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox6)).isChecked()) Activities=Activities+"Studying_";
            if (((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox7)).isChecked()) Activities=Activities+"Talking_";
            if (((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox8)).isChecked()) Activities=Activities+"Walking_";
            if (((CheckBox) findViewById(com.i2r.alan.rate_this_place.R.id.checkBox9)).isChecked()) Activities=Activities+"Working_";
            JsonGenerator_activity.put("Activities", Activities);
            Log.i("JSON", JsonGenerator_activity.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

         AsyncTaskUploadActivity Activityuploader = new AsyncTaskUploadActivity(this, JsonGenerator_activity);
        Activityuploader.execute();

    }


}
