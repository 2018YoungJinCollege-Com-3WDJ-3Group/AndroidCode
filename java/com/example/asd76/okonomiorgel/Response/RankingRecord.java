package com.example.asd76.okonomiorgel.Response;

import android.support.annotation.NonNull;

/**
 * Created by asd76 on 2018-06-13.
 */

public class RankingRecord implements Comparable<RankingRecord>{

    private String subject;
    private int download;
    private String previous_writer;
    private int rank;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public String getPrevious_writer() {
        return previous_writer;
    }

    public void setPrevious_writer(String previous_writer) {
        this.previous_writer = previous_writer;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public int compareTo(@NonNull RankingRecord o) {
        if(rank < o.getRank())
            return 1;
        else if(rank == o.getRank())
            return 0;
        else
            return -1;
    }
}
