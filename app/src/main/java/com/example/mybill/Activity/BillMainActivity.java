package com.example.mybill.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mybill.R;
import com.example.mybill.bean.User;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;

public class BillMainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private BillHomeFragment billHomeFragment;
    private BillStatFragment billStatFragment;
    private BillAddFragment billAddFragment;
    private BillFocusFragment billFocusFragment;
    private BillMeFragment billMeFragment;
    private Fragment[] fragments;
    private int lastFragment;//用于记录上个选择的Fragment

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_main);
        ButterKnife.bind(this);
        initFragment();
        fetchUserInfo();
    }

    private void initFragment()
    {
        billHomeFragment = new BillHomeFragment();
        billStatFragment = new BillStatFragment();
        billAddFragment = new BillAddFragment();
        billFocusFragment = new BillFocusFragment();
        billMeFragment = new BillMeFragment();
        fragments = new Fragment[]{billHomeFragment, billStatFragment, billAddFragment, billFocusFragment, billMeFragment};
        lastFragment =0;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview, billHomeFragment).show(billHomeFragment).commit();
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bnv);

        bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
    }

    //判断选择的菜单
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.id1:
                {
                    if(lastFragment !=0)
                    {
                        switchFragment(lastFragment,0);
                        lastFragment =0;
                    }
                    return true;
                }
                case R.id.id2:
                {
                    if(lastFragment !=1)
                    {
                        switchFragment(lastFragment,1);
                        lastFragment =1;
                    }
                    return true;
                }
                case R.id.id3:
                {
                    if(lastFragment !=2)
                    {
                        switchFragment(lastFragment,2);
                        lastFragment =2;
                    }
                    return true;
                }
                case R.id.id4:
                {
                    if(lastFragment !=3)
                    {
                        switchFragment(lastFragment,3);
                        lastFragment =3;
                    }
                    return true;
                }
                case R.id.id5:
                {
                    if(lastFragment !=4)
                    {
                        switchFragment(lastFragment,4);
                        lastFragment =4;
                    }
                    return true;
                }
            }


            return false;
        }
    };

    //切换Fragment
    private void switchFragment(int lastfragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(!fragments[index].isAdded())
        {
            transaction.add(R.id.mainview,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();

    }

    /**
     * 同步控制台数据到缓存中
     */
    private void fetchUserInfo() {
        BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    final User myUser = BmobUser.getCurrentUser(User.class);
                    Toast.makeText(BillMainActivity.this,"更新用户本地缓存信息成功："+myUser.getUsername(),Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("error",e.getMessage());
                    Toast.makeText(BillMainActivity.this,"更新用户本地缓存信息失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
