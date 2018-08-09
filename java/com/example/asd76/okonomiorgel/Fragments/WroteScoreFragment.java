package com.example.asd76.okonomiorgel.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Adapters.MyScoreAdapter;
import com.example.asd76.okonomiorgel.Activities.CreateScoreActivity;
import com.example.asd76.okonomiorgel.Activities.PlayActivity;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.ScoreInfo;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WroteScoreFragment extends ListFragment {

    MyScoreAdapter adapter;
    ArrayList<ScoreInfo> items;
    AlertDialog.Builder alertDialogBuilder;
    ArrayAdapter dialogAdapter;
    int selectedItem;

    public WroteScoreFragment() {
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        selectedItem = position;

        ArrayList list = new ArrayList();
        list.add("楽譜演奏");
        list.add("楽譜編集");
        list.add("楽譜削除");

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
                        bundle.putParcelable("sheet", (ScoreInfo)adapter.getItem(selectedItem));
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                        break;
                    case 1:
                        //악보 편집
                        intent = new Intent(getActivity(), CreateScoreActivity.class);
                        bundle = new Bundle();
                        bundle.putParcelable("editMode", (ScoreInfo)adapter.getItem(selectedItem));
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

        adapter = new MyScoreAdapter(items, getContext());
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.score_board_list));
    }

}
