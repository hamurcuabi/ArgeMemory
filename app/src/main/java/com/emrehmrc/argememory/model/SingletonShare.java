package com.emrehmrc.argememory.model;

import java.util.ArrayList;

public class SingletonShare {
    private static SingletonShare instance = null;
    private static ArrayList<DepartmentModel> depList;
    private static ArrayList<PersonelModel> persList;
    private static ArrayList<TagModel> tagList;
    private static String comment;
    private static String newTag;
    //Dialogfragment Despcription
    private  String sharedId;
    private String  oldDescp;



    public String getOldDescp() {
        return oldDescp;
    }

    public void setOldDescp(String oldDescp) {
        this.oldDescp = oldDescp;
    }

    private SingletonShare() {
    }

    public String getSharedId() {
        return sharedId;
    }

    public void setSharedId(String sharedId) {
        this.sharedId = sharedId;
    }

    public static SingletonShare getInstance() {
        if (instance == null) {
            instance = new SingletonShare();
            depList = new ArrayList<>();
            persList = new ArrayList<>();
            tagList = new ArrayList<>();
            newTag = "";

        }
        return instance;
    }

    public static void setInstance(SingletonShare instance) {
        SingletonShare.instance = instance;
    }

    public void setNull() {
        instance = null;
    }

    public String getNewTag() {
        return newTag;
    }

    public void setNewTag(String newTag) {
        this.newTag = newTag;
    }

    public ArrayList<DepartmentModel> getDepList() {
        return depList;
    }

    public void setDepList(ArrayList<DepartmentModel> depList) {
        this.depList = depList;
    }

    public ArrayList<PersonelModel> getPersList() {
        return persList;
    }

    public void setPersList(ArrayList<PersonelModel> persList) {
        this.persList = persList;
    }

    public ArrayList<TagModel> getTagList() {
        return tagList;
    }

    public void setTagList(ArrayList<TagModel> tagList) {
        this.tagList = tagList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
