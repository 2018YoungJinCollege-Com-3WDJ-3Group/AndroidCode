package com.example.asd76.okonomiorgel.Response;

import android.support.annotation.NonNull;

/**
 * Created by asd76 on 2018-05-09.
 */

public class Purchase_history implements Comparable<Purchase_history>{

    private int order_id;
    private String created_at;
    private String seller_name;
    private String score_subject;
    private int price;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getScore_subject() {
        return score_subject;
    }

    public void setScore_subject(String score_subject) {
        this.score_subject = score_subject;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int compareTo(@NonNull Purchase_history item) {
        if(order_id < item.getOrder_id())
            return 1;
        else if(order_id == item.getOrder_id())
            return 0;
        else
            return -1;
    }
}
