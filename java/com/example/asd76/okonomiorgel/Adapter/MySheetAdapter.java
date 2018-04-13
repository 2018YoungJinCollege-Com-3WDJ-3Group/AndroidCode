package com.example.asd76.okonomiorgel.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.Item.SheetBoardListItem;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.Sheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by asd76 on 2018-03-26.
 */

public class MySheetAdapter extends BaseAdapter{

    private ArrayList<Sheet> originItems;
    private ArrayList<Sheet> filteredItems;
    Context context;

    public MySheetAdapter(ArrayList<Sheet> items) {

        originItems = new ArrayList<>();
        filteredItems = items;
        originItems = items;

        Log.d("filteredItems", ((Sheet)filteredItems.get(0)).getTitle());
    }

    @Override
    public int getCount() {
        Log.d("getCount", filteredItems.size() + "");
        return filteredItems.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItems.get(position);
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

        Sheet item = filteredItems.get(position);

        TextView titleView = (TextView)convertView.findViewById(R.id.my_sheet_title);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.my_sheet_image);

        titleView.setText(item.getTitle());
        imageView.setImageResource(R.drawable.ic_no_image);

        return convertView;
    }

}
