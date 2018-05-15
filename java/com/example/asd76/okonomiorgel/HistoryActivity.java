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
import com.example.asd76.okonomiorgel.Response.Purchase_history;
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

public class HistoryActivity extends AppCompatActivity{

    OkonomiOrgelService service;
    int user_id = -1;
    ArrayList<Purchase_history> histories;
    HistoryListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //액티비티 타이틀 설정
        getSupportActionBar().setTitle("악보 관리");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //회원 아이디 가져오기
        SharedPreferences pref = getSharedPreferences("user_info", 0);
        user_id = pref.getInt("user_id", -1);

        //아이디가 없으면 종료
        if(user_id == -1){
            Toast.makeText(this, "로그인 후 이용 가능", Toast.LENGTH_SHORT).show();
            finish();
        }

        ListView listView = (ListView)findViewById(R.id.history_listview);
        Spinner spinner = (Spinner)findViewById(R.id.history_spinner);

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        //test
        Purchase_history dummy = new Purchase_history();
        dummy.setOrder_id(0);
        dummy.setSeller_name("금비");
        dummy.setPrice(3000);
        dummy.setCreated_at("2018-05-8 11:29:31");
        dummy.setScore_subject("제목");
        histories = new ArrayList<>();
        histories.add(dummy);

        Purchase_history dummy2 = new Purchase_history();
        dummy2.setOrder_id(2);
        dummy2.setSeller_name("금비2");
        dummy2.setPrice(1000);
        dummy2.setCreated_at("2018-05-09 11:29:31");
        dummy2.setScore_subject("제목2");

        histories.add(dummy2);

        Purchase_history dummy3 = new Purchase_history();
        dummy3.setOrder_id(45);
        dummy3.setSeller_name("금비3");
        dummy3.setPrice(8000);
        dummy3.setCreated_at("2018-05-10 11:29:31");
        dummy3.setScore_subject("제목3");
        histories.add(dummy3);

        adapter = new HistoryListAdapter(histories);
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

    public ArrayList<Purchase_history> getHistoryData(){

        final Call<ArrayList<Purchase_history>> response = service.getHistory(user_id);
        response.enqueue(new Callback<ArrayList<Purchase_history>>() {

            @Override
            public void onResponse(Call<ArrayList<Purchase_history>> call, Response<ArrayList<Purchase_history>> response) {

                if(response.isSuccessful() && response.body() != null){

                    histories = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Purchase_history>> call, Throwable t) {

            }

        });

        return histories;
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
