package com.example.alan.rate_this_place.ratethisplace;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.alan.rate_this_place.utility.DataLogger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RatingToServerIntentService extends IntentService {

    protected static final String FTP_TAG = "FTP";
    String photoFilePath;

    String userid;

    public RatingToServerIntentService(String name) {
        super(name);
    }
    // TODO: Rename actions, choose action names that describe tasks that this




    @Override
    protected void onHandleIntent(Intent intent) {

        String jsonString = intent.getStringExtra("this");
        photoFilePath = intent.getStringExtra("FileURL");
        JSONObject obj = null;
        try {
             obj = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (obj!=null){
            try {
                UploadSimpleRatingtoServer(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

         Log.e("php", "done");
    }





    public void UploadSimpleRatingtoServer(JSONObject obj) throws JSONException {



        URL url = null;
        try {

          url = new URL("http://www.ratethisplace.co/uploadRatingtoDB.php?RatingJson="+obj.toString().replaceAll(" ", "%20"));
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

        //upload photo
        connnectingwithFTP();

    }



    /**
     *
     */
    public void connnectingwithFTP() {

        String ip="ftp.ratethisplace.co";
        String userName=       "FTP@ratethisplace.co";
        String pass=       "uMu6Uv+HRqY";
        boolean status = false;
        FTPClient mFtpClient = new FTPClient();

        userid=getApplication().getSharedPreferences("UserInfo", getApplication().MODE_PRIVATE).getString("UserID","unknown");

        try {

            Log.e(FTP_TAG, String.valueOf(status));
            mFtpClient.connect(InetAddress.getByName(ip));
            status = mFtpClient.login(userName, pass);
            Log.e(FTP_TAG, String.valueOf(status));
            if (FTPReply.isPositiveCompletion(mFtpClient.getReplyCode())) {
                mFtpClient.setFileType(FTP.ASCII_FILE_TYPE);
                mFtpClient.enterLocalPassiveMode();
                FTPFile[] mFileArray = mFtpClient.listFiles();
                Log.e("FTP",  mFileArray.toString());
            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            if (mFtpClient.changeWorkingDirectory("/"+userid)){
            }
            else{
                mFtpClient.makeDirectory(userid);
                mFtpClient.changeWorkingDirectory("/" + userid);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        uploadFile(mFtpClient, new File(photoFilePath), "");


    }

    /**
     *
     * @param ftpClient FTPclient object
     * @param downloadFile local file which need to be uploaded.
     */
    public void uploadFile(FTPClient ftpClient, File downloadFile, String serverfilePath) {

        try {

            FileInputStream srcFileStream = new FileInputStream(downloadFile);
            //e5  Toast.makeText(get, "fpt", Toast.LENGTH_SHORT).show();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  // Ascii vs. Binary Files  Zip is Binary File.

            // ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            Log.e("FTP", "uploading ");
            //  ftpClient.setCopyStreamListener(createListener());

            boolean status = ftpClient.storeFile( downloadFile.getName(),
                    srcFileStream);
            Log.e("FTP", "done "+status);
            srcFileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
