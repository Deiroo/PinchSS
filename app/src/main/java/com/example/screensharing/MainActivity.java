package com.example.screensharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    Button btnOnOff;
    TextView tvTest;
    SwipeListener swipeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraint_layout);
        btnOnOff = findViewById(R.id.btnOnOff);
        tvTest = findViewById(R.id.tvTest);

        swipeListener = new SwipeListener(constraintLayout);


    }


    private class SwipeListener implements View.OnTouchListener{

        GestureDetector gestureDetector;

        SwipeListener(View view){
            final int threshoold = 100;
            final int velocity_threshold = 100;

            GestureDetector.SimpleOnGestureListener listener =
                    new GestureDetector.SimpleOnGestureListener(){
                        @Override
                        public boolean onDown(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            float xDiff = e2.getX() - e1.getX();
                            float yDiff = e2.getY() - e1.getY();

                            try{
                                if(Math.abs(xDiff) > Math.abs(yDiff)){
                                    if(Math.abs(xDiff) > threshoold && Math.abs(velocityX) >velocity_threshold){
                                        if(xDiff>0){
                                            //Right
                                            tvTest.setText("Right");

                                        } else {
                                            //Left
                                            tvTest.setText("Left");
                                        }
                                        return true;
                                    }
                                }else{
                                    if(Math.abs(yDiff) > threshoold && Math.abs(velocityY) > velocity_threshold){
                                        if(yDiff>0){
                                            //Down
                                            tvTest.setText("Down");
                                        } else {
                                            //Up
                                            tvTest.setText("Up");
                                        }
                                        return true;
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            return false;
                        }
                    };
            gestureDetector = new GestureDetector(getApplicationContext(), listener);

            view.setOnTouchListener(this);


        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return gestureDetector.onTouchEvent(event);
        }
    }
}
