package com.example.asd76.okonomiorgel.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.LoginInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-03-06.
 */

public class LoginActivity extends FragmentActivity implements View.OnClickListener{

    EditText input_id;
    EditText input_password;
    Button btn_login;
    Button btn_signup;
    OkonomiOrgelService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //액션바 숨기기
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_id = (EditText)findViewById(R.id.input_id);
        input_password = (EditText)findViewById(R.id.input_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_signup = (Button)findViewById(R.id.btn_signup);

        //버튼 이벤트 리스너 등록
        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);
    }

    public void checkEmptyBox(){

        String id = input_id.getText().toString().trim();
        String password = input_password.getText().toString().trim();

        if(id.equals("") || password.equals(""))
            Toast.makeText(this, "아이디 또는 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        else
            sendUserInfo(id, password);
    }

    public void sendUserInfo(String id, String password){
        final Call<LoginInfo> response = service.login(id, password);
        response.enqueue(new Callback<LoginInfo>() {
            @Override
            public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                if(response.isSuccessful() && response.body() != null){
                    //로그인 성공 시
                    if(response.body().getLogin_check()){
                        LoginInfo userInfo = response.body();

                        if(userInfo == null)
                            Toast.makeText(LoginActivity.this, "회원 정보가 없습니다.", Toast.LENGTH_SHORT).show();

                        setUserInfo(userInfo);
                    }
                    //로그인 실패 시
                    else{
                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                    
                }else{
                    Toast.makeText(LoginActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginInfo> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setUserInfo(LoginInfo userInfo){

        //SharedPreferences 초기화 후 저장
        SharedPreferences user_pref = getSharedPreferences("user_info", 0);
        SharedPreferences.Editor editor = user_pref.edit();
        editor.clear();
        editor.commit();
        editor.putString("user_session_id", userInfo.getSession_id());
        editor.putInt("user_id", userInfo.getUser_id());
        editor.putString("user_name", userInfo.getUser_name());
        editor.putString("user_password", userInfo.getPassword());
        editor.putString("user_email", userInfo.getEmail());
        editor.commit();

        //MainActivity 이동
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra("login", userInfo.getUser_name());
        startActivity(mainIntent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login){
            checkEmptyBox();
        }else if(v.getId() == R.id.btn_signup){
            //회원가입 페이지로 이동
            Intent registerIntent = new Intent(this, RegisterActivity.class);
            startActivity(registerIntent);
        }
    }

}//Activity
