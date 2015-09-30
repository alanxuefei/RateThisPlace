package com.i2r.alan.rate_this_place.mapview;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.i2r.alan.rate_this_place.MainActivity;
import com.i2r.alan.rate_this_place.feedback.FeedbackDialogFragment;
import com.i2r.alan.rate_this_place.myrewards.MyRewardActivity;
import com.i2r.alan.rate_this_place.pasivedatacollection.PassiveDataToFTPIntentService;
import com.i2r.alan.rate_this_place.ratethisplace.RateThisPlaceActivity;
import com.i2r.alan.rate_this_place.utility.globalvariable;
import com.i2r.alan.rate_this_place.visitedplace.VisitedPlacesActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, PopupMenu.OnMenuItemClickListener,LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    protected static final String Googlemap_TAG = "Googlemap";
    protected static final String GPS_Internet_Check_TAG = "GPS_Internet_Check";
    protected static final String FirstRun_TAG = "FirstRun";
    private LocationManager mlocationManager=null;
    private Marker mylocationmark;

    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.i2r.alan.rate_this_place.R.layout.activity_maps);

             /*location */
        // Acquire a reference to the system Location Manager
       // mlocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Register the listener with the Location Manager to receive location updates
       // mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this); //long minTime, float minDistance
       // mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        setUpMapIfNeeded();
    }


    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    protected void onPause() {
        super.onPause();
        //mMap.clear();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctlyde
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(com.i2r.alan.rate_this_place.R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMap.setMyLocationEnabled(true);
        mMap.setIndoorEnabled(true);
        (new AsyncTaskGetRatingDataToMap(this,mMap,mLastLocation)).execute();
        (new AsyncTaskGetActivitiesDataToMap(this,mMap,mLastLocation)).execute();
        if(globalvariable.thelocation==null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.348551, 103.813059), 10));
        }
        else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(globalvariable.thelocation.getLatitude(), globalvariable.thelocation.getLongitude()), 18));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
       // Log.i(Googlemap_TAG, "ready");


    }



    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(com.i2r.alan.rate_this_place.R.menu.menu_mapview, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case com.i2r.alan.rate_this_place.R.id.action_Menu:
               // Toast.makeText(this, "Menu Clicked", Toast.LENGTH_SHORT).show();


                if (isConnectingToInternet()){
                    startActivity(new Intent(this, MainActivity.class));
                }
                else{
                   // Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                }

                return true;
            case com.i2r.alan.rate_this_place.R.id.action_manualupload:
               // Toast.makeText(this, "manualupload", Toast.LENGTH_SHORT).show();
                if (isConnectingToInternet()){
                    startService(new Intent(getBaseContext(), PassiveDataToFTPIntentService.class));
                }
                else{
                    //Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                }
                return true;
            case com.i2r.alan.rate_this_place.R.id.action_visitedplace:
               // Toast.makeText(this, "Music Clicked", Toast.LENGTH_SHORT).show();
                clickImage_activity_log();
                return true;
            case com.i2r.alan.rate_this_place.R.id.action_myreward:
                //Toast.makeText(this, "My Reward", Toast.LENGTH_SHORT).show();
                clickImage_myreward();
                return true;

            case com.i2r.alan.rate_this_place.R.id.action_userprofile:
                //Toast.makeText(this, "userprofile", Toast.LENGTH_SHORT).show();
              //  startActivity(new Intent(this, UserProfileActivity.class));
                return true;

            case com.i2r.alan.rate_this_place.R.id.action_feedback:
               // Toast.makeText(this, "Feedback Clicked", Toast.LENGTH_SHORT).show();
                new FeedbackDialogFragment().show(getSupportFragmentManager(), "FeedbackDialog");
                return true;
            case com.i2r.alan.rate_this_place.R.id.action_aboutus:
                //Toast.makeText(this, "Music Clicked", Toast.LENGTH_SHORT).show();
                return true;

        }
        return true;
    }

    public void clickImage_rate_this_place(View view) {
        // Toast.makeText(this, "Image_rate_this_place", Toast.LENGTH_SHORT).show();
        //DataLogger.writeTolog("_________________________________start_a_new_test____________________________"+"\n");
        Intent intent = new Intent(this, RateThisPlaceActivity.class);
        intent.putExtra("From", "MainActivity");
        startActivity(intent);

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

    public void clickImage_myreward( ) {

        Intent intent = new Intent(this, MyRewardActivity.class);
        startActivity(intent);

    }

    public void clickImage_activity_log() {

        Intent intent = new Intent(this, VisitedPlacesActivity.class);
        startActivity(intent);

    }


     /*location*/

    @Override
    public void onLocationChanged(Location location) {


      /*  double longitude = location.getLongitude();
        double latitude =  location.getLatitude();
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        String Location_information= "L " + longitude + " " + latitude+" "+location.getProvider();
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 18));
        if (mylocationmark!=null) mylocationmark.remove();
        mylocationmark=mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(com.i2r.alan.rate_this_place.R.drawable.mylocation))
                .position(loc));



        if(mMap != null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18.0f));
        }

        Log.i("location", Location_information);

*/
        //Toast.makeText(this, Location_information, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
