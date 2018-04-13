package com.example.asd76.okonomiorgel.Item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asd76 on 2018-03-19.
 */

public class SheetBoardListItem implements Comparable<SheetBoardListItem>{

    @SerializedName("post_id")
    private int num;
    private String writer;
    private int price;
    private String category;
    private int count;
    private String title;
    private String created_at;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    //id로 게시글 생성순으로 정렬
    @Override
    public int compareTo(SheetBoardListItem item) {
        if(num < item.getNum())
            return 1;
        else if(num == item.getNum())
            return 0;
        else
            return -1;
    }
}
