package com.example.myapplication.model;

import java.io.Serializable;

public class MyBill implements Serializable {
    String TITLE, CURRENTDATE, CURRENTTIME, IMAGE, STATUS;
    int TOTALPRICE,TOTALQUANTITY, PRICE;
    String DOCUMENTID;

    public MyBill() {
    }

    public MyBill(String TITLE, String CURRENTDATE, String CURRENTTIME, String IMAGE, String STATUS, int TOTALPRICE, int TOTALQUANTITY, int PRICE, String DOCUMENTID) {
        this.TITLE = TITLE;
        this.CURRENTDATE = CURRENTDATE;
        this.CURRENTTIME = CURRENTTIME;
        this.IMAGE = IMAGE;
        this.STATUS = STATUS;
        this.TOTALPRICE = TOTALPRICE;
        this.TOTALQUANTITY = TOTALQUANTITY;
        this.PRICE = PRICE;
        this.DOCUMENTID = DOCUMENTID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getCURRENTDATE() {
        return CURRENTDATE;
    }

    public void setCURRENTDATE(String CURRENTDATE) {
        this.CURRENTDATE = CURRENTDATE;
    }

    public String getCURRENTTIME() {
        return CURRENTTIME;
    }

    public void setCURRENTTIME(String CURRENTTIME) {
        this.CURRENTTIME = CURRENTTIME;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public int getTOTALPRICE() {
        return TOTALPRICE;
    }

    public void setTOTALPRICE(int TOTALPRICE) {
        this.TOTALPRICE = TOTALPRICE;
    }

    public int getTOTALQUANTITY() {
        return TOTALQUANTITY;
    }

    public void setTOTALQUANTITY(int TOTALQUANTITY) {
        this.TOTALQUANTITY = TOTALQUANTITY;
    }

    public int getPRICE() {
        return PRICE;
    }

    public void setPRICE(int PRICE) {
        this.PRICE = PRICE;
    }

    public String getDOCUMENTID() {
        return DOCUMENTID;
    }

    public void setDOCUMENTID(String DOCUMENTID) {
        this.DOCUMENTID = DOCUMENTID;
    }
}