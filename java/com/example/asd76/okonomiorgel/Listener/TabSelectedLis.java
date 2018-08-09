package com.example.asd76.okonomiorgel.Listener;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

/**
 * Created by asd76 on 2018-03-16.
 */

public class TabSelectedLis implements TabLayout.OnTabSelectedListener{

    ViewPager viewPager;

    public TabSelectedLis(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}
}