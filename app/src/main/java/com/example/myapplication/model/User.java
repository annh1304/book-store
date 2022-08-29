package com.example.myapplication.model;

public class User {
    String EMAIL;
    String PASSWORD;
    String NAME;
    String ADDRESS;
    String BIRTHDAY;
    String PHONE;
    Integer ROLE;

    public User(String EMAIL, String PASSWORD, String NAME, String ADDRESS, String BIRTHDAY, String PHONE, Integer ROLE) {
        this.EMAIL = EMAIL;
        this.PASSWORD = PASSWORD;
        this.NAME = NAME;
        this.ADDRESS = ADDRESS;
        this.BIRTHDAY = BIRTHDAY;
        this.PHONE = PHONE;
        this.ROLE = ROLE;
    }

    public User() {
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getBIRTHDAY() {
        return BIRTHDAY;
    }

    public void setBIRTHDAY(String BIRTHDAY) {
        this.BIRTHDAY = BIRTHDAY;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public Integer getROLE() {
        return ROLE;
    }

    public void setROLE(Integer ROLE) {
        this.ROLE = ROLE;
    }
}
