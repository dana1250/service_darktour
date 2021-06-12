package com.travel.darktour_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment{
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    String mJsonString;

    private List<String> list;          // 데이터를 넣은 리스트변수
    ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;
    Button searchbutton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v=inflater.inflate(R.layout.fragment_search, container, false);

        searchbutton =(Button)v.findViewById(R.id.searchbutton);
        editSearch = (EditText)v.findViewById(R.id.editSearch);
        listView = (ListView)v.findViewById(R.id.listView);

        // 리스트를 생성한다.
        list = new ArrayList<String>();
        // 검색에 사용할 데이터을 미리 저장한다.
        //settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, getActivity().getApplicationContext());

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);
        listView.setVisibility(View.INVISIBLE);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listView.setVisibility(View.VISIBLE);
                        // input창에 문자를 입력할때마다 호출된다.
                        // search 메소드를 호출한다.
                        String text = editSearch.getText().toString();
                        GetData task = new GetData();
                        task.execute(text);
                        Log.d(TAG, "내가 입력한 검색어 - " + text);
                        //search(text);
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//리스트 선택시 값 detailpage에 전달.
                Intent intent = new Intent(getActivity(), DetailPage.class);
                intent.putExtra("historyname",list.get(position));
                startActivity(intent);
            }
        });
        return v;
    }

    // 검색을 수행하는 메소드
    /*
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();

    }
     */

    // 검색에 사용될 데이터를 리스트에 추가한다. ->DB 수정
    /*
    private void settingList(){
        list.add("부산근대역사관");
        list.add("임시수도기념관");
        list.add("40계단");
        list.add("임시수도정부청사");
        list.add("우암동소막마을");
        list.add("가덕도외양포마을");
        list.add("부산민주공원");
        list.add("10.16 기념관");
        list.add("10.16부마민중항쟁탑");
        list.add("4.19 민주혁명추념탑");
        list.add("일광광산채굴마을");
        list.add("아비동비석문화마을");
        list.add("구포만세역사테마거리");
        list.add("흰여울문화마을");
        list.add("가덕도등대");
        list.add("가덕도대항인공동굴");
        list.add("국립일제강제동원역사관");
        list.add("UN기념관");
        list.add("만벵듸 공동장지");
        list.add("진아영할머니 집터");
        list.add("한모살(표선백사장)");
        list.add("중문리 신사터");
        list.add("함덕배사장 & 서우봉");
        list.add("조천중학원 옛터");
        list.add("종남마을");
        list.add("불카분낭(불타버린 나무)");
        list.add("목시물굴");
    }
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),
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
                list.clear();
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0]; // 그 유적지 이름 받아오는 함수 있어야함

            String serverURL = "http://113.198.236.105/search_select.php";
            String postParameters = "WORD=" + searchKeyword1;

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

                    list.add(name);
                }
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }

}