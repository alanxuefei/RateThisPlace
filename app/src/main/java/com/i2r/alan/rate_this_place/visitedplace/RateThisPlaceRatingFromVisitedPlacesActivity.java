package com.i2r.alan.rate_this_place.visitedplace;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.i2r.alan.rate_this_place.R;
import com.i2r.alan.rate_this_place.utility.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RateThisPlaceRatingFromVisitedPlacesActivity extends AppCompatActivity {

    /*google activity detection*/



    private enum Mood { NOFEELING, HAPPY, UNHAPPY}

    private Mood  usermood = Mood.NOFEELING;
    public static String Locationname1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_fromvisitedplaces);
        addListenerOnRatingBar();
        Intent intent = getIntent();
        Locationname1 = intent.getStringExtra("From");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(com.i2r.alan.rate_this_place.R.menu.rate_this_place_menu_main, menu);
        return true;
    }

    public void ReturnButton(View v) {
        Log.i("test", "returen");
        super.onBackPressed();

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

    public void clickImage_unhappyface(View view) {


        ((RadioButton)findViewById(com.i2r.alan.rate_this_place.R.id.radioButton2)).setChecked(true);
        ((TextView)findViewById(com.i2r.alan.rate_this_place.R.id.textView)).setText("This place makes me feel: Unhappy");
        usermood = Mood.UNHAPPY;
    }

    public void clickImage_happyface(View view) {



        ((RadioButton)findViewById(com.i2r.alan.rate_this_place.R.id.radioButton)).setChecked(true);
        ((TextView)findViewById(com.i2r.alan.rate_this_place.R.id.textView)).setText("This place makes me feel: Happy");
        usermood = Mood.HAPPY;

    }

    public void addListenerOnRatingBar() {

        final String[] ratingscale = {"Very poor","Poor", "Average", "Good","Excellent"};

        RatingBar ratingBar = (RatingBar) findViewById(com.i2r.alan.rate_this_place.R.id.ratingBarCLEANNESS);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,  boolean fromUser) {

                ((TextView) findViewById(com.i2r.alan.rate_this_place.R.id.textViewCLEANNESS)).setText(ratingscale[(int) rating - 1]);
            }
        });

        ratingBar = (RatingBar) findViewById(com.i2r.alan.rate_this_place.R.id.ratingBarSAFTY);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,  boolean fromUser) {

                ((TextView) findViewById(com.i2r.alan.rate_this_place.R.id.textViewSAFTY)).setText(ratingscale[(int) rating - 1]);
            }
        });

        ratingBar = (RatingBar) findViewById(com.i2r.alan.rate_this_place.R.id.ratingBarBEAUTIFULNESS);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,  boolean fromUser) {


                ((TextView) findViewById(com.i2r.alan.rate_this_place.R.id.textViewBEAUTIFULNESS)).setText(ratingscale[(int) rating - 1]);
            }
        });

        ratingBar = (RatingBar) findViewById(com.i2r.alan.rate_this_place.R.id.ratingBarGREENNESS);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,  boolean fromUser) {


                ((TextView) findViewById(com.i2r.alan.rate_this_place.R.id.textViewGREENNESS)).setText(ratingscale[(int)rating-1]);
            }
        });


        ratingBar = (RatingBar) findViewById(com.i2r.alan.rate_this_place.R.id.ratingBarFRIENDLINESS);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {


                ((TextView) findViewById(com.i2r.alan.rate_this_place.R.id.textViewFRIENDLINESS)).setText(ratingscale[(int) rating - 1]);
            }
        });

        ratingBar = (RatingBar) findViewById(com.i2r.alan.rate_this_place.R.id.ratingBarCONVENIENCE);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,  boolean fromUser) {

                ((TextView) findViewById(com.i2r.alan.rate_this_place.R.id.textViewCONVENIENCE)).setText(ratingscale[(int) rating - 1]);
            }
        });

    }

    public void clickButton_submit(View view) {

        SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = datetimeformat.format(new Date());
        JSONObject JsonGenerator_rating = new JSONObject();
        JSONObject JsonGenerator_rating_location = new JSONObject();
        double VratingBarCLEANNESS= ((RatingBar) findViewById(R.id.ratingBarCLEANNESS)).getRating();
        double VratingBarSAFTY= ((RatingBar) findViewById(R.id.ratingBarSAFTY)).getRating();
        double VratingBarBEAUTIFULNESS= ((RatingBar) findViewById(R.id.ratingBarBEAUTIFULNESS)).getRating();
        double VratingBarFRIENDLINESS= ((RatingBar) findViewById(R.id.ratingBarFRIENDLINESS)).getRating();
        double VratingBarCONVENIENCE= ((RatingBar) findViewById(R.id.ratingBarCONVENIENCE)).getRating();
        double VratingBarGREENNESS= ((RatingBar) findViewById(R.id.ratingBarGREENNESS)).getRating();

        int usedratingbar=0;
        if (VratingBarCLEANNESS!=0)usedratingbar++;
        if (VratingBarSAFTY!=0)usedratingbar++;
        if (VratingBarBEAUTIFULNESS!=0)usedratingbar++;
        if (VratingBarFRIENDLINESS!=0)usedratingbar++;
        if (VratingBarCONVENIENCE!=0)usedratingbar++;
        if (VratingBarGREENNESS!=0)usedratingbar++;


        double avgrating= (VratingBarCLEANNESS+VratingBarSAFTY+VratingBarBEAUTIFULNESS+VratingBarFRIENDLINESS+VratingBarCONVENIENCE+VratingBarGREENNESS)/usedratingbar;
        DecimalFormat df = new DecimalFormat("0.0");

        try {



            JsonGenerator_rating.put("UserID", this.getSharedPreferences("UserInfo", this.MODE_PRIVATE).getString("UserID", null));


            JsonGenerator_rating.put("Nickname", PreferenceManager.getDefaultSharedPreferences(this).getString("display_name", ""));


            LatLng detectedlocation_LatLng = Constants.AREA_LANDMARKS.get(Locationname1);
            JsonGenerator_rating_location.put("longitude",detectedlocation_LatLng.longitude);
            JsonGenerator_rating_location.put("latitude", detectedlocation_LatLng.latitude);


            JsonGenerator_rating.put("Datatime", timestamp);
            JsonGenerator_rating.put("Location", JsonGenerator_rating_location);
            JsonGenerator_rating.put("Feeling", usermood.toString());
            JsonGenerator_rating.put("Rating_Cleanness", VratingBarCLEANNESS);
            JsonGenerator_rating.put("Rating_Safty", VratingBarSAFTY);
            JsonGenerator_rating.put("Rating_Beauty", VratingBarBEAUTIFULNESS);
            JsonGenerator_rating.put("Rating_Friendliness", VratingBarFRIENDLINESS);
            JsonGenerator_rating.put("Rating_Convenience", VratingBarCONVENIENCE);
            JsonGenerator_rating.put("Rating_Greenness", VratingBarGREENNESS);


         //   JsonGenerator_rating.put("Commentary", ((EditText) findViewById(com.i2r.alan.rate_this_place.R.id.AutoCompleteTextView_Commentary)).getText().toString());
            Log.i("JSON", JsonGenerator_rating.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // clickbuttonRecieve();

        AsyncTaskUploadRatingFromVisitedPlace myfileuploader = new AsyncTaskUploadRatingFromVisitedPlace(this,JsonGenerator_rating);
        myfileuploader.execute();

        this.getSharedPreferences("VisitedPlaceStatus", this.MODE_PRIVATE).edit().putString(Locationname1+"RatingStatus", df.format(avgrating)).apply();


        Log.i("VisitedPlace", Locationname1+"RatingStatus");


    }







}
