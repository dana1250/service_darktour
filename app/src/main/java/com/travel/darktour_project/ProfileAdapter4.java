package com.travel.darktour_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ProfileAdapter4 extends RecyclerView.Adapter<ProfileAdapter4.ViewHolder> {

    private ArrayList<ProfileSampleDataFour> reviews;
    private Context context;
    private static String TAG = "오렐렐";


    public void setContext(Context context) {
        this.context = context;
    }
    public void setData(ArrayList<ProfileSampleDataFour> list){
        reviews = list;
    }

    @NonNull
    @Override
    public ProfileAdapter4.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myreview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter4.ViewHolder viewHolder, int position) {

        final ProfileSampleDataFour  r = reviews.get(position);

        /*Glide.with(viewHolder.itemView.getContext())
                .load(item.getUrl())
                .into(viewHolder.ivMovie);*/


        // 변수 선언해서 getType 받은게 유적지이면 getHis 하고 코스이면 getCourse하기
        //그리고 빨간색 파란색 상자 들고올수 있으면 들고오기
        String type = r.getType();

        if(type.equals("유적지")){
            viewHolder.coursename.setText(r.getHis());
            viewHolder.date.setBackgroundResource(R.color.site_pink);

            //viewHolder.date.setBackgroundResource(R.color.site_pink); //이거 한주 다 먹히넴..?
        }
        else if(type.equals("코스")){
            String course = r.getCourse();
            course = course.substring(0, course.length()-1);
            viewHolder.coursename.setText(course);
            viewHolder.date.setBackgroundResource(R.color.course_blue);
            //viewHolder.date.setBackgroundResource(R.color.course_blue);
        }
        viewHolder.date.setText(r.getType());
        viewHolder.contents.setText(r.getReview());


    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    //public void setItems(ArrayList<Profile4_likehis> items) {
        //this.reviews = items;
    //}

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, coursename,contents;

        ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.traveldate);
            coursename = itemView.findViewById(R.id.coursename);
            contents = itemView.findViewById(R.id.coursereview);
        }
    }
}