package com.example.jsonsocket.jsonsEntities;

import java.util.Date;

public class PinchEvent {
    private String deviceName;
    private String physicalAddress;
    private float posPinchX;
    private float posPinchY;
    private float screenWidth;
    private float screenHeigth;
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

    public float getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public float getScreenHeigth() {
        return screenHeigth;
    }

    public void setScreenHeigth(float screenHeigth) {
        this.screenHeigth = screenHeigth;
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
