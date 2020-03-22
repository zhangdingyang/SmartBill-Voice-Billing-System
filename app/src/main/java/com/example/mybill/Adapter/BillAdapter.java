package com.example.mybill.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mybill.R;
import com.example.mybill.bean.Bill;

import java.util.List;

public class BillAdapter extends ArrayAdapter<Bill> {
    private int resourceId;

    public BillAdapter(Context context, int resource, List<Bill> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Bill bill = getItem(position); //获取当前项的Fruit实例

        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        ViewHolder viewHolder;
        if (convertView == null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder = new ViewHolder();
            viewHolder.date = view.findViewById(R.id.text_date);
            viewHolder.type = view.findViewById(R.id.text_type);
            viewHolder.category = view.findViewById(R.id.text_category);
            viewHolder.amount = view.findViewById(R.id.text_amount);
            viewHolder.title = view.findViewById(R.id.text_title);

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        // 获取控件实例，并调用set...方法使其显示出来
        viewHolder.date.setText(bill.getDate().getDate());
        viewHolder.type.setText(bill.getType());
        viewHolder.category.setText(bill.getCategoryId().getName());
        viewHolder.amount.setText("￥" + bill.getAmount());
        viewHolder.title.setText(bill.getTitle());
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView date;
        TextView type;
        TextView category;
        TextView amount;
        TextView title;
    }
}
