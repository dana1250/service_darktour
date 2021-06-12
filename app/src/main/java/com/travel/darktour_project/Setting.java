package com.travel.darktour_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class Setting extends AppCompatActivity {

    Login login;
    Intent intent;
    LinearLayout linearLayout;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        mContext=this;

        ListView listview;
        SettingAdapter adapter;

        adapter = new SettingAdapter();

        listview = (ListView) findViewById(R.id.settinglist);
        listview.setAdapter(adapter); // listview에 adapter 연결
        linearLayout = findViewById(R.id.setting_back);

        adapter.addItem("로그아웃") ; // adapter에 item 추가
        adapter.addItem("공지사항") ;
        adapter.addItem("관심유적지 변경") ;

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { // listview를 클릭하였을 때
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) { // 설정 item을 클릭하였을 때
                // get item
                SettingItem item = (SettingItem) parent.getItemAtPosition(position);
                String titleStr = item.getTitle(); // item의 title 값 추가
                if (titleStr == "로그아웃") { // 값이 로그아웃인 경우
                    Toast.makeText(v.getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() { // 로그아웃에 성공한 경우
                            PreferenceManager.clear(mContext); //로그인 정보를 모두 날림
                            intent = new Intent(Setting.this, Login.class); // 로그인 화면으로 이동
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            boolean boo = PreferenceManager.getBoolean(mContext,"check"); //자동로그인 정보 기억하기 체크 유무 확인
                            if(boo){ // 체크가 되어있다면 아래 코드를 수행
                                // 저장된 아이디와 암호를 가져와 셋팅한다.
                                PreferenceManager.setBoolean(mContext,"check", false); //자동로그인 체크 false로 변경
                            }
                            startActivity(intent);
                        }
                    });
                } else if (titleStr == "관심유적지 변경") { // 값이 관심유적지인 경우
                    // Toast를 출력하고 관심유적지 선택 화면으로 이동
                    Toast.makeText(v.getContext(), "유적지를 다시 선택해주세요!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Setting.this, Interest.class);
                    // intent하면서 사용자 아이디 값 넘기기
                    intent.putExtra("사용자아이디", PreferenceManager.getString(mContext, "signup_id"));
                    startActivity(intent);
                } else if (titleStr == "공지사항") { // 값이 공지사항인 경우
                    intent = new Intent(Setting.this, Notice.class); // 공지사항 확인 화면으로 이동
                    startActivity(intent);
                }
            }
        }) ;
    }
}
