package com.travel.darktour_project;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertUserReview extends AsyncTask<String, Void, String> {

    private static String TAG = "addreview"; // 로그

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d(TAG, "POST response  - " + result);
        if(result.contains("Database")){
            Log.d(TAG, "dberr");
        }
    }

    @Override
    protected String doInBackground(String... params) {
//--------------------------------------------------- changes params
        String serverURL = (String)params[0];
        String USER_ID = (String)params[1];
        String REVIEW_TYPE= (String)params[2];
        String COURSE_CODE= (String)params[3];
        String HISTORIC_NUM= (String)params[4];
        String REVIEW= (String)params[5];


        String postParameters = "&USER_ID=" + USER_ID
                + "&REVIEW_TYPE=" + REVIEW_TYPE
                + "&COURSE_CODE=" + COURSE_CODE
                + "&HISTORIC_NUM=" + HISTORIC_NUM
                + "&REVIEW=" + REVIEW;

        Log.d("add review : ", postParameters);

        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }


            bufferedReader.close();


            return sb.toString();


        } catch (Exception e) {
            Log.d(TAG, "InsertData: Error ", e);
            return new String("Error: " + e.getMessage());
        }

    }
}

