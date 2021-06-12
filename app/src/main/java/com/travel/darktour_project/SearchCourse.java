package com.travel.darktour_project;

// 코스 탐색 화면

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.edsergeev.TextFloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class SearchCourse extends AppCompatActivity implements View.OnClickListener,TextWatcher{
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    String mJsonString;

    //UI
    Spinner spinner1;
    Spinner spinner2;


    CourseSearch1Adapter adapterSpinner1;//Adapter
    CourseSearch2Adapter adapterSpinner2;

    EditText searchview;
    TextFloatingActionButton favorite_fab ; //fab 버튼

    private SearchSiteRecyclerAdapter adapter = new SearchSiteRecyclerAdapter();; // recyclerview adapter
    private String search_history_name; // 사용자 관심 유적지

    public static Context mContext;
    ArrayList num = new ArrayList<Integer>();
    ArrayList data_name = new ArrayList<String>() ; // 다음 화면(유적지 선택되는 화면) 유적지 이름
    ArrayList data_content = new ArrayList<String>() ; // 다음 화면(유적지 선택되는 화면) 유적지 설명
    ArrayList image_string = new ArrayList<String>() ; // 사진 이미지
    ArrayList histoy_likes = new ArrayList<String>() ; // 사진 이미지
    String location; // 지역
    String transportation; // 이동수단
    String checked_ai; // ai check 여부
    int count = 0;
    ArrayList latitude = new ArrayList<String>(); // 위도
    ArrayList longitude = new ArrayList<String>();// 경도
    Python py;
    // 받아온 결과값 나누는거
    private void showResult(String result){
        try {
            Log.d(TAG, "all" + result);

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                //사용자가 관심 유적지 선택
                search_history_name = item.getString("favorite_his");

            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
    @Override
    public void onBackPressed(){

        super.onBackPressed();
        finish();
    }
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_search);
        Intent intent = getIntent(); // 데이터 수신

        location = intent.getExtras().getString("location"); // 어떤 위치 선택했는지 intent를 통해 받음
        mContext = this;

        searchview = findViewById(R.id.editSearch);
        searchview.addTextChangedListener(this);

        favorite_fab = (TextFloatingActionButton) findViewById(R.id.fab); // fab 선언

        favorite_fab.setOnClickListener(this);
        spinner2 = (Spinner)findViewById(R.id.spinner_2); // 교통
        Switch ai_switch = findViewById(R.id.ai_switch); // ai 버튼
        ai_switch.setEnabled(false); // 초기에 enabled false
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));

        }
        // this will start python

        // now create python instance
        py = Python.getInstance();


        ai_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // ai 버튼 listener

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // check 되어있을 때


                    checked_ai = "AI 추천";

                    GetData2 task2 = new GetData2();
                    String result = null;
                    try {
                        result = task2.execute(PreferenceManager.getString(mContext, "signup_id")).get();
                        showResult(result);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GetAI task = new GetAI();
                    task.execute();



                } else { // check 안되어있을 때

                    init(); // recyclerview 세팅
                    ai_switch.setEnabled(false);
                    ai_switch.setChecked(false);
                    spinner2.setEnabled(true);
                    set_spinner2();
                    clear_array();
                    checked_ai = " "; // 추천 안눌렀을때
                }
            }
        });

        //spinner1 - 지역선택
        set_spinner1();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // spinner1 클릭 event
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchview.setCursorVisible(false);
                if(position == 0){
                    ai_switch.setEnabled(true);
                    ai_switch.setChecked(true);
                    set_spinner2();
                    spinner2.setSelection(1);
                    searchview.setCursorVisible(true);
                    spinner2.setEnabled(false);
                    clear_array();
                }
                else{
                    init(); // recyclerview 세팅
                    ai_switch.setEnabled(false);
                    ai_switch.setChecked(false);
                    spinner2.setEnabled(true);
                    set_spinner2();
                    clear_array();
                }
                searchview.setText(null);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //--------------------------------------------------------------------
        // spinner2 - 교통 선택

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // spinner2 클릭 event
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { // position 3은 hint라서 쓰지않음
                if (position < 3) { // 처음부터 recyclerview 안보이게 할려고
                    init(); // recyclerview 세팅
                    clear_array();
                    //코스탐색 화면에서 지역을 바꿧을때 인식이 안되서 밑에 getSelectedItem 이거 추가! - 혜쥬
                    location = (String) spinner1.getSelectedItem();
                    Log.d(TAG, "location spinner - " + location);
                    if(position == 0){ // 대중교통
                        transportation = "대중교통";
                    }
                    else if(position == 1){ // 자동차
                        transportation = "자동차";
                    }
                    else{ // 도보
                        transportation = "도보";
                    }

                    getData(); // recyclerview 데이터 값 가져오고 넣는 곳!!!
                    searchview.setCursorVisible(true);
                    ai_switch.setEnabled(true);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // spinner1에 위치 설정
        if (location.equals("서울")) { // 서울
            spinner1.setSelection(1);
        } else if (location.equals("제주")) { // 제주
            spinner1.setSelection(2);
        } else if(location.equals("부산")){ // 부산
            spinner1.setSelection(3);
        } else{ // 전체보기
            ai_switch.setEnabled(true);
            spinner1.setSelection(0);
        }



    }
    private class GetAI extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog ;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            if(location.equals("전체")){
                progressDialog = ProgressDialog.show(SearchCourse.this,
                        "AI 계산중", "기다려주세요! \n전체지역은 자동차 경로만 제공합니다!", true, true);
            }else{
                progressDialog = ProgressDialog.show(SearchCourse.this,
                        "AI 계산중", "기다려주세요!", true, true);
            }



            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            Log.d(TAG, "response - " + result);

            if (result == null){
            }
            else {
                mJsonString = result;
                init(); // recyclerview 세팅
                clear_array();

                GetDataAI task = new GetDataAI();
                task.execute(mJsonString);


            }
            progressDialog.dismiss();

        }

        @Override
        protected String doInBackground(String... params) {


            //now create python object
            PyObject probj;
            PyObject obj;

            if(location.equals("전체")){ // 전체지역
                probj =py.getModule("everywhere_recommend"); // give python script name
                obj = probj.callAttr("main",search_history_name);//함수 와 매개변수 호출
            }else{ //선택지역
                probj =py.getModule("location_recommend"); // give python script name
                obj = probj.callAttr("main",search_history_name,location);//함수 와 매개변수 호출
            }


            return obj.toString();
        }
    }



    private void set_spinner1() { // spinner1 설정
        //데이터 - 지역선택
        List<String> data1 = new ArrayList<>(); // 지역 서울 - 제주 -부산 순서
        data1.add("전체 지역"); data1.add("서울"); data1.add("제주"); data1.add("부산"); // spinner1에 넣을 데이터

        //UI생성 spinner1 - 지역선택
        spinner1 = (Spinner)findViewById(R.id.spinner_1); // 지역선택

        //Adapter
        adapterSpinner1 = new CourseSearch1Adapter(this, data1);

        spinner1.setDropDownVerticalOffset(120); // spinner dropdown 간격 주기위해
        // spinner1.setSelection(0, false); //선택되면
        //Adapter 적용 - 지역
        spinner1.setAdapter(adapterSpinner1);

    }
    private void clear_array(){
        num.clear();
        data_name.clear();
        data_content.clear();
        image_string.clear();
        histoy_likes.clear();
        count  = 0;
        favorite_fab.setText(Integer.toString(count));
    }
    private void set_spinner2(){ // spinner2 설정
        //데이터 - 교통선택
        List<String> data2= new ArrayList<>(); // 지역 서울 - 제주 -부산 순서
        data2.add("대중교통"); data2.add("자동차"); data2.add("도보"); data2.add("선택"); // spinner2에 넣을 데이터 마지막이 hint

        //UI생성 spinner2- 교통


        //Adapter
        adapterSpinner2 = new CourseSearch2Adapter(this, data2);

        spinner2.setDropDownVerticalOffset(120); // spinner dropdown 간격 주기위해

        //Adapter 적용 - 교통
        spinner2.setAdapter(adapterSpinner2);

        spinner2.setSelection(adapterSpinner2.getCount()); //힌트로 세팅

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                if(count == 0 || count == 1){
                    Toast.makeText(SearchCourse.this, "2개 이상 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    click_fab(); // fab버튼 눌렀을 때 지금까지 선택된 화면
                }
                break;
        }
    }
    public void click_fab(){ // fab버튼 눌렀을 때 지금까지 선택된 화면
        Intent intent = new Intent(this,FavoriteSite.class);
        String [] arr_title  = (String[]) data_name.toArray(new String[data_name.size()]);
        String [] arr_content  = (String[]) data_content.toArray(new String[data_content.size()]);
        String [] arr_image  = (String[]) image_string.toArray(new String[image_string.size()]);
        String[] arr_lat = (String[]) latitude.toArray(new String[latitude.size()]); //y
        String [] arr_long = (String[]) longitude.toArray(new String[longitude.size()]); //x
        String [] arr_likes  = (String[]) histoy_likes.toArray(new String[histoy_likes.size()]);

        intent.putExtra("select_title",arr_title);
        intent.putExtra("select_content",arr_content);
        intent.putExtra("location",location);
        intent.putExtra("transportation",transportation);
        intent.putExtra("ai",checked_ai);
        intent.putExtra("latitude",arr_lat);
        intent.putExtra("longitude",arr_long);
        intent.putExtra("image",arr_image);
        intent.putExtra("like",arr_likes);

        startActivity(intent);
    }
    private void init() {
        RecyclerView recyclerView = findViewById(R.id.site_recycler); // recyclerview 선언

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SearchSiteRecyclerAdapter(); // SearchSiteRecyclerAdapter로 adapter 이용
        recyclerView.setAdapter(adapter);  // recyclerview 세팅

        adapter.setOnItemClicklistener(new OnSiteItemClickListener() { // adapter의 item 일부를 클릭하엿을 경우
            @Override
            public void onItemClick(SearchSiteRecyclerAdapter.ItemViewHolder holder, View view, int position) { // item 클릭 시

                Boolean clickBefore = adapter.getItem(position).isSelected(); // layout이 클릭되었는지 확인
                if (clickBefore == false){ // item 눌렀을 때
                    if(count < 5) { // item 선택이 5개 이하인 경우 코스에 유적지 추가
                        adapter.getItem(position).setLayout_(R.drawable.press_back); // 해당 포지션의 레이아웃 색상 변경
                        adapter.getItem(position).setSelected(true); // 해당 포지션 layout 클릭 true
                        num.add(count, position); // 해당 유적지 위치 num 저장
                        data_name.add(count, adapter.getItem(position).getTitle()); // 유적지 이름 추가
                        data_content.add(count, adapter.getItem(position).getDesc()); // 유적지 설명 추가
                        latitude.add(count, Double.toString(adapter.getItem(position).getLatitude())); // y 추가
                        longitude.add(count, Double.toString(adapter.getItem(position).getLongitude())); // x 추가
                        image_string.add(count, adapter.getItem(position).getImage()); // 이미지 추가
                        histoy_likes.add(count, adapter.getItem(position).getLike()); // 좋아요 추가
                        adapter.notifyItemChanged(position); // adapter에 변경을 notify
                        count++; // 코스의 유적지 개수 증가

                    }
                    else{ // item 선택이 5개 이상인 경우 코스에 유적지 추가 실패
                        Toast.makeText(SearchCourse.this, "최대 5개까지 선택가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{ // item 선택 취소
                    adapter.getItem(position).setLayout_(R.drawable.write_review_back); // 해당 포지션의 레이아웃 색상 변경
                    adapter.getItem(position).setSelected(false); // 해당 포지션 layout 클릭 false
                    adapter.notifyItemChanged(position); // adapter에게 변경을 notify
                    int temp = num.indexOf(position); // 해당 포지션 값의 데이터 삭제
                    num.remove(temp); // 해당 유적지 위치 num 삭제
                    data_name.remove(temp); // 유적지 이름 삭제
                    data_content.remove(temp);// 유적지 설명 삭제
                    latitude.remove(temp); // y 삭제
                    longitude.remove(temp); // x 삭제
                    image_string.remove(temp); // image 삭제
                    histoy_likes.remove(temp); // 좋아요 삭제
                    count --; // 코스의 유적지 개수 감소

                }

                favorite_fab.setText(Integer.toString(count)); // 플로팅 버튼의 유적지 선택 개수로 변경 및 설정
            }
        });
    }
    public void refresh(int position){
        int temp = (int) num.get(position);
        adapter.getItem(temp).setLayout_(R.drawable.write_review_back);
        adapter.getItem(temp).setSelected(false);
        adapter.notifyItemChanged(temp);

        num.remove(position);
        data_name.remove(position); // 유적지 이름 삭제
        data_content.remove(position);// 유적지 설명 삭제
        longitude.remove(position);// 유적지 설명 삭제
        latitude.remove(position);// 유적지 설명 삭제
        count --;
        favorite_fab.setText(Integer.toString(count));

    }
    private void getData() {
        // 데이터 가져와서 추출 하는 작업
        GetData task = new GetData();
        task.execute(location);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapter.getFilter().filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    // DB 연결
    private class GetData extends AsyncTask<String, Void, String>{


        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "response - " + result);

            if (result == null){
            }
            else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0]; // 그 유적지 이름 받아오는 함수 있어야함

            String serverURL = "http://113.198.236.105/select_area.php";
            String postParameters = "ADDRESS=" + searchKeyword1;

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
        private void showResult(){
            try {
                Log.d(TAG, "all" + mJsonString);

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject item = jsonArray.getJSONObject(i);
                    int historic_num = item.getInt("historic_num");
                    double latitude = item.getDouble("latitude"); // 위도
                    double longitude = item.getDouble("longitude"); // 경도
                    String name = item.getString("name");
                    String incident = item.getString("incident");
                    String explain_his = item.getString("explain_his");
                    String address = item.getString("address");
                    String his_source = item.getString("his_source");
                    String his_image = item.getString("his_image");
                    int count_historic = item.getInt("count_historic");

                    SiteData data = new SiteData();

                    data.setImage(his_image);

                    data.setLayout_(R.drawable.write_review_back); // background 지정
                    data.setDesc(explain_his); // 내용
                    data.setTitle(name);
                    data.setLike(Integer.toString(count_historic));
                    data.setLatitude(latitude); //y
                    data.setLongitude(longitude); // x
                    data.setAccident_text(incident); // 사건

                    adapter.addItem(data);
                }
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }

    // DB 연결
    private class GetData2 extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;

        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SearchCourse.this,
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
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0]; // 그 유적지 이름 받아오는 함수 있어야함

            String serverURL = "http://113.198.236.105/select_favorite_his.php";
            String postParameters = "USER_ID=" + searchKeyword1;

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
    // DB 연결
    private class GetDataAI extends AsyncTask<String, Void, String>{


        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "response - " + result);

            if (result == null){
            }
            else {
                mJsonString = result;
                showResult();
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
        private void showResult(){
            try {
                Log.d(TAG, "all" + mJsonString);

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject item = jsonArray.getJSONObject(i);
                    int historic_num = item.getInt("historic_num");
                    double latitude = item.getDouble("latitude"); // 위도
                    double longitude = item.getDouble("longitude"); // 경도
                    String name = item.getString("name");
                    String incident = item.getString("incident");
                    String explain_his = item.getString("explain_his");
                    String his_image = item.getString("his_image");
                    int count_historic = item.getInt("count_historic");

                    SiteData data = new SiteData();

                    data.setImage(his_image);
                    data.setLayout_(R.drawable.write_review_back); // background 지정
                    data.setDesc(explain_his); // 내용
                    data.setTitle(name);
                    data.setLike(Integer.toString(count_historic));
                    data.setLatitude(latitude); //y
                    data.setLongitude(longitude); // x
                    data.setAccident_text(incident); // 사건

                    adapter.addItem(data);
                }
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }
}
