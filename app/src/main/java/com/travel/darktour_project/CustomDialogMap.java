package com.travel.darktour_project;

import android.app.Activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class CustomDialogMap extends AppCompatActivity {
    String titleString; // 유적지 이름 저장 arr
    TextView title;
    PublicFrag publicFrag;
    CarFrag carFrag;
    RoadFrag roadFrag;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_dialog);
        Intent intent = getIntent();
        titleString = intent.getStringExtra("title");

        title = (TextView) findViewById(R.id.title);
        title.setText(titleString);
        title.setSelected(true);
        title.setSingleLine();
        title.setMarqueeRepeatLimit(-1);
        title.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        GetDataAI task = new GetDataAI();
        task.execute(titleString);



        Button btn1;
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn1 = findViewById(R.id.cancelButton);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();


            }
        });

    }

        private class GetDataAI extends AsyncTask<String, Void, String> {

            String TAG = "getAI";
            String errorString = null;
            public String[] names; // 유적지 이름
            public String[] x; // 경도
            public String[] y; // 위도
            int[] start_finish_arr = new int[2]; // 시작 도착지 좌표
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                   progressDialog = ProgressDialog.show(CustomDialogMap.this,
                           "Please Wait", null, true, true);

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Log.d(TAG, "response - " + result);

                if (result == null){
                }
                else {
                    showResult(result);
                    showDialog();
                    progressDialog.dismiss();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                String searchKeyword1 = params[0]; // 그 유적지 이름 받아오는 함수 있어야함

                String serverURL = "http://113.198.236.105/select_all_historic_AI.php";
                String postParameters = "NAMES=" + searchKeyword1;

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
                    return sb.toString().trim();

                } catch (Exception e) {
                    Log.d(TAG, "InsertData: Error ", e);
                    errorString = e.toString();
                    return null;
                }

            }
            // 받아온 결과값 나누는거
            private void showResult(String res){
                try {
                    Log.d(TAG, "all" + res);

                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("webnautes");
                    names = new String [jsonArray.length()];
                    x = new String [jsonArray.length()];
                    y = new String [jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        x[i] = Double.toString(item.getDouble("longitude")); // 위도
                        y[i] = Double.toString(item.getDouble("latitude")); // 경도
                        names[i] = item.getString("name");
                    }

                    Log.d("name : ", names.toString());

                } catch (JSONException e) {
                    Log.d(TAG, "showResult : ", e);
                }

            }
            private void showDialog(){

                try {
                    //set time in mil
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                String[] names = titleString.split("-");
                Bundle bundle = new Bundle();
                start_finish_arr[0] = 0;
                start_finish_arr[1] = names.length - 1;
                bundle.putStringArray("title", names); // 유적지 이름
                bundle.putStringArray("x", x); // x
                bundle.putStringArray("y", y); // y
                bundle.putIntArray("start_finish_arr", start_finish_arr); // 출발지 도착지 array

                publicFrag = new PublicFrag();
                FragmentTransaction transaction;

                transaction = getSupportFragmentManager().beginTransaction();

                transaction.add(R.id.intercourse_map, publicFrag);

                publicFrag.setArguments(bundle);
                transaction.commit();
            }
        }
    }
