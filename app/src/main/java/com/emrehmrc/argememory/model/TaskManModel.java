package com.emrehmrc.argememory.model;

public class TaskManModel {
    private String memberName;

    public TaskManModel(String memberName) {
        this.memberName = memberName;
    }

    public TaskManModel() {

    }

    public String getMemberName() {

        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
