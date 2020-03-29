package com.example.mybill.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mybill.R;
import com.example.mybill.bean.Category;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private int resourceId;

    public CategoryAdapter(Context context, int resource, List<Category> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Category category = getItem(position); //获取当前项的实例

        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        final ViewHolder viewHolder;
        if (convertView == null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.text_name);
            viewHolder.type = view.findViewById(R.id.text_type);

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        // 获取控件实例，并调用set...方法使其显示出来
        viewHolder.name.setText(category.getName());
        if (category.getType().equals("in"))
            viewHolder.type.setText("收入\t");
        else if (category.getType().equals("out"))
            viewHolder.type.setText("支出\t");
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView name;
        TextView type;
    }
}
