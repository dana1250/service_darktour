package com.travel.darktour_project;
// 윤지 플로팅 버튼 누르면 뜨는 화면
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// 좋아하는 유적지
public class FavoriteSite extends AppCompatActivity implements View.OnClickListener {
    private FavoriteRecyclerAdapter adapter; // recyclerview adapter
    String[] titleNumArr; // 유적지 이름 저장 arr
    String[] contentNumArr; // 설명 저장 arr
    String[] image_data; // image
    String[] likeArr; // 좋아요
    String location; // 지역
    String transportation; // 이동수단
    String checked_ai; // ai 추천 여부
    String[] x; // 경도
    String[] y; // 위도
    
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_site);
        Intent intent = getIntent(); // 데이터 수신
        titleNumArr = intent.getStringArrayExtra("select_title"); // title
        contentNumArr = intent.getStringArrayExtra("select_content"); // 설명
        location = intent.getStringExtra("location"); // 지역
        transportation = intent.getStringExtra("transportation"); // 이동수단
        checked_ai = intent.getStringExtra("ai"); // ai
        x = intent.getStringArrayExtra("longitude"); // 경도
        y = intent.getStringArrayExtra("latitude"); // 위도
        image_data = intent.getStringArrayExtra("image"); // 이미지
        likeArr = intent.getStringArrayExtra("like"); // 좋아요

        TextView location_name = findViewById(R.id.location);
        location_name.setText(location);
        TextView transportation_ = findViewById(R.id.transportation);
        transportation_.setText(transportation);
        TextView ai_ = findViewById(R.id.ai);
        ai_.setText(checked_ai);
        Button select_finish = findViewById(R.id.select_finish);
        select_finish.setOnClickListener(this);
        init();
        setData();

    }
    public void back_button_click(View v){
        super.onBackPressed();
    } // 뒤로가기
    private void init() {
        // recyclerview 세팅
        RecyclerView recyclerView = findViewById(R.id.favorite_site_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new FavoriteRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }
    public void setData(){
        for (int i = 0; i < titleNumArr.length; i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            FavoriteData data = new FavoriteData();

            data.setDesc(contentNumArr[i]); // 내용
            data.setTitle(titleNumArr[i]); // 유적지 이름
            data.setStart_back(R.drawable.ic_not_press_btn);
            data.setFinish_back(R.drawable.ic_not_press_btn);
            data.setStart_text(Color.parseColor("#647C8C"));
            data.setFinish_text(Color.parseColor("#647C8C"));
            data.setPress_start(false);
            data.setPress_finish(false);
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) { // 클릭 메소드
         switch (v.getId()){
             case R.id.select_finish:
                 int count = adapter.getButton_click_count();
                 if(count == 2){
                     Intent intent = new Intent(this, MakeCourse.class);
                     intent.putExtra("title",titleNumArr);
                     intent.putExtra("content",contentNumArr);
                     intent.putExtra("x",x);
                     intent.putExtra("y",y);
                     intent.putExtra("location",location); 
                     intent.putExtra("transportation",transportation);
                     intent.putExtra("start_finish_arr",adapter.getStart_finish_arr());
                     intent.putExtra("image",image_data);
                     intent.putExtra("likes",likeArr);

                     startActivity(intent);
                 }
                 else{
                     Toast.makeText(this, "출발지 도착지를 모두 설정해주세요!", Toast.LENGTH_SHORT).show();
                 }
                 break;

         }
    }
}


