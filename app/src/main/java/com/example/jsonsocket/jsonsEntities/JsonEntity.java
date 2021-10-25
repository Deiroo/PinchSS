package com.example.jsonsocket.jsonsEntities;

public class JsonEntity {
    private String typeMessage;//SCREEN_SHARING, UPDATE_STATE, SYNC_PALETTE,

    private double canvasWidth;

    private double canvasHeigth;

    //Para SCREEN_SHARING
    private PinchEvent pinchEvent;

    //Para UPDATE_STATE


    //Para SYNC_PALETTE


    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public PinchEvent getPinchEvent() {
        return pinchEvent;
    }

    public void setPinchEvent(PinchEvent pinchEvent) {
        this.pinchEvent = pinchEvent;
    }

    public double getCanvasWidth() {
        return canvasWidth;
    }

    public void setCanvasWidth(double canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    public double getCanvasHeigth() {
        return canvasHeigth;
    }

    public void setCanvasHeigth(double canvasHeigth) {
        this.canvasHeigth = canvasHeigth;
    }
}
