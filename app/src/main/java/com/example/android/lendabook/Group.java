package com.example.android.lendabook;

/**
 * Created by ayomide on 11/4/18.
 * A Group POJO
 */
public class Group {
    private String groupName;
    private String groupDesc;
    private String creatorID;
    private float dateCreated;

    public Group(String groupName, String groupDesc, String creatorID, float dateCreated) {
        this.groupName = groupName;
        this.groupDesc = groupDesc;
        this.creatorID = creatorID;
        this.dateCreated = dateCreated;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public float getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(float dateCreated) {
        this.dateCreated = dateCreated;
    }
}
