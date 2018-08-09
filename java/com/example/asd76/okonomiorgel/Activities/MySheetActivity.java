package com.example.asd76.okonomiorgel.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Adapters.CustomViewPager;
import com.example.asd76.okonomiorgel.Adapters.ScorePagerAdapter;
import com.example.asd76.okonomiorgel.Bluetooth;
import com.example.asd76.okonomiorgel.Fragments.PurchasedScoreFragment;
import com.example.asd76.okonomiorgel.Fragments.WroteScoreFragment;
import com.example.asd76.okonomiorgel.Listener.TabSelectedLis;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.ScoreInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-03-26.
 */

public class MySheetActivity extends AppCompatActivity{

    OkonomiOrgelService service;
    ScorePagerAdapter adapter;
    CustomViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<ScoreInfo> downSheetInfo = new ArrayList<>();
    ArrayList<ScoreInfo> wroteSheetInfo = new ArrayList<>();
    int user_id = -1;
    Bluetooth bluetooth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sheet);

        Intent intent = getIntent();
        bluetooth = (Bluetooth)intent.getSerializableExtra("bluetooth");

        //회원 정보 가져오기
        SharedPreferences pref = getSharedPreferences("user_info", 0);
        user_id = pref.getInt("user_id", -1);

        //비회원 접근 불가 처리
        if(user_id == -1){
            Toast.makeText(this, "로그인 후 이용 가능", Toast.LENGTH_SHORT).show();
            finish();
        }

        tabLayout = (TabLayout)findViewById(R.id.sheet_tabLayout);
        viewPager = (CustomViewPager)findViewById(R.id.sheet_viewPager);
        ImageButton btn_back = (ImageButton)findViewById(R.id.my_score_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //스와이핑으로 페이지 이동 불가 처리
        viewPager.setPagingEnabled(false);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabSelectedLis(viewPager));

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        getPurchasedSheet();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void getPurchasedSheet(){

        final Call<ArrayList<ScoreInfo>> response = service.getPurchaseSheet(user_id);
        response.enqueue(new Callback<ArrayList<ScoreInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<ScoreInfo>> call, Response<ArrayList<ScoreInfo>> response) {
                if(response.isSuccessful() && response.body() != null){
                    ArrayList<ScoreInfo> sheetInfos = response.body();
                    downSheetInfo = response.body();
                    getWroteSheet();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ScoreInfo>> call, Throwable t) {
                Log.e("response", "failure");
            }
        });
    }

    public void getWroteSheet(){

        final Call<ArrayList<ScoreInfo>> response = service.getWroteSheet(user_id);
        response.enqueue(new Callback<ArrayList<ScoreInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<ScoreInfo>> call, Response<ArrayList<ScoreInfo>> response) {

                if(response.isSuccessful() && response.body() != null){

                    wroteSheetInfo = response.body();
                    setupViewpager(viewPager);

                }else{
                    Log.e("response", "body is null");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ScoreInfo>> call, Throwable t) {
                Log.e("response", "failure");
            }
        });

    }

    //뷰페이저 설정
    public void setupViewpager(ViewPager viewPager){

        Log.d("downSheetInfo size", downSheetInfo.size()+"");
        Log.d("wroteSheetInfo size", wroteSheetInfo.size()+"");

        //작성한 악보 프래그먼트에 보낼 객체 설정
        Bundle writeBundle = new Bundle();
        writeBundle.putParcelableArrayList("writeFragItems", (ArrayList<? extends Parcelable>) wroteSheetInfo);
        //writeBundle.putSerializable("bluetooth", bluetooth);
        WroteScoreFragment writeFrag = new WroteScoreFragment();
        writeFrag.setArguments(writeBundle);


        //다운받은 악보 프래그먼트에 보낼 객체 설정
        Bundle downBundle = new Bundle();
        downBundle.putParcelableArrayList("downFragItems", (ArrayList<? extends Parcelable>) downSheetInfo);
        //downBundle.putSerializable("bluetooth", bluetooth);
        PurchasedScoreFragment downFrag = new PurchasedScoreFragment();
        downFrag.setArguments(downBundle);

        //페이지 어댑터 설정
        adapter = new ScorePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(writeFrag, "制作した楽譜");
        adapter.addFragment(downFrag, "購入した楽譜             ");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPurchasedSheet();
    }
}
