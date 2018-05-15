package com.example.asd76.okonomiorgel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Response.Check;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.Register_info;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-03-06.
 */

public class RegisterActivity extends FragmentActivity implements View.OnClickListener{

    EditText user_id;
    EditText user_password;
    EditText user_email;
    Button duplicate_check;
    Button btn_ok;
    Button btn_cancel;
    OkonomiOrgelService service;

    Boolean isExistId = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //액션바 숨기기
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user_id = (EditText)findViewById(R.id.user_id);
        user_password = (EditText)findViewById(R.id.user_password);
        user_email = (EditText)findViewById(R.id.user_email);
        duplicate_check = (Button)findViewById(R.id.duplicate_check);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);

        //버튼 리스너 설정
        duplicate_check.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                                                  .addConverterFactory(GsonConverterFactory.create())
                                                  .build();
        service = retrofit.create(OkonomiOrgelService.class);
    }

    //아이디 중복 체크
    public void checkDuplicateId(){

        String id = user_id.getText().toString().trim();

        if(id.equals("")){
            Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        final Call<Check> response = service.duplicateCheck(id);
        response.enqueue(new Callback<Check>() {
            @Override
            public void onResponse(Call<Check> call, Response<Check> response) {
                if(response.isSuccessful() && response.body() != null){

                    Check check = response.body();

                    if(!check.getCheck())
                        Toast.makeText(RegisterActivity.this, "중복된 아이디 입니다", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(RegisterActivity.this, "사용 가능한 아이디 입니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Check> call, Throwable t) {

            }
        });

    }

    //공백란 체크
    public void checkEmptyBox(){
       
        String id = user_id.getText().toString().trim();
        String password = user_password.getText().toString().trim();
        String email = user_email.getText().toString().trim();

        if(id.equals("")||password.equals("")||email.equals(""))
            Toast.makeText(this, "빈 값이 있습니다.", Toast.LENGTH_SHORT).show();
        else if(isExistId)
            Toast.makeText(this, "아이디 중복체크를 해주세요", Toast.LENGTH_SHORT).show();
        else
            sendUserInfo(id, password, email);
    }

    //입력된 회원 정보를 서버로 전송
    public void sendUserInfo(String id, String password, String email){
        final Call<Register_info> response = service.signUp(id, email, password);
        response.enqueue(new Callback<Register_info>() {
            @Override
            public void onResponse(Call<Register_info> call, Response<Register_info> response) {
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(RegisterActivity.this, "가입을 축하합니다", Toast.LENGTH_SHORT).show();
                    moveLoginActivity();
                }else{
                    Toast.makeText(RegisterActivity.this, "회원 가입 오류 발생", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register_info> call, Throwable t) {
                Log.e("response", "response is failure");
            }
        });
    }

    //로그인 페이지로 이동
    public void moveLoginActivity(){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.duplicate_check:
                checkDuplicateId();
                break;
            case R.id.btn_ok:
                checkEmptyBox();
                break;
            case R.id.btn_cancel:
                moveLoginActivity();
                break;
            default:
                break;
        }
    }
}
