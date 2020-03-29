package com.example.mybill.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mybill.Adapter.CategoryAdapter;
import com.example.mybill.R;
import com.example.mybill.bean.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class MyCategoryActivity extends AppCompatActivity {
    ListView listViewIn;
    ListView listViewOut;
    
    List<Category> listInData;
    List<Category> listOutData;

    CategoryAdapter adapter_in;
    CategoryAdapter adapter_out;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_category);
        ButterKnife.bind(this);
        
        //列表初始化
        initListView();

        //收入列表长按一行数据的事件
        listViewIn.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getParent())
                        .setTitle("删除")
                        .setMessage("是否删除该项？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Category category = new Category();
                                category.setObjectId(listInData.get(position).getObjectId());
                                category.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Toast.makeText(getParent(),"删除成功",Toast.LENGTH_SHORT).show();
                                            initListView();
                                        }else{
                                            Toast.makeText(getParent(),"删除失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getParent(), BillMainActivity.class));
                            }
                        })
                        .show();
                return false;
            }
        });

        //支出列表长按一行数据的事件
        listViewOut.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getParent())
                        .setTitle("删除")
                        .setMessage("是否删除该项？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Category category = new Category();
                                category.setObjectId(listOutData.get(position).getObjectId());
                                category.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Toast.makeText(getParent(),"删除成功",Toast.LENGTH_SHORT).show();
                                            initListView();
                                        }else{
                                            Toast.makeText(getParent(),"删除失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getParent(), BillMainActivity.class));
                            }
                        })
                        .show();
                return false;
            }
        });

    }

    //列表初始化
    private void initListView() {
        listViewIn = this.findViewById(R.id.categoryInListView);
        listInData = new ArrayList<>();
        getCategoryData("in");

        listViewOut = this.findViewById(R.id.categoryOutListView);
        listOutData = new ArrayList<>();
        getCategoryData("out");
    }

    //根据条件查询相应的类别
    private void getCategoryData(final String inOrOut){
        final BmobQuery<Category> bmobQuery = new BmobQuery<Category>();
        bmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        if (inOrOut.equals("out"))
            bmobQuery.addWhereEqualTo("type", "out");
        else if (inOrOut.equals("in"))
            bmobQuery.addWhereEqualTo("type", "in");
        bmobQuery.findObjects(new FindListener<Category>() {
            @Override
            public void done(List<Category> list, BmobException e) {
                if (e == null){
                    if (inOrOut.equals("out")){
                        listOutData.addAll(list);
                        adapter_out = new CategoryAdapter(MyCategoryActivity.this, R.layout.category_item, listOutData);
                        listViewOut.setAdapter(adapter_out);
                    }
                    else if (inOrOut.equals("in")){
                        listInData.addAll(list);
                        adapter_in = new CategoryAdapter(MyCategoryActivity.this, R.layout.category_item, listInData);
                        listViewIn.setAdapter(adapter_in);
                    }
                }
                else {
                    Toast.makeText(getParent(),"查询我的类别出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
