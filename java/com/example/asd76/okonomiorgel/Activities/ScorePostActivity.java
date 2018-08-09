package com.example.asd76.okonomiorgel.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.PreviewThread;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.CheckBoolean;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.ScoreBoard_post;
import com.example.asd76.okonomiorgel.Response.ScoreInfo;
import com.example.asd76.okonomiorgel.Response.CheckString;
import com.example.asd76.okonomiorgel.Response.pushLike;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-03-20.
 *
 * 좋아요 이벤트
 * 뒤로가기 구현
 */

public class ScorePostActivity extends Activity {

    OkonomiOrgelService service;
    String scoreString;
    int tempo;
    ScoreBoard_post scoreBoardpost;
    Boolean isRun = false;
    PreviewThread previewThread;
    int user_id = -1;
    String user_name = null;
    int postNum;
    ImageButton btn_buy;
    TextView price;
    TextView num_like;
    ImageButton btn_like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sheet_post);

        Intent intent = getIntent();
        postNum = intent.getIntExtra("postNum", -1);

        if(postNum == -1) finish();

        //유저 확인
        SharedPreferences pref = getSharedPreferences("user_info", 0);
        user_id = pref.getInt("user_id", -1);
        user_name = pref.getString("user_name", null);

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        num_like = (TextView)findViewById(R.id.num_like);

        //X 버튼 클릭 시 종료
        ImageButton btn_clear = (ImageButton)findViewById(R.id.sheet_post_clear);
        btn_clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ImageButton preview_btn = (ImageButton)findViewById(R.id.preview_icon);
        preview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("preview", "onClick");
                //미리듣기 쓰레드 시작
                if(!isRun){
                    previewThread = new PreviewThread(ScorePostActivity.this, scoreString, tempo);
                    previewThread.execute();
                    isRun = true;
                    preview_btn.setImageResource(R.drawable.score_post_stop);

                }else{
                    stopPreview();
                    isRun = false;
                    preview_btn.setImageResource(R.drawable.score_post_preview);

                }
            }
        });

        btn_like = (ImageButton) findViewById(R.id.sheet_post_like);
        //좋아요 버튼 클릭 리스너
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //비회원 처리
                if(user_id == -1){
                    Toast.makeText(ScorePostActivity.this, "로그인 후에 이용 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //데이터 전송 후 현재 like 수 받아오기
                final Call<pushLike> response = service.likePush(user_id, scoreBoardpost.getPost_id());
                response.enqueue(new Callback<pushLike>() {
                    @Override
                    public void onResponse(Call<pushLike> call, Response<pushLike> response) {
                        if(response.isSuccessful() && response != null){
                            pushLike pushLike = response.body();

                            if(pushLike.getSave().equals("heart")){
                                btn_like.setImageResource(R.drawable.ic_like_after);
                                num_like.setTextColor(Color.WHITE);
                            }else{
                                btn_like.setImageResource(R.drawable.ic_like_before);
                                num_like.setTextColor(Color.BLACK);
                            }

                            num_like.setText(pushLike.getLikeCount() + "");

                        }
                    }

                    @Override
                    public void onFailure(Call<pushLike> call, Throwable t) {

                    }
                });

            }
        });

        btn_buy = (ImageButton) findViewById(R.id.sheet_post_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_id != -1)
                    sheetPurchase();
                else
                    Toast.makeText(ScorePostActivity.this, "로그인 후에 구입 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //게시글 가져오기
        getPost();

    }//end of onCreate

    public void getPost(){

        final Call<ScoreBoard_post> response = service.getSheetBoardPost(postNum, user_id);
        response.enqueue(new Callback<ScoreBoard_post>() {
            @Override
            public void onResponse(Call<ScoreBoard_post> call, Response<ScoreBoard_post> response) {
                if(response.isSuccessful() && response != null){
                    scoreBoardpost = response.body();
                    getSheetInfo();
                    if(user_id != -1){
                        buyEnable();
                        setLikeBtn();
                    }

                    setScoreBoardpost(scoreBoardpost);
                }
            }

            @Override
            public void onFailure(Call<ScoreBoard_post> call, Throwable t) {
                Log.e("onFailure", t.getMessage());

            }
        });

    }

   public void getSheetInfo(){

       String str = scoreBoardpost.getScorestring();
       StringTokenizer st = new StringTokenizer(str, ";");
       ArrayList temp = new ArrayList();

       while(st.hasMoreTokens()){
           temp.add(st.nextToken());
       }

       tempo = Integer.parseInt(temp.get(0).toString());
       scoreString = temp.get(1).toString();
       Log.e("score_tempo : ", tempo+"");
       Log.e("scoreString : ", scoreString+"");

   }

    public void sheetPurchase(){

        final Call<CheckString> response = service.sheetPurchase(user_id, scoreBoardpost.getScore_id(), scoreBoardpost.getPost_id());
        response.enqueue(new Callback<CheckString>() {
            @Override
            public void onResponse(Call<CheckString> call, Response<CheckString> response) {
                if(response.isSuccessful() && response.body() != null){
                    CheckString check = response.body();
                    String result = check.getCheck();

                    switch (result){
                        case "save":
                            Toast.makeText(ScorePostActivity.this, "購入できました", Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(ScorePostActivity.this, "이미 구매한 악보입니다.", Toast.LENGTH_SHORT).show();
                            break;
                        case "pointFail":
                            Toast.makeText(ScorePostActivity.this, "보유 포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } else{
                    Log.e("sheetPurchaseresponse", "is null");
                }
            }

            @Override
            public void onFailure(Call<CheckString> call, Throwable t) {
                Log.e("onFailure", ""+t.getMessage());
            }
        });
    }

    public void setScoreBoardpost(ScoreBoard_post scoreBoardpost){

        //좋아요 여부 표시
        //악보 이름 표시
        TextView title = (TextView)findViewById(R.id.sheet_post_title);
        TextView seller = (TextView)findViewById(R.id.sheet_post_seller);
        TextView date = (TextView)findViewById(R.id.sheet_post_date);
        TextView sellnum = (TextView)findViewById(R.id.sheet_post_sellnum);
        TextView contents = (TextView)findViewById(R.id.sheet_post_contents);
        price = (TextView)findViewById(R.id.sheet_post_price);

        //날짜, 시간
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = null;
        try {
            d = format.parse(scoreBoardpost.getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int today = Calendar.getInstance().get(Calendar.DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int postDate = calendar.get(Calendar.DATE);

        String itemDate;
        if(postDate == today){
            itemDate = new java.text.SimpleDateFormat("hh:mm").format(d);
        }else{
            itemDate = new java.text.SimpleDateFormat("yy.MM.dd").format(d);
        }

        title.setText(scoreBoardpost.getTitle());
        seller.setText(scoreBoardpost.getPrevious_writer());
        date.setText(itemDate);
        sellnum.setText("販売数 " + scoreBoardpost.getDownload());
        contents.setText(scoreBoardpost.getComment());
        price.setText(scoreBoardpost.getPrice() + "P");
        num_like.setText(scoreBoardpost.getLike()+"");

    }

    public void stopPreview(){
        if(previewThread != null){
            Log.e("stopPreview", "sheetPreview is not null");
            previewThread.isPlaying = false;
            previewThread.getSoundPool().release();
            previewThread = null;
        }
    }

    public void buyEnable(){

        if(scoreBoardpost.getPrevious_writer().equals(user_name)){
            btn_buy.setImageResource(R.drawable.ic_buy_disable);
            btn_buy.setEnabled(false);
        }
    }

    public void setLikeBtn(){

        if(scoreBoardpost.getIsLiked())
            btn_like.setImageResource(R.drawable.ic_like_after);
        else
            btn_like.setImageResource(R.drawable.ic_like_before);

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

    @Override
    protected void onStop() {
        super.onStop();
        stopPreview();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPreview();
    }
}
