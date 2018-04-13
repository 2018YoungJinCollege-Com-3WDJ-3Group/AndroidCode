package com.example.asd76.okonomiorgel;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by asd76 on 2018-04-05.
 */

public class PreviewThread extends AsyncTask<Void, Void, Void>{

    private Context context;
    private String sheet;
    private int tempo;
    private SoundPool soundPool;
    private HashMap scaleMap;
    public Boolean isPlaying = false;

    public PreviewThread(Context context, String sheet, int tempo){

        this.context = context;
        this.sheet = sheet;
        this.tempo = 30000/tempo;

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(30).build();
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.e("sound...", "isLoaded : "+ sampleId);
                if(sampleId == 30){
                    isPlaying = true;
                }
            }
        });

        scaleMap = new HashMap();
        scaleMap.put("A", soundPool.load(context, R.raw.note_40, 1));
        scaleMap.put("C", soundPool.load(context, R.raw.note_42, 1));
        scaleMap.put("H", soundPool.load(context, R.raw.note_47, 1));
        scaleMap.put("J", soundPool.load(context, R.raw.note_49, 1));
        scaleMap.put("L", soundPool.load(context, R.raw.note_51, 1));
        scaleMap.put("M", soundPool.load(context, R.raw.note_52, 1));
        scaleMap.put("O", soundPool.load(context, R.raw.note_54, 1));
        scaleMap.put("Q", soundPool.load(context, R.raw.note_56, 1));
        scaleMap.put("R", soundPool.load(context, R.raw.note_57, 1));
        scaleMap.put("S", soundPool.load(context, R.raw.note_58, 1));
        scaleMap.put("T", soundPool.load(context, R.raw.note_59, 1));
        scaleMap.put("U", soundPool.load(context, R.raw.note_60, 1));
        scaleMap.put("V", soundPool.load(context, R.raw.note_61, 1));
        scaleMap.put("W", soundPool.load(context, R.raw.note_62, 1));
        scaleMap.put("X", soundPool.load(context, R.raw.note_63, 1));
        scaleMap.put("Y", soundPool.load(context, R.raw.note_64, 1));
        scaleMap.put("Z", soundPool.load(context, R.raw.note_65, 1));
        scaleMap.put("[", soundPool.load(context, R.raw.note_66, 1));
        scaleMap.put("\\", soundPool.load(context, R.raw.note_67, 1));
        scaleMap.put("]", soundPool.load(context, R.raw.note_68, 1));
        scaleMap.put("^", soundPool.load(context, R.raw.note_69, 1));
        scaleMap.put("_", soundPool.load(context, R.raw.note_70, 1));
        scaleMap.put("`", soundPool.load(context, R.raw.note_71, 1));
        scaleMap.put("a", soundPool.load(context, R.raw.note_72, 1));
        scaleMap.put("b", soundPool.load(context, R.raw.note_73, 1));
        scaleMap.put("c", soundPool.load(context, R.raw.note_74, 1));
        scaleMap.put("d", soundPool.load(context, R.raw.note_75, 1));
        scaleMap.put("e", soundPool.load(context, R.raw.note_76, 1));
        scaleMap.put("g", soundPool.load(context, R.raw.note_78, 1));
        scaleMap.put("i", soundPool.load(context, R.raw.note_80, 1));
    }

    @Override
    protected Void doInBackground(Void... voids) {

        while(true){
            if(isPlaying = true) break;
        }

        for(int i = 0; i < sheet.length(); i++){

            if(!isPlaying) break;

            if(sheet.charAt(i) == 'r'){
                try {
                    Thread.sleep(this.tempo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                String temp = "" + sheet.charAt(i);
                soundPool.play((int) scaleMap.get(temp), 1f, 1f, 0, 0, 1f);
            }
        }
        isPlaying = false;
        return null;
    }

    public SoundPool getSoundPool(){
        return this.soundPool;
    }
}
