package com.simplerunner.simplerunner;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class DetailActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    GestureDetector detector;
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
        detector = new GestureDetector(this, this);
    }

    private void slideDown(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.buttom_in, R.anim.buttom_out);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float distance = e2.getY()-e1.getY();
        if(distance>100){
            slideDown();
        }else{

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        slideDown();
    }
}
