package com.travel.darktour_project;
// 리뷰 recycler
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ReviewFragment extends Fragment{
    View v;
    // adapter에 들어갈 list 입니다.
    private ReviewRecyclerAdapter adapter;
    public static Context mContext;
    RecyclerView recyclerView;
    Spinner spinner;
    boolean i = true; // 버튼 눌려졌는지 확인
    int pos;
    private int num; //버튼에 따른 좋아요 숫자 확인하기 위해서 넣은 변수로 나중에 변경 혹은 삭제 해야함

    private static final String TAG_JSON = "review";
    String REVIEW_TYPE; // review 타입 -유적지 / 코스
    String IP_ADDRESS = "113.198.236.105";
    String reviewnum;
    boolean chk;
    boolean click = false;



    // https://black-jin0427.tistory.com/222
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_review, container, false);


        spinner = (Spinner) v.findViewById(R.id.spinner); // 목록 상자
        ImageButton write = (ImageButton) v.findViewById(R.id.write_review); // 리뷰 쓰기 버튼
        mContext = getActivity();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 0은 코스 1은 유적지
                pos = position;
                if(click){
                    if (position == 0) {
                        Log.d("코스선택"," ");
                        init();
                        GetReview task = new GetReview(); // db 연동
                        task.execute("코스");
                    } else if (position == 1) {
                        Log.d("유적지선택"," ");
                        init();
                        GetReview task = new GetReview(); // db 연동
                        task.execute("유적지");
                    }
                }
                click = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        write.setOnClickListener(new View.OnClickListener() { // 리뷰 쓰기
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WriteReview.class); // 리뷰 쓰기 화면
                // id 보내야하나? 보내야하는디
                startActivity(intent);
            }
        });
        return v;
    }

    private void init() {
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ReviewRecyclerAdapter();
        recyclerView.setAdapter(adapter);

    }

    public void onResume() {


        super.onResume();
        init();

        GetReview task = new GetReview(); // db 연동
        if(pos==0)
        {
            task.execute("코스");
        }else{
            task.execute("유적지");
        }


    }

    public class GetReview extends AsyncTask<String, Void, String> {
        String errorString = null;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),
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


                if (REVIEW_TYPE == "유적지") {
                    data.setTitle(historicnum);
                    data.setTag_color(R.color.site_pink);
                    data.setCategory("유적지");
                } else if (REVIEW_TYPE == "코스") {
                    coursecode = coursecode.substring(0, coursecode.length()-1);
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


