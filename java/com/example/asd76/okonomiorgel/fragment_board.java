package com.example.asd76.okonomiorgel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class fragment_board extends ListFragment {

    TabListAdapter adapter;

    public fragment_board() {
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
        items.add(new ListViewItem(R.drawable.board_free,
                "자유게시판"));
        items.add(new ListViewItem(R.drawable.board_share,
                "악보 공유 게시판"));
        items.add(new ListViewItem(R.drawable.board_rank,
                "랭킹 게시판"));
        return items;
    }
}
