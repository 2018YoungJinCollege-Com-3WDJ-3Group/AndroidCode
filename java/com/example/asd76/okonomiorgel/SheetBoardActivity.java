package com.example.asd76.okonomiorgel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Adapter.SheetBoardAdapter;
import com.example.asd76.okonomiorgel.Item.SheetBoardListItem;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.User_info;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-03-19.
 */

public class SheetBoardActivity extends AppCompatActivity{

    OkonomiOrgelService service;
    Spinner spinner;
    ListView listView;
    android.support.v7.widget.SearchView searchView;
    SheetBoardAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicsheet_board);

        //액션바 타이틀 지우기
        getSupportActionBar().setTitle("악보 공유 게시판");
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        //getSupportActionBar().setHomeAsUpIndicator(new ColorDrawable(Color.GREEN));

        listView = (ListView)findViewById(R.id.sheet_board_list);
        //spinner = (Spinner)findViewById(R.id.sheet_board_spinner);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        adapter = new SheetBoardAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SheetBoardListItem item = (SheetBoardListItem)parent.getAdapter().getItem(position);
                Intent postIntent = new Intent(SheetBoardActivity.this, SheetPostActivity.class);
                postIntent.putExtra("postNum", item.getNum());
                SheetBoardActivity.this.startActivity(postIntent);
            }
        });

        //스피너 설정
        /*
        final ArrayList spinnerItem = new ArrayList();
        spinnerItem.add("최신순");
        spinnerItem.add("판매순");
        spinnerItem.add("낮은 가격순");
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
        */

        //새로고침 시 데이터 새로 받아오기
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clearItem();
                getBoardList(0);
                refreshLayout.setRefreshing(false);
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

        final Call<ArrayList<SheetBoardListItem>> response = service.getSheetBoardList(listStartNum);
        response.enqueue(new Callback<ArrayList<SheetBoardListItem>>() {
            @Override
            public void onResponse(Call<ArrayList<SheetBoardListItem>> call, Response<ArrayList<SheetBoardListItem>> response) {

                if(response.isSuccessful() && response.body() != null){
                    ArrayList<SheetBoardListItem> itemList = response.body();
                    for(int i = 0; i < itemList.size(); i++)
                        adapter.addItem(itemList.get(i));
                    adapter.notifyDataSetChanged();
                }else{
                    Log.e("response", "body is null");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SheetBoardListItem>> call, Throwable t) {
                Log.e("response", "onFailure");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sheet_board, menu);
        searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.sheet_board_search).getActionView();

        //검색창 설정
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String q = query.toString().trim();
                adapter.getFilter().filter(q);
                //spinner.setSelection(0);
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

        return true;
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
