package com.example.asd76.okonomiorgel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by asd76 on 2018-03-06.
 */

public class TabListAdapter extends BaseAdapter{

    private ArrayList<ListViewItem> Items = new ArrayList<>();

    public TabListAdapter(ArrayList<ListViewItem> items) {
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
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
        }

        ImageView iconImageView = (ImageView)view.findViewById(R.id.item_imageView);
        TextView titleTextView = (TextView)view.findViewById(R.id.item_title);

        ListViewItem item = Items.get(i);

        iconImageView.setImageResource(item.getIcon());
        titleTextView.setText(item.getTitle());

        return view;
    }
}
