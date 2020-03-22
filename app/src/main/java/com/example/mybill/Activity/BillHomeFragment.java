package com.example.mybill.Activity;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.Adapter.BillAdapter;
import com.example.mybill.R;
import com.example.mybill.bean.Bill;
import com.example.mybill.bean.PaymentMethod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class BillHomeFragment extends Fragment {
    ListView listView;
    ArrayAdapter<Bill> billArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bill_home_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Bill> listData = getBillData();
        ArrayAdapter<Bill> adapter = new BillAdapter(getActivity(), R.layout.bill_item, listData);
        listView = getActivity().findViewById(R.id.billListView);
        listView.setAdapter(adapter);

        //单击一行数据的事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        //长按一行数据的事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

    }

    private List<Bill> getBillData(){
        final List<Bill> bills = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        BmobQuery<Bill> bmobQuery = new BmobQuery<Bill>();
        bmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        bmobQuery.addWhereGreaterThan("createdAt", new BmobDate(new Date(calendar.get(Calendar.YEAR-1), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))));
        bmobQuery.findObjects(new FindListener<Bill>() {
            @Override
            public void done(List<Bill> list, BmobException e) {
                if (e == null){
                    bills.addAll(list);
                }
                else {
                    Toast.makeText(getActivity(),"查询账单出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        return bills;
    }

}
