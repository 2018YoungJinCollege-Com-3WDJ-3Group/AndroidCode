package com.example.asd76.okonomiorgel.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.Item.SheetBoardItem;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-03-19.
 */

public class ScoreBoardAdapter extends BaseAdapter implements Filterable{

    Filter itemFilter;
    private ArrayList<SheetBoardItem> originItemList = new ArrayList<>();
    private ArrayList<SheetBoardItem> filteredItemList = originItemList;
    private Context context;
    OkonomiOrgelService service;
    String base_url;

    public ScoreBoardAdapter(Context context) {
        this.context = context;
        base_url = this.context.getString(R.string.base_url);

        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);
    }

    public void addItem(SheetBoardItem item){
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
        TextView view_categoty = (TextView) convertView.findViewById(R.id.sheet_board_category);
        TextView view_title = (TextView) convertView.findViewById(R.id.sheet_board_title);
        TextView view_price = (TextView) convertView.findViewById(R.id.sheet_board_price);
        final ImageView view_thumbnail = (ImageView) convertView.findViewById(R.id.sheet_board_thumnail);

        SheetBoardItem item = filteredItemList.get(position);

        //썸네일 획득
        Call<ResponseBody> response = service.getImage(base_url + item.getThumbnail_path());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null){

                    InputStream is = null;
                    BufferedInputStream bis;
                    Bitmap thumbnail;

                    try{
                        is = response.body().byteStream();
                        bis = new BufferedInputStream(is);
                        thumbnail = BitmapFactory.decodeStream(bis);
                        view_thumbnail.setImageBitmap(thumbnail);


                    }catch (Exception e){
                        Log.e("downloadImage", e.getMessage());
                    }
                }
                else{
                    Log.e("responseBody", "error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        //각 뷰에 현재 포지션의 아이템 값 설정
        view_categoty.setText("[" + item.getCategory()+"]");
        view_title.setText(item.getTitle());
        view_price.setText(item.getPrice() + "P");

        return convertView;
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

            ArrayList<SheetBoardItem> tempItemList = new ArrayList<>();

            //검색할 값이 없으면 기존 배열 반환
            if(constraint == null || constraint.equals("") || constraint.length() == 0){
                results.values = originItemList;
                results.count = originItemList.size();
            }else{
                //기존 배열의 제목과 검색 값을 비교
                for(SheetBoardItem item : originItemList){
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
            filteredItemList = (ArrayList<SheetBoardItem>) results.values;
            Collections.sort(filteredItemList);

            if(results.count > 0)
                notifyDataSetChanged();
            else
                notifyDataSetInvalidated();

        }

    }//itemFilter
}//Adapter
