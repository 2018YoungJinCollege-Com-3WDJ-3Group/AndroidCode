package com.example.asd76.okonomiorgel.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.asd76.okonomiorgel.Adapter.MySheetAdapter;
import com.example.asd76.okonomiorgel.PlayActivity;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.Sheet;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_downSheet extends ListFragment {

    MySheetAdapter adapter;
    ArrayList<Sheet> items;
    AlertDialog.Builder alertDialogBuilder;
    ArrayAdapter dialogAdapter;
    int selectedItem;

    public fragment_downSheet() {
        // Required empty public constructor
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        selectedItem = position;

        ArrayList list = new ArrayList();
        list.add("악보 연주");
        list.add("악보 편집");
        list.add("악보 삭제");

        dialogAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, list);

        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //다이얼로그 지우기
                dialog.dismiss();
                switch (which){
                    case 0:
                        Intent intent=new Intent(getActivity(),PlayActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putParcelable("sheet",(Sheet)adapter.getItem(selectedItem));
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                        break;
                    case 1:
                        break;
                }
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        items = bundle.getParcelableArrayList("downFragItems");
        adapter = new MySheetAdapter(items);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }
}
