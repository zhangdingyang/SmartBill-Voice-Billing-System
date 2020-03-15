package com.example.mybill.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.mybill.R;

import butterknife.ButterKnife;

public class BillMainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private BillHomeFragment billHomeFragment;
    private BillStatFragment billStatFragment;
    private BillFocusFragment billFocusFragment;
    private BillMeFragment billMeFragment;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_main);
        ButterKnife.bind(this);
    }

    private void initFragment()
    {
        billHomeFragment = new BillHomeFragment();
        billStatFragment = new BillStatFragment();
        billFocusFragment = new BillFocusFragment();
        billMeFragment = new BillMeFragment();
        fragments = new Fragment[]{billHomeFragment, billStatFragment, billFocusFragment, billMeFragment};
        lastfragment=0;
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
                    if(lastfragment!=0)
                    {
                        switchFragment(lastfragment,0);
                        lastfragment=0;
                    }
                    return true;
                }
                case R.id.id2:
                {
                    if(lastfragment!=1)
                    {
                        switchFragment(lastfragment,1);
                        lastfragment=1;
                    }
                    return true;
                }
                case R.id.id3:
                {
                    if(lastfragment!=2)
                    {
                        switchFragment(lastfragment,2);
                        lastfragment=2;
                    }
                    return true;
                }
                case R.id.id4:
                {
                    if(lastfragment!=3)
                    {
                        switchFragment(lastfragment,3);
                        lastfragment=3;
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

}
