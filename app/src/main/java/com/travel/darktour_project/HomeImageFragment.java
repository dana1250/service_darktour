package com.travel.darktour_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class HomeImageFragment extends Fragment {

    Bundle args;
    Bundle name;
    @SuppressLint("Range")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        ImageView imageView = view.findViewById(R.id.main_image);
        TextView textView = view.findViewById(R.id.name);

        Drawable alpha = view.findViewById(R.id.name_layout).getBackground();
        alpha.setAlpha(150); // 유적지 이름 세팅하는 곳 불투명하게 함

        if (getArguments() != null) { // HomeFragment에서 받아온 값이 null이 아닌 경우
            args = getArguments(); // args - 유적지 이미지 받기
            name = getArguments(); // name - 유적지 이름 받기
            // HomeFragment에서 받아온 Resource를 ImageView에 셋팅
            Glide.with(this).load(args.getString("imgRes")).into(imageView);
            textView.setText(args.getString("nameRes"));
        }

        imageView.setOnClickListener(new View.OnClickListener() { // 추천된 이미지 클릭 이벤트
            @Override
            public void onClick(View v) {
                Log.d("Debug", args.getString("nameRes"));
                Intent intent = new Intent(getActivity(), DetailPage.class); // 유적지 상세페이지로 이동
                intent.putExtra("historyname",args.getString("nameRes")); // 코스이름을 nameRes라는 키값에 저장하여 DetailPage로 넘김
                startActivity(intent);
            }
        });
        return view;
    }
}