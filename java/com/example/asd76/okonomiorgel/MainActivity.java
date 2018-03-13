package com.example.asd76.okonomiorgel;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    boolean isDrawerOpened;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    ViewPager viewPager;
    RelativeLayout relativeLayout;
    TabLayout tabLayout;
    int [] tabIcons = {R.drawable.tab_musicsheet,
                       R.drawable.tab_play,
                       R.drawable.tab_board};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        setupViewpager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
        setupTabIcons();

        //네비게이션 바 리스너 등록
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationItemLis(this));

        isDrawerOpened = false;
        toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.drawer_open, R.string.drawer_close){

            //네비게이션 바 이벤트 설정
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isDrawerOpened = false;
            }
        };
        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();
    }

    //뷰페이저 설정
    public void setupViewpager(ViewPager viewPager){

        tabPagerAdapter adapter = new tabPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_musicsheet(), "musicsheet");
        adapter.addFragment(new fragment_play(), "play");
        adapter.addFragment(new fragment_board(), "board");
        viewPager.setAdapter(adapter);
    }

    //탭 아이콘 설정
    public void setupTabIcons(){
        for(int i = 0; i < tabIcons.length; i++)
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
    }

    //네비게이션 창이 열려있으면 닫기
    @Override
    public void onBackPressed() {
        if(isDrawerOpened)
            drawer.closeDrawers();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return false;
        else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    //페이지어댑터
    class tabPagerAdapter extends FragmentPagerAdapter{

        List<Fragment> fragments = new ArrayList<>();
        List<String> tabTitles = new ArrayList<>();

        public tabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            tabTitles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) { return null; }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


}
