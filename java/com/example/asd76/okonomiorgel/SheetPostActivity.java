package com.example.asd76.okonomiorgel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.Post;
import com.example.asd76.okonomiorgel.Response.Sheet;
import com.example.asd76.okonomiorgel.Response.purchaseCheck;

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

public class SheetPostActivity extends Activity {

    OkonomiOrgelService service;
    String scoreString;
    int tempo;
    Sheet sheet;
    Post post;
    Boolean isRun = false;
    PreviewThread previewThread;
    Boolean isLiked = false;
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

        //액션바 타이틀 지우기
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    previewThread = new PreviewThread(SheetPostActivity.this, scoreString, tempo);
                    previewThread.execute();
                    isRun = true;
                    preview_btn.setImageResource(R.drawable.stop_preview);

                }else{
                    stopPreview();
                    isRun = false;
                    preview_btn.setImageResource(R.drawable.play_preview);
                }
            }
        });

        btn_like = (ImageButton) findViewById(R.id.sheet_post_like);
        //좋아요 버튼 클릭 리스너
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //데이터 전송 후 현재 like 수 받아오기
                if(!isLiked)
                    btn_like.setImageResource(R.drawable.ic_like_after);
                else
                    btn_like.setImageResource(R.drawable.ic_like_before);

                isLiked = !isLiked;
            }
        });

        btn_buy = (ImageButton) findViewById(R.id.sheet_post_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_id != -1)
                    sheetPurchase();
                else
                    Toast.makeText(SheetPostActivity.this, "로그인 후에 구입 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //게시글 가져오기
        getPost();

    }//end of onCreate

    public void getPost(){

        final Call<ArrayList<Post>> response = service.getSheetBoardPost(postNum);
        response.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                if(response.isSuccessful() && response != null){
                    ArrayList<Post> posts = response.body();
                    post = new Post();
                    Log.e("posts size", posts.size()+"");

                    post.setPost_id(posts.get(0).getPost_id());
                    post.setBrd_id(posts.get(1).getBrd_id());
                    post.setWriter(posts.get(2).getWriter());
                    post.setScore_id(posts.get(3).getScore_id());
                    post.setPrice(posts.get(4).getPrice());
                    post.setCategory(posts.get(5).getCategory());
                    post.setCount(posts.get(6).getCount());
                    post.setLike(posts.get(7).getLike());
                    post.setTitle(posts.get(8).getTitle());
                    post.setBody(posts.get(9).getBody());
                    post.setCreated_at(posts.get(10).getCreated_at());
                    post.setScore_string(posts.get(11).getScore_string());
                    post.setScore_thumnail(posts.get(12).getScore_thumnail());
                    post.setScore_title(posts.get(13).getScore_title());

                    getSheetInfo();
                    buyEnable();
                    setPost(post);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Log.e("onFailure", t.getMessage());

            }
        });

    }

   public void getSheet(int score_id){
       Call<ArrayList<Sheet>> response = service.getSheetBoardSheet(score_id);
       response.enqueue(new Callback<ArrayList<Sheet>>() {
           @Override
           public void onResponse(Call<ArrayList<Sheet>> call, Response<ArrayList<Sheet>> response) {
               if(response.isSuccessful() && response.body() != null){
                   ArrayList<Sheet> sheets = response.body();
                   sheet = sheets.get(0);
                   getSheetInfo();
               }
           }

           @Override
           public void onFailure(Call<ArrayList<Sheet>> call, Throwable t) {
               Log.e("reponse", "onFailure");
           }
       });
   }

   public void getSheetInfo(){
       String str = post.getScore_string();
       StringTokenizer st = new StringTokenizer(str, ";");
       ArrayList temp = new ArrayList();
       while(st.hasMoreTokens()){
           temp.add(st.nextToken());
       }
       tempo = Integer.parseInt(temp.get(0).toString());
       scoreString = temp.get(1).toString();
       Log.e("tempo : ", tempo+"");
       Log.e("scoreString : ", scoreString+"");

   }

    public void purchaseAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("악보 구매 성공");
        builder.setMessage("악보 관리 페이지로 이동하시겠습니까?");
        builder.setPositiveButton("이동", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SheetPostActivity.this, MySheetActivity.class);
                SheetPostActivity.this.startActivity(intent);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void sheetPurchase(){

        final Call<purchaseCheck> response = service.sheetPurchase(user_id, post.getScore_id(), post.getPost_id());
        response.enqueue(new Callback<purchaseCheck>() {
            @Override
            public void onResponse(Call<purchaseCheck> call, Response<purchaseCheck> response) {
                if(response.isSuccessful() && response.body() != null){
                    purchaseCheck check = response.body();
                    String result = check.getCheck();
                    Log.e("sheetPurchasecheck   : ", "/"+result + "/");
                    switch (result){
                        case "save":
                            Toast.makeText(SheetPostActivity.this, "구매 성공", Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            Toast.makeText(SheetPostActivity.this, "이미 구매한 악보입니다.", Toast.LENGTH_SHORT).show();
                            break;
                        case "pointFail":
                            Toast.makeText(SheetPostActivity.this, "보유 포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } else{
                    Log.e("sheetPurchaseresponse", "is null");
                    if(!response.isSuccessful()) Log.e("majide", "null");
                }
            }

            @Override
            public void onFailure(Call<purchaseCheck> call, Throwable t) {
                Log.e("onFailure", ""+t.getMessage());
            }
        });
    }

    public void setPost(Post post){

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
            d = format.parse(post.getCreated_at());
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

        title.setText(post.getTitle());
        seller.setText(post.getWriter());
        date.setText(itemDate);
        sellnum.setText("판매수 " + post.getCount());
        contents.setText(post.getBody());
        price.setText(post.getPrice() + "P");
        num_like.setText(post.getLike()+"");

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

        if(post.getWriter().equals(user_name)){
            btn_buy.setImageResource(R.drawable.ic_buy_disable);
            btn_buy.setEnabled(false);
        }
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
