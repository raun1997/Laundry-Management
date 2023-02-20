package com.appdev.laundarymanagement;

public class Particular {
        private String particular,unit,rate,SNo;

        public Particular(String particular, String unit,String rate,String SNo) {
            this.particular = particular;
            this.unit = unit;
            this.rate = rate;
            this.SNo=SNo;
        }

    public String getParticular() {
        return particular;
    }

    public String getUnit() {
        return unit;
    }

    public String getRate() {
        return rate;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setSNo(String SNo) {
        this.SNo = SNo;
    }

    public String getSNo() {
        return SNo;
    }
    // getters and setters
}
