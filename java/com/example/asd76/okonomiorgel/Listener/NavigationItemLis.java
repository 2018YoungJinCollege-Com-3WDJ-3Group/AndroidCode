package com.example.asd76.okonomiorgel.Listener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.example.asd76.okonomiorgel.LoginActivity;
import com.example.asd76.okonomiorgel.MainActivity;
import com.example.asd76.okonomiorgel.R;

/**
 * Created by asd76 on 2018-03-06.
 */

public class NavigationItemLis implements NavigationView.OnNavigationItemSelectedListener{

    Context context;

    public NavigationItemLis(Context context) {
        this.context = context;
    }

    public void logout(){

        //SharedPreferences 정보 삭제
        SharedPreferences user_pref = context.getSharedPreferences("user_info", 0);
        SharedPreferences.Editor editor = user_pref.edit();
        editor.clear();
        editor.commit();

        //MainActivity 이동
        Intent mainIntent = new Intent(context, MainActivity.class);
        context.startActivity(mainIntent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_login:
                Intent loginIntent = new Intent(context, LoginActivity.class);
                context.startActivity(loginIntent);
                break;
            case R.id.menu_logout:
                logout();
        }
        return false;
    }
}
