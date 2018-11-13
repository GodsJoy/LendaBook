package com.example.android.lendabook;

/**
 * Created by ayomide on 11/8/18.
 * A User_Group POJO. maps users to groups
 */
public class User_Group {
    private String user_id;
    private String group_id;
    private String group_name;

    public User_Group(){}

    public User_Group(String user_id, String group_id, String group_name) {
        this.user_id = user_id;
        this.group_id = group_id;
        this.group_name = group_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
