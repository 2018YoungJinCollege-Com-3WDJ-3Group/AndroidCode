package com.example.asd76.okonomiorgel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by asd76 on 2018-03-06.
 */

public class NavigationItemLis implements NavigationView.OnNavigationItemSelectedListener{

    Context context;

    public NavigationItemLis(Context context) {
        this.context = context;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.account_management:
                Intent loginIntent = new Intent(context, LoginActivity.class);
                context.startActivity(loginIntent);
        }
        return false;
    }
}
