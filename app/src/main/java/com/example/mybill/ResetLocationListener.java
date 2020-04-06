package com.example.mybill;

import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.example.mybill.Activity.BillMeFragment;
import com.example.mybill.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class ResetLocationListener extends BDAbstractLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        String country = location.getCountry();    //获取国家
        String province = location.getProvince();    //获取省份
        String city = location.getCity();    //获取城市
        String district = location.getDistrict();    //获取区县

        int errorCode = location.getLocType();
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
        if (errorCode != 61 && errorCode != 161){
            Toast.makeText(getApplicationContext(),"定位出错，错误代码：" + errorCode,Toast.LENGTH_SHORT).show();
            return;
        }

        //修改用户信息
        User currentUser = new User();
        currentUser.setObjectId(BmobUser.getCurrentUser().getObjectId());
        currentUser.setAddress(country + province + city + district);
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(getApplicationContext(),"修改用户所在地成功。",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"修改用户所在地出错：" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
