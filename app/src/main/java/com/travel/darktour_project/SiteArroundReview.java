package com.travel.darktour_project;
// 유적지에 대한 리뷰가 궁금하신가요? 리뷰 or 코스에 대한 리뷰가 궁금하신가요? 리뷰
// 우선은 유적지 리뷰 중심으로 만듬

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;


public class SiteArroundReview extends AppCompatActivity {
    // adapter에 들어갈 list 입니다.
    private ReviewRecyclerAdapter adapter;
    final String TAG_JSON = "review";
    String reviewnum;
    boolean chk;
    TextView history_name;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_site_arround);
        Intent intent = getIntent(); // 데이터 수신
        String his_name = intent.getStringExtra("historic_name"); // 유적지이름
        history_name = findViewById(R.id.his_name_text); // 유적지 이름 세팅
        history_name.setText(his_name);
        GetReview task = new GetReview();
        task.execute(his_name);
        init();

    }
    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ReviewRecyclerAdapter();
        recyclerView.setAdapter(adapter);

    }

    public class GetReview extends AsyncTask<String, Void, String> {
        String errorString = null;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SiteArroundReview.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("result : ", result);

            if (result == null){
            }
            else {

                showResult(result);
                progressDialog.dismiss();
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://113.198.236.105/getreview_detail.php";
            String SITE_NAME = (String) params[0];

            String postParameters = "SITE=" + SITE_NAME;

            Log.d("getreveiw : ", postParameters);

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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();

                Log.d("sb : ", sb.toString().trim());


                return sb.toString().trim();
            } catch (Exception e) {

                Log.d(TAG, "getReivew: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }

    }
    private void showResult(String mJsonString) {
        try {
            Log.d(TAG, "all" + mJsonString);

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                reviewnum = item.getString("REVIEW_NUM");
                Log.d(TAG, "REVIEW_NUM" + reviewnum);
                String userid = item.getString("USER_ID");
                String reviewtype = item.getString("REVIEW_TYPE");
                String coursecode = item.getString("COURSE_CODE");
                String historicnum = item.getString("HISTORIC_NUM");
                String reviewcontent = item.getString("REVIEW");
                String countreview = item.getString("COUNT_REVIEW");
                String his_image = item.getString("HIS_IMAGE");

                // 각 List의 값들을 data 객체에 set 해줍니다.
                ReviewData data = new ReviewData();
                data.setId(userid);
                data.setReview(reviewcontent);
                data.setLike(countreview);
                data.setThumb_image(R.drawable.thumbs_up);// 따봉
                if(chk){

                    data.setThumb_image(R.drawable.press_thumbs_up);
                }else{
                    data.setThumb_image(R.drawable.thumbs_up);
                }

                data.setImage(his_image); // 리뷰사진
                data.setReview_num(reviewnum);




                    data.setTitle(historicnum);
                    data.setTag_color(R.color.site_pink);
                    data.setCategory("유적지");



                // 각 값이 들어간 data를 adapter에 추가합니다.
                adapter.addItem(data);
            }
            if(jsonArray.length() == 0){ // 리뷰 개수가 0개인 경우
                ReviewData data = new ReviewData();
                data.setId("");
                data.setReview("작성된 리뷰가 없어요ㅜ");

                adapter.addItem(data);
            }

            // adapter의 값이 변경되었다는 것을 알려줍니다.
            adapter.notifyDataSetChanged();


        } catch (JSONException e) {
            Log.d(TAG, "showReviewResult : ", e);
        }
    }

    // 좋아요 연결
    private class editlike extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SiteArroundReview.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0]; // 그 유적지 이름 받아오는 함수 있어야함
            String TABLE = params[1]; // 그 유적지 이름 받아오는 함수 있어야함
            String USER_ID = params[2]; // 그 유적지 이름 받아오는 함수 있어야함
            String CONTENT = params[3]; // 그 유적지 이름 받아오는 함수 있어야함

            String postParameters = "TABLENAME=" + TABLE + "&USER_ID=" + USER_ID+ "&CONTENT=" + CONTENT;
            Log.d("editlike Param : ", postParameters);

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
                String text = "/select.php";
                if(serverURL.contains(text)) {
                    chk = sb.toString().contains(reviewnum);


                }
                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }

        }
    }
}
