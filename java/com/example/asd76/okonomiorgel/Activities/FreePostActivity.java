package com.example.asd76.okonomiorgel.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.FreeBoardRecord;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-03-27.
 */

public class FreePostActivity extends AppCompatActivity{

    OkonomiOrgelService service;
    FreeBoardRecord post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_post);

        Intent intent = getIntent();
        int postNum = intent.getIntExtra("postNum", -1);

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        final Call<FreeBoardRecord> response = service.getFreeBoardPost(postNum);
        response.enqueue(new Callback<FreeBoardRecord>() {
            @Override
            public void onResponse(Call<FreeBoardRecord> call, Response<FreeBoardRecord> response) {
                if(response.isSuccessful() && response != null){
                    post = response.body();
                    setPost(post);
                }
            }

            @Override
            public void onFailure(Call<FreeBoardRecord> call, Throwable t) {

            }
        });
    }

    public void setPost(FreeBoardRecord post){

        TextView title = (TextView)findViewById(R.id.free_post_title);
        TextView writer = (TextView)findViewById(R.id.free_post_writer);
        TextView date = (TextView)findViewById(R.id.free_post_date);
        TextView contents = (TextView)findViewById(R.id.free_post_contents);

        //날짜가 오늘과 같으면 시간으로 표시
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
        writer.setText(post.getUser_id()+"");
        date.setText(itemDate);
        contents.setText(post.getComment());
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
}
