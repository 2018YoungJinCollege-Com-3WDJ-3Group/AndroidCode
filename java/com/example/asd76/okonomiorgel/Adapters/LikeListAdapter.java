package com.example.asd76.okonomiorgel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.LikeRecord;
import com.example.asd76.okonomiorgel.Response.PurchaseRecords;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by asd76 on 2018-05-10.
 */

public class LikeListAdapter extends BaseAdapter{

    private ArrayList<LikeRecord> likeRecords;

    public LikeListAdapter(ArrayList<LikeRecord> likeRecords) {
        this.likeRecords = likeRecords;
    }

    public void sortItem(String sort){
        switch (sort){
            case "최신순":
                Collections.sort(likeRecords);
                break;
            case "높은 가격순":
                Collections.sort(likeRecords, new Comparator<LikeRecord>() {
                    @Override
                    public int compare(LikeRecord o1, LikeRecord o2) {
                        if(o1.getPrice() < o2.getPrice())
                            return 1;
                        else if(o1.getPrice() > o2.getPrice())
                            return -1;
                        else
                            return 0;
                    }
                });
                break;
            case "낮은 가격순":
                Collections.sort(likeRecords, new Comparator<LikeRecord>() {
                    @Override
                    public int compare(LikeRecord o1, LikeRecord o2) {
                        if(o1.getPrice() > o2.getPrice())
                            return 1;
                        else if(o1.getPrice() < o2.getPrice())
                            return -1;
                        else
                            return 0;
                    }
                });
                break;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return likeRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return likeRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.like_record_item, parent, false);
        }

        LikeRecord record = likeRecords.get(position);

        TextView title = convertView.findViewById(R.id.like_record_title);
        TextView price = convertView.findViewById(R.id.like_record_price);

        title.setText(record.getTitle());
        price.setText(record.getPrice() + "P");

        return convertView;
    }
}
