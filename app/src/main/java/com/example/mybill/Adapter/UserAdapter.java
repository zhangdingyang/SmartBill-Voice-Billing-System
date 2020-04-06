package com.example.mybill.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.R;
import com.example.mybill.bean.User;
import com.example.mybill.bean.UserFollow;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserAdapter extends ArrayAdapter<User> {
    private int resourceId;

    public UserAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final User users = getItem(position); //获取当前项的实例

        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        final UserAdapter.ViewHolder viewHolder;
        if (convertView == null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder = new ViewHolder();
            viewHolder.userName = view.findViewById(R.id.text_useritem_name);
            viewHolder.gender = view.findViewById(R.id.text_useritem_gender);
            viewHolder.address = view.findViewById(R.id.text_useritem_address);
            viewHolder.followButton = view.findViewById(R.id.btn_follow);

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(UserAdapter.ViewHolder) view.getTag();
        }

        //正确设置关注和取关按钮
        setFollowButton(users, viewHolder);

        // 获取控件实例，并调用set...方法使其显示出来
        viewHolder.userName.setText(users.getUsername());
        viewHolder.gender.setText(users.getGender() + "\t");
        viewHolder.address.setText(users.getAddress() + "\t");
        return view;
    }

    /**
     * 正确设置关注和取关按钮
     * @param users
     * @param viewHolder
     */
    private void setFollowButton(final User users, final ViewHolder viewHolder) {

        BmobQuery<UserFollow> query = new BmobQuery<>();
        query.addWhereEqualTo("follow", BmobUser.getCurrentUser().getObjectId());
        query.addWhereEqualTo("followed", users.getObjectId());
        query.findObjects(new FindListener<UserFollow>() {
            @Override
            public void done(final List<UserFollow> list, BmobException e) {
                if (e == null){
                    if (list == null || list.size() == 0){
                        viewHolder.followButton.setText("未关注，点击关注");
                        viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UserFollow userFollow = new UserFollow();
                                userFollow.setFollow(BmobUser.getCurrentUser());
                                userFollow.setFollowed(users);
                                userFollow.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null){
                                            Toast.makeText(getContext(),"关注成功",Toast.LENGTH_SHORT).show();
                                            setFollowButton(users, viewHolder);
                                        }
                                        else
                                            Toast.makeText(getContext(),"关注出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                    else {
                        viewHolder.followButton.setText("已关注，点击取消关注");
                        viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UserFollow userFollow = list.get(0);
                                userFollow.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            Toast.makeText(getContext(),"取消关注成功",Toast.LENGTH_SHORT).show();
                                            setFollowButton(users, viewHolder);
                                        }
                                        else
                                            Toast.makeText(getContext(),"取消关注出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
                else
                    Toast.makeText(getContext(),"查询用户关注关系出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView userName;
        TextView gender;
        TextView address;
        Button followButton;
    }
}
