package com.example.asd76.okonomiorgel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.Score_info;

import java.util.ArrayList;

/**
 * Created by asd76 on 2018-03-26.
 */

public class MySheetAdapter extends BaseAdapter{

    private ArrayList<Score_info> sheetInfos;

    public MySheetAdapter(ArrayList<Score_info> items) {
        sheetInfos = items;
    }

    @Override
    public int getCount() {
        return sheetInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return sheetInfos.get(position);
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
            convertView = inflater.inflate(R.layout.my_sheet_item, parent, false);
        }

        Score_info item = sheetInfos.get(position);

        TextView titleView = (TextView)convertView.findViewById(R.id.my_sheet_title);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.my_sheet_image);

        titleView.setText(item.getSubject());
        imageView.setImageResource(R.drawable.ic_no_image);

        return convertView;
    }

}
