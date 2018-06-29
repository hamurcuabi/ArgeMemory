package com.emrehmrc.argememory.model;

import java.io.Serializable;
import java.security.SecureRandom;

public class ShareCommentModel implements Serializable{
     private String commenter;
     private String date;
     private String comment;

    public ShareCommentModel(String commenter, String date, String comment) {
        this.commenter = commenter;
        this.date = date;
        this.comment = comment;
    }

    public ShareCommentModel() {
    }

    public String getCommenter() {

        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
