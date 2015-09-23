package com.i2r.alan.rate_this_place.visitedplace;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.i2r.alan.rate_this_place.database.DBContract;
import com.i2r.alan.rate_this_place.database.DBHelper;
import com.i2r.alan.rate_this_place.ratethisplace.RateThisPlaceActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class VisitedPlacesActivity extends AppCompatActivity {

    protected static final String HumanActivityTAG = "HumanActivity";

   // JSONArray mJsonArray = new JSONArray(o.toString().replace("[],",""));

    List<String> VisitedPlaceList = new ArrayList<>();

    private ListView HumanActivityListView;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.i2r.alan.rate_this_place.R.layout.activity_visited_places);


        readDB(VisitedPlaceList);

        HumanActivityListView = (ListView) findViewById(com.i2r.alan.rate_this_place.R.id.listView_HumanActivity);

        // this-The current activity context.
        // Second param is the resource Id for list layout row item
        // Third param is input array
        arrayAdapter = new ArrayAdapter(this, com.i2r.alan.rate_this_place.R.layout.visitedplaces_list_eachrow, com.i2r.alan.rate_this_place.R.id.thetheTextView, VisitedPlaceList.toArray());
        HumanActivityListView.setAdapter(arrayAdapter);

        HumanActivityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                clickthelistview(a, v, position, id);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.i2r.alan.rate_this_place.R.menu.menu_human_activity_diary, menu);
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

    public void clickthelistview(AdapterView<?> a, View v, int position, long id) {

       String value = (String)a.getItemAtPosition(position);
        Log.i(HumanActivityTAG, value);

        Intent startRatingIntent = new Intent(this, RateThisPlaceActivity.class);
        startRatingIntent.putExtra("From","VisitedPlacesActivity");
        startRatingIntent.putExtra("TheLocation", value.split(" ")[1]);
        startActivity(startRatingIntent);


    }

    public void ReturnButton(View v) {
        Log.i("test", "return");
        super.onBackPressed();
    }


    public void readDB(List<String> VisitedPlaceList) {

        DBHelper mDbHelper = new DBHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                DBContract.FeedEntry.COLUMN_NAME_DATE,
                DBContract.FeedEntry.COLUMN_NAME_TIME,
                DBContract.FeedEntry.COLUMN_LOCATION_LONGITUDE,
                DBContract.FeedEntry.COLUMN_LOCATION_LATITUDE,
                DBContract.FeedEntry.COLUMN_LOCATION_NAME
        };

// How you want the results sorted in the resulting Cursor


        Cursor c = db.query(
                DBContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        c.moveToFirst();
        while (!c.isLast()){
            String vLOCATION_NAME = c.getString(
                    c.getColumnIndexOrThrow(DBContract.FeedEntry.COLUMN_LOCATION_NAME)
            );

            String  vTIME = c.getString(
                    c.getColumnIndexOrThrow(DBContract.FeedEntry.COLUMN_NAME_TIME)
            );

            VisitedPlaceList.add(vTIME+" "+vLOCATION_NAME);
            c.moveToNext();

        }
        db.close();
        mDbHelper.close();

    }
}
