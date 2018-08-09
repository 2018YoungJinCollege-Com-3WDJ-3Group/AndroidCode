package com.example.asd76.okonomiorgel.Response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asd76 on 2018-03-26.
 */

public class ScoreInfo implements Parcelable, Comparable<ScoreInfo>{

    String bca_value;
    int score_id;
    String subject;
    String describe;
    String scorestring;
    String previous_writer;
    String created_at;
    private String thumbnail_path;

    public String getThumbnail_path() {
        return thumbnail_path;
    }

    public void setThumbnail_path(String thumbnail_path) {
        this.thumbnail_path = thumbnail_path;
    }

    public String getBca_value() {
        return bca_value;
    }

    public void setBca_value(String bca_value) {
        this.bca_value = bca_value;
    }

    public int getScore_id() {
        return score_id;
    }

    public void setScore_id(int score_id) {
        this.score_id = score_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getScorestring() {
        return scorestring;
    }

    public void setScorestring(String scorestring) {
        this.scorestring = scorestring;
    }

    public String getPrevious_writer() {
        return previous_writer;
    }

    public void setPrevious_writer(String previous_writer) {
        this.previous_writer = previous_writer;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ScoreInfo() {

    }

    public ScoreInfo(Parcel in){
        bca_value = in.readString();
        score_id = in.readInt();
        subject = in.readString();
        describe = in.readString();
        scorestring = in.readString();
        previous_writer = in.readString();
        created_at = in.readString();
        thumbnail_path = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bca_value);
        dest.writeInt(score_id);
        dest.writeString(subject);
        dest.writeString(describe);
        dest.writeString(scorestring);
        dest.writeString(previous_writer);
        dest.writeString(created_at);
        dest.writeString(thumbnail_path);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public ScoreInfo createFromParcel(Parcel in) {
            return new ScoreInfo(in);
        }

        @Override
        public ScoreInfo[] newArray(int size) {
            return new ScoreInfo[size];
        }
    };

    @Override
    public int compareTo(ScoreInfo item) {
        return created_at.compareTo(item.created_at);
    }
}
