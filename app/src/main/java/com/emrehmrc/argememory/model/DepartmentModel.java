package com.emrehmrc.argememory.model;

import java.io.Serializable;

public class DepartmentModel implements Serializable {
    private boolean isOk;
    private String text;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DepartmentModel() {
    }

    public DepartmentModel(boolean isOk, String text, String id) {

        this.isOk = isOk;
        this.text = text;
        this.id=id;
    }
}
