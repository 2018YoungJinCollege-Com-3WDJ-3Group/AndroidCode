package com.example.asd76.okonomiorgel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.asd76.okonomiorgel.Adapter.TabListAdapter;
import com.example.asd76.okonomiorgel.Item.ListViewItem;
import com.example.asd76.okonomiorgel.Listener.*;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{

    NavigationView navigationView;
    boolean isDrawerOpened;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    RelativeLayout relativeLayout;
    ListView listView;
    TabListAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //액션바 타이틀 지우기
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        int color = Color.rgb(255, 214, 229);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        getSupportActionBar().setElevation(0);

        drawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        listView = (ListView)findViewById(R.id.main_listView);

        //네비게이션 헤더 객체 획득
        TextView nav_header = (TextView)navigationView.getHeaderView(0);
        //네비게이션 메뉴 객체 획득
        Menu nav_menu = (Menu)navigationView.getMenu();

        SharedPreferences pref = getSharedPreferences("user_info", 0);
        String user_name = pref.getString("user_name", null);

        if(user_name != null){
            //네비게이션 바 헤더 변경
            nav_header.setText(user_name);
            //로그인 메뉴 비활성화
            nav_menu.findItem(R.id.menu_login).setVisible(false);
            //로그아웃 메뉴 활성화
            nav_menu.findItem(R.id.menu_logout).setVisible(true);
        }

        if(user_name == null){
            //네비게이션 바 헤더 변경
            nav_header.setText("비회원");
            //로그인 메뉴 활성화
            nav_menu.findItem(R.id.menu_login).setVisible(true);
            //로그아웃 메뉴 비활성화
            nav_menu.findItem(R.id.menu_logout).setVisible(false);
        }

        //네비게이션 바 리스너 등록
        navigationView.setNavigationItemSelectedListener(new NavigationItemLis(this));

        isDrawerOpened = false;
        toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.drawer_open, R.string.drawer_close){

            //네비게이션 바 이벤트 설정
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isDrawerOpened = false;
            }
        };

        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();

        //메인 아이템 리스트업
        ArrayList<ListViewItem> items = new ArrayList<>();
        items.add(new ListViewItem(R.drawable.main_sheet_management, "악보 관리"));
        items.add(new ListViewItem(R.drawable.main_transfer, "외부 악보 변환"));
        items.add(new ListViewItem(R.drawable.main_piano, "실시간 연주"));
        items.add(new ListViewItem(R.drawable.main_free_board, "자유 게시판"));
        items.add(new ListViewItem(R.drawable.main_share_board, "악보 거래 게시판"));

        //리스트 어댑터 설정
        adapter = new TabListAdapter(items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem)parent.getAdapter().getItem(position);
                Intent intent;
                switch (item.getTitle()){
                    case "악보 관리":
                        intent = new Intent(MainActivity.this, MySheetActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case "외부 악보 변환":
                        break;
                    case "실시간 연주":
                        intent = new Intent(MainActivity.this, PianoActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case "자유 게시판":
                        intent = new Intent(MainActivity.this, FreeBoardActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case "악보 거래 게시판":
                        intent = new Intent(MainActivity.this, SheetBoardActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                }
            }
        });

    }

    //네비게이션 창이 열려있으면 닫기
    @Override
    public void onBackPressed() {
        if(isDrawerOpened)
            drawer.closeDrawers();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return false;
        else
            return super.onOptionsItemSelected(item);
    }

}
