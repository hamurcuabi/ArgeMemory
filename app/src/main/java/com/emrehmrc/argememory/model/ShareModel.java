package com.emrehmrc.argememory.model;

import java.io.Serializable;

public class ShareModel implements Serializable {

    private String shareDate;
    private String shareOwner;
    private String shareTag;
    private String shareDescp;
    private String shareCountMember;
    private String shareID;
    private String shareCountComment;

    public ShareModel(String shareDate, String shareOwner, String shareTag, String shareDescp, String shareCountMember, String shareID, String shareCountComment) {
        this.shareDate = shareDate;
        this.shareOwner = shareOwner;
        this.shareTag = shareTag;
        this.shareDescp = shareDescp;
        this.shareCountMember = shareCountMember;
        this.shareID = shareID;
        this.shareCountComment = shareCountComment;
    }

    public ShareModel() {
    }

    public String getShareCountComment() {
        return shareCountComment;
    }

    public void setShareCountComment(String shareCountComment) {
        this.shareCountComment = shareCountComment;
    }

    public String getShareDate() {

        return shareDate;
    }

    public void setShareDate(String shareDate) {
        this.shareDate = shareDate;
    }

    public String getShareOwner() {
        return shareOwner;
    }

    public void setShareOwner(String shareOwner) {
        this.shareOwner = shareOwner;
    }

    public String getShareTag() {
        return shareTag;
    }

    public void setShareTag(String shareTag) {
        this.shareTag = shareTag;
    }

    public String getShareDescp() {
        return shareDescp;
    }

    public void setShareDescp(String shareDescp) {
        this.shareDescp = shareDescp;
    }

    public String getShareCountMember() {
        return shareCountMember;
    }

    public void setShareCountMember(String shareCountMember) {
        this.shareCountMember = shareCountMember;
    }

    public String getShareID() {
        return shareID;
    }

    public void setShareID(String shareID) {
        this.shareID = shareID;
    }
}
