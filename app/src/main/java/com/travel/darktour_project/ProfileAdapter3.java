package com.travel.darktour_project;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ProfileAdapter3 extends RecyclerView.Adapter<ProfileAdapter3.ViewHolder> {

    private ArrayList<ProfileSampleDataThree> mycourse;
    private Context context;
    private static String TAG = "마이페이지 나의코스";

    public void setContext(Context context) {
        this.context = context;
    }
    public void setData(ArrayList<ProfileSampleDataThree> list){
        mycourse = list;
    }

    @NonNull
    @Override
    public ProfileAdapter3.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycourse_itme, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter3.ViewHolder viewHolder, int position) {

        final ProfileSampleDataThree c = mycourse.get(position);
        /*Glide.with(viewHolder.itemView.getContext())
                .load(item.getUrl())
                .into(viewHolder.ivMovie);*/

        viewHolder.startlocal.setText(c.getStart()); // 출발지
        viewHolder.finishlocal.setText(c.getFinish()); // 소요시간
        viewHolder.time.setText(c.getTime()); // 도착지
        viewHolder.time.setSelected(true);
        viewHolder.time.setSingleLine();
        viewHolder.time.setMarqueeRepeatLimit(-1);
        viewHolder.time.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    }

    @Override
    public int getItemCount() {
        return mycourse.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView startlocal, finishlocal, time;

        ViewHolder(View itemView) {
            super(itemView);

            startlocal = itemView.findViewById(R.id.startlocal);
            finishlocal = itemView.findViewById(R.id.finishlocal);
            time = itemView.findViewById(R.id.traffictime);

        }
    }
}