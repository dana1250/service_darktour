package com.travel.darktour_project;

import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfileAdapter2 extends RecyclerView.Adapter<ProfileAdapter2.ViewHolder>implements OnFCItemClickListener {

    ArrayList<Profile2> items = new ArrayList<>();
    OnFCItemClickListener listener;
    @NonNull
    @Override
    public ProfileAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.interestcourse_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Profile2 item = items.get(position);
        String str = item.getFavoriteSite();
        viewHolder.favoriteCourse.setText(str);
        viewHolder.favoriteCourse.setSelected(true);
        viewHolder.favoriteCourse.setSingleLine();
        viewHolder.favoriteCourse.setMarqueeRepeatLimit(-1);
        viewHolder.favoriteCourse.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void setOnItemClicklistener(OnFCItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener!=null){
            listener.onItemClick(holder, view, position);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView favoriteCourse;


        ViewHolder(View itemView) {
            super(itemView);
            favoriteCourse=itemView.findViewById(R.id.favoriteCourse);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.onItemClick(ViewHolder.this, v, position);

                    }
                }
            });

        }
        }
    public void addItem(Profile2 item) {
        items.add(item);
    }
    public void setItems(ArrayList<Profile2> items){
        this.items=items;
    }
    public Profile2 getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, Profile2 item){
        items.set(position,item);
    }
}
