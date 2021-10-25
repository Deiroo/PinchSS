package com.example.jsonsocket.currentState;


import com.example.jsonsocket.jsonsEntities.PinchEvent;

//Aplicando Singleton
public final class CurrentState {
    private static CurrentState instance;

    //Para SCREEN_SHARING
    private PinchEvent pinchEvent;

    //OBTENER EL ORIGEN QUE SERIA DEL LIENZO (0,0) en el primero, en el segundo seria (200,100) por ejemplo

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
