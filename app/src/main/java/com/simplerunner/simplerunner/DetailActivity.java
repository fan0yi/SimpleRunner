package com.simplerunner.simplerunner;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        serAdapter();
    }

    private void serAdapter(){
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabLayout = findViewById(R.id.tablayout_activity_detail);
        viewPager = findViewById(R.id.viewpager_activity_detail);
        adapter.AddFragment(new Page1Fragment(),"日常");
        adapter.AddFragment(new Page2Fragment(),"步數");
        adapter.AddFragment(new Page3Fragment(),"時間");
        adapter.AddFragment(new Page4Fragment(),"大卡");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
