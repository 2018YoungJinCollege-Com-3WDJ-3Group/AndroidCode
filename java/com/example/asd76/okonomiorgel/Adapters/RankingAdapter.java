package com.example.asd76.okonomiorgel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.LikeRecord;
import com.example.asd76.okonomiorgel.Response.RankingRecord;

import java.util.ArrayList;

/**
 * Created by asd76 on 2018-06-13.
 */

public class RankingAdapter extends BaseAdapter{

    private ArrayList<RankingRecord> rankingRecords;

    public RankingAdapter(ArrayList<RankingRecord> rankingRecords) {
        this.rankingRecords = rankingRecords;
    }

    @Override
    public int getCount() {
        return rankingRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return rankingRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ranking_item, parent, false);
        }

        RankingRecord record = rankingRecords.get(position);

        TextView rank = (TextView)convertView.findViewById(R.id.ranking_rank);
        TextView subject = (TextView)convertView.findViewById(R.id.ranking_subject);
        TextView writer = (TextView)convertView.findViewById(R.id.ranking_writer);
        TextView download_count = (TextView)convertView.findViewById(R.id.ranking_download_count);

        rank.setText(record.getRank() + "위");
        subject.setText(record.getSubject());
        writer.setText("작성자  " + record.getPrevious_writer());
        download_count.setText("다운 수 " + record.getDownload());

        return convertView;
    }
}
