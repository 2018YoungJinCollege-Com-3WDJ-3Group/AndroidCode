package com.example.asd76.okonomiorgel.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asd76.okonomiorgel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by asd76 on 2018-04-25.
 */

public class SheetScaleAdapter extends BaseAdapter{

    private int lineNum;
    private ArrayList<String> scaleArr;
    private ArrayList<String> codeArr;
    private StringBuffer sheetString;
    private HashMap<String, String> scaleMap;

    public SheetScaleAdapter() {

        lineNum = 7;
        this.clearSheet();

        //음계별 코드 설정
        scaleMap = new HashMap<>();
        scaleMap.put("C0", "A");
        scaleMap.put("D0", "C");
        scaleMap.put("G0", "H");
        scaleMap.put("A0", "J");
        scaleMap.put("B0", "L");
        scaleMap.put("C1", "M");
        scaleMap.put("D1", "O");
        scaleMap.put("E1", "Q");
        scaleMap.put("F1", "R");
        scaleMap.put("F1#", "S");

        scaleMap.put("G1", "T");
        scaleMap.put("G1#", "U");
        scaleMap.put("A1", "V");
        scaleMap.put("A1#", "W");
        scaleMap.put("B1", "X");
        scaleMap.put("C2", "Y");
        scaleMap.put("C2#", "Z");
        scaleMap.put("D2", "[");
        scaleMap.put("D2#", "\\");
        scaleMap.put("E2", "]");

        scaleMap.put("F2", "^");
        scaleMap.put("F2#", "_");
        scaleMap.put("G2", "`");
        scaleMap.put("G2#", "a");
        scaleMap.put("A2", "b");
        scaleMap.put("A2#", "c");
        scaleMap.put("B2", "d");
        scaleMap.put("C3", "e");
        scaleMap.put("D3", "g");
        scaleMap.put("E3", "i");
    }

    public void clearSheet(){

        scaleArr = new ArrayList<>();
        codeArr = new ArrayList<>();

        addLine();
        addLine();
        addLine();
        addLine();
        addLine();
        addLine();
        addLine();
        addLine();

        this.notifyDataSetChanged();
    }

    public boolean checkDuplicateScale(int position, String scale){

        int temp = position / lineNum;
        int line = temp * lineNum;

        for(int i = line; i < (line + lineNum); i++){
            if(scaleArr.get(i).equals(scale)){
                return false;
            }
        }
        return true;
    }

    public String getSheet(){

        sheetString = new StringBuffer();
        ArrayList<Integer> selector = new ArrayList<>();
        int lastLine = 0;

        //마지막 위치 체크
        for(int i = codeArr.size()-1; i >= 0; i--){
            if(!codeArr.get(i).equals("")){
                lastLine = i/lineNum;
                break;
            }
        }

        for(int i = 1; i <= lastLine; i++){
            selector.add(i*lineNum);
        }

        for(int i = 0; i < codeArr.size(); i++){

            if(selector.indexOf(i) != -1)
                sheetString.append('r');

            sheetString.append(codeArr.get(i));
        }

        return sheetString.toString();
    }

    public int editSheet(String sheet){

        //악보 클리어
        clearSheet();

        //악보 템포, 스트링 분리
        String str = sheet;
        StringTokenizer st = new StringTokenizer(str, ";");
        ArrayList strArr = new ArrayList();

        while(st.hasMoreTokens()){
            strArr.add(st.nextToken());
        }

        int tempo = Integer.parseInt(strArr.get(0).toString());
        String sheetString = strArr.get(1).toString();

        //악보 길이
        int sheetSize = 0;
        for(int i = 0; i < sheetString.length(); i++){
            if(sheetString.charAt(i) == 'r')
                sheetSize += 7;
            else
                sheetSize++;
        }

        //악보 길이만큼 공간 추가
        for(int i = 0; i < sheetSize/lineNum; i++){
            addLine();
        }

        //악보 옮기기
        int count = 0;
        String key = null;
        for(int i = 0; i < sheetString.length(); i++){
            if(sheetString.charAt(i) == 'r'){
                int temp = count / lineNum;
                int currLineFirst = temp * lineNum;
                count = currLineFirst + lineNum;
            }else{
                key = getKey(sheetString.charAt(i)+"");
                setItem(count, key);
                count++;
            }
        }

        return tempo;
    }

    public void addLine(){

        for(int i = 0; i < lineNum; i++){
            scaleArr.add("");
            codeArr.add("");
        }

    }

    public void setItem(int position, String changeScale){
        scaleArr.set(position, changeScale);
        String code = scaleMap.get(changeScale);
        codeArr.set(position, code);
    }

    public String getKey(String value){
        for(String key : scaleMap.keySet()){
            if(scaleMap.get(key).equals(value))
                return key;
        }
        return null;
    }

    @Override
    public int getCount() {
        return scaleArr.size();
    }

    @Override
    public Object getItem(int position) {
        return scaleArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
        }

        String scale = scaleArr.get(position);

        TextView item_txt = (TextView)convertView.findViewById(R.id.item_txt);
        item_txt.setText(scale);

        return convertView;
    }
}
