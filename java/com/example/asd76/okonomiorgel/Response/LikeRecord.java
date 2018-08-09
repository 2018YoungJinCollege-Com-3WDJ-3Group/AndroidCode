package com.example.asd76.okonomiorgel.Response;

import android.support.annotation.NonNull;

/**
 * Created by asd76 on 2018-05-10.
 */

public class LikeRecord implements Comparable<LikeRecord>{

    private int favorite_id;
    private int user_id;
    private String title;
    private int price;
    private int post_id;
    private int score_id;

    public int getFavorite_id() {
        return favorite_id;
    }

    public void setFavorite_id(int favorite_id) {
        this.favorite_id = favorite_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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


    @Override
    public int compareTo(@NonNull LikeRecord o) {
        if(favorite_id < o.getFavorite_id())
            return 1;
        else if(favorite_id == o.getFavorite_id())
            return 0;
        else
            return -1;
    }
}
