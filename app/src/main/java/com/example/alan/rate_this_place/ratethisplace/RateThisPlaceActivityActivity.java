package com.example.alan.rate_this_place.ratethisplace;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.alan.rate_this_place.R;
import com.example.alan.rate_this_place.utility.Constants;
import com.example.alan.rate_this_place.utility.DataLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RateThisPlaceActivityActivity extends AppCompatActivity  {

    private AutoCompleteTextView actv;
    String[] languages={"This place is not clean","This is a most crowded place on Earth","IOS","SQL","JDBC","Web services"};
    /*google activity detection*/
    protected GoogleApiClient mGoogleApiClient;
    public AddressResultReceiver mResultReceiver = new AddressResultReceiver(this);
    String mAddressOutput;
    Location mLastLocation= new Location("");

    private enum Mood {NOFEELING, HAPPY, UNHAPPY, SURPRISE,FUNNY,ANGRY,DISLIKE};
    private Mood  usermood =Mood.NOFEELING;



    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Top Rated", "Games", "Movies" };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        Intent intent = getIntent();

        String From= intent.getStringExtra("From");
        if (From!=null){
            Log.i("LoactionName", From);
            if (From.equals("MainActivity")){

            }
            else{
                if (From.equals("VisitedPlacesActivity")){
                    String TheLocation= intent.getStringExtra("TheLocation");

                    LatLng thelocation= Constants.BAY_AREA_LANDMARKS.get(TheLocation);

                    mLastLocation.setLatitude(thelocation.latitude);//your coords of course
                    mLastLocation.setLongitude(thelocation.longitude);
                    ProgressBar mprogressBar_locationname = (ProgressBar) findViewById(R.id.progressBar_locationname);
                    mprogressBar_locationname.setVisibility(View.GONE);

                }
            }
        }

        final EditText mEdit_Activity_Others = (EditText) findViewById(R.id.Edit_Activity_Others);

        mEdit_Activity_Others.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                final Button mbutton_editdone = (Button) findViewById(R.id.button_editdone);
                mbutton_editdone.setVisibility(View.VISIBLE);
                mbutton_editdone.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View view) {


                        mbutton_editdone.setVisibility(View.GONE);
                        //release the focus
                        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                });

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
         getMenuInflater().inflate(R.menu.rate_this_place_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

         /*location*/



    protected void startLocationNameIntentService(Location location) {


        Log.i("locationname", String.valueOf(mResultReceiver));
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
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


                    ProgressBar mprogressBar_locationname = (ProgressBar) findViewById(R.id.progressBar_locationname);
                    mprogressBar_locationname.setVisibility(View.GONE);

                }
            });



        }
    }

    public void clickImage_unhappyface(View view) {



    }

    public void clickImage_happyface(View view) {


    }



    static final int REQUEST_TAKE_PHOTO = 1;
    File photoFile = null;
    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = this.getSharedPreferences("UserInfo", this.MODE_PRIVATE).getString("UserID", null)
                             + "_"+timeStamp + "_Lat_"+mLastLocation.getLatitude()+"_Lon_"+mLastLocation.getLongitude()+"_"+mLastLocation.getProvider()+"_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/" + "RateThisPlace" + "/" + "ActiveData/");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }



    public void clickButton_submit(View view) {

        SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = datetimeformat.format(new Date());
        JSONObject JsonGenerator_basicrating = new JSONObject();
        JSONObject JsonGenerator_basicrating_location = new JSONObject();

        try {
            JsonGenerator_basicrating.put("UserID", this.getSharedPreferences("UserInfo", this.MODE_PRIVATE).getString("UserID", null));
            JsonGenerator_basicrating.put("Nickname",   PreferenceManager.getDefaultSharedPreferences(this).getString("display_name", ""));
            if (mLastLocation==null){JsonGenerator_basicrating_location=null;}
            else {
                JsonGenerator_basicrating_location.put("longitude", mLastLocation.getLongitude());
                JsonGenerator_basicrating_location.put("latitude", mLastLocation.getLatitude());
            }
            JsonGenerator_basicrating.put("Datatime", timestamp);
            JsonGenerator_basicrating.put("Location", JsonGenerator_basicrating_location);
            JsonGenerator_basicrating.put("Feeling", usermood.toString());
            JsonGenerator_basicrating.put("Cleanness", ((RatingBar) findViewById(R.id.ratingBarCLEANNESS)).getRating());
            JsonGenerator_basicrating.put("Safty", ((RatingBar) findViewById(R.id.ratingBarSAFTY)).getRating());
            JsonGenerator_basicrating.put("Beauty", ((RatingBar) findViewById(R.id.ratingBarBEAUTIFULNESS)).getRating());
            JsonGenerator_basicrating.put("Greenness", ((RatingBar) findViewById(R.id.ratingBarGREENNESS)).getRating());
            JsonGenerator_basicrating.put("Friendliness", ((RatingBar) findViewById(R.id.ratingBarFRIENDLINESS)).getRating());
            JsonGenerator_basicrating.put("Convenience", ((RatingBar) findViewById(R.id.ratingBarCONVENIENCE)).getRating());
            JsonGenerator_basicrating.put("Commentary", ((EditText) findViewById(R.id.AutoCompleteTextView_Commentary)).getText().toString());
            if (photoFile!= null) {JsonGenerator_basicrating.put("PhotoFileName",  photoFile.getName());}
            Log.i("JSON", JsonGenerator_basicrating.toString());
            DataLogger.writeSimpleRatingTolog(JsonGenerator_basicrating.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncTaskUploadDetaledRating myfileuploader = new AsyncTaskUploadDetaledRating(this,JsonGenerator_basicrating,photoFile);
        myfileuploader.execute();

    }


}
