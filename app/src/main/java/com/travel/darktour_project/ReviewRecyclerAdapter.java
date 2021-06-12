package com.travel.darktour_project;
// reviewrecycleradapter 리뷰 리사이클러뷰 윤지
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<ReviewData> listData = new ArrayList<>();
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    String mJsonString;
    ReviewData data = new ReviewData();
    private ItemViewHolder mContext;
    private Context mContext2;
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.

        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        mContext2 = parent.getContext();
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        ReviewData item = listData.get(position);
        holder.onBind(listData.get(position));
        holder.thumb_button.setImageResource(item.getThumb_image());
        holder.total_like.setText(item.getLike()); // 좋아요 숫자

    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }


    void addItem(ReviewData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }


    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView user_id;
        private TextView user_review;
        private TextView user_title; // 유적지 or 코스 이름
        private TextView total_like; // 따봉 숫자
        private TextView category; // 코스 인지 유적지인지 카테고리
        private ImageButton thumb_button; // 따봉 버튼
        private ImageView image; // 리뷰 사진
        String IP_ADDRESS = "113.198.236.105";
        Boolean clickBefore = false;
        boolean chk = false;
        String reviewnum;
        //String userid = PreferenceManager.getString(Rthis, "signup_id");
        String userid = PreferenceManager.getString(mContext2, "signup_id");


        ItemViewHolder(View itemView) {
            super(itemView);
            user_id = itemView.findViewById(R.id.user_id);
            user_review = itemView.findViewById(R.id.user_review);
            user_title = itemView.findViewById(R.id.title);
            total_like = itemView.findViewById(R.id.thumb_count);
            category = itemView.findViewById(R.id.tag);
            thumb_button = itemView.findViewById(R.id.thumb_button);


            thumb_button.setOnClickListener(this);
        }



        void onBind(ReviewData data) {
            user_id.setText(data.getId());


            Html.ImageGetter imgGetter = new Html.ImageGetter(){ // 옆에 책 모양 넣을라고

                public Drawable getDrawable(String source) {

                    //Drawable d = itemView.getResources().getDrawable(id);
                    Drawable d = ResourcesCompat.getDrawable(user_review.getResources(), R.drawable.more_img, null);

                    d.setBounds(10, 0,60,40);
                    //d.setBounds(0, 0, 30,30);
                    return d;

                }
            };

            //String userid = data.getId();
            reviewnum = data.getReview_num();
            Editlike editLike = new Editlike();
            Log.d(TAG, "alllolololo" + userid);
            editLike.execute("http://" + IP_ADDRESS + "/select.php","likereview", userid, reviewnum);

            try {
                //    //set time in mil
                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }

            /*
            try {
                String r = editLike.execute("http://" + IP_ADDRESS + "/select.php","likereview", userid, reviewnum).get();
                showLike(r);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             */


            String review =  data.getReview()+ "<img src=\"more_img\">";

            user_review.setText(Html.fromHtml(review,imgGetter,null));
            user_title.setText(data.getTitle());
            total_like.setText(data.getLike()); // 좋아요 숫자
            category.setBackgroundResource(data.getTag_color()); // 카테고리 색상
            category.setText(data.getCategory()); // 카테고리 이름
            thumb_button.setImageResource(data.getThumb_image()); // 따봉 이미지
            //image.setImageResource(data.getImage());// 리뷰 사진 이미지
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.thumb_button:
                    mContext = this;
                    clickBefore = listData.get(getAdapterPosition()).isPress();
                    if(clickBefore == false){ // 좋아요 눌렀을때
                        listData.get(getAdapterPosition()).setThumb_image(R.drawable.press_thumbs_up);
                        listData.get(getAdapterPosition()).setPress(true);
                        String re_num = listData.get(getAdapterPosition()).getReview_num();
                        String cata = listData.get(getAdapterPosition()).getCategory();
                        String contents = listData.get(getAdapterPosition()).getTitle();

                        // 좋아요 숫자 증가
                        InsertReviewCount insertcount = new InsertReviewCount();
                        insertcount.execute("http://" + IP_ADDRESS + "/update_review_plus.php", re_num);
                        // likereview 테이블에 user_id에 리뷰 넘버 넣기
                        Editlike editlikere = new Editlike();
                        //String userid = listData.get(getAdapterPosition()).getId();
                        editlikere.execute("http://" + IP_ADDRESS + "/insert.php", "likereview", userid, re_num);
                        // 코스 게시판에서 좋아요할 경우 likecourse에도 들어가야함
                        if(cata.equals("코스")){
                            Editlike editlikeco = new Editlike();
                            editlikeco.execute("http://" + IP_ADDRESS + "/insert.php", "likecourse", userid, contents);
                            InsertCourseCount insertcoursecount = new InsertCourseCount();
                            insertcoursecount.execute("http://" + IP_ADDRESS + "/update_count_plus.php", re_num);
                        }
                        // 좋아요 숫자 가져오기
                        InsertReviewCount select = new InsertReviewCount();
                        try {
                            String r = select.execute("http://" + IP_ADDRESS + "/select_review_count.php", re_num).get();
                            showResult(r);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //int num = Integer.parseInt(listData.get(getAdapterPosition()).getLike()) + 1 ; // 좋아요 숫자 변경
                        //listData.get(getAdapterPosition()).setLike(Integer.toString(num)); //  좋아요 숫자 설정
                        notifyItemChanged(getAdapterPosition());

                        //String ho = data.getCategory();

                        //GetReview task = new GetReview();
                        //task.execute(ho);
                    }
                    else{
                        listData.get(getAdapterPosition()).setThumb_image(R.drawable.thumbs_up);
                        listData.get(getAdapterPosition()).setPress(false);

                        String re_num = listData.get(getAdapterPosition()).getReview_num();
                        String cata = listData.get(getAdapterPosition()).getCategory();
                        String contents = listData.get(getAdapterPosition()).getTitle();


                        // 좋아요 숫자 감소
                        InsertReviewCount insertcount = new InsertReviewCount();
                        insertcount.execute("http://" + IP_ADDRESS + "/update_review_minus.php", re_num);
                        // likereview 테이블에 user_id 통해서 review num 삭제
                        Editlike editlikere = new Editlike();
                        //String userid = listData.get(getAdapterPosition()).getId();
                        editlikere.execute("http://" + IP_ADDRESS + "/delete.php", "likereview", userid, re_num);
                        // 코스 게시판에서 좋아요 삭제할 경우 likecourse에서도 삭제되야 함
                        if(cata.equals("코스")){
                            Editlike editlikeco = new Editlike();
                            editlikeco.execute("http://" + IP_ADDRESS + "/delete.php", "likecourse", userid, contents);
                            InsertCourseCount insertcoursecount = new InsertCourseCount();
                            insertcoursecount.execute("http://" + IP_ADDRESS + "/update_count_minus.php", re_num);

                        }
                        // 좋아요 숫자 가져오기
                        InsertReviewCount select = new InsertReviewCount();
                        try {
                            String r = select.execute("http://" + IP_ADDRESS + "/select_review_count.php", re_num).get();
                            showResult(r);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //int num = Integer.parseInt(listData.get(getAdapterPosition()).getLike()) - 1 ; // 좋아요 숫자 변경
                        //listData.get(getAdapterPosition()).setLike(Integer.toString(num)); //  좋아요 숫자 설정
                        notifyItemChanged(getAdapterPosition());
                    }

            }
        }

        // 인기있는 유적지 3개 받아와서 띄우는거
        public void showResult(String result){
            try {
                Log.d(TAG, "all" + result);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i<jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    String count_review = item.getString("count_review");

                    //int num = Integer.parseInt(listData.get(getAdapterPosition()).getLike()) + 1 ; // 좋아요 숫자 변경
                    clickBefore = false;
                    listData.get(getAdapterPosition()).setLike(count_review); //  좋아요 숫자 설정
                    notifyItemChanged(getAdapterPosition());
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }

        // 인기있는 유적지 3개 받아와서 띄우는거
        /*
        public void showLike(String result){
            try {
                Log.d(TAG, "all" + result);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i<jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    String count_review = item.getString("count_review");
                    Log.d(TAG, "DB에서 가져온 리뷰 좋아요 숫자아아아아아: " + count_review);
                    //int num = Integer.parseInt(listData.get(getAdapterPosition()).getLike()) + 1 ; // 좋아요 숫자 변경
                    listData.get(getAdapterPosition()).setLike(count_review); //  좋아요 숫자 설정
                    notifyItemChanged(getAdapterPosition());
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
         */

        // 좋아요 연결
        private class Editlike extends AsyncTask<String, Void, String>{

            ProgressDialog progressDialog;
            String errorString = null;

            /*
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(mContext,
                        "Please Wait", null, true, true);
            }
             */

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                //progressDialog.dismiss();
                Log.d(TAG, "response - " + result);
            }

            @Override
            protected String doInBackground(String... params) {

                String serverURL = params[0]; // 그 유적지 이름 받아오는 함수 있어야함
                String TABLE = params[1]; // 그 유적지 이름 받아오는 함수 있어야함
                String USER_ID = params[2]; // 그 유적지 이름 받아오는 함수 있어야함
                String CONTENT = params[3]; // 그 유적지 이름 받아오는 함수 있어야함

                String postParameters = "TABLENAME=" + TABLE + "&USER_ID=" + USER_ID+ "&CONTENT=" + CONTENT;
                Log.d("editlike Param : ", postParameters);

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
                    String text = "/select.php";
                    if(serverURL.contains(text)) {
                        //data.setThumb_image(R.drawable.thumbs_up);// 따봉
                        chk = sb.toString().contains(reviewnum);
                        if(sb.toString().contains(reviewnum)){
                            listData.get(getAdapterPosition()).setThumb_image(R.drawable.press_thumbs_up);
                            listData.get(getAdapterPosition()).setPress(true);

                        }
                        else{
                            listData.get(getAdapterPosition()).setThumb_image(R.drawable.thumbs_up);
                            listData.get(getAdapterPosition()).setPress(false);

                        }

                        notifyItemChanged(getAdapterPosition());

                        //thumb_button.setImageResource(R.drawable.press_thumbs_up); // 따봉 이미지

                    }
                    //String text = "/select.php";
                    //if(serverURL.contains(text)) {
                    //    chk = sb.toString().contains(his_name);
                    //}

                    return sb.toString().trim();

                } catch (Exception e) {
                    Log.d(TAG, "InsertData: Error ", e);
                    errorString = e.toString();
                    return null;
                }
            }
        }
    }

    public ReviewData getItem(int position){
        return listData.get(position); }


}



