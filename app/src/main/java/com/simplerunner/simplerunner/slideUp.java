package com.simplerunner.simplerunner;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class slideUp {
    public slideUp(final Fragment fragment, View view){
        final GestureDetector detector = new GestureDetector(fragment.getActivity(),
                new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        float distance = e2.getY()-e1.getY();
                        if(distance>100){
                            Intent intent = new Intent(fragment.getActivity(), MainActivity.class);
                            fragment.startActivity(intent);
                            fragment.getActivity().overridePendingTransition(R.anim.buttom_in, R.anim.buttom_out);
                        }else{

                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });

    }
}
