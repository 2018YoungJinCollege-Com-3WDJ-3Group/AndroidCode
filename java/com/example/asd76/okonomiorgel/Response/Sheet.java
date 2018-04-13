package com.example.asd76.okonomiorgel.Response;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.asd76.okonomiorgel.Item.SheetBoardListItem;
import com.google.gson.annotations.SerializedName;

/**
 * Created by asd76 on 2018-03-26.
 */

public class Sheet implements Parcelable, Comparable<Sheet>{

    @SerializedName("score_id")
    private int score_id;
    private String writer;
    private String title;
    private String category;
    private String comment;
    private String scorestring;
    private String thumnail;
    private String created_at;

    public int getScore_id() {
        return score_id;
    }

    public void setScore_id(int score_id) {
        this.score_id = score_id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getScorestring() {
        return scorestring;
    }

    public void setScorestring(String scorestring) {
        this.scorestring = scorestring;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public static Creator getCREATOR() {
        return CREATOR;
    }

    public Sheet(){}

    public Sheet(Parcel in){
        score_id = in.readInt();
        title = in.readString();
        writer = in.readString();
        category = in.readString();
        comment = in.readString();
        scorestring = in.readString();
        thumnail = in.readString();
        created_at = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(score_id);
        dest.writeString(title);
        dest.writeString(writer);
        dest.writeString(category);
        dest.writeString(comment);
        dest.writeString(scorestring);
        dest.writeString(thumnail);
        dest.writeString(created_at);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Sheet createFromParcel(Parcel in) {
            return new Sheet(in);
        }

        @Override
        public Sheet[] newArray(int size) {
            return new Sheet[size];
        }
    };

    @Override
    public int compareTo(Sheet item) {
        return created_at.compareTo(item.created_at);
    }
}
