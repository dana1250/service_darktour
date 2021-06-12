package com.travel.darktour_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ProfileFragment extends Fragment {
    View v;
    static TextView favoritecourse;
    ImageButton setting;
    private LinearLayoutManager mLayoutManger;
    private LinearLayoutManager mLayoutManger2;
    private LinearLayoutManager mLayoutManger3;
    private LinearLayoutManager mLayoutManger4;

    private ProfileAdapter adapter = new ProfileAdapter();
    private ProfileAdapter2 adapter2 = new ProfileAdapter2();
    private ProfileAdapter3 adapter3 = new ProfileAdapter3();
    private ProfileAdapter4 adapter4 = new ProfileAdapter4();

    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    RecyclerView recyclerView3;
    RecyclerView recyclerView4;
    ArrayList<ProfileSampleDataFour> data = new ArrayList<>();
    ArrayList<ProfileSampleDataThree> data2 = new ArrayList<>();


    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    String mJsonString;
    String IP_ADDRESS = "113.198.236.105";
    private Context mContext;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        setting = v.findViewById(R.id.imageButton);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Setting.class);
                startActivity(intent);
            }
        });

        super.onCreate(savedInstanceState);
        TextView id = v.findViewById(R.id.Id);
        id.setText((PreferenceManager.getString(getContext(), "signup_id"))+" 의 여행 기록");
        //recycleView 초기화

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView2 = v.findViewById(R.id.recycler_view2);
        recyclerView3 = v.findViewById(R.id.recycler_view3);
        recyclerView4 = v.findViewById(R.id.recycler_view4);


        mLayoutManger=new LinearLayoutManager(v.getContext());
        mLayoutManger.setOrientation(LinearLayoutManager.VERTICAL);

        mLayoutManger2=new LinearLayoutManager(v.getContext());
        mLayoutManger2.setOrientation(LinearLayoutManager.VERTICAL);

        mLayoutManger3=new LinearLayoutManager(v.getContext());
        mLayoutManger3.setOrientation(LinearLayoutManager.HORIZONTAL);

        mLayoutManger4=new LinearLayoutManager(v.getContext());
        mLayoutManger4.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(mLayoutManger);
        recyclerView2.setLayoutManager(mLayoutManger2);
        recyclerView3.setLayoutManager(mLayoutManger3);
        recyclerView4.setLayoutManager(mLayoutManger4);

        recyclerView.setAdapter(adapter);
        recyclerView2.setAdapter(adapter2);
        //recyclerView3.setAdapter(adapter3);

        //adapter3.setItems(new ProfileSampleDataThree().getItems());
        //adapter4.setItems(new ProfileSampleDataFour().getItems());

        //코스 관련
        adapter3 = new ProfileAdapter3();
        adapter3.setData(data2);
        recyclerView3.setAdapter(adapter3);

        //리뷰 관련
        adapter4 = new ProfileAdapter4();
        adapter4.setData(data);
        recyclerView4.setAdapter(adapter4);

        mContext = getActivity();


        //사용자 리뷰 가져오는 곳  + 코스도 가져오는곳
        try {
            GetData task = new GetData();
            String result = task.execute("http://" + IP_ADDRESS + "/select_myreview.php",PreferenceManager.getString(mContext, "signup_id")).get();
            showResult(result);
            GetData task2 = new GetData();
            String result2 = task2.execute("http://" + IP_ADDRESS + "/select_mycourse.php",PreferenceManager.getString(mContext, "signup_id")).get();
            showResult2(result2);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //아이템 로드
        try {
            adapter.setItems(new ProfileSampleData(PreferenceManager.getString(getContext(), "signup_id")).getItems());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter.setOnItemClicklistener(new OnProfileItemClickListener(){
            @Override
            public void onItemClick(ProfileAdapter.ViewHolder holder, View view, int position) {
                Intent intent = new Intent(getActivity(), DetailPage.class);
                intent.putExtra("historyname",adapter.items.get(position).getTitle());
                startActivity(intent);
            }
        });

        try {
            adapter2.setItems(new ProfileSampleDataTwo(PreferenceManager.getString(getContext(), "signup_id")).getItems());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //recyclerView2.setAdapter(adapter2);
        favoritecourse=(TextView) v.findViewById(R.id.favoriteCourse);

        adapter2.setOnItemClicklistener(new OnFCItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(ProfileAdapter2.ViewHolder holder, View view, int position) {
                String example = adapter2.getItem(position).getFavoriteSite();

                Intent intent = new Intent(getContext(),CustomDialogMap.class);
                intent.putExtra("title",example);
                startActivity(intent);
            }
        });
        return v;

    }
    public void onResume() {

        super.onResume();
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.detach(this).attach(this).commit();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    // 좋아요 연결
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        //@Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   progressDialog = ProgressDialog.show(,
            //           "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
            Log.d(TAG, "get my page my review response - " + result);

            if (result == null){
            }
            else {
                mJsonString = result;
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0]; // 그 유적지 이름 받아오는 함수 있어야함
            String searchKeyword1 = params[1];

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
                Log.d(TAG, "get mypage my review2 response code - " + responseStatusCode);
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
                //String text = "/select.php";
                //if(serverURL.contains(text)) {
                //chk = sb.toString().contains(his_name);
                //}
                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "Error ", e);
                errorString = e.toString();
                return null;
            }

        }
    }

    public void showResult(String result){
        try {
            Log.d(TAG, "all" + result);

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String review_type = item.getString("review_type");
                String course = item.getString("course_code");
                String historic = item.getString("historic_num");
                String review = item.getString("review");

                if(review_type=="유적지"){
                   // data.setTag_color(R.color.site_pink);
                }

                data.add(new ProfileSampleDataFour(review_type,course,historic,review));
                //return items;

            }
            adapter4.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showResult2(String result){
        //String[] c2 = new String[5]; // 경유지 넣기 위해 필요한 배열
        try {
            Log.d(TAG, "all" + result);

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String strr = null;
                String course = item.getString("MY_COURSE");
                course = course.substring(0, course.length()-1);

                String c[] = course.split("-");
                for(int j=1; j<c.length-2; j++){
                    strr = String.join("-",c[j]);

                }
                //코스 받아온거 분리해서 출발지랑 도착지 정하는거 해야함
                //여기서 총 소요시간 받아오는 클래스 객체 생성해서 받아오깅

                //출발지, 도착지, 전체 코스 넣깅
                data2.add(new ProfileSampleDataThree(c[0],c[c.length-1],course));
                //return items;

            }
            adapter3.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }


}