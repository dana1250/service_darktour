package com.travel.darktour_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class WeatherInfoTask extends AsyncTask<String, String, String> { // 날씨 api

    Date date = new Date(); // 현재 날짜
    Calendar cal = Calendar.getInstance(); // 시간 추출
    Calendar cal1 = Calendar.getInstance(); // 시간 추출
    Calendar cal2 = Calendar.getInstance(); // 다은날
    String[] weather_x = {"60","52","98"}; // x
    String[] weather_y = {"127","38","76"}; // y
    Context mContext;
    ProgressDialog progressDialog;
    SimpleDateFormat format_ = new SimpleDateFormat("yyyyMMdd"); // 날짜
    private static final String WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
    private static final String SERVICE_KEY = "DEkomlDfGx1Zp0dH%2FHX%2BX1sL6wGeLJvTMDoBr0JIH0SK3bjPdlwtJe8s0N5qnfJYwAX%2BqGlJkf6NxUpbhkxevg%3D%3D";
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected String doInBackground(String... params) {
        // nx = 60 / ny = 127 -> 서울
        // nx = 52 / ny = 38 -> 제주
        // nx = 98 / ny = 76 -> 부산
        String location = params[0];
        int choice = 0;
        if(location.equals("서울")){
            choice = 0;
        }else if(location.equals("제주")){
            choice = 1;
        }else if(location.equals("부산")){
            choice = 2;
        }
        String nx = weather_x[choice];	//위도
        String ny = weather_y[choice];	//경도
        cal.add(Calendar.DATE, -1);
        cal2.add(Calendar.DATE, +1);


        Date d = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat ( "HH");
        String right_now = format1.format(d);
        String baseDate;
        if(Integer.parseInt(right_now) >=23){
            baseDate = format_.format(cal1.getTime());	//오늘
        }
        else{
            baseDate = format_.format(cal.getTime());	//조회하고싶은 날짜
        }



        String pageNo = "1"; // 페이지 수

        //int hour = cal.get(Calendar.HOUR_OF_DAY); // 시간 계산
        //String baseTime = Integer.toString(hour);	//API 제공 시간
        String baseTime = "2300";	//API 제공 시간
        String dataType = "json";	//타입 xml, json
        String numOfRows = "200";	//한 페이지 결과 수

        StringBuilder urlBuilder = new StringBuilder(WEATHER_URL); /*URL*/
        HttpURLConnection conn = null;
        BufferedReader rd = null;
        StringBuilder sb = null;
        try {
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+SERVICE_KEY);
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));	/* 한 페이지 결과 수 )*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));	/* 페이지 번호*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));	/* 타입 */
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */

            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도

            /*각각의 base_time 로 검색 참고자료 참조 : 규정된 시각 정보를 넣어주어야 함 */
            URL url = new URL(urlBuilder.toString());

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());

            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            if(conn != null) {
                conn.disconnect();
            }
            if(rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("Debug", sb.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("HH00"); // 현재 시간
        String timestr = sdf.format(cal.getTime()); // 현재 시간

        String time1 = format_.format(d);

        StringBuffer string = new StringBuffer();
        try {
            // 가장 큰 JSONObject를 가져옵니다.
            JSONObject jObject = new JSONObject(sb.toString());

            // response 키를 가지고 데이터를 파싱
            JSONObject parse_response = (JSONObject) jObject.get("response");
            // response 로 부터 body 찾기
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            // body 로 부터 items 찾기
            JSONObject parse_items = (JSONObject) parse_body.get("items");

            // items로 부터 itemlist 를 받기
            JSONArray parse_item = (JSONArray) parse_items.get("item");
            String category;
            JSONObject weather; // parse_item은 배열형태이기 때문에 하나씩 데이터를 하나씩 가져올때 사용
            // 카테고리와 값만 받아오기
            String day = "";
            String time = "";
            for (int i = 0; i < parse_item.length(); i++) {
                weather = (JSONObject) parse_item.get(i);
                Object fcstValue = weather.get("fcstValue");
                Object fcstDate = weather.get("fcstDate");
                Object fcstTime = weather.get("fcstTime");

                //double형으로 받고싶으면 아래내용 주석 해제
                //double fcstValue = Double.parseDouble(weather.get("fcstValue").toString());
                category = (String) weather.get("category");
                // 출력
                    /*if (!day.equals(fcstDate.toString())) {
                        day = fcstDate.toString();
                    }*/
                int now = Integer.parseInt(timestr);
                String calTime = null;
                if((now >=2100)){ // 9시 이상
                    time1 = format_.format(cal2.getTime());	// 다음 날자
                    calTime = "0300";

                }
                else{
                    if(now == 000){
                        now += 100;
                    }
                    while(now % 300 != 0){
                        now += 100;
                    }
                    calTime = String.format("%04d",now);

                }

                String value = fcstValue.toString();


                if (time1.equals(fcstDate.toString()) && (category.equals("SKY") || category.equals("PTY")) && calTime.equals(fcstTime.toString())) {

                    if ((category.equals("PTY") && value.equals("0"))||(category.equals("SKY") && (value.equals("1") || value.equals("3") || value.equals("4")))){ // 강수형태가 없을 때
                        if (category.equals("SKY") && value.equals("1")){ // 하늘이 맑고 구름 많음
                            // sun
                            string.append("sun/");
                        }
                        else{
                            // cloudy
                            string.append("cloudy/");
                        }
                    } else if (category.equals("PTY") && (value.equals("1") || value.equals("2") || value.equals("4") || value.equals("5") || value.equals("6"))) {
                        // rainy
                        string.append("rainy/");
                    }
                    else if (category.equals("PTY") && value.equals("3")  && value.equals("7")) {
                        // snowman
                        string.append("snowman/");
                    }

                }


            }

        }
        catch ( JSONException e) {
            e.printStackTrace();
        }
        String[] array = string.toString().split("/"); // 날씨들
        String weather = null; // 날씨
        int count = array.length; // 날씨 개수


        if(count > 1 ){ // 날씨가 두개면 PTY로
            if(Arrays.asList(array).contains("snowman")){
                int index = Arrays.binarySearch(array,"snowman");
                weather = array[index];
            }
            else if(Arrays.asList(array).contains("rainy")){
                int index = Arrays.binarySearch(array,"rainy");
                weather = array[index];
            }
            else if(Arrays.asList(array).contains("sun")){
                int index = Arrays.binarySearch(array,"sun");
                weather = array[index];
            }
            else if(Arrays.asList(array).contains("cloudy")){
                int index = Arrays.binarySearch(array,"cloudy");
                weather = array[index];
            }

        }
        else{
            weather = array[0];
        }
        return weather;
    }



}
