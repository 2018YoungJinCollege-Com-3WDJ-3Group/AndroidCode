package com.example.asd76.okonomiorgel.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asd76 on 2018-03-20.
 */

public class Post {

    @SerializedName("post_id")
    private int post_id;
    @SerializedName("brd_id")
    private int brd_id;
    @SerializedName("writer")
    private String writer;
    @SerializedName("score_id")
    private int score_id;
    @SerializedName("category")
    private String category;
    @SerializedName("price")
    private int price;
    @SerializedName("count")
    private int count;
    @SerializedName("like")
    private int like;
    @SerializedName("title")
    private String title;
    @SerializedName("body")
    private String body;
    @SerializedName("created_at")
    private String created_at;

    public int getScore_id() {
        return score_id;
    }

    public void setScore_id(int score_id) {
        this.score_id = score_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getBrd_id() {
        return brd_id;
    }

    public void setBrd_id(int brd_id) {
        this.brd_id = brd_id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
