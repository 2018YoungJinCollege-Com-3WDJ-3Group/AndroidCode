package com.example.asd76.okonomiorgel.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.FreeBoard;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by asd76 on 2018-03-19.
 */

public class FreeBoardAdapter extends BaseAdapter implements Filterable{

    Filter itemFilter;
    private ArrayList<FreeBoard> originItemList = new ArrayList<>();
    private ArrayList<FreeBoard> filteredItemList = originItemList;

    public void addItem(FreeBoard item){
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
            convertView = inflater.inflate(R.layout.free_board_item, parent, false);
        }

        //레이아웃의 뷰 참조 획득
        TextView categoty = (TextView) convertView.findViewById(R.id.free_board_category);
        TextView title = (TextView) convertView.findViewById(R.id.free_board_title);
        TextView writer = (TextView) convertView.findViewById(R.id.free_board_writer);
        TextView reg_date = (TextView) convertView.findViewById(R.id.free_board_reg_date);
        TextView hits = (TextView) convertView.findViewById(R.id.free_board_hits);

        FreeBoard item = filteredItemList.get(position);

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(item.getReg_date());
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
        writer.setText(item.getWriter());
        reg_date.setText(itemDate);
        hits.setText("조회수 "+ item.getHits());

        return convertView;
    }

    public void sortItem(String sort){
        switch (sort){
            case "최신순":
                Collections.sort(filteredItemList);
                break;
            case "조회순":
                Collections.sort(filteredItemList, new Comparator<FreeBoard>() {
                    @Override
                    public int compare(FreeBoard o1, FreeBoard o2) {
                        if(o1.getHits() < o2.getHits())
                            return 1;
                        else if(o1.getHits() > o2.getHits())
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

            ArrayList<FreeBoard> tempItemList = new ArrayList<>();

            //검색할 값이 없으면 기존 배열 반환
            if(constraint == null || constraint.equals("") || constraint.length() == 0){
                results.values = originItemList;
                results.count = originItemList.size();
            }else{
                //기존 배열의 제목과 검색 값을 비교
                for(FreeBoard item : originItemList){
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
            filteredItemList = (ArrayList<FreeBoard>) results.values;
            Collections.sort(filteredItemList);

            if(results.count > 0)
                notifyDataSetChanged();
            else
                notifyDataSetInvalidated();
        }

    }//itemFilter
}//Adapter
