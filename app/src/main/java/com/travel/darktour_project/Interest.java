package com.travel.darktour_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Interest extends AppCompatActivity {
    RecyclerView mVerticalView;
    RecyclerView mVerticalView2;
    RecyclerView mVerticalView3;
    VerticalAdapter mAdapter;
    VerticalAdapter mAdapter2;
    VerticalAdapter mAdapter3;
    LinearLayoutManager mLayoutManager;
    LinearLayoutManager mLayoutManager2;
    LinearLayoutManager mLayoutManager3;
    ArrayList<VerticalData> data = new ArrayList<>();
    ArrayList<VerticalData> data2 = new ArrayList<>();
    ArrayList<VerticalData> data3 = new ArrayList<>();

    Button completed;
    TextView textView;

    private Context mContext;

    String resultText = ""; // 체크되었을 때 값을 저장할 스트링 값

    int count = 0;

    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_history);
        mContext = this;

        completed = findViewById(R.id.completed_bt);
        textView = findViewById(R.id.horizon_description2);

        //// 서울 유적지
        mVerticalView = findViewById(R.id.history_recycler_seoul);
        // init LayoutManager
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        // setLayoutManager
        mVerticalView.setLayoutManager(mLayoutManager);
        // init Adapter
        mAdapter = new VerticalAdapter();
        // set Data
        mAdapter.setData(data);
        // set Adapter
        mVerticalView.setAdapter(mAdapter);
        // 코스 data 추가
        //GetData task = new GetData();
        //task.execute("서울");

        //// 제주 유적지
        mVerticalView2 = findViewById(R.id.history_recycler_jeju);
        // init LayoutManager
        mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        // setLayoutManager
        mVerticalView2.setLayoutManager(mLayoutManager2);
        // init Adapter
        mAdapter2 = new VerticalAdapter();
        // set Data
        mAdapter2.setData(data2);
        // set Adapter
        mVerticalView2.setAdapter(mAdapter2);
        // 코스 data 추가
        //GetData task2 = new GetData();
        //task2.execute("제주");

        //// 부산 유적지
        mVerticalView3 = findViewById(R.id.history_recycler_busan);
        // init LayoutManager
        mLayoutManager3 = new LinearLayoutManager(getApplicationContext());
        mLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        // setLayoutManager
        mVerticalView3.setLayoutManager(mLayoutManager3);
        // init Adapter
        mAdapter3 = new VerticalAdapter();
        // set Data
        mAdapter3.setData(data3);
        // set Adapter
        mVerticalView3.setAdapter(mAdapter3);
        // 코스 data 추가
        //GetData task3= new GetData();
        //task3.execute("부산");

        GetData task = new GetData();
        String IP_ADDRESS = "113.198.236.105";
        task.execute();

        // 선택완료 버튼을 눌렀을 경우
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] array= resultText.split(",");
                for(int i = 0; i <array.length; i++) {
                    count++;
                    Log.d("유적지 갯수", String.valueOf(count));
                }
                Log.d("it_check", resultText);
                if (count >= 2 && count <= 5) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("it_check", resultText);
                    Log.d("it_check - ", resultText);

                    //String signupid = PreferenceManager.getString(mContext, "signup_id");
                    Intent intent2 = getIntent();
                    String user_id = intent2.getExtras().getString("사용자아이디");


                    String IP_ADDRESS = "113.198.236.105";
                    InsertFavorite inserthistory = new InsertFavorite();
                    inserthistory.execute("http://" + IP_ADDRESS + "/update_favorite_his.php", user_id, resultText);
                    Toast.makeText(getApplicationContext(), "관심유적지가 선택되었습니다!", Toast.LENGTH_LONG).show();
                    startActivity(intent);

                    count = 0;
                } else if (count == 0 || count == 1) {
                    Toast.makeText(getApplicationContext(), "관심유적지를 2개 이상 선택해 주세요!", Toast.LENGTH_LONG).show();

                    count = 0;
                } else if (count > 5) {
                    Toast.makeText(getApplicationContext(), "5개 이하로 선택해주세요!", Toast.LENGTH_LONG).show();

                    count = 0;
                }
            }
        });
    }

    // 카드뉴스
    class VerticalAdapter extends RecyclerView.Adapter<VerticalViewHolder> {

        private ArrayList<VerticalData> verticalDatas;
        private Context context;


        public void setContext(Context context) {
            this.context = context;
        }
        public void setData(ArrayList<VerticalData> list){
            verticalDatas = list;
        }

        @Override
        public VerticalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 사용할 아이템의 뷰를 생성해준다.
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.historic_sites_item, parent, false);

            VerticalViewHolder holder = new VerticalViewHolder(view);

            return holder;
        }

        public boolean isChecked(int position) {
            return verticalDatas.get(position).checked;
        }

        @Override
        public void onBindViewHolder(VerticalViewHolder holder, int position) {
            final VerticalData data = verticalDatas.get(position);

            // setData
            //holder.icon.setImageResource(data.getImg());
            Glide.with(Interest.this).load(data.getImg()).into(holder.icon);
            holder.description.setText(data.getArea());
            holder.name.setText(data.getHistory());
            holder.checkbox.setText(data.getHistory());
            holder.key.setText(data.getKeyword());

            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean newState = !verticalDatas.get(position).isChecked();
                    verticalDatas.get(position).checked = newState;
                    if(holder.checkbox.isChecked()) {
                        resultText += data.getHistory() + ",";

                    } else {
                        resultText = resultText.replaceAll("\\(", "");
                        resultText = resultText.replaceAll("\\)", "");
                        resultText = resultText.replaceAll(data.getHistory() + ",", "");

                    }
                }
            });
            holder.checkbox.setChecked(isChecked(position));
        }
        @Override
        public int getItemCount() {
            return verticalDatas.size();
        }
    }

    class VerticalViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView description;
        public TextView name;
        public CheckBox checkbox;
        public TextView key;

        public VerticalViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
            description = (TextView) itemView.findViewById(R.id.horizon_description);
            description.setSelected(true);
            name = (TextView) itemView.findViewById(R.id.horizon_description2);
            checkbox = (CheckBox) itemView.findViewById(R.id.his_check);
            key = (TextView) itemView.findViewById(R.id.horizon_description3);
        }
    }
    //사용자 추천 코스 순위 data
    class VerticalData {

        public boolean checked;
        private String img;
        private String area;
        private String history;
        private String keyword;


        public VerticalData(String img, String area, String history, String keyword) {
            this.img = img;
            this.area = area;
            this.history = history;
            this.keyword = keyword;
        }
        public String getImg() { return this.img; }

        public String getArea() { return this.area; }

        public String getHistory() { return this.history; }

        public boolean isChecked() { return checked; }

        public String getKeyword() { return this.keyword; }
    }

    // DB 연결
    private class GetData extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Interest.this,
                    "Please Wait", null, true, true);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null) {
            } else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {

            //String searchKeyword1 = params[0]; // 그 유적지 이름 받아오는 함수 있어야함

            //String serverURL =  params[0];
            //String postParameters = params[1];

            try {

                String IP_ADDRESS = "113.198.236.105";
                //task.execute("http://" + IP_ADDRESS + "/select_all_historic.php");

                URL url = new URL("http://" + IP_ADDRESS + "/select_all_historic.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                // OutputStream outputStream = httpURLConnection.getOutputStream();
                //outputStream.write(postParameters.getBytes("UTF-8"));
                //outputStream.flush();
                //outputStream.close();

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
                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }

        }

        // 받아온 결과값 나누는거
        private void showResult() {
            try {
                Log.d(TAG, "all" + mJsonString);

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i<jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String name = item.getString("name");
                    String incident = item.getString("incident");
                    String his_image = item.getString("his_image");
                    String address = item.getString("address");
                    String keyword = item.getString("keyword");

                    if(address.startsWith("서울")){
                        data.add(new VerticalData(his_image, incident, name, keyword));
                    }

                    else if(address.startsWith("제주")){
                        data2.add(new VerticalData(his_image, incident, name, keyword));
                    }
                    else if(address.startsWith("부산")){
                        data3.add(new VerticalData(his_image, incident, name, keyword));
                    }
                }
                mAdapter.notifyDataSetChanged();
                mAdapter2.notifyDataSetChanged();
                mAdapter3.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }
}