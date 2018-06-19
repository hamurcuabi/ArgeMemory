package com.emrehmrc.argememory.model;

import java.io.Serializable;

public class PersonelModel implements Serializable{

    private String id;
    private String name;
    private boolean isOk;

    public PersonelModel()  {
    }

    public PersonelModel(String id, String name, boolean isOk) {

        this.id = id;
        this.name = name;
        this.isOk = isOk;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }
}
