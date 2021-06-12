package com.travel.darktour_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notice extends AppCompatActivity {
    Intent intent;
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    String mJsonString;

    NoticeAdapter adapter = new NoticeAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);

        ListView listview;

        listview = (ListView) findViewById(R.id.noticelist);
        listview.setAdapter(adapter);

        //adapter.addItem("1", "2102.05.49","무") ;
        //adapter.addItem("2", "2102.13.25","야") ;
        //adapter.addItem("3", "2108.01.99","호") ;
        GetData task = new GetData();
        task.execute();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                NoticeItem item = (NoticeItem) parent.getItemAtPosition(position);
                String contentsstr = item.getContents();
                Toast.makeText(v.getContext(), contentsstr, Toast.LENGTH_SHORT).show();
            }
        }) ;
    }

    // DB 연결
    private class GetData extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Notice.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){
            }
            else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String IP_ADDRESS = "113.198.236.105";

                URL url = new URL("http://" + IP_ADDRESS + "/select_notice.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

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
        private void showResult(){
            try {
                Log.d(TAG, "all" + mJsonString);
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                for (int i = 0; i<jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    int no_num = item.getInt("no_num");
                    String no_contents = item.getString("no_contents");
                    String no_date = item.getString("no_date");

                    adapter.addItem(Integer.toString(no_num), no_contents, no_date) ;
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }
}
