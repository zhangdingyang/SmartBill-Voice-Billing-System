package com.example.mybill.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mybill.R;
import com.example.mybill.bean.Moments;


import java.util.List;

public class MomentsAdapter extends ArrayAdapter<Moments> {
    private int resourceId;

    public MomentsAdapter(Context context, int resource, List<Moments> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Moments moments = getItem(position); //获取当前项的实例

        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        final MomentsAdapter.ViewHolder viewHolder;
        if (convertView == null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder = new ViewHolder();
            viewHolder.userName = view.findViewById(R.id.text_moments_username);
            viewHolder.title = view.findViewById(R.id.text_moments_title);
            viewHolder.comment = view.findViewById(R.id.text_moments_comment);
            viewHolder.type = view.findViewById(R.id.text_moments_type);
            viewHolder.amount = view.findViewById(R.id.text_moments_amount);
            viewHolder.createdAt = view.findViewById(R.id.text_moments_createdat);

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(MomentsAdapter.ViewHolder) view.getTag();
        }

        // 获取控件实例，并调用set...方法使其显示出来
        if (!moments.getUserName().equals("附近用户"))
            viewHolder.userName.setText("你关注的用户：" + moments.getUserName());
        else
            viewHolder.userName.setText("你附近某土豪大手一挥");
        if (moments.getType().equals("in"))
            viewHolder.type.setText("收入\t");
        else if (moments.getType().equals("out"))
            viewHolder.type.setText("支出\t");
        viewHolder.title.setText(moments.getTitle());
        viewHolder.comment.setText(moments.getComment());
        viewHolder.amount.setText("￥" + moments.getAmount() + "\t");
        viewHolder.createdAt.setText(moments.getCreatedAt());
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView userName;
        TextView title;
        TextView comment;
        TextView type;
        TextView amount;
        TextView createdAt;
    }
}
