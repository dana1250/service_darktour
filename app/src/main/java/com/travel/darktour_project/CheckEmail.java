package com.travel.darktour_project;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckEmail extends AsyncTask<String, Void, String> {
    private static String TAG = "checkemail";
    String errorString = null;

    @Override
    protected void onPreExecute() { super.onPreExecute(); }


    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        Log.d("result : ", result);
    }

    @SuppressLint("WrongThread")
    @Override
    protected String doInBackground(String... params) {
        String serverURL = (String)params[0];
        String NAME = (String)params[1];
        String USER_ID= (String)params[2];
        String USER_PWD= (String)params[3];

        String postParameters = "email=" + USER_ID;

        Log.d("checkemail : ", postParameters);

        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

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
            String line;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }

            bufferedReader.close();

            Log.d("sb : ", sb.toString().trim());
            if(!sb.toString().trim().contains(USER_ID)){
                InsertUserData insertdata = new InsertUserData();
                String IP_ADDRESS = "113.198.236.105";

                insertdata.execute("http://" + IP_ADDRESS + "/register.php", NAME, USER_ID, USER_PWD);

                Log.d("insert name - ", NAME);
                Log.d("insert email - ", USER_ID);
                Log.d("insert pwd - ", USER_PWD);
            }else{
                Log.d(TAG, "existing email : " + USER_ID);
            }
            return sb.toString().trim();


        } catch (Exception e) {

            Log.d(TAG, "checkemail: Error ", e);
            return new String("Error: " + e.getMessage());
        }

    }
}