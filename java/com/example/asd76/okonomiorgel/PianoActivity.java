package com.example.asd76.okonomiorgel;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Created by asd76 on 2018-03-08.
 */

public class PianoActivity extends FragmentActivity implements View.OnClickListener{

    private static final int REQUEST_ENABLE_BT = 1;

    ScrollView scrollView;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    BluetoothSocket socket = null;
    ArrayList<String> deviceNameList;
    ArrayList<String > deviceAddressList;
    ArrayAdapter adapter;
    AlertDialog.Builder alertDialogBuilder;
    OutputStream ops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //액션바 숨기기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //풀스크린
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piano);

        //디바이스 ArrayList
        deviceNameList = new ArrayList<>();
        deviceAddressList = new ArrayList<>();

        //리스트 어댑터
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, deviceNameList);

        //스크롤 위치 초기화
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, scrollView.getBottom());
            }
        });

        //피아노키 이벤트 설정
        Button testkey = (Button)findViewById(R.id.testKey);
        testkey.setOnClickListener(this);
        Button a = (Button)findViewById(R.id.A);
        a.setOnClickListener(this);

        //브로드캐스트 리시버 등록
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter);

        //블루투스 어댑터 획득
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //블루투스 활성화
        if(!bluetoothAdapter.isEnabled()){
            setBluetoothEnable();
        }

        //디바이스 목록 호출
        showSelectDialog();

    }//onCreate

    @Override
    public void onClick(View v) {
        //데이터 전송
        if(socket == null|| !socket.isConnected()){
            Toast.makeText(this, "디바이스와 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
        }else if(socket.isConnected()){
            sendData("A");
        }
    }

    public void setBluetoothEnable(){
        Intent bi = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(bi, REQUEST_ENABLE_BT);
    }

    public void getPairedDevices (){
        Set deviceSet = bluetoothAdapter.getBondedDevices();
        Iterator it = deviceSet.iterator();
        while(it.hasNext()){
            BluetoothDevice device = (BluetoothDevice)it.next();
            deviceNameList.add(device.getName());
            deviceAddressList.add(device.getAddress());
        }
    }

    public void showSelectDialog(){

        //주변 기기 검색
        bluetoothAdapter.startDiscovery();
        //페어링된 디바이스 받아오기
        getPairedDevices();

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("디바이스를 선택하세요");
        alertDialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //다이얼로그 지우기
                dialog.dismiss();
                //선택된 디바이스 객체를 설정
                device = bluetoothAdapter.getRemoteDevice(deviceAddressList.get(which));
                //선택된 디바이스와 연결
                connectToSelectedDevice();
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    public void connectToSelectedDevice(){

        //디바이스 검색 중지
        bluetoothAdapter.cancelDiscovery();
        ParcelUuid[] uuids = device.getUuids();

        try{
            socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            socket.connect();
            ops = socket.getOutputStream();
            
            if(socket.isConnected()){
                Toast.makeText(this, "디바이스와 연결되었습니다.", Toast.LENGTH_SHORT).show();
                sendData("(");
            }

        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void sendData(String data){
        try{
            ops.write(data.getBytes());
        }catch(Exception e){
            Toast.makeText(this, "데이터 전송 중 오류 발생", Toast.LENGTH_SHORT).show();
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        boolean overlapFlag = false;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //디바이스 중복 제거
                for(int i = 0; i < deviceAddressList.size(); i++){

                    //디바이스 mac 주소가 중복되면 플래그 true
                    if(deviceAddressList.get(i) == device.getAddress()){
                        overlapFlag = true;
                    }
                    //마지막 검사 후 플래그가 false 상태면 리스트에 추가
                    if(i == (deviceAddressList.size() -1) && overlapFlag == false){
                        deviceNameList.add(device.getName());
                        deviceAddressList.add(device.getAddress());
                        adapter.notifyDataSetChanged();
                        overlapFlag = false;
                    }
                }
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            //블루투스 활성화
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    Toast.makeText(this, "블루투스가 활성화 되었습니다.", Toast.LENGTH_SHORT).show();
                    showSelectDialog();
                }
                else if(resultCode == RESULT_CANCELED)
                    Toast.makeText(this, "블루투스가 활성화 되지 못하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        sendData(")");
        try{ops.close();
            socket.close();
        }catch (Exception e){}

        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}//activity
