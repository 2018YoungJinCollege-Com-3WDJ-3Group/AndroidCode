package com.example.asd76.okonomiorgel.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Bluetooth;
import com.example.asd76.okonomiorgel.Listener.*;
import com.example.asd76.okonomiorgel.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


public class MainActivity extends Activity implements View.OnClickListener, View.OnTouchListener{

    NavigationView navigationView;
    boolean isDrawerOpened;
    DrawerLayout drawerLayout;
    ImageButton btn_drawer;
    Bluetooth bluetooth;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_drawer = (ImageButton)findViewById(R.id.btn_drawer);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        ImageButton piano_device = (ImageButton)findViewById(R.id.piano_device);



        //네비게이션 닫기 버튼 객체 획득
        ImageButton btn_nav_exit = (ImageButton)navigationView.getHeaderView(0).findViewById(R.id.navigation_exit);
        btn_nav_exit.setOnClickListener(this);

        //네비게이션 유저 이름 객체 획득
        TextView txt_user_name = (TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_user_name);

        //네비게이션 메뉴 객체 획득
        Menu nav_menu = (Menu)navigationView.getMenu();

        //유저 이름 획득
        SharedPreferences pref = getSharedPreferences("user_info", 0);
        String user_name = pref.getString("user_name", null);

        ///로그인 시
        if(user_name != null){
            //네비게이션 바 헤더 변경
            txt_user_name.setText(user_name);
            //로그인 메뉴 비활성화
            nav_menu.findItem(R.id.menu_login).setVisible(false);
            //로그아웃 메뉴 활성화
            nav_menu.findItem(R.id.menu_logout).setVisible(true);
        }

        //로그아웃 시
        if(user_name == null){
            //네비게이션 바 헤더 변경
            txt_user_name.setText("비회원");
            //로그인 메뉴 활성화
            nav_menu.findItem(R.id.menu_login).setVisible(true);
            //로그아웃 메뉴 비활성화
            nav_menu.findItem(R.id.menu_logout).setVisible(false);
        }

        piano_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth = new Bluetooth();
                bluetooth.setContext(MainActivity.this);
                bluetooth.connect();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationItemLis(this));

        TextView btn_ranking = (TextView)findViewById(R.id.btn_main_ranking);
        TextView btn_sheet = (TextView)findViewById(R.id.btn_main_sheet);
        TextView btn_piano = (TextView)findViewById(R.id.btn_main_piano);
        TextView btn_share = (TextView)findViewById(R.id.btn_main_share);
        TextView btn_free= (TextView)findViewById(R.id.btn_main_free);

        btn_drawer.setOnClickListener(this);
        btn_ranking.setOnClickListener(this);
        btn_sheet.setOnClickListener(this);
        btn_piano.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_free.setOnClickListener(this);

        btn_ranking.setOnTouchListener(this);
        btn_sheet.setOnTouchListener(this);
        btn_piano.setOnTouchListener(this);
        btn_share.setOnTouchListener(this);
        btn_free.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        TextView touchedView = (TextView)findViewById(v.getId());

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                touchedView.setTextColor(Color.BLACK);
                touchedView.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            case MotionEvent.ACTION_UP:
                touchedView.setTextColor(getResources().getColor(R.color.oldTextColor));
                touchedView.setTypeface(Typeface.DEFAULT);
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_main_sheet:
                intent = new Intent(MainActivity.this, MySheetActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.btn_main_ranking:
                intent = new Intent(MainActivity.this, RankingBoardActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.btn_main_piano:
                intent = new Intent(MainActivity.this, PianoActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.btn_main_free:
                intent = new Intent(MainActivity.this, FreeBoardActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.btn_main_share:
                intent = new Intent(MainActivity.this, ScoreBoardActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.btn_drawer:
                drawerLayout.openDrawer(Gravity.START);
                isDrawerOpened = true;
                break;
            case R.id.navigation_exit:
                drawerLayout.closeDrawers();
        }
    }

    @Override
    public void onBackPressed() {
        if(isDrawerOpened)
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return false;
        else
            return super.onOptionsItemSelected(item);
    }*/

}
