package com.example.asd76.okonomiorgel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.asd76.okonomiorgel.Adapter.FreeBoardAdapter;
import com.example.asd76.okonomiorgel.Response.FreeBoard;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;

import java.util.ArrayList;

/**
 * Created by asd76 on 2018-03-27.
 */

public class FreeBoardActivity extends AppCompatActivity{

    OkonomiOrgelService service;
    Spinner spinner;
    ListView listView;
    android.support.v7.widget.SearchView searchView;
    FreeBoardAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_board);

        //액션바 타이틀 지우기
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.free_board_list);
        searchView = (android.support.v7.widget.SearchView)findViewById(R.id.free_board_search_view);
        spinner = (Spinner)findViewById(R.id.free_board_spinner);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.free_board_refreshLayout);
        adapter = new FreeBoardAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FreeBoard item = (FreeBoard) parent.getAdapter().getItem(position);
                //임시 인텐트
                Intent postIntent = new Intent(FreeBoardActivity.this, FreePostActivity.class);
                startActivity(postIntent);
            }
        });

        //스피너 설정
        final ArrayList spinnerItem = new ArrayList();
        spinnerItem.add("최신순");
        spinnerItem.add("조회순");

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

        //검색창 설정
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String q = query.toString().trim();
                adapter.getFilter().filter(q);
                spinner.setSelection(0);
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

        //새로고침 시 데이터 새로 받아오기
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clearItem();
                //getBoardList(0);
                refreshLayout.setRefreshing(false);
            }
        });

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit

        //test
        FreeBoard item = new FreeBoard();
        item.setCategory("클래식");
        item.setTitle("ㅇㅅㅇ ㅇㅅㅇ ㅇㅅㅇ!!!!");
        item.setWriter("금비");
        item.setContents("내용......");
        item.setNum(0);
        item.setHits(10);
        item.setReg_date("2018-03-26 14:20:47");

        adapter.addItem(item);

        item = new FreeBoard();
        item.setCategory("클래식");
        item.setTitle("ㅇㅅㅇ ㅇㅅㅇ 2222");
        item.setWriter("황금비");
        item.setContents("내용...22222...");
        item.setNum(1);
        item.setHits(5);
        item.setReg_date("2018-03-27 14:20:47");
        adapter.addItem(item);
        adapter.notifyDataSetChanged();

        item = new FreeBoard();
        item.setCategory("클래식");
        item.setTitle("ㅇㅅㅇ ㅇㅅㅇ 222332");
        item.setWriter("황금비2");
        item.setContents("내용...2223322...");
        item.setNum(2);
        item.setHits(1);
        item.setReg_date("2018-03-28 14:20:47");
        adapter.addItem(item);
        adapter.notifyDataSetChanged();

    }//end of onCreate

    public void getBoardList(int listStartNum){}

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
