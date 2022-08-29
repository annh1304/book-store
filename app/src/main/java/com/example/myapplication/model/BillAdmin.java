package com.example.myapplication.model;

import java.io.Serializable;

public class BillAdmin implements Serializable {
    String USERID, TITLE, PHONE, NAME, CURRENTDATE, CURRENTTIME, ADDRESS, STATUS, IMAGE, DOCUMENTID;
    int TOTALQUANTITY, PRICE, TOTALPRICE;

    public BillAdmin() {
    }

    public BillAdmin(String USERID, String TITLE, String PHONE, String NAME, String CURRENTDATE, String CURRENTTIME, String ADDRESS, String STATUS, String IMAGE, String DOCUMENTID, int TOTALQUANTITY, int PRICE, int TOTALPRICE) {
        this.USERID = USERID;
        this.TITLE = TITLE;
        this.PHONE = PHONE;
        this.NAME = NAME;
        this.CURRENTDATE = CURRENTDATE;
        this.CURRENTTIME = CURRENTTIME;
        this.ADDRESS = ADDRESS;
        this.STATUS = STATUS;
        this.IMAGE = IMAGE;
        this.DOCUMENTID = DOCUMENTID;
        this.TOTALQUANTITY = TOTALQUANTITY;
        this.PRICE = PRICE;
        this.TOTALPRICE = TOTALPRICE;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
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

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getDOCUMENTID() {
        return DOCUMENTID;
    }

    public void setDOCUMENTID(String DOCUMENTID) {
        this.DOCUMENTID = DOCUMENTID;
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

    public int getTOTALPRICE() {
        return TOTALPRICE;
    }

    public void setTOTALPRICE(int TOTALPRICE) {
        this.TOTALPRICE = TOTALPRICE;
    }
}
