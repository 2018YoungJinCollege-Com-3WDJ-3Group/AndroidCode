package com.example.asd76.okonomiorgel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class fragment_musicsheet extends ListFragment {

    TabListAdapter adapter;

    public fragment_musicsheet() {
        // Required empty public constructor
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
        items.add(new ListViewItem(R.drawable.musicsheet_add,
                "악보 추가"));
        items.add(new ListViewItem(R.drawable.musicsheet_edit,
                "악보 편집"));
        items.add(new ListViewItem(R.drawable.musicsheet_traslator,
                "외부 악보 변환"));
        return items;
    }

}
