package com.example.mybill.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mybill.R;
import com.example.mybill.bean.Bill;
import com.example.mybill.bean.Category;
import com.example.mybill.bean.PaymentMethod;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class BillAdapter extends ArrayAdapter<Bill> {
    private int resourceId;

    public BillAdapter(Context context, int resource, List<Bill> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Bill bill = getItem(position); //获取当前项的实例

        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        final ViewHolder viewHolder;
        if (convertView == null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder = new ViewHolder();
            viewHolder.date = view.findViewById(R.id.text_date);
            viewHolder.type = view.findViewById(R.id.text_type);
            viewHolder.category = view.findViewById(R.id.text_category);
            viewHolder.paymentMethod = view.findViewById(R.id.text_paymentMethod);
            viewHolder.amount = view.findViewById(R.id.text_amount);
            viewHolder.title = view.findViewById(R.id.text_title);

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        // 获取控件实例，并调用set...方法使其显示出来
        viewHolder.date.setText(bill.getDate().getDate().split(" ")[0]);
        if (bill.getType().equals("in"))
            viewHolder.type.setText("收入\t");
        else if (bill.getType().equals("out"))
            viewHolder.type.setText("支出\t");
        new BmobQuery<Category>().getObject(bill.getCategoryId().getObjectId(), new QueryListener<Category>() {
            @Override
            public void done(Category category, BmobException e) {
                if (e == null)
                    viewHolder.category.setText(category.getName() + "\t");
            }
        });
        new BmobQuery<PaymentMethod>().getObject(bill.getPaymentMethod().getObjectId(), new QueryListener<PaymentMethod>() {
            @Override
            public void done(PaymentMethod paymentMethod, BmobException e) {
                if (e == null)
                    viewHolder.paymentMethod.setText(paymentMethod.getName() + "\t");
            }
        });
        viewHolder.amount.setText("￥" + bill.getAmount());
        viewHolder.title.setText(bill.getTitle());
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView date;
        TextView type;
        TextView category;
        TextView paymentMethod;
        TextView amount;
        TextView title;
    }
}
