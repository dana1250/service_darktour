package com.travel.darktour_project;
// 메인홈화면
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {
    //String url = "https://mblogthumb-phinf.pstatic.net/MjAxOTA3MDFfMzcg/MDAxNTYxOTQxODYzMjIz.nQXA6YgLuEERXIREJ8-e4moXBBGUPkood1szLuv7rGIg.61D5segxlQpIbExqQIlkzYz0NzToReDAfNrx_QmEOy4g.JPEG.mypetparty/delfi-de-la-rua-R-hcT_Svk8Y-unsplash.jpg?type=w800";
    View v;
    ViewPager viewPager;
    Timer timer;
    ArrayList<String> listImage;
    ArrayList<String> listname;

    int currentPage = 0;
    final long DELAY_MS = 3000; // 오토 플립용 타이머 시작 후 해당 시간에 작동(초기 웨이팅 타임) ex) 앱 로딩 후 3초 뒤 플립됨.
    final long PERIOD_MS = 5000; // 5초 주기로 작동
    CircleIndicator indicator; // 이미지 인디케이터
    RecyclerView mVerticalView;
    RecyclerView mVerticalView2;
    VerticalAdapter mAdapter;
    VerticalAdapter mAdapter2;
    LinearLayoutManager mLayoutManager;
    LinearLayoutManager mLayoutManager2;

    TextView textView; // 글씨굵기
    TextView textView2;
    TextView textView3;
    int page;
    Bundle bundle;

    GetData task2 = new GetData();
    GetData random = new GetData();
    GetData2 task3 = new GetData2();
    GetData2 task4 = new GetData2();
    GetData2 task5 = new GetData2();



    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    String mJsonString;

    ArrayList<VerticalData> data = new ArrayList<>();
    ArrayList<VerticalData> data2 = new ArrayList<>();

    private Context mContext;
    String random_his="a-";
    String[] arr = new String[3];//랜덤유적지 뽑는 배열
    String[] course = new String[3];//코스 저장하는 배열도 필요하넴
    String[] image = new String[6];//이미지 URI 저장하는 배열
    String[] name = new String[6];//유적지 이름 저장하는 배열

    String course_historic_image;
    String course_incident;
    String course_contents; // 코스 내용

    String IP_ADDRESS = "113.198.236.105";
    String[] cth = {"null","null","null","null","null","null"};


    //코스 내용 받아와서 분리하고 코스 중에서 랜덤 유적지 선택하는거
    public void showRandom(String result){
        try {
            Log.d(TAG, "all" + result);

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                course_contents= item.getString("contents");
                course[i]=course_contents;
                String c[] = course_contents.split("-");
                int random = (int) ((Math.random()*c.length-1)+0);

                arr[i] = c[random];
                cth[i]=c[i];
                //Random r = new Random(c[random]);
                //task2.cancel(true);
                //task3.execute(c[random]);
                //task3.execute(c[random]);
                //random_his.concat(c[random]+"-");

            }
        } catch (JSONException e) {
            Log.d(TAG, "showRandomResult : ", e);
        }
    }

    // 인기있는 유적지 3개 받아와서 띄우는거
    public void showhistoric(String result){
        try {
            Log.d(TAG, "all" + result);

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString("name");
                String incident = item.getString("incident");
                String his_image = item.getString("his_image");

                data2.add(new VerticalData(Integer.toString(i+1), his_image, incident, name));
            }
            mAdapter2.notifyDataSetChanged();


        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    // 받아온 결과값 나누는거 - 제일 위에 카드뉴스 이미지 랜덤하게 띄움
    public void showRandomImage(String result){
        try {
            Log.d(TAG, "all" + result);

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String his_image = item.getString("his_image");
                String his_name = item.getString("name");
                //listImage = new ArrayList<>(); // viewpager 이미지 추가
                //listImage.add(his_image);
                image[i]=his_image;
                name[i]=his_name;

            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private void showCourseImage(String result){
        try {
            Log.d(TAG, "all" + result);

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                course_historic_image = item.getString("his_image");
                course_incident = item.getString("incident");


            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            String IP_ADDRESS = "113.198.236.105";
            String image = random.execute("http://" + IP_ADDRESS + "/select_image_random.php").get();

            showRandomImage(image);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mContext = getActivity();
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_home, container, false);

        listImage = new ArrayList<>();
        listname = new ArrayList<>();
        for(int i=0; i<image.length;i++){ // DB에서 랜덤으로 받아온 image의 갯수만큼 추가
            listImage.add(image[i]); // viewpager 이미지 추가
            listname.add(name[i]); // viewpager 유적지 이름 추가
        }

        viewPager = v.findViewById(R.id.mainhome_viewpager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getFragmentManager());
        // ViewPager와  FragmentAdapter 연결
        viewPager.setAdapter(fragmentAdapter);

        for (page = 0; page < listImage.size(); page++) { // FragmentAdapter에 Image 개수만큼 fragment 추가
            HomeImageFragment imageFragment = new HomeImageFragment();
            bundle = new Bundle();
            bundle.putString("imgRes", listImage.get(page)); // 유적지 이미지 키값에 추가
            bundle.putString("nameRes", listname.get(page)); // 유적지 이름 키값에 추가
            imageFragment.setArguments(bundle); // HomeImageFragment에 bundle에 저장된 값 보내기
            fragmentAdapter.addItem(imageFragment);
        }
        fragmentAdapter.notifyDataSetChanged(); // adapter 변경


        // md 추천 코스 인디케이터
        indicator = v.findViewById(R.id.homeindi);
        indicator.setViewPager(viewPager);


        //// 많이 추천된 코스
        mVerticalView = v.findViewById(R.id.home_recycler);
        // init LayoutManager
        mLayoutManager = new LinearLayoutManager(v.getContext());
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
        //data.add(new VerticalData("1", Integer.toString(R.drawable.seoul), "[서울]","을사늑약 체결"));
        //data.add(new VerticalData("2", Integer.toString(R.drawable.jeju), "[제주]","제주 4.3 사건"));
        //data.add(new VerticalData("3", Integer.toString(R.drawable.busan), "[부산]","부산민주공원"));

        //task2.execute();
        try {
            String IP_ADDRESS = "113.198.236.105";
            String result = task2.execute("http://" + IP_ADDRESS + "/select_pop_course.php").get();
            //task2.cancel(true);

            showRandom(result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //코스의 유적지별로 정보 가져오기기
        for(int i=0;i<course.length;i++){
            String c[] = course[i].split("-");
            String cc[] = {"null","null","null","null","null"};
            for(int j=0; j<c.length; j++){
                if(c[j] != null){
                    cc[j]=c[j];
                }
            }


            GetData3 gg = new GetData3();
            gg.execute("http://" + IP_ADDRESS + "/course_to_his.php",cc[0],cc[1],cc[2],cc[3],cc[4]);
        }


        try {
            String r1 = task3.execute(arr[0]).get();
            showCourseImage(r1);
            data.add(new VerticalData(Integer.toString(1), course_historic_image, course_incident, course[0].substring(0, course[0].length()-1)));
            String r2 = task4.execute(arr[1]).get();
            showCourseImage(r2);
            data.add(new VerticalData(Integer.toString(2), course_historic_image, course_incident, course[1].substring(0, course[1].length()-1)));
            String r3 = task5.execute(arr[2]).get();
            showCourseImage(r3);
            data.add(new VerticalData(Integer.toString(3), course_historic_image, course_incident, course[2].substring(0, course[2].length()-1)));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //// 많이 추천된 유적지
        mVerticalView2 = v.findViewById(R.id.home_recycler2);

        //ArrayList<VerticalData> data2 = new ArrayList<>();

        // init LayoutManager
        mLayoutManager2 = new LinearLayoutManager(v.getContext());
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        // setLayoutManager
        mVerticalView2.setLayoutManager(mLayoutManager2);
        // init Adapter
        mAdapter2 = new VerticalAdapter();
        // set Data
        mAdapter2.setData(data2);
        // set Adapter
        mVerticalView2.setAdapter(mAdapter2);
        // 유적지 data 추가
        GetData task = new GetData();
        String IP_ADDRESS = "113.198.236.105";
        try {
            String result2 = task.execute("http://" + IP_ADDRESS + "/select_pop_historic.php").get();
            showhistoric(result2);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //data2.add(new VerticalData("1", R.drawable.seoul, "[서울]","덕수궁중명전"));
        //data2.add(new VerticalData("2", R.drawable.jeju, "[제주]","제주시 충혼묘지 4·3추모비"));
        //data2.add(new VerticalData("3", R.drawable.busan, "[부산]","부산민주공원"));


        //MD 글씨 굵게
        textView = v.findViewById(R.id.md_cos);
        String content = textView.getText().toString();
        SpannableString spannableString = new SpannableString(content);

        String word = "'MD'";
        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);

        //추천한 코스 글씨 굵게
        textView2 = v.findViewById(R.id.rank1);
        String content2 = textView2.getText().toString();
        SpannableString spannableString2 = new SpannableString(content2);

        String word2 = "추천한 코스";
        int start2 = content2.indexOf(word2);
        int end2 = start2 + word2.length();

        spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString2.setSpan(new StyleSpan(Typeface.BOLD), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString2.setSpan(new RelativeSizeSpan(1f), start2, end2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView2.setText(spannableString2);

        //추천한 유적지 글씨 굵게
        textView3 = v.findViewById(R.id.rank2);
        String content3 = textView3.getText().toString();
        SpannableString spannableString3 = new SpannableString(content3);

        String word3 = "추천한 유적지";
        int start3 = content3.indexOf(word3);
        int end3 = start3 + word3.length();

        spannableString3.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), start3, end3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new StyleSpan(Typeface.BOLD), start3, end3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new RelativeSizeSpan(1f), start3, end3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView3.setText(spannableString3);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // MD 추천
    static class FragmentAdapter extends FragmentPagerAdapter {

        // ViewPager에 들어갈 Fragment들을 담을 리스트
        private ArrayList<Fragment> fragments = new ArrayList<>();

        // 필수 생성자
        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        // List에 Fragment를 담을 함수
        void addItem(Fragment fragment) {
            fragments.add(fragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final int NUM_PAGES = listImage.size(); // 이미지의 총 갯수

        // Adapter 세팅 후 타이머 실행
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                currentPage = viewPager.getCurrentItem();
                int nextPage = currentPage + 1;

                if (nextPage >= NUM_PAGES) {
                    nextPage = 0;
                }
                viewPager.setCurrentItem(nextPage, true);
                currentPage = nextPage;
            }
        };

        timer = new Timer(); // thread에 작업용 thread 추가
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

    }

    @Override
    public void onPause() {
        super.onPause();
        // 다른 액티비티나 프레그먼트 실행시 타이머 제거
        // 현재 페이지의 번호는 변수에 저장되어 있으니 취소해도 상관없음
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // 사용자가 많이 추천한 코스와 유적지
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item, parent, false);
            VerticalViewHolder holder = new VerticalViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(VerticalViewHolder holder, int position) {
            final VerticalData data = verticalDatas.get(position);

            // setData
            holder.num.setText(data.getRank()); // 순위 설정
            Glide.with(HomeFragment.this).load(data.getImg()).into(holder.icon); // 이미지 설정
            holder.description.setText(data.getArea()); // 사건 설정
            holder.name.setText(data.getHistory()); // 코스 - 코스 유적지 설정, 유적지 - 유적지 이름 설정

            // 추천된 코스와 유적지를 클릭했을 경우
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String array[] = data.getHistory().split("-"); // 값을 '-' 단위로 잘라 array[]에 저장

                    if (array.length == 1) { // array의 길이가 1일 경우 = 추천 유적지
                        Intent intent = new Intent(getActivity(), DetailPage.class); // 유적지 상세 페이지로 이동
                        intent.putExtra("historyname",data.getHistory()); // 코스이름 DetailPage로 넘김
                        startActivity(intent);
                    }else{ // array의 길이가 1이 아닐 경우 = 추천 코스
                        Intent intent = new Intent(getContext(),CustomDialogMap.class); // 코스 정보 페이지로 이동, 다이얼로그 띄움
                        intent.putExtra("title",data.getHistory());
                        startActivity(intent);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return verticalDatas.size();
        }
    }

    class VerticalViewHolder extends RecyclerView.ViewHolder {

        public TextView num;
        public ImageView icon;
        public TextView description;
        public TextView name;

        public VerticalViewHolder(View itemView) {
            super(itemView);

            num = (TextView) itemView.findViewById(R.id.rank_num);
            icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
            description = (TextView) itemView.findViewById(R.id.horizon_description);
            name = (TextView) itemView.findViewById(R.id.horizon_description2);

        }
    }
    //사용자 추천 코스 순위 data
    class VerticalData {

        private String rank;
        private String img;
        private String area;
        private String history;


        public VerticalData(String rank, String img, String area, String history) {
            this.rank = rank;
            this.img = img;
            this.area = area;
            this.history = history;
        }

        public String getRank() { return this.rank;}

        public String getImg() {
            return this.img;
        }

        public String getArea() { return this.area; }

        public String getHistory() { return this.history; }
    }


    // 좋아요 연결
    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),
                    "메인화면 로딩 중..", "잠시만 기다려주세요!", true, true);
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

            String serverURL = params[0]; // 그 유적지 이름 받아오는 함수 있어야함

            try {

                URL url = new URL(serverURL);
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
                String text = "/select.php";
                if(serverURL.contains(text)) {
                    //chk = sb.toString().contains(his_name);
                }
                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
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
            progressDialog = ProgressDialog.show(getActivity(),
                    "메인화면 로딩 중..", "잠시만 기다려주세요!", true, true);

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
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0]; // 그 유적지 이름 받아오는 함수 있어야함
            String serverURL = "http://113.198.236.105/select_course_image.php";
            String postParameters = "WORD1=" + searchKeyword1;

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
    // 받아온 결과값 나누는거 랜덤 유적지 선택된거 이미지랑 관련사건 가져오는거
    private void showResult(){
        try {
            Log.d(TAG, "all" + mJsonString);

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                String his_image = item.getString("his_image");
                String incident = item.getString("incident");
                String name = item.getString("name");

                //data.add(new VerticalData(Integer.toString(i+1), his_image, incident, course[i]));
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    // DB 연결
    private class GetData3 extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),
                    "메인화면 로딩 중..", "잠시만 기다려주세요!", true, true);

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
                showResult5();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0]; // 그 유적지 이름 받아오는 함수 있어야함
            String searchKeyword1 = params[1]; // 그 유적지 이름 받아오는 함수 있어야함
            String searchKeyword2 = params[2];
            String searchKeyword3 = params[3];
            String searchKeyword4 = params[4];
            String searchKeyword5 = params[5];

            String postParameters = "WORD1=" + searchKeyword1 + "&WORD2=" + searchKeyword2 + "&WORD3=" + searchKeyword3
                    + "&WORD4=" + searchKeyword4 + "&WORD5=" + searchKeyword5;

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
    // 받아온 결과값 나누는거
    private void showResult5(){
        try {
            Log.d(TAG, "all" + mJsonString);

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                Double latitude = item.getDouble("latitude");
                Double longitude = item.getDouble("longitude");
                String name = item.getString("name");
                //data.add(new VerticalData(Integer.toString(i+1), his_image, incident, course[i]));
                //listImage.add(his_image);

            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}