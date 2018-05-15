package com.example.asd76.okonomiorgel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.Like_record;

import java.util.ArrayList;

/**
 * Created by asd76 on 2018-05-10.
 */

public class LikeRecordAdapter extends BaseAdapter{

    private ArrayList<Like_record> likeRecords;

    public LikeRecordAdapter(ArrayList<Like_record> likeRecords) {
        this.likeRecords = likeRecords;
    }

    @Override
    public int getCount() {
        return likeRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return likeRecords.get(position);
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
            convertView = inflater.inflate(R.layout.like_record_item, parent, false);
        }

        Like_record record = likeRecords.get(position);

        return convertView;
    }
}
