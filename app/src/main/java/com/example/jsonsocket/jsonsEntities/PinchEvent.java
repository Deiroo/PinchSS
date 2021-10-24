package com.example.jsonsocket.jsonsEntities;

import java.util.Date;

public class PinchEvent {
    private String deviceName;
    private String physicalAddress;
    private float posPinchX;
    private float posPinchY;
    private float screenSizeX;
    private float screenSizeY;
    private Date timePinch;
    private String directionPinch;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public float getPosPinchX() {
        return posPinchX;
    }

    public void setPosPinchX(float posPinchX) {
        this.posPinchX = posPinchX;
    }

    public float getPosPinchY() {
        return posPinchY;
    }

    public void setPosPinchY(float posPinchY) {
        this.posPinchY = posPinchY;
    }

    public float getScreenSizeX() {
        return screenSizeX;
    }

    public void setScreenSizeX(float screenSizeX) {
        this.screenSizeX = screenSizeX;
    }

    public float getScreenSizeY() {
        return screenSizeY;
    }

    public void setScreenSizeY(float screenSizeY) {
        this.screenSizeY = screenSizeY;
    }

    public Date getTimePinch() {
        return timePinch;
    }

    public void setTimePinch(Date timePinch) {
        this.timePinch = timePinch;
    }

    public String getDirectionPinch() {
        return directionPinch;
    }

    public void setDirectionPinch(String directionPinch) {
        this.directionPinch = directionPinch;
    }
}
