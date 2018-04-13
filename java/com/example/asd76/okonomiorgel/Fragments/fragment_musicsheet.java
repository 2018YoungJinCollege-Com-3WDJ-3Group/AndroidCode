package com.example.asd76.okonomiorgel.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asd76.okonomiorgel.Item.ListViewItem;
import com.example.asd76.okonomiorgel.MySheetActivity;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Adapter.TabListAdapter;

import java.util.ArrayList;

public class fragment_musicsheet extends ListFragment {

    TabListAdapter adapter;

    public fragment_musicsheet() {
        // Required empty public constructor
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ListViewItem lvi = (ListViewItem)adapter.getItem(position);

        switch (lvi.getTitle()){
            case "악보 편집":
                Intent intent = new Intent(getActivity(), MySheetActivity.class);
                getActivity().startActivity(intent);
                break;
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
        items.add(new ListViewItem(R.drawable.musicsheet_add,
                "악보 추가"));
        items.add(new ListViewItem(R.drawable.musicsheet_edit,
                "악보 편집"));
        items.add(new ListViewItem(R.drawable.main_transfer,
                "외부 악보 변환"));
        return items;
    }

}
