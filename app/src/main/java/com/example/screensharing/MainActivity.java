package com.example.screensharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jsonsocket.currentState.CurrentState;
import com.example.jsonsocket.jsonsEntities.PinchEvent;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    Button btnOnOff;
    TextView tvTest;
    SwipeListener swipeListener;


    private int getHeigthDevice() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private int getWidthDevice() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private String getMACAddress() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }

    private String getDeviceName() {
        String userDeviceName = Settings.Global.getString(getContentResolver(), Settings.Global.DEVICE_NAME);
        if(userDeviceName == null)
            userDeviceName = Settings.Secure.getString(getContentResolver(), "bluetooth_name");
        return userDeviceName;
    }

    private boolean touchAScreenLimit(float limit, float coord) {
        if(coord <0) {
            return true;
        }
        int absValue = (int) Math.abs(coord - limit);
        return absValue <= 35;
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

    private void sendCoordsToPinch(float xDiff, float yDiff, MotionEvent e2,int threshoold, float velocityX, float velocityY, int velocity_threshold) {
        if(Math.abs(xDiff) > Math.abs(yDiff)){
            if(Math.abs(xDiff) > threshoold && Math.abs(velocityX) >velocity_threshold){
                if(xDiff>0){
                    //Right
                    tvTest.setText("Right");
                    if(touchAScreenLimit(getWidthDevice(),e2.getX())) {
                        //INFO TO SEND
                        PinchEvent pinchEvent = new PinchEvent();
                        pinchEvent.setDeviceName(getDeviceName());
                        pinchEvent.setPhysicalAddress("");
                        pinchEvent.setPosPinchX(getWidthDevice());
                        pinchEvent.setPosPinchY(e2.getY());
                        pinchEvent.setScreenHeigth(getHeigthDevice());
                        pinchEvent.setScreenWidth(getWidthDevice());
                        pinchEvent.setTimePinch(new Date());
                        pinchEvent.setDirectionPinch("Right");

                        CurrentState.getInstance().setPinchEvent(pinchEvent);
                    }

                } else {
                    //Left
                    tvTest.setText("Left");
                    if(touchAScreenLimit(0,e2.getX())) {
                        //INFO TO SEND
                        PinchEvent pinchEvent = new PinchEvent();
                        pinchEvent.setDeviceName(getDeviceName());
                        pinchEvent.setPhysicalAddress("");
                        pinchEvent.setPosPinchX(0);
                        pinchEvent.setPosPinchY(e2.getY());
                        pinchEvent.setScreenHeigth(getHeigthDevice());
                        pinchEvent.setScreenWidth(getWidthDevice());
                        pinchEvent.setTimePinch(new Date());
                        pinchEvent.setDirectionPinch("Left");

                        CurrentState.getInstance().setPinchEvent(pinchEvent);
                    }

                }
            }
        }else{
            if(Math.abs(yDiff) > threshoold && Math.abs(velocityY) > velocity_threshold){
                if(yDiff>0){
                    //Down
                    tvTest.setText("Down");
                    if(touchAScreenLimit(getHeigthDevice(),e2.getY())) {
                        //INFO TO SEND
                        PinchEvent pinchEvent = new PinchEvent();
                        pinchEvent.setDeviceName(getDeviceName());
                        pinchEvent.setPhysicalAddress("");
                        pinchEvent.setPosPinchX(e2.getX());
                        pinchEvent.setPosPinchY(getHeigthDevice());
                        pinchEvent.setScreenHeigth(getHeigthDevice());
                        pinchEvent.setScreenWidth(getWidthDevice());
                        pinchEvent.setTimePinch(new Date());
                        pinchEvent.setDirectionPinch("Down");

                        CurrentState.getInstance().setPinchEvent(pinchEvent);
                    }

                } else {
                    //Up
                    tvTest.setText("Up");
                    if(touchAScreenLimit(0,e2.getY())) {
                        //INFO TO SEND
                        PinchEvent pinchEvent = new PinchEvent();
                        pinchEvent.setDeviceName(getDeviceName());
                        pinchEvent.setPhysicalAddress("");
                        pinchEvent.setPosPinchX(e2.getX());
                        pinchEvent.setPosPinchY(0);
                        pinchEvent.setScreenHeigth(getHeigthDevice());
                        pinchEvent.setScreenWidth(getWidthDevice());
                        pinchEvent.setTimePinch(new Date());
                        pinchEvent.setDirectionPinch("Up");

                        CurrentState.getInstance().setPinchEvent(pinchEvent);
                    }
                }
            }
        }
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
                                sendCoordsToPinch(xDiff, yDiff, e2, threshoold, velocityX, velocityY, velocity_threshold);
                                return true;
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
