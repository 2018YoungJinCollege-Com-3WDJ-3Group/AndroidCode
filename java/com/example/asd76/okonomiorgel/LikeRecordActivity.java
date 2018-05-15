package com.example.asd76.okonomiorgel;

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

import com.example.asd76.okonomiorgel.Adapter.HistoryListAdapter;
import com.example.asd76.okonomiorgel.Response.Like_record;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-05-10.
 */

public class LikeRecordActivity extends AppCompatActivity{

    OkonomiOrgelService service;
    int user_id = -1;
    ArrayList<Like_record> records;
    HistoryListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_record);

        //액티비티 타이틀 설정
        getSupportActionBar().setTitle("좋아요 내역");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //회원 아이디 가져오기
        SharedPreferences pref = getSharedPreferences("user_info", 0);
        user_id = pref.getInt("user_id", -1);

        //아이디가 없으면 종료
        if(user_id == -1){
            Toast.makeText(this, "로그인 후 이용 가능", Toast.LENGTH_SHORT).show();
            finish();
        }

        ListView listView = (ListView)findViewById(R.id.like_record_listview);
        Spinner spinner = (Spinner)findViewById(R.id.like_record_spinner);

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        //test


        //adapter = new HistoryListAdapter(histories);
        listView.setAdapter(adapter);

        final ArrayList spinnerItem = new ArrayList();

        spinnerItem.add("최신순");
        spinnerItem.add("높은 가격순");
        spinnerItem.add("낮은 가격순");
        spinner.setSelection(0);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, spinnerItem);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.sortItem(spinnerItem.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public ArrayList<Like_record> getHistoryData(){

        final Call<ArrayList<Like_record>> response = service.getLikeRecord(user_id);
        response.enqueue(new Callback<ArrayList<Like_record>>() {

            @Override
            public void onResponse(Call<ArrayList<Like_record>> call, Response<ArrayList<Like_record>> response) {

                if(response.isSuccessful() && response.body() != null){

                    records = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Like_record>> call, Throwable t) {

            }

        });

        return records;
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
