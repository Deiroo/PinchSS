package com.example.screensharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.jsonsocket.WifiDirectConnector;
import com.example.jsonsocket.WifiDirectReceiver;
import com.example.jsonsocket.currentState.CurrentState;
import com.example.jsonsocket.jsonsEntities.JsonEntity;
import com.example.jsonsocket.jsonsEntities.PinchEvent;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    //Button btnOnOff;
    //TextView tvTest;
    SwipeListener swipeListener;
    ImageView imageView;
    Button discoverButton;
    Switch pinch, isPrincipal;
    TextView connectionStatus;
    ListView listView;

    boolean isPrincipalApp = false;
    boolean isPinchActivate = false;

    //Para Wifi Direct:
    BroadcastReceiver receiver;
    IntentFilter intentFilter;
    Gson gson = new Gson();
    WifiDirectConnector wifiDirectConnector = new WifiDirectConnector();


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

        initialWork();

        exqListener();
    }

    private void initialWork() {
        constraintLayout = findViewById(R.id.constraint_layout);
        connectionStatus = findViewById(R.id.connectionStatus);
        imageView = findViewById(R.id.imageViewPattern);
        discoverButton = findViewById(R.id.btnDiscover);
        pinch = findViewById(R.id.switchEnablePinch);
        isPrincipal = findViewById(R.id.switchIsPrincipal);
        listView = findViewById(R.id.listView);

        swipeListener = new SwipeListener(constraintLayout);

        wifiDirectConnector.setPeerListListener(wifiDirectConnector.initPeerListListener(listView, connectionStatus, getApplicationContext()));
        wifiDirectConnector.setConnectionInfoListener(wifiDirectConnector.initConnectionInfoListener(connectionStatus, imageView));

        //seteamos el manager de wifip2p con el contexto del sistema
        wifiDirectConnector.setManager((WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE));

        if (wifiDirectConnector.getManager() != null) {
            wifiDirectConnector.setChannel(wifiDirectConnector.getManager().initialize(this, getMainLooper(), null));
        }

        //llama al constructor de WifiDirectReceiver
        receiver = new WifiDirectReceiver(wifiDirectConnector.getManager(), wifiDirectConnector.getChannel(),
                wifiDirectConnector.getPeerListListener(), wifiDirectConnector.getConnectionInfoListener(), connectionStatus);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
    }

    private void exqListener() {
        pinch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    isPinchActivate = true;
                } else {
                    isPinchActivate = false;
                }
            }
        });

        isPrincipal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    isPrincipalApp = true;
                    //Se pondra en 0,0
                    imageView.setX(0);
                    imageView.setY(0);
                } else {
                    isPrincipalApp = false;
                }
            }
        });

        discoverButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                wifiDirectConnector.discoverPeers(connectionStatus);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WifiP2pDevice device = wifiDirectConnector.getDeviceArray()[position];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;

                wifiDirectConnector.connectDevice(connectionStatus, config, device);

            }
        });


    }

    private void sendMessageToNetwork(JsonEntity jsonEntity) {
        String jsonMessage = gson.toJson(jsonEntity);
        wifiDirectConnector.sendMessage(jsonMessage);
    }

    private void sendCoordsToPinch(float xDiff, float yDiff, MotionEvent e2,int threshoold, float velocityX, float velocityY, int velocity_threshold) {
        if(Math.abs(xDiff) > Math.abs(yDiff)){
            if(Math.abs(xDiff) > threshoold && Math.abs(velocityX) >velocity_threshold){
                if(xDiff>0){
                    //Right
                    //tvTest.setText("Right");
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

                        //Lo metemos al estado global
                        CurrentState.getInstance().setPinchEvent(pinchEvent);

                        JsonEntity  jsonEntity = new JsonEntity();
                        jsonEntity.setTypeMessage("SCREEN_SHARING");
                        jsonEntity.setCanvasHeigth(1113);
                        jsonEntity.setCanvasWidth(2600);
                        jsonEntity.setPinchEvent(pinchEvent);

                        sendMessageToNetwork(jsonEntity);
                    }

                } else {
                    //Left
                    //tvTest.setText("Left");
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

                        //Lo metemos al estado global
                        CurrentState.getInstance().setPinchEvent(pinchEvent);

                        JsonEntity  jsonEntity = new JsonEntity();
                        jsonEntity.setTypeMessage("SCREEN_SHARING");
                        jsonEntity.setCanvasHeigth(1113);
                        jsonEntity.setCanvasWidth(2600);
                        jsonEntity.setPinchEvent(pinchEvent);

                        sendMessageToNetwork(jsonEntity);
                    }

                }
            }
        }else{
            if(Math.abs(yDiff) > threshoold && Math.abs(velocityY) > velocity_threshold){
                if(yDiff>0){
                    //Down
                    //tvTest.setText("Down");
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

                        //Lo metemos al estado global
                        CurrentState.getInstance().setPinchEvent(pinchEvent);

                        JsonEntity  jsonEntity = new JsonEntity();
                        jsonEntity.setTypeMessage("SCREEN_SHARING");
                        jsonEntity.setCanvasHeigth(1113);
                        jsonEntity.setCanvasWidth(2600);
                        jsonEntity.setPinchEvent(pinchEvent);

                        sendMessageToNetwork(jsonEntity);
                    }

                } else {
                    //Up
//                    tvTest.setText("Up");
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

                        //Lo metemos al estado global
                        CurrentState.getInstance().setPinchEvent(pinchEvent);

                        JsonEntity  jsonEntity = new JsonEntity();
                        jsonEntity.setTypeMessage("SCREEN_SHARING");
                        jsonEntity.setCanvasHeigth(1113);
                        jsonEntity.setCanvasWidth(2600);
                        jsonEntity.setPinchEvent(pinchEvent);

                        sendMessageToNetwork(jsonEntity);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
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
                                if(isPinchActivate) {
                                    sendCoordsToPinch(xDiff, yDiff, e2, threshoold, velocityX, velocityY, velocity_threshold);
                                }
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
