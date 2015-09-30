package com.i2r.alan.rate_this_place.myrewards;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.i2r.alan.rate_this_place.R;

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

public class AsyncTaskGetDataToMyRewardBar extends AsyncTask {
    private Context context;
    private String UserID;
    protected static final String AsyncTaskGetDataToMyReward_TAG = "AsyncTaskGetData_MYREWARDS";
    ImageView image0,image1,image2,image3;

    TextView  TextViewConnection,TextViewRewards;

    LinearLayout mLinearLayoutrewardbar;
    ProgressBar ProgressBar_rewards;




    public AsyncTaskGetDataToMyRewardBar(Context context0, ImageView image0,ImageView image1,ImageView image2,ImageView image3,ProgressBar ProgressBar_rewards,TextView Rewards) {
        super();
        this.context=context0;
        this.image0=image0;
        this.image1=image1;
        this.image2=image2;
        this.image3=image3;
        this.ProgressBar_rewards=ProgressBar_rewards;
        this.TextViewRewards=Rewards;


       // this.progressBar_points=progressBar_points0;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();



    }


    @Override
    protected Object doInBackground(Object[] params) {
       // Log.i(AsyncTaskGetDataToMyReward_TAG, "start");
        URL url = null;

        JSONObject JsonGenerator_basicrating = new JSONObject();
        try {
            JsonGenerator_basicrating.put("UserID", context.getSharedPreferences("UserInfo", context.MODE_PRIVATE).getString("UserID", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            url = new URL("http://www.ratethisplace.co/getMyRewards.php?MyRewardsJson="+JsonGenerator_basicrating.toString().replaceAll(" ", "%20"));
            Log.i(AsyncTaskGetDataToMyReward_TAG, "http://www.ratethisplace.co/getMyRewards.php?MyRewardsJson="+JsonGenerator_basicrating.toString().replaceAll(" ", "%20"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (urlConnection!=null) {

            InputStream in = null;
            try {
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (in!=null) {
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

                return total.toString();
            }
        }
        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
       // Log.i(AsyncTaskGetDataToMyReward_TAG, o.toString());
        if (o!=null){


            try {
                JSONObject mJsonResponse = new JSONObject(o.toString().replace("[],",""));
                int thepoints=Integer.parseInt(mJsonResponse.getString("Reward"));
                context.getSharedPreferences("UserInfo", context.MODE_PRIVATE)
                        .edit()
                        .putString("Rewards", mJsonResponse.getString("Reward"))
                .apply();


                int cups=thepoints/10;
                switch (cups) {
                    case 0:
                        image0.setImageResource(R.drawable.rewards_cup_unactive);
                        image1.setImageResource(R.drawable.rewards_cup_unactive);
                        image2.setImageResource(R.drawable.rewards_cup_unactive);
                        image3.setImageResource(R.drawable.rewards_cup_unactive);
                        break;
                    case 1:
                        image0.setImageResource(R.drawable.rewards_cup);
                        image1.setImageResource(R.drawable.rewards_cup_unactive);
                        image2.setImageResource(R.drawable.rewards_cup_unactive);
                        image3.setImageResource(R.drawable.rewards_cup_unactive);
                        break;
                    case 2:
                        image0.setImageResource(R.drawable.rewards_cup);
                        image1.setImageResource(R.drawable.rewards_cup);
                        image2.setImageResource(R.drawable.rewards_cup_unactive);
                        image3.setImageResource(R.drawable.rewards_cup_unactive);
                        break;

                    case 3:
                        image0.setImageResource(R.drawable.rewards_cup);
                        image1.setImageResource(R.drawable.rewards_cup);
                        image2.setImageResource(R.drawable.rewards_cup);
                        image3.setImageResource(R.drawable.rewards_cup_unactive);
                        break;
                    case 4:
                        image0.setImageResource(R.drawable.rewards_cup);
                        image1.setImageResource(R.drawable.rewards_cup);
                        image2.setImageResource(R.drawable.rewards_cup);
                        image3.setImageResource(R.drawable.rewards_cup);
                        break;

                    default:
                        image0.setImageResource(R.drawable.rewards_cup);
                        image1.setImageResource(R.drawable.rewards_cup);
                        image2.setImageResource(R.drawable.rewards_cup);
                        image3.setImageResource(R.drawable.rewards_cup);
                        break;

                }
                if (cups>=5){
                    ProgressBar_rewards.setProgress(100);
                    TextViewRewards.setText("100%");
                }
                else {
                    ProgressBar_rewards.setProgress((thepoints % 10)*10);
                    TextViewRewards.setText((thepoints%10)*10+"%");
                }


              //  progressBar_points.setProgress(Integer.parseInt(mJsonResponse.getString("Reward")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            TextViewConnection.setText("Internet is not available");

        }


    }




    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        Toast.makeText(this.context, "Connecting to The Server", Toast.LENGTH_SHORT).show();

    }

    public void addcup() {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (30 * scale + 0.5f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pixels , pixels );
        pixels = (int) (5 * scale + 0.5f);
        params.setMargins(pixels/2, pixels, 0, pixels);
        ImageView mRewardCup = new ImageView(context);
        mRewardCup.setLayoutParams(params);
        mRewardCup.setImageResource(R.drawable.rewards_cup);
        mRewardCup.setScaleType(ImageView.ScaleType.FIT_XY);
        mLinearLayoutrewardbar.addView(mRewardCup);

    }

    public void addunactivecup() {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (30 * scale + 0.5f);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pixels , pixels );
        pixels = (int) (5 * scale + 0.5f);

        params.setMargins(pixels/2, pixels, 0, pixels);
        ImageView mRewardCup = new ImageView(context);
        mRewardCup.setLayoutParams(params);
        mRewardCup.setImageResource(R.drawable.rewards_cup_unactive);
        mRewardCup.setScaleType(ImageView.ScaleType.FIT_XY);
        mLinearLayoutrewardbar.addView(mRewardCup);

    }

    public void addcircle(int progress) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (30 * scale + 0.5f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pixels , pixels );
        pixels = (int) (5 * scale + 0.5f);
        params.setMargins(pixels, pixels, pixels, pixels);
        ProgressBar mRewardCup = new ProgressBar(context, null,  R.style.Widget_AppCompat_ProgressBar_Horizontal);

        mRewardCup.setIndeterminate(false);



        mRewardCup.setProgress(progress * 10);
        mLinearLayoutrewardbar.addView(mRewardCup);

    }




}