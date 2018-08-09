package com.example.asd76.okonomiorgel.Activities;

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
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.PurchaseRecords;
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

public class PurchaseRecordActivity extends AppCompatActivity{

    OkonomiOrgelService service;
    int user_id = -1;
    ArrayList<PurchaseRecords> histories;
    HistoryListAdapter adapter;
    ListView listView;
    Spinner spinner;
    ArrayList spinnerItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //회원 아이디 가져오기
        SharedPreferences pref = getSharedPreferences("user_info", 0);
        user_id = pref.getInt("user_id", -1);

        //아이디가 없으면 종료
        if(user_id == -1){
            Toast.makeText(this, "로그인 후 이용 가능", Toast.LENGTH_SHORT).show();
            finish();
        }

        listView = (ListView)findViewById(R.id.history_listview);
        spinner = (Spinner)findViewById(R.id.history_spinner);

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        getPurchaseRecords();

        spinnerItem = new ArrayList();

        spinnerItem.add("최신순");
        spinnerItem.add("높은 가격순");
        spinnerItem.add("낮은 가격순");
        spinner.setSelection(0);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, spinnerItem);
        spinner.setAdapter(spinnerAdapter);

    }

    public ArrayList<PurchaseRecords> getPurchaseRecords(){

        final Call<ArrayList<PurchaseRecords>> response = service.getPurchaseRecords(user_id);
        response.enqueue(new Callback<ArrayList<PurchaseRecords>>() {

            @Override
            public void onResponse(Call<ArrayList<PurchaseRecords>> call, Response<ArrayList<PurchaseRecords>> response) {

                if(response.isSuccessful() && response.body() != null){
                    histories = response.body();
                    setting();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PurchaseRecords>> call, Throwable t) {

            }

        });

        return histories;
    }

    public void setting(){

        adapter = new HistoryListAdapter(histories);
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
