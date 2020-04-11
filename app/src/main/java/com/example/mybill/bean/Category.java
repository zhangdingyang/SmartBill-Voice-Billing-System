package com.example.mybill.bean;

import cn.bmob.v3.BmobObject;

public class Category extends BmobObject {
    private String name;
    private String type;
    private User userId;
    private Boolean isDeleted;

    public Category(){}

    public Category(String categoryId){
        this.setObjectId(categoryId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
