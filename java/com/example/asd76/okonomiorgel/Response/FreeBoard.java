package com.example.asd76.okonomiorgel.Response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asd76 on 2018-03-27.
 */

public class FreeBoard implements Comparable<FreeBoard>{

    private int num;
    private String category;
    private int like;
    private String writer;
    private String title;
    private String contents;
    private int hits;
    private String reg_date;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    @Override
    public int compareTo(FreeBoard item) {
        if(num < item.getNum())
            return 1;
        else if(num == item.getNum())
            return 0;
        else
            return -1;
    }
}
