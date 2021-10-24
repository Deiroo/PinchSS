package com.example.jsonsocket.currentState;


import com.example.jsonsocket.jsonsEntities.PinchEvent;

//Aplicando Singleton
public final class CurrentState {
    private static CurrentState instance;

    //Para SCREEN_SHARING
    private PinchEvent pinchEvent;


    private CurrentState() {
    }

    public static CurrentState getInstance() {
        if (instance == null) {
            instance = new CurrentState();
        }
        return instance;
    }

    public static void setInstance(CurrentState instance) {
        CurrentState.instance = instance;
    }

    public PinchEvent getPinchEvent() {
        return pinchEvent;
    }

    public void setPinchEvent(PinchEvent pinchEvent) {
        this.pinchEvent = pinchEvent;
    }
}
