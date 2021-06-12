package com.travel.darktour_project;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetReview extends AsyncTask<String, Void, String> {
    private static String TAG = "insert"; // 로그
    private static final String TAG_JSON = "review";
    private ReviewRecyclerAdapter adapter;
    ReviewData data = new ReviewData();

    String REVIEW_TYPE; // review 타입 -유적지 / 코스
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d("result : ", result);

        if (result == null){
        }
        else {

            showResult(result);
        }
    }

    @SuppressLint("WrongThread")
    @Override
    protected String doInBackground(String... params) {
        String serverURL = "http://113.198.236.105/getreview.php";
        REVIEW_TYPE = (String) params[0];

        String postParameters = "REVIEW_TYPE=" + REVIEW_TYPE;

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
            //showResult(REVIEW_TYPE, sb.toString().trim());

            return sb.toString().trim();
        } catch (Exception e) {

            Log.d(TAG, "getReivew: Error ", e);
            return new String("Error: " + e.getMessage());
        }

    }

    private void showResult(String mJsonString) {
        try {
            Log.d(TAG, "all" + mJsonString);

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String reveiwnum = item.getString("REVIEW_NUM");
                Log.d(TAG, "REVIEW_NUM" + reveiwnum);
                String userid = item.getString("USER_ID");
                String reviewtype = item.getString("REVIEW_TYPE");
                String coursecode = item.getString("COURSE_CODE");
                String historicnum = item.getString("HISTORIC_NUM");
                String reviewcontent = item.getString("REVIEW");
                String countreview = item.getString("COUNT_REVIEW");
                String his_image = item.getString("HIS_IMAGE");

                // 각 List의 값들을 data 객체에 set 해줍니다.
                data.setId(userid);
                data.setReview(reviewcontent);
                data.setLike(countreview);
                data.setThumb_image(R.drawable.thumbs_up);// 따봉
                data.setImage(his_image); // 리뷰사진

                if (REVIEW_TYPE == "유적지") {
                    data.setTitle(historicnum);
                    data.setTag_color(R.color.site_pink);
                    data.setCategory("유적지");
                } else if (REVIEW_TYPE == "코스") {
                    data.setTitle(coursecode);
                    data.setTag_color(R.color.course_blue);
                    data.setCategory("코스");
                }

                // 각 값이 들어간 data를 adapter에 추가합니다.
                adapter.addItem(data);
            }

            // adapter의 값이 변경되었다는 것을 알려줍니다.
            adapter.notifyDataSetChanged();


        } catch (JSONException e) {
            Log.d(TAG, "showReviewResult : ", e);
        }
    }
}