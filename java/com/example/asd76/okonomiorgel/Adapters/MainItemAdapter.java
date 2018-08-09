package com.example.asd76.okonomiorgel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.asd76.okonomiorgel.Item.MainItem;
import com.example.asd76.okonomiorgel.R;

import java.util.ArrayList;

/**
 * Created by asd76 on 2018-04-15.
 */

public class MainItemAdapter extends BaseAdapter {

    private ArrayList<MainItem> Items = new ArrayList<>();

    public MainItemAdapter(ArrayList<MainItem> items) {
        Items = items;
    }

    @Override
    public int getCount() {
        return Items.size();
    }

    @Override
    public Object getItem(int i) {
        return Items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.main_, viewGroup, false);
        }

        ImageView iconImageView = (ImageView)view.findViewById(R.id.main_icon);

        MainItem item = Items.get(i);

        iconImageView.setImageResource(item.getIcon());

        return view;
    }
}
