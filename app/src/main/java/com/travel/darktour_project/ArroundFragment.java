package com.travel.darktour_project;
// 윤지 상세페이지 주변 정보 프래그먼트
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;


public class ArroundFragment extends Fragment {
    String getPhone []; //전화번호 저장용
    String getPlace_name []; // 음식점 이름
    String getX []; // x
    String getY [] ; // y
    String getCategory[] ; // 음식점 카테고리 이름
    String getUrl[]; // url

    List<String> getplace;
    List<String> getPhone_num;
    List<String> getCategory_food;
    List<String> getlon; // x
    List<String> getlat; // y
    List<String> getUrl_;
    private FoodRecyclerAdapter adapter;
    TextView total;
    View v;
    String his_name;
    private String lon;
    private String lat;

    public ArroundFragment(String x, String y,String his_name){ // 생성자
        lon = x;
        lat = y;
        this.his_name = his_name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_arround, container, false);
        total = v.findViewById(R.id.move_review);
        total.setOnClickListener(new View.OnClickListener() { // 음식점 전체보기 클릭
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodMap.class);
                intent.putExtra("center_lon",lon); // 유적지 lon
                intent.putExtra("center_lat",lat); // 유적지 lat
                intent.putExtra("is_all","total");
                intent.putExtra("center_name",his_name); //유적지 이름 (중심)
                startActivity(intent);
            }
        });
        init();
        NetworkThread thread = new NetworkThread();
        thread.start();
        return v;
    }
    private void init() {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new FoodRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClicklistener(new OnItemClickListener() {
            @Override public void onItemClick(FoodRecyclerAdapter.ItemViewHolder holder, View view, int position) {

                ArroundData item = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), FoodMap.class);

                // 이부분 수정*******************
                intent.putExtra("is_all","no_total");

                intent.putExtra("center_lon",lon); // 유적지 lon
                intent.putExtra("center_lat",lat); // 유적지 lat
                intent.putExtra("food_lon",item.getX()); // 음식점 lon
                intent.putExtra("food_lat",item.getY()); // 음식점 lat
                intent.putExtra("food_place",item.getTitle()); // 음식점 이름
                intent.putExtra("food_url",item.getUrl()); // 음식점 url
                intent.putExtra("center_name",his_name); //유적지 이름 (중심)
                startActivity(intent);


            } });



    }


    class NetworkThread extends Thread{
        @Override
        public void run() {
            try{
                String keyword = "category_group_code=FD6&page=1&size=15&x="+lon+"&y="+lat+"&sort=distance";
                // x 경도 y 위도

                String address = "https://dapi.kakao.com/v2/local/search/category.json?"+ keyword;

                URL url = new URL(address);
                //접속
                URLConnection conn = url.openConnection();
                //요청헤더 추가
                conn.setRequestProperty("Authorization","KakaoAK 7ce78d3c36644e24fc44fdc6afa0f7f2");

                //서버와 연결되어 있는 스트림을 추출한다.
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                StringBuffer buf = new StringBuffer();

                //읽어온다.
                do{
                    str = br.readLine();
                    if(str!=null){
                        buf.append(str);
                    }
                }while(str!=null);

                final String result = buf.toString();

                // 가장 큰 JSONObject를 가져옵니다.
                JSONObject jObject = new JSONObject(result);
                // 배열을 가져옵니다.
                JSONArray jArray = jObject.getJSONArray("documents");
                int list_cnt = jArray.length(); //Json 배열 내 JSON 데이터 개수를 가져옴
                //key의 value를 가져와 저장하기 위한 배열을 생성한다
                getPhone = new String[list_cnt]; //전화번호 저장용
                getPlace_name = new String[list_cnt]; // 음식점 이름
                getCategory = new String[list_cnt]; // 카테고리
                getUrl = new String[list_cnt]; // url
                getX = new String[list_cnt]; // x
                getY = new String[list_cnt]; // y


                // 배열의 모든 아이템
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject obj = jArray.getJSONObject(i);
                    getCategory[i] = obj.getString("category_name"); // 음식점 카테고리 이름
                    getPhone[i] = obj.getString("phone"); // 음식점 전화번호
                    getPlace_name[i] = obj.getString("place_name");// 음식점 이름.
                    getUrl[i] = obj.getString("place_url"); // 음식점 정보 제공 url
                    getX[i] = obj.getString("x"); //음식점 x
                    getY[i] = obj.getString("y"); // 음식점 y
                }
                // array를 list로 변환
                getplace = Arrays.asList(getPlace_name);
                getPhone_num = Arrays.asList(getPhone);
                getCategory_food = Arrays.asList(getCategory);
                getlon = Arrays.asList(getX);
                getlat = Arrays.asList(getY);
                getUrl_ = Arrays.asList(getUrl);

                // 주변 마커 추가
                getActivity().runOnUiThread (new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < getplace.size(); i++) {
                            // 각 List의 값들을 data 객체에 set 해줍니다.
                            ArroundData data = new ArroundData();
                            data.setTitle(getplace.get(i));
                            data.setContent(getPhone_num.get(i));

                            data.setFood(getCategory_food.get(i));
                            data.setX(getlon.get(i));
                            data.setY(getlat.get(i));

                            data.setUrl(getUrl_.get(i));
                            // 각 값이 들어간 data를 adapter에 추가합니다.
                            adapter.addItem(data);
                        }

                        // adapter의 값이 변경되었다는 것을 알려줍니다.
                        adapter.notifyDataSetChanged();
                    }

                });

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}



