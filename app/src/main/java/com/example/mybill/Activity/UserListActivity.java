package com.example.mybill.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mybill.Adapter.CategoryAdapter;
import com.example.mybill.Adapter.UserAdapter;
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

public class UserListActivity extends AppCompatActivity {
    ListView userListView;
    List<User> listData;
    UserAdapter adapter;

    Intent intent;
    String searchString;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        intent = getIntent();
        //列表初始化
        initListView();
    }

    //列表初始化
    private void initListView() {
        userListView = this.findViewById(R.id.userListView);
        listData = new ArrayList<>();
        searchString = intent.getStringExtra("searchString");
        getUserData();
    }

    /**
     * 查询符合条件的用户
     */
    private void getUserData() {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereNotEqualTo("objectId", BmobUser.getCurrentUser().getObjectId());
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null){
                    for (User user: list){
                        if (user.getUsername().contains(searchString))
                            listData.add(user);
                    }

                    adapter = new UserAdapter(UserListActivity.this, R.layout.user_item, listData);
                    userListView.setAdapter(adapter);
                }
                else
                    Toast.makeText(UserListActivity.this,"查询用户出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
