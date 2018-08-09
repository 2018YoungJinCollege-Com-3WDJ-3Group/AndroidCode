package com.example.asd76.okonomiorgel.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Adapters.HistoryListAdapter;
import com.example.asd76.okonomiorgel.Adapters.LikeListAdapter;
import com.example.asd76.okonomiorgel.Adapters.RankingAdapter;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.LikeRecord;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.RankingRecord;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-05-10.
 */

public class RankingBoardActivity extends AppCompatActivity{

    OkonomiOrgelService service;
    int user_id = -1;
    ArrayList<RankingRecord> records;
    RankingAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        listView = (ListView)findViewById(R.id.ranking_listview);

        //회원 아이디 가져오기
        SharedPreferences pref = getSharedPreferences("user_info", 0);
        user_id = pref.getInt("user_id", -1);

        //아이디가 없으면 종료
        if(user_id == -1){
            Toast.makeText(this, "로그인 후 이용 가능", Toast.LENGTH_SHORT).show();
            finish();
        }

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        getRecords();
    }

    public void getRecords(){

        final Call<ArrayList<RankingRecord>> response = service.getRankingRecords();
        response.enqueue(new Callback<ArrayList<RankingRecord>>() {

            @Override
            public void onResponse(Call<ArrayList<RankingRecord>> call, Response<ArrayList<RankingRecord>> response) {

                if(response.isSuccessful() && response.body() != null){
                    records = response.body();
                    adapter = new RankingAdapter(records);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RankingRecord>> call, Throwable t) {

            }

        });
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
