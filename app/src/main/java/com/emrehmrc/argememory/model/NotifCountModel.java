package com.emrehmrc.argememory.model;

public class NotifCountModel {

    private String countNotf;

    public NotifCountModel() {
        countNotf="0";
    }

    public NotifCountModel(String countNotf) {

        this.countNotf = countNotf;
    }

    public String getCountNotf() {

        return countNotf;
    }

    public void setCountNotf(String countNotf) {
        this.countNotf = countNotf;
    }
}
