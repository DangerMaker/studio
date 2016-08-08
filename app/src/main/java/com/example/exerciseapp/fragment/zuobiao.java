package com.example.exerciseapp.fragment;


public class zuobiao {
    public double jingdu;
    public double weidu;
    public double altit;

    public double getAltit() {
        return altit;
    }

    public void setAltit(double altit) {
        this.altit = altit;
    }

    public double getJingdu() {
        return jingdu;
    }

    public void setJingdu(double jingdu) {
        this.jingdu = jingdu;
    }

    public double getWeidu() {
        return weidu;
    }

    public void setWeidu(double weidu) {
        this.weidu = weidu;
    }

    public zuobiao() {
    }

    public zuobiao(double x, double y) {
        this.jingdu = x;
        this.weidu = y;
    }

    public void zuoBiaoAdd() {
        this.jingdu += 0.00001;
        this.weidu += 0.00002;
    }


}