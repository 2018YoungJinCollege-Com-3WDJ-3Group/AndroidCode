package com.example.asd76.okonomiorgel;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by asd76 on 2018-04-26.
 */

public class CreateScoreDialog extends Dialog implements View.OnClickListener{

    ImageButton btn_exit;
    EditText input_tempo;
    ImageButton player;
    int tempo = 0;
    String sheetString = null;
    Boolean isRun = false;
    PreviewThread previewThread;

    public CreateScoreDialog(@NonNull Context context, String sheetString) {
        super(context);
        this.sheetString = sheetString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_sheet);

        btn_exit = (ImageButton)findViewById(R.id.dialog_exit);
        input_tempo = (EditText)findViewById(R.id.input_tempo);
        player = (ImageButton)findViewById(R.id.dialog_player);

        btn_exit.setOnClickListener(this);
        input_tempo.setOnClickListener(this);
        player.setOnClickListener(this);

        //test
        Log.e("sheetString : ", sheetString);

    }

    public void previewPlay(){

        //미리듣기 쓰레드 시작
        if(!isRun){
            previewThread = new PreviewThread(getContext(), sheetString, tempo);
            previewThread.execute();
            isRun = true;
            player.setImageResource(R.drawable.creaet_dialog_stop);

        }else{
            stopPreview();
            isRun = false;
            player.setImageResource(R.drawable.create_dialog_play);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_exit:
                dismiss();
                break;
            case R.id.dialog_player:

                //악보 값이 null 이면
                if(sheetString == null || sheetString.equals("")){
                    Toast.makeText(getContext(), "빈 악보입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                //템포 값이 빈 값이면
                if(input_tempo.getText().toString().equals("")){
                    Toast.makeText(getContext(), "템포 값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    break;
                }

                tempo = Integer.parseInt(input_tempo.getText().toString().trim());
                previewPlay();
        }
    }

    public void stopPreview(){
        if(previewThread != null){
            Log.e("stopPreview", "sheetPreview is not null");
            previewThread.isPlaying = false;
            previewThread.getSoundPool().release();
            previewThread = null;
        }
    }

    @Override
    public void dismiss() {
        stopPreview();
        super.dismiss();
    }
}
