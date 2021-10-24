package com.example.jsonsocket.jsonsEntities;

public class JsonEntity {
    private String typeMessage;//SCREEN_SHARING, UPDATE_STATE, SYNC_PALETTE,

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
}
