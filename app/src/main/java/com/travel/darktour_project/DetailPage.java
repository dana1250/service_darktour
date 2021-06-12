package com.travel.darktour_project;

// 상세페이지 - 윤지

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailPage extends AppCompatActivity  {
    private static String TAG = "detailpage";
    private static final String TAG_JSON="webnautes";
    String mJsonString;



    TextView textView;
    ImageView weatherimage; // 날씨 사진
    TextView weatherstate; // 날씨 상태

    static String history_name; // intent된 유적지 이름

     String x;
     String y;
    ViewPager pager;
    TabLayout tabLayout;
    // nx = 60 / ny = 127 -> 서울
    // nx = 52 / ny = 38 -> 제주
    // nx = 98 / ny = 76 -> 부산
    

    int choice ; // 지역
    String location; // 지역
    LinearLayout back_image; // 뒷배경 설정을 위해
    /* 0 - 서울
       1 - 제주
       2 - 부산
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage);

        textView = (TextView)findViewById(R.id.text);
        weatherimage = (ImageView)findViewById(R.id.weather);
        weatherstate = (TextView)findViewById(R.id.weather_state);

        back_image = (LinearLayout) findViewById(R.id.back_selection); // 뒷배경을 위해 선언

        Intent intent =getIntent();

        history_name= intent.getExtras().getString("historyname");
        GetData task = new GetData();
        task.execute(history_name);





        // ViewPager랑 TabLayout 연동
        pager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab);


        

    }
    public void back_button_click(View v){
        super.onBackPressed();
    }
    public void set_tablayout(){
        setBackground(); //배경 설정
        pager.setOffscreenPageLimit(1); //현재 페이지의 양쪽에 보유해야하는 페이지 수를 설정 (상황에 맞게 사용하시면 됩니다.) 2개랑 1개 차이를 모르겠어요 그래서 1개함
        tabLayout.setupWithViewPager(pager); //텝레이아웃과 뷰페이저를 연결
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(),this)); //뷰페이저 어뎁터 설정 연결

    }
    public void setBackground(){

        if (location.equals("서울")){ // 서울
            back_image.setBackgroundResource(R.drawable.seoul_backimage);
            choice = 0;
        }
        else if (location.equals("제주")){ // 제주
            back_image.setBackgroundResource(R.drawable.jeju_backimage);
            choice = 1;
        }
        else{ // 부산
            back_image.setBackgroundResource(R.drawable.busan_backimage);
            choice = 2;
        }
    }
     class PageAdapter extends FragmentStatePagerAdapter { //뷰 페이저 어뎁터

        PageAdapter(FragmentManager fm, Context context) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) { //프래그먼트 사용 포지션 설정 0 이 첫탭
                return new SiteFragment(y,x,history_name);
            } else {
                return new ArroundFragment(y,x,history_name);
            }
        }


        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) { //텝 레이아웃의 타이틀 설정
                return "유적지 정보";
            } else {
                return "주변 음식점";
            }
        }

    }
    private void getWeatherInfo(String location) { // 날씨 api
        WeatherInfoTask weatherTask = null;
        if(weatherTask != null) {
            weatherTask.cancel(true);
        }
        weatherTask = new WeatherInfoTask();
        try {
            String weather = weatherTask.execute(location).get(); // 날씨 정보 get
            setWeatherimage(weather);
        } catch (Exception e) {

            e.printStackTrace();

        }
    }
    public void setWeatherimage(String weather){
        if (weather.equals("cloudy")){ // 흐림
            weatherimage.setImageResource(R.drawable.cloudy);
            weatherstate.setText("흐림");
        }
        else if(weather.equals("sun")){ // 맑음
            weatherimage.setImageResource(R.drawable.sun);
            weatherstate.setText("맑음");
        }
        else if(weather.equals("snowman")){ // 눈
            weatherimage.setImageResource(R.drawable.snowman);
            weatherstate.setText("눈");
        }
        else if(weather.equals("rainy")){ // 비
            weatherimage.setImageResource(R.drawable.rainy);
            weatherstate.setText("비");
        }
    }


    // DB 연결
    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(DetailPage.this,
                    "Please Wait", null, true, true);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            Log.d(TAG, "response - " + result);

            if (result == null){
            }
            else {
                mJsonString = result;
                try {
                    Log.d(TAG, "all" + mJsonString);

                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        int historic_num = item.getInt("historic_num");
                        double latitude = item.getDouble("latitude");
                        double longitude = item.getDouble("longitude");
                        String name = item.getString("name");
                        String incident = item.getString("incident");
                        String explain_his = item.getString("explain_his");
                        String address = item.getString("address");
                        String his_source = item.getString("his_source");
                        String his_image = item.getString("his_image");
                        int count_historic = item.getInt("count_historic");

                        x=Double.toString(latitude);
                        y=Double.toString(longitude);
                        location = address.substring(0,2); // 지역위치



                        set_tablayout();
                        getWeatherInfo(location);
                        //날씨 api

                    }
                } catch (JSONException e) {
                    Log.d(TAG, "showResult : ", e);
                }

            }
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0]; // 그 유적지 이름 받아오는 함수 있어야함

            String serverURL = "http://113.198.236.105/historic_explain.php";
            String postParameters = "NAME=" + searchKeyword1;

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
    }
}