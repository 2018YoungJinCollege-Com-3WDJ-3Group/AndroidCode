package com.example.asd76.okonomiorgel.Item;

import android.graphics.drawable.Drawable;

/**
 * Created by asd76 on 2018-03-06.
 */

public class ListViewItem {

    private int icon ;
    private String title;

    public ListViewItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
