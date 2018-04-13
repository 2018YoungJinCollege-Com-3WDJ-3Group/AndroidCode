package com.example.asd76.okonomiorgel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.Response.FreeBoard;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by asd76 on 2018-03-27.
 */

public class FreePostActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_post);

        //액션바 타이틀 지우기
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int postNum = intent.getIntExtra("postNum", -1);

        //if(postNum == -1) return;

        Log.e("postNum", postNum+"");

        FreeBoard item = new FreeBoard();
        item.setCategory("클래식");
        item.setTitle("타이틀입니다");
        item.setWriter("황금비");
        item.setContents("내용...22222...");
        item.setNum(1);
        item.setHits(5);
        item.setReg_date("2018-03-27 14:20:47");
        item.setLike(18);
        setPost(item);
    }

    public void setPost(FreeBoard post){

        TextView category = (TextView)findViewById(R.id.free_post_category);
        TextView title = (TextView)findViewById(R.id.free_post_title);
        TextView writer = (TextView)findViewById(R.id.free_post_writer);
        TextView date = (TextView)findViewById(R.id.free_post_date);
        TextView hits = (TextView)findViewById(R.id.free_post_hits);
        TextView contents = (TextView)findViewById(R.id.free_post_contents);
        TextView likeNum = (TextView)findViewById(R.id.free_num_like);

        //날짜가 오늘과 같으면 시간으로 표시
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = null;
        try {
            d = format.parse(post.getReg_date());
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

        category.setText("[ " + post.getCategory() + " ]");
        title.setText(post.getTitle());
        writer.setText(post.getWriter());
        date.setText(itemDate);
        hits.setText("조회수 " + post.getHits());
        contents.setText(post.getContents());
        likeNum.setText(post.getLike()+"");
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
