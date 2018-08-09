package com.example.asd76.okonomiorgel.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.asd76.okonomiorgel.Adapters.ScoreScaleAdapter;
import com.example.asd76.okonomiorgel.CreateScoreDialog;
import com.example.asd76.okonomiorgel.R;
import com.example.asd76.okonomiorgel.Response.CheckBoolean;
import com.example.asd76.okonomiorgel.Response.OkonomiOrgelService;
import com.example.asd76.okonomiorgel.Response.PurchaseRecords;
import com.example.asd76.okonomiorgel.Response.ScoreInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asd76 on 2018-04-24.
 */

public class CreateScoreActivity extends AppCompatActivity implements View.OnClickListener{

    OkonomiOrgelService service;
    ScoreScaleAdapter adapter;
    Button[] buttons = new Button[30];
    SelectedBtn selectedBtn;
    ScoreInfo score_info;
    int score_tempo = 0;
    String mode = "create";
    int user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sheet);

        //미디어 볼륨으로 조정
        setVolumeControlStream ( AudioManager.STREAM_MUSIC );

        //버튼 리스너 설정
        setBtnListener();

        //어댑터 생성
        adapter = new ScoreScaleAdapter();

        //그리드 뷰 설정
        GridView gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        //그리드 뷰의 스크롤이 마지막 부분일 때, 악보 칸 늘리기
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean lastItemVisibleFlag = false;
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    adapter.addLine();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedBtn != null){
                    if(adapter.checkDuplicateScale(position, selectedBtn.scale)){
                        adapter.setItem(position, selectedBtn.scale);
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(CreateScoreActivity.this, "중복된 음", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //회원 아이디 가져오기
        SharedPreferences pref = getSharedPreferences("user_info", 0);
        user_id = pref.getInt("user_id", -1);

        //아이디가 없으면 종료
        if(user_id == -1){
            Toast.makeText(this, "로그인 후 이용 가능", Toast.LENGTH_SHORT).show();
            finish();
        }

        //url 지정
        String base_url = this.getString(R.string.base_url);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OkonomiOrgelService.class);

        //인텐트 값 받아오기
        Bundle bundle = getIntent().getExtras();
        score_info = bundle.getParcelable("editMode");

        //인텐트 값이 존재하면 편집 모드 호출
        if(score_info != null){
            editMode(score_info.getScorestring());
            mode = "edit";
        }

        Log.e("score", score_info.getScorestring());

    }//end of onCreate

    class SelectedBtn {

        String scale;
        Button button;

        public SelectedBtn(String scale, Button button) {
            this.scale = scale;
            this.button = button;
        }
    }

    public void editMode(String sheet){
        score_tempo = adapter.editSheet(sheet);
        Toast.makeText(this, "기존 악보 템포 : " + score_tempo, Toast.LENGTH_SHORT).show();
    }

    public void showDialog(){

        AlertDialog.Builder ad = new AlertDialog.Builder(CreateScoreActivity.this);

        ad.setMessage("템포 값을 입력하세요");

        final EditText editText = new EditText(CreateScoreActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        ad.setView(editText);

        ad.setPositiveButton("저장", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                try{
                    String str = editText.getText().toString().trim();
                    score_tempo = Integer.parseInt(str);
                    String sheetString = adapter.getSheet();

                    if(score_tempo != 0 && !sheetString.equals("")){
                        //서버로 전송...
                        String score = score_tempo + ";" + sheetString;
                            Log.e("saveSheet", score);
                        Log.e("user_id", ""+user_id);
                        Log.e("saveSheet", score_info.getScore_id()+"");


                        final Call<CheckBoolean> response = service.editSheet(user_id, score, score_info.getScore_id());
                        response.enqueue(new Callback<CheckBoolean>() {
                            @Override
                            public void onResponse(Call<CheckBoolean> call, Response<CheckBoolean> response) {
                                if(response.isSuccessful() && response.body() != null){
                                    if(response.body().getCheck()){
                                        Toast.makeText(CreateScoreActivity.this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(CreateScoreActivity.this, "수정 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Log.e("onResponse", response.toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<CheckBoolean> call, Throwable t) {

                            }
                        });
                        dialog.dismiss();
                    }

                } catch(NumberFormatException e){
                    Toast.makeText(CreateScoreActivity.this, "다시 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }

    public void setBtnListener(){

        buttons[0] = (Button)findViewById(R.id.btn_C0);
        buttons[1] = (Button)findViewById(R.id.btn_D0);
        buttons[2] = (Button)findViewById(R.id.btn_G0);
        buttons[3] = (Button)findViewById(R.id.btn_A0);
        buttons[4] = (Button)findViewById(R.id.btn_B0);
        buttons[5] = (Button)findViewById(R.id.btn_C1);
        buttons[6] = (Button)findViewById(R.id.btn_D1);
        buttons[7] = (Button)findViewById(R.id.btn_E1);
        buttons[8] = (Button)findViewById(R.id.btn_F1);
        buttons[9] = (Button)findViewById(R.id.btn_F1_);

        buttons[10] = (Button)findViewById(R.id.btn_G1);
        buttons[11] = (Button)findViewById(R.id.btn_G1_);
        buttons[12] = (Button)findViewById(R.id.btn_A1);
        buttons[13] = (Button)findViewById(R.id.btn_A1_);
        buttons[14] = (Button)findViewById(R.id.btn_B1);
        buttons[15] = (Button)findViewById(R.id.btn_C2);
        buttons[16] = (Button)findViewById(R.id.btn_C2_);
        buttons[17] = (Button)findViewById(R.id.btn_D2);
        buttons[18] = (Button)findViewById(R.id.btn_D2_);
        buttons[19] = (Button)findViewById(R.id.btn_E2);

        buttons[20] = (Button)findViewById(R.id.btn_F2);
        buttons[21] = (Button)findViewById(R.id.btn_F2_);
        buttons[22] = (Button)findViewById(R.id.btn_G2);
        buttons[23] = (Button)findViewById(R.id.btn_G2_);
        buttons[24] = (Button)findViewById(R.id.btn_A2);
        buttons[25] = (Button)findViewById(R.id.btn_A2_);
        buttons[26] = (Button)findViewById(R.id.btn_B2);
        buttons[27] = (Button)findViewById(R.id.btn_C3);
        buttons[28] = (Button)findViewById(R.id.btn_D3);
        buttons[29] = (Button)findViewById(R.id.btn_E3);

        for(int i = 0; i < buttons.length; i++){
            buttons[i].setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {

        //전에 선택된 버튼색 초기화
        if(selectedBtn != null){
            selectedBtn.button.setBackgroundResource(R.drawable.scale_btn_create_sheet);
        }

        String tempScale = null;
        Button tempBtn = (Button)findViewById(v.getId());

        tempScale = tempBtn.getText().toString();
        tempBtn.setBackgroundColor(Color.rgb(232, 232, 127));

        selectedBtn = new SelectedBtn(tempScale, tempBtn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_sheet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //왜 return true???
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_create_sheet_save:
                showDialog();
                return true;
            case R.id.menu_create_sheet_clear:
                //악보 초기화
                adapter.clearSheet();
                return true;
            case R.id.menu_create_sheet_play:
                //템포를 입력 받을 다이얼로그 호출
                CreateScoreDialog previewDialog = new CreateScoreDialog(CreateScoreActivity.this, adapter.getSheet());
                previewDialog.setCancelable(false);
                previewDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
