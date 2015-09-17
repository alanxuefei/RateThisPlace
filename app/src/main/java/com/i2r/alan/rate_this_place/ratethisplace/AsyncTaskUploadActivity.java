package com.i2r.alan.rate_this_place.ratethisplace;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.i2r.alan.rate_this_place.utility.DataLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Xue Fei on 1/7/2015.
 */

public class AsyncTaskUploadActivity extends AsyncTask {
    private Context context;
    String userid;
    protected static final String FTP_TAG = "FTP";
    JSONObject JsonGenerator_activity;




    public AsyncTaskUploadActivity(Context context, JSONObject JsonGenerator_activity0) {
        super();
        this.context=context;
        this.JsonGenerator_activity =JsonGenerator_activity0;

    }

    ProgressDialog barProgressDialog;

    @Override
    protected void onPreExecute() {
        barProgressDialog = new ProgressDialog(context);
        barProgressDialog.setTitle("Uploading");
        barProgressDialog.setMessage("Upload in progress ...");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);

        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(10);
        barProgressDialog.show();


        super.onPreExecute();
        userid=context.getSharedPreferences("UserInfo", context.MODE_PRIVATE).getString("UserID","unknown");
    }


    @Override
    protected Object doInBackground(Object[] params) {



        try {
            UploadActivitytoServer(JsonGenerator_activity);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Toast.makeText(this.context, "File is uploaded successfully", Toast.LENGTH_SHORT).show();
        barProgressDialog.dismiss();
    }


    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        Toast.makeText(this.context, "uploading", Toast.LENGTH_SHORT).show();
    }



    public void UploadActivitytoServer(JSONObject obj) throws JSONException {



        URL url = null;
        try {

            url = new URL("http://www.ratethisplace.co/uploadActivitytoDB.php?ActivityJson="+obj.toString().replaceAll(" ", "%20"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            Log.i("php",  "network is not available");
            DataLogger.writeSimpleRatingTolog(url.toString());
            e.printStackTrace();
        }



        InputStream in = null;
        try {
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("php", total.toString());


    }


}