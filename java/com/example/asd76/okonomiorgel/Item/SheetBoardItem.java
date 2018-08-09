package com.example.asd76.okonomiorgel.Item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asd76 on 2018-03-19.
 */

public class SheetBoardItem implements Comparable<SheetBoardItem>{

    @SerializedName("post_id")
    private int num;
    private int price;
    private String category;
    private String title;
    private int download;
    private String thumbnail_path;

    public String getThumbnail_path() {
        return thumbnail_path;
    }

    public void setThumbnail_path(String thumbnail_path) {
        this.thumbnail_path = thumbnail_path;
    }

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
    public int compareTo(SheetBoardItem item) {
        if(num < item.getNum())
            return 1;
        else if(num == item.getNum())
            return 0;
        else
            return -1;
    }
}
