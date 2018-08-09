package com.example.asd76.okonomiorgel.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.asd76.okonomiorgel.Adapters.FreeBoardAdapter;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.FreeBoardRecord;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.pushLike;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-03-27.
 */

public class FreeBoardActivity extends AppCompatActivity{

    OkonomiOrgelService service;
    ListView listView;
    FreeBoardAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_board);

        listView = (ListView)findViewById(R.id.free_board_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FreeBoardRecord item = (FreeBoardRecord) parent.getAdapter().getItem(position);
                //임시 인텐트
                Intent postIntent = new Intent(FreeBoardActivity.this, FreePostActivity.class);
                postIntent.putExtra("postNum", item.getPost_id());
                startActivity(postIntent);
            }
        });

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        getBoardList();

    }//end of onCreate

    public void getBoardList(){

        final Call<ArrayList<FreeBoardRecord>> response = service.getFreeBoardRecords();
        response.enqueue(new Callback<ArrayList<FreeBoardRecord>>() {
            @Override
            public void onResponse(Call<ArrayList<FreeBoardRecord>> call, Response<ArrayList<FreeBoardRecord>> response) {
                if(response.isSuccessful() && response != null){
                    adapter = new FreeBoardAdapter(response.body());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FreeBoardRecord>> call, Throwable t) {

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

}//end of class
