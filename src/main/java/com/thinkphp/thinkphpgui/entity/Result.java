package com.thinkphp.thinkphpgui.entity;


public class Result {
    boolean res;
    String payload;
    String vuln;

    public boolean isRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }

    public String getPayload() {
        return payload;
    }

    public String getVuln() {
        return vuln;
    }

    public Result(boolean res, String vuln, String payload) {
        this.res = res;
        this.payload = payload;
        this.vuln = vuln;
    }

}
