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
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.LikeRecord;
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
    ArrayList<LikeRecord> records;
    LikeListAdapter adapter;
    ListView listView;
    Spinner spinner;
    ArrayList spinnerItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_record);

        //회원 아이디 가져오기
        SharedPreferences pref = getSharedPreferences("user_info", 0);
        user_id = pref.getInt("user_id", -1);

        //아이디가 없으면 종료
        if(user_id == -1){
            Toast.makeText(this, "로그인 후 이용 가능", Toast.LENGTH_SHORT).show();
            finish();
        }

        //아이템 클릭 시 게시글 보기
        listView = (ListView)findViewById(R.id.like_record_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LikeRecord record = (LikeRecord)parent.getAdapter().getItem(position);
                Intent intent = new Intent(LikeRecordActivity.this, ScorePostActivity.class);
                intent.putExtra("postNum", record.getPost_id());
                LikeRecordActivity.this.startActivity(intent);
            }
        });

        spinner = (Spinner)findViewById(R.id.like_record_spinner);

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        getLikeRecords();

        spinnerItem = new ArrayList();

        spinnerItem.add("최신순");
        spinnerItem.add("높은 가격순");
        spinnerItem.add("낮은 가격순");
        spinner.setSelection(0);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, spinnerItem);
        spinner.setAdapter(spinnerAdapter);

    }

    public ArrayList<LikeRecord> getLikeRecords(){

        final Call<ArrayList<LikeRecord>> response = service.getLikeRecords(user_id);
        response.enqueue(new Callback<ArrayList<LikeRecord>>() {

            @Override
            public void onResponse(Call<ArrayList<LikeRecord>> call, Response<ArrayList<LikeRecord>> response) {

                if(response.isSuccessful() && response.body() != null){
                    records = response.body();
                    setting();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LikeRecord>> call, Throwable t) {

            }

        });
        return records;
    }

    public void setting(){

        adapter = new LikeListAdapter(records);
        listView.setAdapter(adapter);

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
