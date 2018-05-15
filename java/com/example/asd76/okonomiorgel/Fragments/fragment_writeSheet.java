package com.example.asd76.okonomiorgel.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.asd76.okonomiorgel.Adapter.MySheetAdapter;
import com.example.asd76.okonomiorgel.CreateSheetActivity;
import com.example.asd76.okonomiorgel.PlayActivity;
import com.example.asd76.okonomiorgel.Response.Score_info;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_writeSheet extends ListFragment {

    MySheetAdapter adapter;
    ArrayList<Score_info> items;
    AlertDialog.Builder alertDialogBuilder;
    ArrayAdapter dialogAdapter;
    int selectedItem;

    public fragment_writeSheet() {
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
                Intent intent;
                Bundle bundle;
                switch (which){
                    case 0:
                        intent=new Intent(getActivity(),PlayActivity.class);
                        bundle=new Bundle();
                        bundle.putParcelable("sheet", (Score_info)adapter.getItem(selectedItem));
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                        break;
                    case 1:
                        //악보 편집
                        intent = new Intent(getActivity(), CreateSheetActivity.class);
                        bundle = new Bundle();
                        bundle.putParcelable("editMode", (Score_info)adapter.getItem(selectedItem));
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
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
        items = bundle.getParcelableArrayList("writeFragItems");

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
