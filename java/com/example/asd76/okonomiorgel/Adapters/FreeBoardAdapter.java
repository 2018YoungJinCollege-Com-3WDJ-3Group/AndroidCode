package com.example.asd76.okonomiorgel.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.FreeBoardRecord;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by asd76 on 2018-03-19.
 */

public class FreeBoardAdapter extends BaseAdapter{

    private ArrayList<FreeBoardRecord> freeBoardRecords;

    public FreeBoardAdapter(ArrayList<FreeBoardRecord> freeBoardRecords) {
        this.freeBoardRecords = freeBoardRecords;
    }

    @Override
    public int getCount() {
        return freeBoardRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return freeBoardRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.free_board_item, parent, false);
        }

        //레이아웃의 뷰 참조 획득
        TextView title = (TextView) convertView.findViewById(R.id.free_board_title);
        TextView reg_date = (TextView) convertView.findViewById(R.id.free_board_date);

        FreeBoardRecord record = freeBoardRecords.get(position);

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(record.getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int today = Calendar.getInstance().get(Calendar.DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int postDate = calendar.get(Calendar.DATE);

        String itemDate;
        if(postDate == today){
            itemDate = new java.text.SimpleDateFormat("hh:mm").format(date);
        }else{
            itemDate = new java.text.SimpleDateFormat("yy.MM.dd").format(date);
        }

        //각 뷰에 현재 포지션의 아이템 값 설정
        title.setText(record.getTitle());
        reg_date.setText(itemDate);

        return convertView;
    }

}//Adapter
