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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mybill.Adapter.CategoryAdapter;
import com.example.mybill.R;
import com.example.mybill.bean.Category;
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


public class MyCategoryActivity extends AppCompatActivity {
    ListView listViewIn;
    ListView listViewOut;
    
    List<Category> listInData;
    List<Category> listOutData;

    CategoryAdapter adapter_in;
    CategoryAdapter adapter_out;

    ImageButton btn_add_out;
    ImageButton btn_add_in;

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
                new AlertDialog.Builder(MyCategoryActivity.this)
                        .setTitle("删除")
                        .setMessage("是否删除该项？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Category category = new Category();
                                category.setObjectId(listInData.get(position).getObjectId());
                                category.setDeleted(true);
                                category.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Toast.makeText(MyCategoryActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                            initListView();
                                        }else{
                                            Toast.makeText(MyCategoryActivity.this,"删除失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MyCategoryActivity.this, BillMainActivity.class));
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
                new AlertDialog.Builder(MyCategoryActivity.this)
                        .setTitle("删除")
                        .setMessage("是否删除该项？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Category category = new Category();
                                category.setObjectId(listOutData.get(position).getObjectId());
                                category.setDeleted(true);
                                category.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Toast.makeText(MyCategoryActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                            initListView();
                                        }else{
                                            Toast.makeText(MyCategoryActivity.this,"删除失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MyCategoryActivity.this, BillMainActivity.class));
                            }
                        })
                        .show();
                return false;
            }
        });

        //新增支出类别按钮初始化
        btn_add_out = this.findViewById(R.id.btn_add_out);
        btn_add_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText name = new EditText(MyCategoryActivity.this);
                new AlertDialog.Builder(MyCategoryActivity.this)
                        .setTitle("新增支出类别")
                        .setView(name)
                        .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Category category = new Category();
                                User currentUser = new User();
                                currentUser.setObjectId(BmobUser.getCurrentUser().getObjectId());
                                category.setUserId(currentUser);
                                category.setType("out");
                                category.setDeleted(false);
                                category.setName(name.getText().toString());

                                category.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e==null){
                                            Toast.makeText(MyCategoryActivity.this,"添加数据成功，返回objectId为:" + s,Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MyCategoryActivity.this, MyCategoryActivity.class));
                                        }else{
                                            Toast.makeText(MyCategoryActivity.this,"创建数据失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
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

        //新增收入类别按钮初始化
        btn_add_in = this.findViewById(R.id.btn_add_in);
        btn_add_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText name = new EditText(MyCategoryActivity.this);
                new AlertDialog.Builder(MyCategoryActivity.this)
                        .setTitle("新增收入类别")
                        .setView(name)
                        .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Category category = new Category();
                                User currentUser = new User();
                                currentUser.setObjectId(BmobUser.getCurrentUser().getObjectId());
                                category.setUserId(currentUser);
                                category.setType("in");
                                category.setName(name.getText().toString());
                                category.setDeleted(false);

                                category.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e==null){
                                            Toast.makeText(MyCategoryActivity.this,"添加数据成功，返回objectId为:" + s,Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MyCategoryActivity.this, MyCategoryActivity.class));
                                        }else{
                                            Toast.makeText(MyCategoryActivity.this,"创建数据失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
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
        bmobQuery.addWhereEqualTo("isDeleted", false);
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
