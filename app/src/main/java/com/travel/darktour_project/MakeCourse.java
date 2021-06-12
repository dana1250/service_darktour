package com.travel.darktour_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

// 윤지 코스 만든 화면
public class MakeCourse extends AppCompatActivity {
    String[] titleNumArr; // 유적지 이름 저장 arr
    String[] contentNumArr; // 설명 저장 arr
    String[] x; // 경도
    String[] y; // 위도
    String[] image_data; // image
    String location; // 지역
    int[] start_finish_arr; // 시작 도착지 좌표
    String[] likeArr; // 좋아요
    String transportation; // 대중교통
    CarFrag carfrag;
    PublicFrag publicfrag;
    RoadFrag roadfrag;
    RecyclerView detail_view;
    LinearLayoutManager mLayoutManager;
    Vertical_Adapter mAdapter;
    ArrayList finish_course = new ArrayList<String>(); // 만들어진 코스
    ImageView transportimage; // 날씨 사진
    boolean click_check = false;

    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makecourse);
        transportimage = (ImageView)findViewById(R.id.transport);
        mContext = this;


        Intent intent = getIntent(); // 데이터 수신
        titleNumArr = intent.getStringArrayExtra("title"); // title
        contentNumArr = intent.getStringArrayExtra("content"); // 설명
        x = intent.getStringArrayExtra("x"); // 경도
        y = intent.getStringArrayExtra("y"); // 위도
        location = intent.getStringExtra("location"); // 지역
        transportation = intent.getStringExtra("transportation"); // 대중교통
        start_finish_arr = intent.getIntArrayExtra("start_finish_arr"); // 시작-0 도착-1
        image_data = intent.getStringArrayExtra("image"); // 이미지
        likeArr = intent.getStringArrayExtra("likes"); // 좋아요



        make_course(); // 코스 생성

        TextView location_name = findViewById(R.id.location);
        location_name.setText(location);

        carfrag = new CarFrag(); //프래그먼트 객채셍성
        publicfrag = new PublicFrag(); //프래그먼트 객채셍성
        roadfrag = new RoadFrag(); //프래그먼트 객채셍성
        detail_view = findViewById(R.id.recycler_detail); // 자세한 유적지
        ArrayList<Vertical_Data> data = new ArrayList<>();
        setImage_transport(); // 이동수단 사진 설정
        // init LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        // setLayoutManager
        detail_view.setLayoutManager(mLayoutManager);
        // init Adapter
        mAdapter = new Vertical_Adapter();
        // set Data
        mAdapter.setData(data);
        // set Adapter
        detail_view.setAdapter(mAdapter);
        // 코스 data 추가
        for(int i =0; i< titleNumArr.length; i++){
            data.add(new Vertical_Data( image_data[i], titleNumArr[i],likeArr[i],contentNumArr[i]));
        }

        switch (transportation){
            case "자동차":
                setFrag(0);
                break;
            case "대중교통":
                setFrag(1);
                break;
            case "도보":
                setFrag(2);
                break;
        }


    }

    public void setFrag(int n){    //프래그먼트를 교체하는 작업을 하는 메소드를 만들었습니다
        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putStringArray("title", titleNumArr); // 유적지 이름
        bundle.putStringArray("x", x); // x
        bundle.putStringArray("y", y); // y
        bundle.putIntArray("start_finish_arr", start_finish_arr); // 출발지 도착지 array

        switch (n){
            case 0:
                carfrag.setArguments(bundle);
                transaction.replace(R.id.main_frame, carfrag);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                transaction.commit();
                break;
            case 1:
                publicfrag.setArguments(bundle);
                transaction.replace(R.id.main_frame, publicfrag);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                transaction.commit();
                break;
            case 2:
                roadfrag.setArguments(bundle);
                transaction.replace(R.id.main_frame, roadfrag);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                transaction.commit();
                break;
        }
    }
    public void make_course(){ // 코스 생성

        StringBuffer course = new StringBuffer(); // 코스 저장되는 변수
        finish_course.add(titleNumArr[start_finish_arr[0]]); // 출발지 추가
        for(int i =1; i <titleNumArr.length-1; i++){
            finish_course.add(titleNumArr[i]); // 중간 경유지 추가
        }
        finish_course.add(titleNumArr[start_finish_arr[1]]); // 도착지 추가

        for(int i=0; i<finish_course.size(); i++){
            course.append(finish_course.get(i)+ "-");
        }

        String mycourse = course.toString();
        String IP_ADDRESS = "113.198.236.105";
        InsertMycourse insertcourse = new InsertMycourse();
        insertcourse.execute("http://" + IP_ADDRESS + "/insert_course.php", mycourse);
        String USER_ID = PreferenceManager.getString(mContext, "signup_id");


        UpdateMycourseMypage updatemypage = new UpdateMycourseMypage();
        updatemypage.execute("http://" + IP_ADDRESS + "/insert_mycourse.php", USER_ID, mycourse);
    }

    public void setImage_transport(){ // 대중교통 이미지 추가
        switch (transportation){
            case "자동차":
                transportimage.setImageResource(R.drawable.ic_car);
                break;
            case "대중교통":
                transportimage.setImageResource(R.drawable.ic_bus);
                break;
            case "도보":
                transportimage.setImageResource(R.drawable.ic_human);
                break;
            default:
                break;
        }


    }
    public void back_button_click(View v){
        super.onBackPressed();
    } // 뒤로가기
    // 카드뉴스
    class Vertical_Adapter extends RecyclerView.Adapter<VerticalView_Holder> {

        private ArrayList<Vertical_Data> vertical_Datas;
        private Context context;

        public void setContext(Context context) {
            this.context = context;
        }
        public void setData(ArrayList<Vertical_Data> list){
            vertical_Datas = list;
        }

        @Override
        public VerticalView_Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 사용할 아이템의 뷰를 생성해준다.
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.detail_item, parent, false);

            VerticalView_Holder holder = new VerticalView_Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(VerticalView_Holder holder, int position) {
            final Vertical_Data data = vertical_Datas.get(position);

            // setData


            Glide.with(MakeCourse.this).asBitmap().load(data.getImg()).
                    into(holder.icon);
            holder.name.setText(data.getArea_name());
            holder.likes.setText(data.getLike());
            holder.desc.setText(data.getExplain_());


            // setOnClick
            holder.touch_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.d("Debug", data.getArea());
                    Intent intent = new Intent(getApplicationContext(), DetailPage.class);

                    intent.putExtra("historyname",data.getArea_name());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return vertical_Datas.size();
        }
    }

    class VerticalView_Holder extends RecyclerView.ViewHolder {


        public ImageView icon;
        public TextView likes;
        public TextView name;
        public TextView desc;
        public LinearLayout touch_back;
        public VerticalView_Holder(View itemView) {
            super(itemView);


            icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
            name = (TextView) itemView.findViewById(R.id.site_name);
            name.setSingleLine();
            name.setMarqueeRepeatLimit(-1);
            name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            name.setSelected(true);
            likes = (TextView) itemView.findViewById(R.id.likes_count);
            desc = (TextView) itemView.findViewById(R.id.explain); //설명
            touch_back = (LinearLayout) itemView.findViewById(R.id.touch_back); // 뒷배경
        }
    }
    //유적지 data
    class Vertical_Data {


        private String img;
        private String area_name; // 유적지 이름
        private String like; // 좋아요 수
        private String explain_; // 설명

        public Vertical_Data(String img, String area_name,String like,String explain_ ) {

            this.img = img;
            this.area_name = area_name;
            this.like = like;
            this.explain_ = explain_;
        }

        public String getArea_name() {
            return area_name;
        }

        public String getImg() {
            return img;
        }

        public String getLike() {
            return like;
        }

        public String getExplain_() {
            return explain_;
        }
    }
}