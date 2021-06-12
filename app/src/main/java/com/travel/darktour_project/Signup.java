package com.travel.darktour_project;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Signup extends AppCompatActivity {
    private EditText signupname;
    private EditText signupemail;
    private EditText signuppw;
    private EditText confirmpw;
    private Button signupbt;
    private TextView name_eroor;
    private TextView email_eroor;
    private TextView pw_eroor;
    private TextView pw_con_eroor;
    private CheckBox ok_box;
    public static int check_for_register = 0;
    public static Context mContext;
    private boolean check = true;
    public int reduplication = 0;
    private String TAG = "여기는 signup";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        mContext = this;

        signupname = (EditText) findViewById(R.id.signup_name);
        signupemail = (EditText) findViewById(R.id.signup_email);
        signuppw = (EditText) findViewById(R.id.signup_password);
        confirmpw = (EditText) findViewById(R.id.signup_confirm_pw);
        signupbt = (Button) findViewById(R.id.signup_bt);
        name_eroor = findViewById(R.id.name_eroor);
        email_eroor = findViewById(R.id.email_eroor);
        pw_eroor = findViewById(R.id.pw_eroor);
        pw_con_eroor = findViewById(R.id.pw_con_eroor);
        ok_box = findViewById(R.id.ok_box);

        // 회원가입 버튼을 눌렀을 때
        signupbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이름을 입력하지 입력하지 않은 경우
                if (signupname.getText().toString().length() == 0) {
                    name_eroor.setText("이름을 입력하세요");
                    signupname.setBackgroundResource(R.drawable.red_rectangle);
                    signupname.requestFocus();
                    return;
                } else {
                    name_eroor.setText("");
                    signupname.setBackgroundResource(R.drawable.input_rectangle);
                }
                // 이메일을 입력하지 입력하지 않은 경우
                if (signupemail.getText().toString().length() == 0) {
                    email_eroor.setText("이메일을 입력하세요");
                    signupemail.setBackgroundResource(R.drawable.red_rectangle);
                    signupemail.requestFocus();
                    return;
                } else {
                    email_eroor.setText("");
                    signupemail.setBackgroundResource(R.drawable.input_rectangle);
                }
                // 비밀번호를 입력하지 입력하지 않은 경우
                if (signuppw.getText().toString().length() == 0) {
                    pw_eroor.setText("비밀번호를 입력하세요");
                    signuppw.setBackgroundResource(R.drawable.red_rectangle);
                    signuppw.requestFocus();
                    return;
                } else {
                    pw_eroor.setText("");
                    signuppw.setBackgroundResource(R.drawable.input_rectangle);
                }
                // 비밀번호 확인을 입력하지 않은 경우
                if (confirmpw.getText().toString().length() == 0) {
                    pw_con_eroor.setText("비밀번호 확인을 입력하세요");
                    confirmpw.setBackgroundResource(R.drawable.red_rectangle);
                    confirmpw.requestFocus();
                    return;
                } else {
                    pw_con_eroor.setText("");
                    confirmpw.setBackgroundResource(R.drawable.input_rectangle);
                }
                // 비밀번호가 일치하지 않는 경우
                if (!signuppw.getText().toString().equals(confirmpw.getText().toString())) {
                    signuppw.setText("");
                    confirmpw.setText("");
                    pw_con_eroor.setText("비밀번호가 일치하지 않습니다");
                    signuppw.setBackgroundResource(R.drawable.red_rectangle);
                    confirmpw.setBackgroundResource(R.drawable.red_rectangle);
                    signuppw.requestFocus();
                    return;
                } else {
                    pw_con_eroor.setText("");
                    confirmpw.setBackgroundResource(R.drawable.input_rectangle);
                }
                // 약관동의를 체크하지 않은 경우
                if (!ok_box.isChecked()) {
                    Toast.makeText(Signup.this, "약관에 동의해주세요.", Toast.LENGTH_LONG).show();
                    ok_box.requestFocus();
                    return;
                }
                /*if (! signuppw.getText().toString().equals(confirmpw.getText().toString())) {
                    signuppw.setText("");
                    confirmpw.setText("");
                    pw_con_eroor.setText("비밀번호가 일치하지 않습니다");
                    signuppw.setBackgroundResource(R.drawable.red_rectangle);
                    confirmpw.setBackgroundResource(R.drawable.red_rectangle);
                    signuppw.requestFocus();
                    return;
                }
                else {
                    pw_con_eroor.setText("");
                    confirmpw.setBackgroundResource(R.drawable.input_rectangle);
                }*/

                /*

                Log.d(TAG, "reduplication : " + reduplication);

                if(reduplication>0){
                    Toast.makeText(mContext, "존재하는 이메일입니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Signup.class);
                    startActivity(intent);

                } else {
                    Log.d(TAG, "reduplication : " + reduplication);

                    Intent intent = new Intent(getApplicationContext(), Interest.class);
                    startActivity(intent);
                }

                 */

                //PreferenceManager.setBoolean(mContext, "check",check); //현재 체크박스 상태 값 저장
                PreferenceManager.setString(mContext, "signup_id", signupemail.getText().toString()); //id라는 키값으로 저장
                //check = false;
                //PreferenceManager.setBoolean(mContext, "check",check); //현재 체크박스 상태 값 저장
                CheckEmail checkemail = new CheckEmail();
                String IP_ADDRESS = "113.198.236.105";
                checkemail.execute("http://" + IP_ADDRESS + "/check_email.php", signupname.getText().toString(), signupemail.getText().toString(), signuppw.getText().toString());
            }
        });
        // 이메일 입력받는 박스
        signupemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    email_eroor.setText("올바른 이메일 형식을 입력하세요");
                    signupemail.setBackgroundResource(R.drawable.red_rectangle);
                }
                else {
                    email_eroor.setText("");
                    signupemail.setBackgroundResource(R.drawable.input_rectangle);
                }
            }
        });
        // 비밀번호 입력, 비밀번호 확인 입력 박스
        confirmpw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = signuppw.getText().toString();
                String confirm = confirmpw.getText().toString();

                if(password.equals(confirm)) {
                    pw_con_eroor.setText("");
                    signuppw.setBackgroundResource(R.drawable.input_rectangle);
                    confirmpw.setBackgroundResource(R.drawable.input_rectangle);
                }
                else {
                    pw_con_eroor.setText("비밀번호가 일치하지 않습니다");
                    signuppw.setBackgroundResource(R.drawable.red_rectangle);
                    confirmpw.setBackgroundResource(R.drawable.red_rectangle);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void getcode(int red){
        reduplication = red;
    }


    public class CheckEmail extends AsyncTask<String, Void, String> {
        private String TAG = "checkemail";
        String errorString = null;
        ProgressDialog progressDialog;
        public int redd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Signup.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
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

            Log.d("checkemail : ", postParameters);

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
                    //InsertUseridMypage mypage = new InsertUseridMypage();
                    String IP_ADDRESS = "113.198.236.105";

                    insertdata.execute("http://" + IP_ADDRESS + "/register.php", NAME, USER_ID, USER_PWD);
                    //mypage.execute("http://" + IP_ADDRESS + "/insert_userid_mypage.php", USER_ID);

                    Log.d("insert name - ", NAME);
                    Log.d("insert email - ", USER_ID);
                    Log.d("insert pwd - ", USER_PWD);
                    redd=0;



                    Intent intent = new Intent(getApplicationContext(), Interest.class);
                    intent.putExtra("사용자아이디",USER_ID);
                    startActivity(intent);

                }else{
                    Log.d(TAG, "existing email : " + USER_ID);
                    redd=1;
                    Log.d(TAG, "existing email  + red: " + USER_ID+redd);
                    Log.d(TAG, "reduplication : " + reduplication);


                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 사용하고자 하는 코드
                            Toast.makeText(Signup.this, "존재하는 이메일입니다.", Toast.LENGTH_LONG).show();
                        }
                    }, 0);

                    //Intent intent = new Intent(getApplicationContext(), Signup.class);
                    //startActivity(intent);

                }
                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "checkemail: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }


    }
}
