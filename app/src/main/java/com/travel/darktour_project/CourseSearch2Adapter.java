package com.travel.darktour_project;
// 코스 선택 spinner2 (교통) adapter 윤지

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class CourseSearch2Adapter  extends BaseAdapter {


    Context context;
    List<String> data;
    LayoutInflater inflater;


    public CourseSearch2Adapter(Context context, List<String> data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.course_search_spinner2_normal, parent, false);
        }

        if(data!=null){
            //데이터세팅
                String text = data.get(position);


                if (position == data.size() -1){ // hint
                    ((TextView)convertView.findViewById(R.id.spinnerTrans)).setText("");
                    ((TextView)convertView.findViewById(R.id.spinnerTrans)).setHint(text);
                }
                else{
                    ((TextView)convertView.findViewById(R.id.spinnerTrans)).setText(text);
                    if (position == 0){ // 대중교통 사진
                        ((ImageView)convertView.findViewById(R.id.img)).setImageResource(R.drawable.ic_bus);
                    }
                    else if(position == 1){ // 자동차 사진
                        ((ImageView)convertView.findViewById(R.id.img)).setImageResource(R.drawable.ic_car);
                    }
                    else if (position == 2){ // 도보 사진
                        ((ImageView)convertView.findViewById(R.id.img)).setImageResource(R.drawable.ic_human);
                    }
                }



            }



        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) { // dropdownview
        if(convertView==null){
            convertView = inflater.inflate(R.layout.course_search_spinner2_dropdown, parent, false);
        }

        //데이터세팅
        String text = data.get(position);
        ((TextView)convertView.findViewById(R.id.spinnerTrans)).setText(text);


        return convertView;
    }

    @Override
    public int getCount() {
        if(data!=null) return data.size()-1;
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}


