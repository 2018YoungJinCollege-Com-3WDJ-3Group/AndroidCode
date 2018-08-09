package com.example.asd76.okonomiorgel;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by asd76 on 2018-07-10.
 */

public class Bluetooth extends Application{

    public Context context;
    public BluetoothAdapter bluetoothAdapter;
    public BluetoothDevice device;
    public static BluetoothSocket socket = null;
    public ArrayList<String> deviceNameList;
    public ArrayList<String > deviceAddressList;
    public ArrayAdapter adapter;
    public AlertDialog.Builder alertDialogBuilder;
    public static OutputStream ops;

    public Bluetooth() {
        //디바이스 ArrayList
        deviceNameList = new ArrayList<>();
        deviceAddressList = new ArrayList<>();
    }

    public void setContext(Context context){
        this.context = context;
        registerReceiver();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void registerReceiver(){
        //브로드캐스트 리시버 등록
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(broadcastReceiver, filter);
    }

    public void connect(){

        disconnectBluetooth();

        //블루투스 어댑터 획득
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //블루투스 활성화
        if(bluetoothAdapter.isEnabled()){
            Toast.makeText(context, "블루투스가 활성화 되었습니다.", Toast.LENGTH_SHORT).show();
            showSelectDialog();
        }else{
            Toast.makeText(context, "블루투스 활성화 X", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendData(String data){
        try{
            ops.write(data.getBytes());
        }catch(Exception e){

            Log.e("Exception", e.getMessage());
            //Toast.makeText(this, "데이터 전송 중 오류 발생 -> '"+data+"'", Toast.LENGTH_SHORT).show();
        }

    }

    public void showSelectDialog(){

        //리스트 어댑터
        adapter = new ArrayAdapter(context,
                android.R.layout.simple_list_item_1, deviceNameList);

        //주변 기기 검색
        bluetoothAdapter.startDiscovery();

        //페어링된 디바이스 받아오기
        getPairedDevices();

        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("デバイスを選んでください");
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

    public void getPairedDevices (){

        Set deviceSet = bluetoothAdapter.getBondedDevices();

        Iterator it = deviceSet.iterator();
        while(it.hasNext()){
            BluetoothDevice device = (BluetoothDevice)it.next();
            deviceNameList.add(device.getName());
            deviceAddressList.add(device.getAddress());
        }
    }

    public void connectToSelectedDevice(){

        //디바이스 검색 중지
        bluetoothAdapter.cancelDiscovery();
        while(true){
            if(!bluetoothAdapter.isDiscovering())
                break;
        }
        ParcelUuid[] uuids = device.getUuids();

        try{
            socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            socket.connect();
            ops = socket.getOutputStream();

            if(socket.isConnected()){
                Toast.makeText(context, "デバイスとコネクトされました", Toast.LENGTH_SHORT).show();
            }

        }catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            /*try {
                Log.e("","trying fallback...");

                socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                socket.connect();

                Log.e("","Connected");
            }
            catch (Exception e2) {
                Log.e("", "Couldn't establish Bluetooth connection!");
            }finally {
                if(socket.isConnected()){
                    Toast.makeText(context, "デバイスとコネクトされました", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "nnnn", Toast.LENGTH_SHORT).show();
                }
            }*/
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

    public void unregisterReceiver(){
        context.unregisterReceiver(broadcastReceiver);
    }
    public void disconnectBluetooth(){

        if(ops != null){
            try {ops.close();} catch (Exception e) {}
            ops = null;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(socket != null){
            try {socket.close();} catch (Exception e) {}
        }

    }

    @Override
    public void onTerminate() {
        unregisterReceiver();
        disconnectBluetooth();
        super.onTerminate();
    }
}
