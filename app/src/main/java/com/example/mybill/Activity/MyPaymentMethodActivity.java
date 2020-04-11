package com.example.mybill.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mybill.Adapter.PaymentMethodAdapter;
import com.example.mybill.R;
import com.example.mybill.bean.Bill;
import com.example.mybill.bean.Category;
import com.example.mybill.bean.PaymentMethod;
import com.example.mybill.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class MyPaymentMethodActivity extends AppCompatActivity {
    ListView listView;

    List<PaymentMethod> listData;

    PaymentMethodAdapter paymentMethodAdapter;

    ImageButton btn_add;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_payment_method);
        ButterKnife.bind(this);

        //列表初始化
        initListView();

        //长按一行数据的事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MyPaymentMethodActivity.this)
                        .setTitle("删除")
                        .setMessage("是否删除该项？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PaymentMethod paymentMethod = new PaymentMethod();
                                paymentMethod.setObjectId(listData.get(position).getObjectId());
                                paymentMethod.setDeleted(true);
                                paymentMethod.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Toast.makeText(MyPaymentMethodActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                            initListView();
                                        }else{
                                            Toast.makeText(MyPaymentMethodActivity.this,"删除失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MyPaymentMethodActivity.this, BillMainActivity.class));
                            }
                        })
                        .show();
                return false;
            }
        });

        //新增交易方式按钮初始化
        btn_add = this.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText name = new EditText(MyPaymentMethodActivity.this);
                new AlertDialog.Builder(MyPaymentMethodActivity.this)
                        .setTitle("新增交易方式")
                        .setView(name)
                        .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PaymentMethod paymentMethod = new PaymentMethod();
                                User currentUser = new User();
                                currentUser.setObjectId(BmobUser.getCurrentUser().getObjectId());
                                paymentMethod.setUserId(currentUser);
                                paymentMethod.setName(name.getText().toString());
                                paymentMethod.setDeleted(false);

                                paymentMethod.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e==null){
                                            Toast.makeText(MyPaymentMethodActivity.this,"添加数据成功，返回objectId为:" + s,Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MyPaymentMethodActivity.this, MyPaymentMethodActivity.class));
                                        }else{
                                            Toast.makeText(MyPaymentMethodActivity.this,"创建数据失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    //列表初始化
    private void initListView() {
        listData = new ArrayList<>();
        listView = MyPaymentMethodActivity.this.findViewById(R.id.paymentMethodListView);
        BmobQuery<PaymentMethod> bmobQuery = new BmobQuery<PaymentMethod>();
        bmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        bmobQuery.addWhereEqualTo("isDeleted", false);
        bmobQuery.findObjects(new FindListener<PaymentMethod>() {
            @Override
            public void done(List<PaymentMethod> list, BmobException e) {
                if (e == null){
                    listData.addAll(list);

                    paymentMethodAdapter = new PaymentMethodAdapter(MyPaymentMethodActivity.this, R.layout.payment_method_item, listData);
                    listView.setAdapter(paymentMethodAdapter);
                }
                else {
                    Toast.makeText(MyPaymentMethodActivity.this,"查询交易方式出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
