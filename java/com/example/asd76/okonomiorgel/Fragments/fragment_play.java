package com.example.asd76.okonomiorgel.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asd76.okonomiorgel.Item.ListViewItem;
import com.example.asd76.okonomiorgel.PianoActivity;
import com.example.asd76.okonomiorgel.PlayActivity;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Adapter.TabListAdapter;

import java.util.ArrayList;

public class fragment_play extends ListFragment {

    TabListAdapter adapter;

    public fragment_play() {

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ListViewItem lvi = (ListViewItem)adapter.getItem(position);

        switch (lvi.getTitle()){
            case "실시간 연주":
                Intent pianoIntent = new Intent(getActivity(), PianoActivity.class);
                getActivity().startActivity(pianoIntent);
                break;
            case "오르골 연주":
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                getActivity().startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<ListViewItem> items = inputItems();
        adapter = new TabListAdapter(items);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public ArrayList inputItems(){
        ArrayList<ListViewItem> items = new ArrayList<>();
        items.add(new ListViewItem(R.drawable.play_list,
                "오르골 연주"));
        items.add(new ListViewItem(R.drawable.main_piano,
                "실시간 연주"));
        return items;
    }
}
