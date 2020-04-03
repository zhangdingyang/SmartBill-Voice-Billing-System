package com.example.mybill.bean;

import cn.bmob.v3.BmobObject;

public class PaymentMethod extends BmobObject {
    private String name;
    private User userId;

    public PaymentMethod(){}

    public PaymentMethod(String objectId){
        this.setObjectId(objectId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
