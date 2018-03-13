package com.example.asd76.okonomiorgel;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
        items.add(new ListViewItem(R.drawable.play_play,
                "오르골 연주"));
        items.add(new ListViewItem(R.drawable.play_pianokey,
                "실시간 연주"));
        return items;
    }
}
