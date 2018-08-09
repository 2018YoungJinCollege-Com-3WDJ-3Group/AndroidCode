package com.example.asd76.okonomiorgel.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.ScoreInfo;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-03-26.
 */

public class MyScoreAdapter extends BaseAdapter{

    private ArrayList<ScoreInfo> sheetInfos;
    private Context context;
    private Bitmap thumbnail;
    OkonomiOrgelService service;
    String base_url;

    public MyScoreAdapter(ArrayList<ScoreInfo> items, Context context) {
        this.sheetInfos = items;
        this.context = context;

        base_url = context.getString(R.string.base_url);

        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);
    }

    @Override
    public int getCount() {
        return sheetInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return sheetInfos.get(position);
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
            convertView = inflater.inflate(R.layout.my_sheet_item, parent, false);
        }

        ScoreInfo item = sheetInfos.get(position);

        TextView titleView = (TextView)convertView.findViewById(R.id.my_sheet_title);
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.my_sheet_image);

        Call<ResponseBody> response = service.getImage(base_url + item.getThumbnail_path());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null){

                    InputStream is = null;
                    BufferedInputStream bis;

                    try{
                        is = response.body().byteStream();
                        bis = new BufferedInputStream(is);
                        thumbnail = BitmapFactory.decodeStream(bis);
                        imageView.setImageBitmap(thumbnail);

                    }catch (Exception e){
                        Log.e("downloadImage", e.getMessage());
                    }

                }else{
                    Log.e("responseBody", "error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        titleView.setText(item.getSubject());

        return convertView;
    }

}
