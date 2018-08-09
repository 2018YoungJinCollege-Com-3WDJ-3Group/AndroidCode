package com.example.asd76.okonomiorgel.Response;

import android.widget.Spinner;

import com.google.gson.annotations.SerializedName;

import java.util.StringTokenizer;

/**
 * Created by asd76 on 2018-03-20.
 */

public class ScoreBoard_post {

    private Boolean isLiked;
    private int post_id;
    private int score_id;
    private String previous_writer;
    private String title;
    private String comment;
    private int price;
    private int download;
    private int like;
    private String created_at;
    private String scorestring;
    private int thumnail_id;
    private String subject;
    private String bca_value;

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean liked) {
        isLiked = liked;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getScore_id() {
        return score_id;
    }

    public void setScore_id(int score_id) {
        this.score_id = score_id;
    }

    public String getPrevious_writer() {
        return previous_writer;
    }

    public void setPrevious_writer(String previous_writer) {
        this.previous_writer = previous_writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getScorestring() {
        return scorestring;
    }

    public void setScorestring(String scorestring) {
        this.scorestring = scorestring;
    }

    public int getThumnail_id() {
        return thumnail_id;
    }

    public void setThumnail_id(int thumnail_id) {
        this.thumnail_id = thumnail_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBca_value() {
        return bca_value;
    }

    public void setBca_value(String bca_value) {
        this.bca_value = bca_value;
    }
}
