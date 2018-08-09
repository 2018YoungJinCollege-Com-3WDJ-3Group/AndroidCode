package com.example.asd76.okonomiorgel.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Bluetooth;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.ScoreInfo;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-03-26.
 */

public class PlayActivity extends FragmentActivity {

    String scoreString;
    ImageButton btn_play;
    ImageButton btn_pause;
    ImageButton btn_stop;
    DonutProgress progressBar;
    TextView progress_time;
    changeProgressBar changeProgressBar;
    ScoreInfo sheetInfo;
    Double tempo;
    int timeIs;
    Boolean isPlaying = false;
    Boolean isPressed = false;
    Bitmap thumbnail;
    Bluetooth bluetooth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        bluetooth = (Bluetooth)getApplicationContext();
        bluetooth.setContext(this);

        if(bluetooth == null || !bluetooth.socket.isConnected()){
            Toast.makeText(this, "블루투스 에러", Toast.LENGTH_SHORT).show();
            finish();
        }

        //재생할 악보 정보 획득
        Bundle bundle = getIntent().getExtras();
        sheetInfo = bundle.getParcelable("sheet");

        if(sheetInfo == null){
            Toast.makeText(this, "악보 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        progress_time = (TextView)findViewById(R.id.progress_time);
        progressBar = (DonutProgress) findViewById(R.id.progress_bar);
        btn_play = (ImageButton)findViewById(R.id.btn_play);
        btn_pause = (ImageButton)findViewById(R.id.btn_pause);
        btn_stop = (ImageButton)findViewById(R.id.btn_stop);

        //뒤로가기 버튼 설정
        ImageButton btn_exit = (ImageButton)findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSheetInfo();

        //플레이어 버튼 리스너 추가
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetooth.socket == null || !bluetooth.socket.isConnected()){
                    Toast.makeText(PlayActivity.this, "디바이스와 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                }else if(bluetooth.socket.isConnected() && isPressed != true) {
                    bluetooth.sendData(scoreString);
                    bluetooth.sendData("n");
                    changeProgressBar = new changeProgressBar();
                    changeProgressBar.execute();
                    isPressed = true;
                }
            }
        });
        btn_play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        btn_play.setImageResource(R.drawable.score_post_preview_pressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_play.setImageResource(R.drawable.score_post_preview);
                        break;
                }
                return false;
            }
        });

        //btn_pause.setOnClickListener();
        //btn_stop.setOnClickListener();

        //썸네일 획득
        getThumbnail(sheetInfo.getThumbnail_path());

        TextView title = (TextView)findViewById(R.id.play_title);
        title.setText(sheetInfo.getSubject());

        scoreString = sheetInfo.getScorestring();

    }//end of onCreate

    public void getThumbnail(String thumbnail_path){

        String base_url = this.getString(R.string.base_url);
        OkonomiOrgelService service;

        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        Call<ResponseBody> response = service.getImage(base_url + thumbnail_path);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null){

                    InputStream is = null;
                    BufferedInputStream bis;

                    try{
                        is = response.body().byteStream();
                        bis = new BufferedInputStream(is);
                        thumbnail = BitmapFactory.decodeStream(bis);

                        ImageView thumbnail_view = (ImageView)findViewById(R.id.play_thumbnail);
                        thumbnail_view.setImageBitmap(thumbnail);

                    }catch (Exception e){
                        Log.e("downloadImage", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getSheetInfo(){

        //악보 템포와 스트링 분리
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

        //재생 시간 구하기
        Double a = 30/tempo;
        Double b = a * count; // 전부 시간
                //  분:초/분:초
        timeIs = b.intValue();
        progress_time.setText(0 + " : " + 0);
    }

    class changeProgressBar extends AsyncTask<Void, Integer, Void> {
        int totalTime;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isPlaying = true;
            totalTime = timeIs;
            progressBar.setMax(totalTime);
        }

        @Override
        protected Void doInBackground(Void... voids) {

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
        super.onDestroy();
    }
}