package com.emrehmrc.argememory.model;

import java.io.Serializable;

public class TagModel implements Serializable {

    private String tag;
    private boolean isOk;
    private String id;

    public TagModel() {
    }

    public TagModel(String tag, boolean isOk, String id) {

        this.tag = tag;
        this.isOk = isOk;
        this.id = id;
    }

    public String getTag() {

        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
