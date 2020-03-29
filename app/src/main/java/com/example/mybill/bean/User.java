package com.example.mybill.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobReturn;

public class User extends BmobUser {

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    //为新用户创建默认交易方式和类别
    public static void setDefaultCategoryAndPaymentMethod(String userId){
        User user = new User();
        user.setObjectId(userId);

        Category category = new Category();
        category.setName("消费-饮食");
        category.setType("out");
        category.setUserId(user);
        category.save();

        Category category2 = new Category();
        category2.setName("消费-其它");
        category2.setType("out");
        category2.setUserId(user);
        category2.save();

        Category category3 = new Category();
        category3.setName("工资");
        category3.setType("in");
        category3.setUserId(user);
        category3.save();

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setName("现金");
        paymentMethod.setUserId(user);
        paymentMethod.save();

        PaymentMethod paymentMethod2 = new PaymentMethod();
        paymentMethod2.setName("微信支付");
        paymentMethod2.setUserId(user);
        paymentMethod2.save();

        PaymentMethod paymentMethod3 = new PaymentMethod();
        paymentMethod3.setName("支付宝");
        paymentMethod3.setUserId(user);
        paymentMethod3.save();
    }
}
