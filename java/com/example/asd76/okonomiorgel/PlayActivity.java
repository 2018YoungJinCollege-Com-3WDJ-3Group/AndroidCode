package com.example.asd76.okonomiorgel;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Response.Score_info;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by asd76 on 2018-03-26.
 */

public class PlayActivity extends FragmentActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    BluetoothSocket socket = null;
    OutputStream ops;
    AlertDialog.Builder alertDialogBuilder;
    ArrayAdapter adapter;
    ArrayList<String> deviceNameList;
    ArrayList<String > deviceAddressList;
    String scoreString;
    ImageButton btn_play;
    ImageButton btn_pause;
    ImageButton btn_stop;
    ProgressBar progressBar;
    TextView progress_time;
    TextView progress_total_time;
    changeProgressBar changeProgressBar;
    Score_info sheetInfo;
    Double tempo;
    int timeIs;
    Boolean isPlaying = false;

    protected void onCreate(Bundle savedInstanceState) {

        //액션바 숨기기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //풀스크린
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Bundle bundle = getIntent().getExtras();
        sheetInfo = bundle.getParcelable("score_info");
        Log.e("onCreate", sheetInfo.getSubject());

        progress_total_time = (TextView)findViewById(R.id.progress_total_time);
        progress_time = (TextView)findViewById(R.id.progress_time);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        btn_play = (ImageButton)findViewById(R.id.btn_play);
        btn_pause = (ImageButton)findViewById(R.id.btn_pause);
        btn_stop = (ImageButton)findViewById(R.id.btn_stop);

        getSheetInfo();

        //플레이어 버튼 리스너 추가
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(socket == null || !socket.isConnected()){
                    Toast.makeText(PlayActivity.this, "디바이스와 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                }else if(socket.isConnected()) {
                    sendData(scoreString);
                    sendData("n");
                    changeProgressBar = new changeProgressBar();
                    changeProgressBar.execute();
                }
            }
        });
        //btn_pause.setOnClickListener();
        //btn_stop.setOnClickListener();

        //디바이스 ArrayList
        deviceNameList = new ArrayList<>();
        deviceAddressList = new ArrayList<>();

        //리스트 어댑터
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, deviceNameList);

        //브로드캐스트 리시버 등록
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter);

        //블루투스 어댑터 획득
        //bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //if(bluetoothAdapter == null) Log.d("bluetooth", "adpater is null");
        //블루투스 활성화
        //if(!bluetoothAdapter.isEnabled()){
          //  setBluetoothEnable();
        //}

        TextView title = (TextView)findViewById(R.id.play_title);
        title.setText(sheetInfo.getSubject());

        scoreString = sheetInfo.getScorestring();

        //showSelectDialog();

    }//end of onCreate

    public void getSheetInfo(){

        String str = sheetInfo.getScorestring();
        StringTokenizer st = new StringTokenizer(str, ";");
        ArrayList temp = new ArrayList();
        while(st.hasMoreTokens()){
            temp.add(st.nextToken());
        }
        tempo = Double.parseDouble(temp.get(0).toString());
        scoreString = temp.get(1).toString();
        int count = 0;
        for(int i = 0; i<scoreString.length(); i++){
            if(scoreString.charAt(i) == 'r'){
                count++;
            }
        }
        Double a = 30/tempo;
        Double b = a * count; // 전부 시간
                //  분:초/분:초
        timeIs = b.intValue();
        progress_time.setText(0 + " : " + 0);
        progress_total_time.setText(timeIs/60 +" : "+timeIs%60);
        Log.e("b", b+"");
    }

    class changeProgressBar extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            isPlaying = true;
            int totalTime = timeIs;
            Log.d("doInBackground", "timeIs" + totalTime);
            progressBar.setMax(totalTime);
            for(int i = 0; i <= totalTime; i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            isPlaying = false;
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress_time.setText(values[0]/60 + " : " + values[0]%60);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setProgress(0);
        }
    }

    public void setBluetoothEnable(){
        Intent bi = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(bi, REQUEST_ENABLE_BT);
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

    public void getPairedDevices (){
        Set deviceSet = bluetoothAdapter.getBondedDevices();
        Iterator it = deviceSet.iterator();
        while(it.hasNext()){
            BluetoothDevice device = (BluetoothDevice)it.next();
            deviceNameList.add(device.getName());
            deviceAddressList.add(device.getAddress());
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

    public void stopProgress(){
        if(changeProgressBar != null){
            changeProgressBar.cancel(true);
            changeProgressBar = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopProgress();
    }

    @Override
    protected void onDestroy() {

        stopProgress();

        try{ops.close();
            socket.close();
        }catch (Exception e){}

        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}