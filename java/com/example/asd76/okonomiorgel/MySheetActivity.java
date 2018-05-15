package com.example.asd76.okonomiorgel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Adapter.CustomViewPager;
import com.example.asd76.okonomiorgel.Adapter.SheetPagerAdapter;
import com.example.asd76.okonomiorgel.Fragments.fragment_downSheet;
import com.example.asd76.okonomiorgel.Fragments.fragment_writeSheet;
import com.example.asd76.okonomiorgel.Listener.TabSelectedLis;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.Score_info;

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
    SheetPagerAdapter adapter;
    CustomViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<Score_info> downSheetInfo = new ArrayList<>();
    ArrayList<Score_info> wroteSheetInfo = new ArrayList<>();
    int user_id = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sheet);

        getSupportActionBar().setTitle("악보 관리");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        SharedPreferences pref = getSharedPreferences("user_info", 0);
        user_id = pref.getInt("user_id", -1);

        if(user_id == -1){
            Toast.makeText(this, "로그인 후 이용 가능", Toast.LENGTH_SHORT).show();
            finish();
        }

        tabLayout = (TabLayout)findViewById(R.id.sheet_tabLayout);
        viewPager = (CustomViewPager)findViewById(R.id.sheet_viewPager);

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

    public void getPurchasedSheet(){

        final Call<ArrayList<Score_info>> response = service.getPurchaseSheet(user_id);
        response.enqueue(new Callback<ArrayList<Score_info>>() {
            @Override
            public void onResponse(Call<ArrayList<Score_info>> call, Response<ArrayList<Score_info>> response) {
                if(response.isSuccessful() && response.body() != null){
                    ArrayList<Score_info> sheetInfos = response.body();
                    downSheetInfo = response.body();
                    getWroteSheet();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Score_info>> call, Throwable t) {
                Log.e("response", "failure");
            }
        });
    }

    public void getWroteSheet(){

        final Call<ArrayList<Score_info>> response = service.getWroteSheet(user_id);
        response.enqueue(new Callback<ArrayList<Score_info>>() {
            @Override
            public void onResponse(Call<ArrayList<Score_info>> call, Response<ArrayList<Score_info>> response) {

                if(response.isSuccessful() && response.body() != null){

                    wroteSheetInfo = response.body();
                    setupViewpager(viewPager);

                }else{
                    Log.e("response", "body is null");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Score_info>> call, Throwable t) {
                Log.e("response", "failure");
            }
        });

    }

    //뷰페이저 설정
    public void setupViewpager(ViewPager viewPager){

        Log.d("downSheetInfo", downSheetInfo.size()+"");
        Log.d("wroteSheetInfo", wroteSheetInfo.size()+"");

        //작성한 악보 프래그먼트에 보낼 객체 설정
        Bundle writeBundle = new Bundle();
        writeBundle.putParcelableArrayList("writeFragItems", (ArrayList<? extends Parcelable>) wroteSheetInfo);
        fragment_writeSheet writeFrag = new fragment_writeSheet();
        writeFrag.setArguments(writeBundle);

        //다운받은 악보 프래그먼트에 보낼 객체 설정
        Bundle downBundle = new Bundle();
        downBundle.putParcelableArrayList("downFragItems", (ArrayList<? extends Parcelable>) downSheetInfo);
        fragment_downSheet downFrag = new fragment_downSheet();
        downFrag.setArguments(downBundle);

        //페이지 어댑터 설정
        adapter = new SheetPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(writeFrag, "작성한 악보");
        adapter.addFragment(downFrag, "다운받은 악보");
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
}
