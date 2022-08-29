package com.example.myapplication.model;
import java.io.Serializable;

public class MyCart implements Serializable {
    String TITLE, CURRENTDATE, CURRENTTIME, IMAGE;
    int TOTALPRICE,TOTALQUANTITY,PRICE ;
    String DOCUMENTID;

    public MyCart() {
    }

    public MyCart(String TITLE, String CURRENTDATE, String CURRENTTIME, String IMAGE, int TOTALPRICE, int TOTALQUANTITY, int PRICE, String DOCUMENTID) {
        this.TITLE = TITLE;
        this.CURRENTDATE = CURRENTDATE;
        this.CURRENTTIME = CURRENTTIME;
        this.IMAGE = IMAGE;
        this.TOTALPRICE = TOTALPRICE;
        this.TOTALQUANTITY = TOTALQUANTITY;
        this.PRICE = PRICE;
        this.DOCUMENTID = DOCUMENTID;
    }

    public String getDOCUMENTID() {
        return DOCUMENTID;
    }

    public void setDOCUMENTID(String DOCUMENTID) {
        this.DOCUMENTID = DOCUMENTID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public int getTOTALQUANTITY() {
        return TOTALQUANTITY;
    }

    public void setTOTALQUANTITY(int TOTALQUANTITY) {
        this.TOTALQUANTITY = TOTALQUANTITY;
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

    public int getTOTALPRICE() {
        return TOTALPRICE;
    }

    public void setTOTALPRICE(int TOTALPRICE) {
        this.TOTALPRICE = TOTALPRICE;
    }

    public int getPRICE() {
        return PRICE;
    }

    public void setPRICE(int PRICE) {
        this.PRICE = PRICE;
    }
}
