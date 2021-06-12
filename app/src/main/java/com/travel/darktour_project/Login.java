package com.travel.darktour_project;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Login extends AppCompatActivity {
    private ImageView kakaologinbutton;
    private LoginButton login;
    private Button loginbutton;
    private Button signupbutton;
    private EditText loginemail;
    private EditText loginpassword;
    private TextView login_email_eroor;
    private TextView login_pw_eroor;
    private CheckBox cb_save;
    private Context mContext;
    private SessionCallback sessionCallback;

    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;
    private static String TAG = "phpquerytest";

    private static final String TAG_JSON="login";
    private static final String TAG_ID = "id";
    private static final String TAG_PWD ="pwd";

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;

    FrameLayout kakao_frame;
    String kakaopw = "kakao";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = this;

        kakao_frame= (FrameLayout) findViewById(R.id.kakao_frame);
        kakaologinbutton = (ImageView) findViewById(R.id.kakao_login_bt);
        login = (LoginButton) findViewById(R.id.kakao_login);
        loginbutton = (Button) findViewById(R.id.login_bt);
        loginemail = (EditText) findViewById(R.id.signup_name);
        loginpassword = (EditText) findViewById(R.id.login_password);
        login_email_eroor = (TextView) findViewById(R.id.login_email_eroor);
        login_pw_eroor = (TextView) findViewById(R.id.login_pw_eroor);
        cb_save = (CheckBox) findViewById(R.id.autologin);

        boolean boo = PreferenceManager.getBoolean(mContext,"check"); //로그인 정보 기억하기 체크 유무 확인

        if(boo){ // 체크가 되어있다면 아래 코드를 수행
            // 저장된 아이디와 암호를 가져와 셋팅한다.
            loginemail.setText(PreferenceManager.getString(mContext, "signup_id"));
            loginpassword.setText(PreferenceManager.getString(mContext, "pw"));
            if (loginemail.getText().toString().equals("") || loginpassword.getText().toString().equals("")) {
                loginpassword.setText("");
                loginemail.setText("");
                Log.d("loginemail", loginemail.getText().toString());
                Log.d("loginpassword", loginpassword.getText().toString());
                cb_save.setChecked(false);
            } else {
                GetData task = new GetData();
                task.execute( loginemail.getText().toString(), loginpassword.getText().toString());
                cb_save.setChecked(true); //체크박스는 여전히 체크 표시 하도록 셋팅
            }
        }

        sessionCallback = new SessionCallback(); // 카카오 로그인 요청
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

        // 카카오 로그인 버튼을 눌렀을 때
        kakaologinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.performClick(); // login 클릭 이벤트 실행
            }
        });

        //로그인 버튼을 눌렀을 때
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //아이디, 암호 입력창에서 텍스트를 가져와 PreferenceManager에 저장함
                PreferenceManager.setString(mContext, "signup_id", loginemail.getText().toString()); //id라는 키값으로 저장
                PreferenceManager.setString(mContext, "id", loginemail.getText().toString()); //id라는 키값으로 저장
                PreferenceManager.setString(mContext, "pw", loginpassword.getText().toString()); //pw라는 키값으로 저장

                // 아이디를 입력하지 않은 경우 에러 메시지 표시
                if (loginemail.getText().toString().length() == 0) { // 아이디의 길이가 0일 경우
                    login_email_eroor.setText("아이디를 입력하세요"); // 에러 메시지 표시
                    loginemail.setBackgroundResource(R.drawable.red_rectangle); // 아이디 입력란 배경 변경
                    loginemail.requestFocus(); // 아이디 입력란으로 포커스
                    return;
                } else { // 아이디의 길이가 0이 아닐 경우
                    login_email_eroor.setText(""); // 에러 메시지는 뜨지 않도록 공란으로 표시
                    loginemail.setBackgroundResource(R.drawable.input_rectangle); // 아이디 입력란 배경 변경
                }
                // 비밀번호를 입력하지 않은 경우 에러 메시지 표시
                if (loginpassword.getText().toString().length() == 0) { // 비밀번호의 길이가 0일 경우
                    login_pw_eroor.setText("비밀번호를 입력하세요"); // 에러 메시지 표시
                    loginpassword.setBackgroundResource(R.drawable.red_rectangle); // 비밀번호 입력란 배경 변경
                    loginpassword.requestFocus(); // 비밀번호 입력란으로 포커스
                    return;
                } else { // 비밀번호의 길이가 0이 아닐 경우
                    login_pw_eroor.setText(""); // 에러 페시지는 뜨지 않도록 공란으로 표시
                    loginpassword.setBackgroundResource(R.drawable.input_rectangle); // 비밀번호 입력란 배경 변경
                }

                    mArrayList.clear();
                    GetData task = new GetData();
                    task.execute( loginemail.getText().toString(), loginpassword.getText().toString()); // DB에 사용자 아이디와 비밀번호가 있는지 확인





            }
        });

        //로그인 기억하기 체크박스 유무에 따른 동작 구현
        cb_save.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) { // 체크박스 체크 되어 있으면
                    // editText에서 아이디와 암호 가져와 PreferenceManager에 저장한다.
                    PreferenceManager.setString(mContext, "signup_id", loginemail.getText().toString()); //id 키값으로 저장
                    PreferenceManager.setString(mContext, "id", loginemail.getText().toString()); //id 키값으로 저장
                    PreferenceManager.setString(mContext, "pw", loginpassword.getText().toString()); //pw 키값으로 저장
                    PreferenceManager.setBoolean(mContext, "check", cb_save.isChecked()); //현재 체크박스 상태 값 저장
                    } else {
                        //체크박스가 해제되어있으면
                        PreferenceManager.setBoolean(mContext, "check", cb_save.isChecked()); //현재 체크박스 상태 값 저장
                        PreferenceManager.clear(mContext); //로그인 정보를 모두 날림
                    }
            }
        }) ;

        mArrayList = new ArrayList<>();


        // 회원가입 버튼을 눌렀을 때
        signupbutton = (Button) findViewById(R.id.signup_bt);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    ////카카오 로그인
    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() { // 카카오 로그인에 성공한 상태
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) { // 사용자 정보 요청에 실패한 경우
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) { // 세션 오픈 실패, 세션이 삭제된 경우
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다.lo 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) { // 사용자 정보 요청에 성공한 경우

                    PreferenceManager.setString(mContext, "signup_id", result.getKakaoAccount().getEmail()); // 사용자 이메일을 signup_id라는 키값으로 저장
                    PreferenceManager.setString(mContext, "id", result.getKakaoAccount().getEmail()); //사용자 이메일을 id라는 키값으로 저장
                    PreferenceManager.setString(mContext, "pw", kakaopw); // 카카오 사용자의 비밀번호는 kakaopw라고 하여 pw라는 키값으로 저장

                    Sign sign = new Sign();
                    String IP_ADDRESS = "113.198.236.105";
                    // DB에 카카오 사용자 추가
                    sign.execute("http://" + IP_ADDRESS + "/check_email.php", result.getNickname(), result.getKakaoAccount().getEmail(), kakaopw);
                    Log.d(TAG, "kakao response - " + result.getKakaoAccount().getEmail());
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) { // 카카오 로그인에 실패한 상태
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+exception.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Login.this,
                    "로그인 중..", "잠시만 기다려 주세요!", true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"로그인 실패!", Toast.LENGTH_LONG).show(); // 인터넷이 안될때 로그인 실패
                //mTextViewResult.setText(errorString);
            }
            else {
                if(result.contains("User Found")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    PreferenceManager.setBoolean(mContext, "check",cb_save.isChecked() ); //현재 체크박스 상태 값 저장
                    startActivity(intent);
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"로그인 실패!", Toast.LENGTH_LONG).show(); // 인터넷이 안될때 로그인 실패
                    PreferenceManager.setBoolean(mContext, "check", false); //현재 체크박스 상태 값 저장
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];

            String serverURL = "http://113.198.236.105//login4.php";
            String postParameters = "USER_ID=" + searchKeyword1 + "&USER_PWD=" + searchKeyword2;


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
                //line.equalsIgnoreCase("User Found")


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "Data: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        //super.onBackPressed();

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finishAffinity();
            toast.cancel();
        }
    }

    public class Sign extends AsyncTask<String, Void, String> {
        private String TAG = "카카오사용자";
        String errorString = null;
        ProgressDialog progressDialog;
        public int redd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Login.this,
                    "카카오 로그인 중..", "잠시만 기다려 주세요!", true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog = ProgressDialog.show(Login.this,
                    "카카오 로그인 중..", "잠시만 기다려 주세요!", true, true);
            Log.d("result : ", result);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String)params[0];
            String NAME = (String)params[1];
            String USER_ID= (String)params[2];
            String USER_PWD= (String)params[3];

            String postParameters = "email=" + USER_ID;

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

                Log.d("sb : ", sb.toString().trim());
                if(!sb.toString().trim().contains(USER_ID)){
                    InsertUserData insertdata = new InsertUserData();
                    String IP_ADDRESS = "113.198.236.105";

                    insertdata.execute("http://" + IP_ADDRESS + "/register.php", NAME, USER_ID, USER_PWD);

                    Log.d("insert name - ", NAME);
                    Log.d("insert email - ", USER_ID);
                    Log.d("insert pwd - ", USER_PWD);
                    redd=0;

                    Intent intent = new Intent(getApplicationContext(), Interest.class);
                    intent.putExtra("사용자아이디",USER_ID);
                    startActivity(intent);
                    finish();

                }else{
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return sb.toString().trim();
            } catch (Exception e) {
                return new String("Error: " + e.getMessage());
            }

        }


    }
}


