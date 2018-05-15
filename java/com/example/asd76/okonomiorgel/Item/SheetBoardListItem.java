package com.example.asd76.okonomiorgel.Item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asd76 on 2018-03-19.
 */

public class SheetBoardListItem implements Comparable<SheetBoardListItem>{

    @SerializedName("post_id")
    private int num;
    private int price;
    private String category;
    private String title;
    private int download;

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
