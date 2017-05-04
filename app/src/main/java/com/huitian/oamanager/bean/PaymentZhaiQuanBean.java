package com.huitian.oamanager.bean;

/**
 * Created by Chen on 2017/5/2.
 */

public class PaymentZhaiQuanBean {


    /**
     * CREDIT_AMOUNT : 589130.56
     * day : 0
     * month : 0
     * year : 1236622.94
     */

    private String CREDIT_AMOUNT;
    private int day;
    private int month;
    private String year;

    public String getCREDIT_AMOUNT() {
        return CREDIT_AMOUNT;
    }

    public void setCREDIT_AMOUNT(String CREDIT_AMOUNT) {
        this.CREDIT_AMOUNT = CREDIT_AMOUNT;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
