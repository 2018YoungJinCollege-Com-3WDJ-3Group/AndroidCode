package com.example.asd76.okonomiorgel.Activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Bluetooth;
import com.example.asd76.okonomiorgel.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Created by asd76 on 2018-03-08.
 */

public class PianoActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{

    ScrollView scrollView;
    Boolean scrollable = true;
    Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_piano);

        bluetooth = (Bluetooth)getApplicationContext();
        bluetooth.setContext(this);


        if(bluetooth == null || !bluetooth.socket.isConnected()){
            Toast.makeText(this, "블루투스 에러", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            bluetooth.sendData("(");
        }

        final ImageButton piano_back = (ImageButton)findViewById(R.id.piano_back);
        final ImageButton piano_scroll = (ImageButton)findViewById(R.id.piano_scroll);

        piano_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        piano_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scrollable){
                    scrollable = false;
                    setScrollable();
                    piano_scroll.setImageResource(R.drawable.piano_scroll_disable);
                }
                else{
                    scrollable = true;
                    scrollView.setOnTouchListener(null);
                    piano_scroll.setImageResource(R.drawable.piano_scroll);
                }
            }
        });

        //스크롤 위치 초기화
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, scrollView.getBottom());
            }
        });

        //피아노키 이벤트 설정
        Button C0 = (Button)findViewById(R.id.C0);
        Button D0 = (Button)findViewById(R.id.D0);
        Button G0 = (Button)findViewById(R.id.G0);
        Button A0 = (Button)findViewById(R.id.A0);
        Button B0 = (Button)findViewById(R.id.B0);
        Button C1 = (Button)findViewById(R.id.C1);
        Button D1 = (Button)findViewById(R.id.D1);
        Button E1 = (Button)findViewById(R.id.E1);
        Button F1 = (Button)findViewById(R.id.F1);
        Button F1_ = (Button)findViewById(R.id.F1_);
        Button G1 = (Button)findViewById(R.id.G1);
        Button G1_ = (Button)findViewById(R.id.G1_);
        Button A1 = (Button)findViewById(R.id.A1);
        Button A1_ = (Button)findViewById(R.id.A1_);
        Button B1 = (Button)findViewById(R.id.B1);
        Button C2 = (Button)findViewById(R.id.C2);
        Button C2_ = (Button)findViewById(R.id.C2_);
        Button D2 = (Button)findViewById(R.id.D2);
        Button D2_ = (Button)findViewById(R.id.D2_);
        Button E2 = (Button)findViewById(R.id.E2);
        Button F2 = (Button)findViewById(R.id.F2);
        Button F2_ = (Button)findViewById(R.id.F2_);
        Button G2 = (Button)findViewById(R.id.G2);
        Button G2_ = (Button)findViewById(R.id.G2_);
        Button A2 = (Button)findViewById(R.id.A2);
        Button A2_ = (Button)findViewById(R.id.A2_);
        Button B2 = (Button)findViewById(R.id.B2);
        Button C3 = (Button)findViewById(R.id.C3);
        Button D3 = (Button)findViewById(R.id.D3);
        Button E3 = (Button)findViewById(R.id.E3);

        C0.setOnClickListener(this);
        D0.setOnClickListener(this);
        G0.setOnClickListener(this);
        A0.setOnClickListener(this);
        B0.setOnClickListener(this);
        C1.setOnClickListener(this);
        D1.setOnClickListener(this);
        E1.setOnClickListener(this);
        F1.setOnClickListener(this);
        F1_.setOnClickListener(this);
        G1.setOnClickListener(this);
        G1_.setOnClickListener(this);
        A1.setOnClickListener(this);
        A1_.setOnClickListener(this);
        B1.setOnClickListener(this);
        C2.setOnClickListener(this);
        C2_.setOnClickListener(this);
        D2.setOnClickListener(this);
        D2_.setOnClickListener(this);
        E2.setOnClickListener(this);
        F2.setOnClickListener(this);
        F2_.setOnClickListener(this);
        G2.setOnClickListener(this);
        G2_.setOnClickListener(this);
        A2.setOnClickListener(this);
        A2_.setOnClickListener(this);
        B2.setOnClickListener(this);
        C3.setOnClickListener(this);
        D3.setOnClickListener(this);
        E3.setOnClickListener(this);

    }//onCreate


    public void setScrollable(){
        scrollView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onClick(View v) {
        String data ="";
        //데이터 전송
        if(bluetooth.socket == null|| !bluetooth.socket.isConnected()){
            Toast.makeText(this, "디바이스와 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
        }else if(bluetooth.socket.isConnected()){
            switch (v.getId()){
                case R.id.C0:
                    data = "A";
                    break;
                case R.id.D0:
                    data = "C";
                    break;
                case R.id.G0:
                    data = "H";
                    break;
                case R.id.A0:
                    data = "J";
                    break;
                case R.id.B0:
                    data = "L";
                    break;
                case R.id.C1:
                    data = "M";
                    break;
                case R.id.D1:
                    data = "O";
                    break;
                case R.id.E1:
                    data = "Q";
                    break;
                case R.id.F1:
                    data = "R";
                    break;
                case R.id.G1:
                    data = "T";
                    break;
                case R.id.A1:
                    data = "V";
                    break;
                case R.id.B1:
                    data = "X";
                    break;
                case R.id.C2:
                    data = "Y";
                    break;
                case R.id.D2:
                    data = "[";
                    break;
                case R.id.E2:
                    data = "]";
                    break;
                case R.id.F2:
                    data = "^";
                    break;
                case R.id.G2:
                    data = "`";
                    break;
                case R.id.A2:
                    data = "b";
                    break;
                case R.id.B2:
                    data = "d";
                    break;
                case R.id.C3:
                    data = "e";
                    break;
                case R.id.D3:
                    data = "g";
                    break;
                case R.id.E3:
                    data = "i";
                    break;
                case R.id.F1_:
                    data = "S";
                    break;
                case R.id.G1_:
                    data = "U";
                    break;
                case R.id.A1_:
                    data = "W";
                    break;
                case R.id.C2_:
                    data = "Z";
                    break;
                case R.id.D2_:
                    data = "\\";
                    break;
                case R.id.F2_:
                    data = "_";
                    break;
                case R.id.G2_:
                    data = "a";
                    break;
                case R.id.A2_:
                    data = "c";
                    break;
            }
        bluetooth.sendData(data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_piano_scroll:

                if(scrollable){
                    scrollable = false;
                    scrollView.setOnTouchListener(this);
                    item.setIcon(R.drawable.piano_scroll_disable);
                }
                else{
                    scrollable = true;
                    scrollView.setOnTouchListener(null);
                    item.setIcon(R.drawable.piano_scroll);
                }

                return true;
            case R.id.menu_piano_device:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        bluetooth.sendData(")");
        super.onDestroy();
    }

}//activity
