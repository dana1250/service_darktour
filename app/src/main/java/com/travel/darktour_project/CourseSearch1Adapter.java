package com.travel.darktour_project;
// 코스 선택 spinner1 (지역) adapter 윤지

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class CourseSearch1Adapter extends BaseAdapter {


    Context context;
    List<String> data;
    LayoutInflater inflater;


    public CourseSearch1Adapter(Context context, List<String> data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        if(data!=null) return data.size();
        else return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.course_search_spinner1_normal, parent, false);
        }

        if(data!=null){
            //데이터세팅
            String text = data.get(position);
            ((TextView)convertView.findViewById(R.id.spinnerLocation)).setText(text);
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) { // dropdownview
        if(convertView==null){
            convertView = inflater.inflate(R.layout.course_search_spinner1_dropdown, parent, false);
        }

        //데이터세팅
        String text = data.get(position);
        ((TextView)convertView.findViewById(R.id.spinnerLocation)).setText(text);

        return convertView;
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


