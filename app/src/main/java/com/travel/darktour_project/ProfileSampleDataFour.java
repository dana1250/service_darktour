package com.travel.darktour_project;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class ProfileSampleDataFour{

    private String review_type; // 어떤 리뷰인지 유적지인지 코스인지
    private String course; // 리뷰한 코스 이름
    private String historic; // 리뷰한 유적지 이름
    private String review; // 리뷰 내용
    private int tag_color;


    public ProfileSampleDataFour(String review_type, String course, String historic, String review) {
        this.review_type = review_type;
        this.historic = historic;
        this.course = course;
        this.review = review;
    }

    public String getType() { return this.review_type; }

    public String getHis() { return this.historic; }

    public String getCourse() { return this.course; }

    public String getReview() { return this.review; }

}
