package Model;

import java.io.Serializable;

public class Retail implements Serializable {
    private static final long id = 1L;

    private String retName;
    private String retPhone;
    private String retId;
    private String retCode;
    private String retCat;
    private double retLat;
    private double retLng;
    private String retAdd;
    private String retCity;
    private String retPin;
    private String retImage;
    private String retDesc;
    private String retType;

    public Retail(String retName, String retPhone, String retId, String retCode, String retCat, double retLat, double retLng, String retAdd, String retCity, String retPin, String retImage, String retDesc, String retType) {
        this.retName = retName;
        this.retPhone = retPhone;
        this.retId = retId;
        this.retCode = retCode;
        this.retCat = retCat;
        this.retLat = retLat;
        this.retLng = retLng;
        this.retAdd = retAdd;
        this.retCity = retCity;
        this.retPin = retPin;
        this.retImage = retImage;
        this.retDesc = retDesc;
        this.retType = retType;
    }



    public Retail() {
    }

    public String getRetName() {
        return retName;
    }

    public void setRetName(String retName) {
        this.retName = retName;
    }

    public String getRetPhone() {
        return retPhone;
    }

    public void setRetPhone(String retPhone) {
        this.retPhone = retPhone;
    }

    public String getRetId() {
        return retId;
    }

    public void setRetId(String retId) {
        this.retId = retId;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetCat() {
        return retCat;
    }

    public void setRetCat(String retCat) {
        this.retCat = retCat;
    }

    public double getRetLat() {
        return retLat;
    }

    public void setRetLat(double retLat) {
        this.retLat = retLat;
    }

    public double getRetLng() {
        return retLng;
    }

    public void setRetLng(double retLng) {
        this.retLng = retLng;
    }

    public String getRetAdd() {
        return retAdd;
    }

    public void setRetAdd(String retAdd) {
        this.retAdd = retAdd;
    }

    public String getRetCity() {
        return retCity;
    }

    public void setRetCity(String retCity) {
        this.retCity = retCity;
    }

    public String getRetPin() {
        return retPin;
    }

    public void setRetPin(String retPin) {
        this.retPin = retPin;
    }

    public String getRetImage() {
        return retImage;
    }

    public void setRetImage(String retImage) {
        this.retImage = retImage;
    }

    public String getRetDesc() {
        return retDesc;
    }

    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    public String getRetType() {
        return retType;
    }

    public void setRetType(String retType) {
        this.retType = retType;
    }

}

