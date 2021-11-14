package com.example.jsonsocket;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jsonsocket.currentState.CurrentState;
import com.example.jsonsocket.jsonsEntities.JsonEntity;
import com.example.jsonsocket.jsonsEntities.PinchEvent;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientWifiDirectSocket extends Thread {
    //CAPAZ EL SOCKET DEBA SER COMPARTIDO
    String hostAdd;
    private InputStream inputStream;
    private OutputStream outputStream;
    Socket socket;

    ImageView imageView = null;
    Gson gson = new Gson();


    public ClientWifiDirectSocket(InetAddress hostAddress, ImageView imageView){
        hostAdd = hostAddress.getHostAddress();
        socket = new Socket();
        this.imageView = imageView;
    }

    public void write(byte[] bytes){
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            final Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    final byte[] buffer = new byte[1024];
                    int bytes;

                    while (socket != null){
                        try {
                            bytes = inputStream.read(buffer);
                            if(bytes>0){
                                final int finalBytes = bytes;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //ACA SE RECIBEN LOS MENSAJES DEL CLIENTE
                                        String tempMSG = new String(buffer, 0, finalBytes);
                                        //puede ser nulo, se puede modificar a futuro para cambiar la estructura de los mensajes
                                        //se puede convertir de texto a JSON si Fuera Necesario
                                        JsonEntity jsonEntity = gson.fromJson(tempMSG, JsonEntity.class);

                                        if(jsonEntity.getTypeMessage().equals("SCREEN_SHARING")) {
                                            PinchEvent currentPinchEvent = CurrentState.getInstance().getPinchEvent();
                                            Long timeInMyLastPinch = (long) -1000000000;
                                            if(currentPinchEvent != null) {
                                                timeInMyLastPinch = currentPinchEvent.getTimePinch().getTime();
                                            }
                                            boolean isInRange = false;
                                            if(jsonEntity.getPinchEvent() != null) {
                                                Date datePinch = jsonEntity.getPinchEvent().getTimePinch();
                                                Long timeInPinch = datePinch.getTime();
                                                long absValue = Math.abs(timeInMyLastPinch - timeInPinch);
                                                isInRange = absValue<=1000;
                                            }

                                            if(isInRange) {
                                                //evento ocurre
                                                if(CurrentState.getInstance().getPinchEvent().getDirectionPinch().equals("Right") &&
                                                    jsonEntity.getPinchEvent().getDirectionPinch().equals("Left")) {
                                                }
                                                if(CurrentState.getInstance().getPinchEvent().getDirectionPinch().equals("Left") &&
                                                        jsonEntity.getPinchEvent().getDirectionPinch().equals("Right")) {
                                                    float jsonPosPinchX = jsonEntity.getPinchEvent().getPosPinchX();
                                                    float jsonPosPinchY = jsonEntity.getPinchEvent().getPosPinchY();
                                                    float jsonScreenHeigth = jsonEntity.getPinchEvent().getScreenHeigth();
                                                    float jsonScreenWidth = jsonEntity.getPinchEvent().getScreenWidth();

                                                    double lienzoH = jsonEntity.getCanvasHeigth();
                                                    double lienzoW = jsonEntity.getCanvasWidth();

                                                    float deviceScreenHeigth = CurrentState.getInstance().getPinchEvent().getScreenHeigth();
                                                    float deviceScreenWidth = CurrentState.getInstance().getPinchEvent().getScreenWidth();

                                                    float currentPosPinchX = CurrentState.getInstance().getPinchEvent().getPosPinchX();
                                                    float currentPosPinchY = CurrentState.getInstance().getPinchEvent().getPosPinchY();

                                                    float newX = (-1)*(jsonPosPinchX + 0)+ currentPosPinchX - 100;//a futuro ese 0 seran las coordenadas origen (0,0) acumulado
                                                    float newY = (-1)*(jsonPosPinchY + 0) + currentPosPinchY;//a futuro ese 0 seran las coordenadas origen (0,0) acumulado

                                                    imageView.setY(newY);
                                                    imageView.setX(newX);
                                                }
                                            }
                                        }

                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}