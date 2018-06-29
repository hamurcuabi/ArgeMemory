package com.emrehmrc.argememory.model;

public class MemberModel {

    String name;
    String id;
    String email;
    String compID;
    String image;

    public MemberModel(String name, String id, String email, String compID, String image) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.compID = compID;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompID() {
        return compID;
    }

    public void setCompID(String compID) {
        this.compID = compID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public MemberModel() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
