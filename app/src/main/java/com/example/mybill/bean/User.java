package com.example.mybill.bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }
}
