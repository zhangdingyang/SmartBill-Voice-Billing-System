package com.example.mybill.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class UserFollow extends BmobObject {
    private BmobUser follow;
    private BmobUser followed;

    public BmobUser getFollow() {
        return follow;
    }

    public void setFollow(BmobUser follow) {
        this.follow = follow;
    }

    public BmobUser getFollowed() {
        return followed;
    }

    public void setFollowed(BmobUser followed) {
        this.followed = followed;
    }
}
