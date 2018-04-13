package com.example.asd76.okonomiorgel.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.Item.SheetBoardListItem;
import com.example.asd76.okonomiorgel.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by asd76 on 2018-03-19.
 */

public class SheetBoardAdapter extends BaseAdapter implements Filterable{

    Filter itemFilter;
    private ArrayList<SheetBoardListItem> originItemList = new ArrayList<>();
    private ArrayList<SheetBoardListItem> filteredItemList = originItemList;

    public void addItem(SheetBoardListItem item){
        originItemList.add(item);
        Collections.sort(originItemList);
    }

    public void clearItem(){
        originItemList.clear();
    }
    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sheet_board_list_item, parent, false);
        }

        //레이아웃의 뷰 참조 획득
        TextView categoty = (TextView) convertView.findViewById(R.id.sheet_board_category);
        TextView title = (TextView) convertView.findViewById(R.id.sheet_board_title);
        TextView seller = (TextView) convertView.findViewById(R.id.sheet_board_seller);
        TextView created_at = (TextView) convertView.findViewById(R.id.sheet_board_created_at);
        TextView sellnum = (TextView) convertView.findViewById(R.id.sheet_board_sellnum);
        TextView price = (TextView) convertView.findViewById(R.id.sheet_board_price);

        SheetBoardListItem item = filteredItemList.get(position);

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(item.getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int today = Calendar.getInstance().get(Calendar.DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int postDate = calendar.get(Calendar.DATE);

        String itemDate;
        if(postDate == today){
            itemDate = new java.text.SimpleDateFormat("hh:mm").format(date);
        }else{
            itemDate = new java.text.SimpleDateFormat("yy.MM.dd").format(date);
        }

        //각 뷰에 현재 포지션의 아이템 값 설정
        categoty.setText("[" + item.getCategory()+"]");
        title.setText(item.getTitle());
        seller.setText(item.getWriter());
        created_at.setText(itemDate);
        sellnum.setText("판매수 "+ item.getCount());
        price.setText(item.getPrice() + "P");

        return convertView;
    }

    public void sortItem(String sort){
        switch (sort){
            case "최신순":
                Collections.sort(filteredItemList);
                break;
            case "판매순":
                Collections.sort(filteredItemList, new Comparator<SheetBoardListItem>() {
                    @Override
                    public int compare(SheetBoardListItem o1, SheetBoardListItem o2) {
                        if(o1.getCount() < o2.getCount())
                            return 1;
                        else if(o1.getCount() > o2.getCount())
                            return -1;
                        else
                            return 0;
                    }
                });
                break;
            case "낮은 가격순":
                Collections.sort(filteredItemList, new Comparator<SheetBoardListItem>() {
                    @Override
                    public int compare(SheetBoardListItem o1, SheetBoardListItem o2) {
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
    public Filter getFilter() {
        if(itemFilter == null)
            itemFilter = new itemFilter();
        return itemFilter;
    }

    private class itemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            ArrayList<SheetBoardListItem> tempItemList = new ArrayList<>();

            //검색할 값이 없으면 기존 배열 반환
            if(constraint == null || constraint.equals("") || constraint.length() == 0){
                results.values = originItemList;
                results.count = originItemList.size();
            }else{
                //기존 배열의 제목과 검색 값을 비교
                for(SheetBoardListItem item : originItemList){
                    if(item.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())){
                        tempItemList.add(item);
                    }
                }
                results.values = tempItemList;
                results.count = tempItemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItemList = (ArrayList<SheetBoardListItem>) results.values;
            Collections.sort(filteredItemList);

            if(results.count > 0)
                notifyDataSetChanged();
            else
                notifyDataSetInvalidated();

        }

    }//itemFilter
}//Adapter
