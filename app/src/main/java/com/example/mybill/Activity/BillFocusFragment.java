package com.example.mybill.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.Adapter.MomentsAdapter;
import com.example.mybill.R;
import com.example.mybill.bean.Bill;
import com.example.mybill.bean.Moments;
import com.example.mybill.bean.User;
import com.example.mybill.bean.UserFollow;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;


public class BillFocusFragment extends Fragment {
    ListView listViewMoments;

    List<Moments> listData;

    MomentsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bill_focus_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listViewMoments = getActivity().findViewById(R.id.momentsListView);
        listData = new ArrayList<>();

        //获取当前用户的位置，再找到附近用户的动态
        new BmobQuery<User>().getObject(BmobUser.getCurrentUser().getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    BmobQuery<User> innerQuery = new BmobQuery<>();
                    innerQuery.addWhereEqualTo("address", user.getAddress());
                    BmobQuery<Bill> bmobQuery = new BmobQuery<>();
                    bmobQuery.setLimit(5);
                    bmobQuery.addWhereGreaterThanOrEqualTo("amount", 1000);
                    bmobQuery.addWhereNotEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
                    bmobQuery.addWhereMatchesQuery("userId", "_User", innerQuery);
                    bmobQuery.order("-createdAt");
                    bmobQuery.findObjects(new FindListener<Bill>() {
                        @Override
                        public void done(List<Bill> list, BmobException e) {
                            if (e == null){
                                for(Bill bill: list){
                                    Moments moments = new Moments();
                                    moments.setUserName("附近用户");
                                    moments.setAmount(bill.getAmount().floatValue());
                                    moments.setComment(bill.getComment());
                                    moments.setCreatedAt(bill.getCreatedAt());
                                    moments.setTitle(bill.getTitle());
                                    moments.setType(bill.getType());
                                    listData.add(moments);
                                }
                                adapter = new MomentsAdapter(getActivity(), R.layout.moments_item, listData);
                                listViewMoments.setAdapter(adapter);
                            }
                            else
                                Toast.makeText(getActivity(),"查询动态列表出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(getActivity(),"查询用户出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        //获取用户关注者并找出其动态
        BmobQuery<UserFollow> followBmobQuery = new BmobQuery<>();
        followBmobQuery.addWhereEqualTo("follow", BmobUser.getCurrentUser().getObjectId());
        followBmobQuery.findObjects(new FindListener<UserFollow>() {
            @Override
            public void done(List<UserFollow> list, BmobException e) {
                if (e == null){
                    List<String> users = new ArrayList<>();
                    for (UserFollow userFollow: list){
                        users.add(userFollow.getFollowed().getObjectId());
                    }
                    //获取用户关注者的动态
                    BmobQuery<Bill> bmobQuery = new BmobQuery<>();
                    bmobQuery.setLimit(5);
                    bmobQuery.include("userId");
                    bmobQuery.addWhereGreaterThanOrEqualTo("amount", 1000);
                    bmobQuery.addWhereNotEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
                    bmobQuery.addWhereContainedIn("userId", users);
                    bmobQuery.order("-createdAt");
                    bmobQuery.findObjects(new FindListener<Bill>() {
                        @Override
                        public void done(List<Bill> list, BmobException e) {
                            if (e == null){
                                for (Bill bill: list){
                                    Moments moments = new Moments();
                                    moments.setUserName(bill.getUserId().getUsername());
                                    moments.setAmount(bill.getAmount().floatValue());
                                    moments.setComment(bill.getComment());
                                    moments.setCreatedAt(bill.getCreatedAt());
                                    moments.setTitle(bill.getTitle());
                                    moments.setType(bill.getType());
                                    listData.add(moments);
                                }
                                adapter = new MomentsAdapter(getActivity(), R.layout.moments_item, listData);
                                listViewMoments.setAdapter(adapter);
                            }
                            else
                                Toast.makeText(getActivity(),"查询动态列表出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(getActivity(),"查询用户关注列表出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}
