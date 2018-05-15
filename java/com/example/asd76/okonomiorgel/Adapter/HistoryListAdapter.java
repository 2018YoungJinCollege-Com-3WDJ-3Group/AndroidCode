package com.example.asd76.okonomiorgel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.Purchase_history;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by asd76 on 2018-05-09.
 */

public class HistoryListAdapter extends BaseAdapter{

    private ArrayList<Purchase_history> purchasehistoryArr;

    public HistoryListAdapter(ArrayList<Purchase_history> arr) {
        this.purchasehistoryArr = arr;
        Collections.sort(purchasehistoryArr);
    }

    public void sortItem(String sort){
        switch (sort){
            case "최신순":
                Collections.sort(purchasehistoryArr);
                break;
            case "높은 가격순":
                Collections.sort(purchasehistoryArr, new Comparator<Purchase_history>() {
                    @Override
                    public int compare(Purchase_history o1, Purchase_history o2) {
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
                Collections.sort(purchasehistoryArr, new Comparator<Purchase_history>() {
                    @Override
                    public int compare(Purchase_history o1, Purchase_history o2) {
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
        return purchasehistoryArr.size();
    }

    @Override
    public Object getItem(int position) {
        return purchasehistoryArr.get(position);
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
            convertView = inflater.inflate(R.layout.history_item, parent, false);
        }

        Purchase_history item = purchasehistoryArr.get(position);

        //날짜, 시간
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = null;
        try {
            d = format.parse(item.getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int today = Calendar.getInstance().get(Calendar.DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int postDate = calendar.get(Calendar.DATE);

        String itemDate;
        if(postDate == today){
            itemDate = new java.text.SimpleDateFormat("hh:mm").format(d);
        }else{
            itemDate = new java.text.SimpleDateFormat("yy.MM.dd").format(d);
        }

        TextView date = (TextView)convertView.findViewById(R.id.history_date);
        TextView seller = (TextView)convertView.findViewById(R.id.history_seller);
        TextView title = (TextView)convertView.findViewById(R.id.history_title);
        TextView price = (TextView)convertView.findViewById(R.id.history_price);

        date.setText(itemDate);
        seller.setText(item.getSeller_name());
        title.setText(item.getScore_subject());
        price.setText(item.getPrice() + "P");

        return convertView;
    }
}
