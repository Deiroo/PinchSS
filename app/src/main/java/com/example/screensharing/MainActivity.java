package com.example.screensharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    Button btnOnOff;
    TextView tvTest;
    SwipeListener swipeListener;



    private int getHeigthDevice() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        int height = point.y;
        //int width = point.x;
        return height;
    }

    private int getWidthDevice() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        //int height = point.y;
        int width = point.x;
        return width;
    }

    private String getMACAddress() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }

    private boolean touchAScreenLimit(float limit, float coord) {
        int absValue = (int) Math.abs(coord - limit);
        return absValue <= 30;
    }

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
                                            //INFO TO SEND
                                            Log.i("//", "////////////////////");

                                            Log.i("GET MAC ADDRESS", getMACAddress());
                                            Log.i("TOUCH A LIMIT", String.valueOf(touchAScreenLimit(getWidthDevice(),e2.getX())));
                                            Log.i("XPOS", String.valueOf(getWidthDevice()));
                                            Log.i("YPOS", String.valueOf(e2.getY()));
                                            Log.i("TIME", String.valueOf(new Date()));
                                            Log.i("DIRECTION", "Right");

                                            Log.i("//", "////////////////////");


                                        } else {
                                            //Left
                                            tvTest.setText("Left");

                                            //INFO TO SEND
                                            Log.i("//", "////////////////////");

                                            Log.i("GET MAC ADDRESS", getMACAddress());
                                            Log.i("TOUCH A LIMIT", String.valueOf(touchAScreenLimit(0,e2.getX())));
                                            Log.i("XPOS", String.valueOf(0));
                                            Log.i("YPOS", String.valueOf(e2.getY()));
                                            Log.i("TIME", String.valueOf(new Date()));
                                            Log.i("DIRECTION", "Left");

                                            Log.i("//", "////////////////////");

                                        }
                                        return true;
                                    }
                                }else{
                                    if(Math.abs(yDiff) > threshoold && Math.abs(velocityY) > velocity_threshold){
                                        if(yDiff>0){
                                            //Down
                                            tvTest.setText("Down");

                                            //INFO TO SEND
                                            Log.i("//", "////////////////////");

                                            Log.i("GET MAC ADDRESS", getMACAddress());
                                            Log.i("TOUCH A LIMIT", String.valueOf(touchAScreenLimit(getHeigthDevice(),e2.getY())));
                                            Log.i("XPOS", String.valueOf(e2.getX()));
                                            Log.i("YPOS", String.valueOf(getHeigthDevice()));
                                            Log.i("TIME", String.valueOf(new Date()));
                                            Log.i("DIRECTION", "Down");

                                            Log.i("//", "////////////////////");

                                        } else {
                                            //Up
                                            tvTest.setText("Up");

                                            //INFO TO SEND
                                            Log.i("//", "////////////////////");

                                            Log.i("GET MAC ADDRESS", getMACAddress());
                                            Log.i("TOUCH A LIMIT", String.valueOf(touchAScreenLimit(0,e2.getY())));
                                            Log.i("XPOS", String.valueOf(e2.getX()));
                                            Log.i("YPOS", String.valueOf(0));
                                            Log.i("TIME", String.valueOf(new Date()));
                                            Log.i("DIRECTION", "Up");

                                            Log.i("//", "////////////////////");

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
