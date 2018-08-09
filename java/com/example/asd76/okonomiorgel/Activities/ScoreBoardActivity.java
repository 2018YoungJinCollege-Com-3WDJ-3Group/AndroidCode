package com.example.asd76.okonomiorgel.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.asd76.okonomiorgel.Adapters.ScoreBoardAdapter;
import com.example.asd76.okonomiorgel.Item.SheetBoardItem;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-03-19.
 */

public class ScoreBoardActivity extends AppCompatActivity{

    OkonomiOrgelService service;
    ListView listView;
    android.support.v7.widget.SearchView searchView;
    ScoreBoardAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicsheet_board);

        ImageButton btn_back = (ImageButton)findViewById(R.id.score_board_back);
        listView = (ListView)findViewById(R.id.sheet_board_list);
        searchView = (SearchView)findViewById(R.id.score_board_search_view);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        adapter = new ScoreBoardAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SheetBoardItem item = (SheetBoardItem)parent.getAdapter().getItem(position);
                Intent postIntent = new Intent(ScoreBoardActivity.this, ScorePostActivity.class);
                postIntent.putExtra("postNum", item.getNum());
                ScoreBoardActivity.this.startActivity(postIntent);
            }
        });

        //새로고침 시 데이터 새로 받아오기
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clearItem();
                getBoardList(0);
                refreshLayout.setRefreshing(false);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //검색창 설정
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String q = query.toString().trim();
                adapter.getFilter().filter(q);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //검색창 닫으면 필터 삭제
        searchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.getFilter().filter("");
                return false;
            }
        });

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        getBoardList(0);

    }//onCreate

    public void getBoardList(int listStartNum){

        final Call<ArrayList<SheetBoardItem>> response = service.getSheetBoardList(listStartNum);
        response.enqueue(new Callback<ArrayList<SheetBoardItem>>() {
            @Override
            public void onResponse(Call<ArrayList<SheetBoardItem>> call, Response<ArrayList<SheetBoardItem>> response) {

                if(response.isSuccessful() && response.body() != null){
                    ArrayList<SheetBoardItem> itemList = response.body();
                    for(int i = 0; i < itemList.size(); i++)
                        adapter.addItem(itemList.get(i));
                    adapter.notifyDataSetChanged();

                }else{
                    Log.e("response", "body is null");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SheetBoardItem>> call, Throwable t) {
                Log.e("response", "onFailure");
            }
        });
    }

}
