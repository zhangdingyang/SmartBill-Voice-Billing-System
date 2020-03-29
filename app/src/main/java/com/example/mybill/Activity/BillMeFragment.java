package com.example.mybill.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mybill.R;

import cn.bmob.v3.BmobUser;


public class BillMeFragment extends Fragment {
    private TextView textView;
    private Button button_exit;
    private Button button_myCategory;
    private Button button_myPaymentMethod;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bill_me_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textView=(TextView)getActivity().findViewById(R.id.textView);
        textView.setText(textView.getText() + BmobUser.getCurrentUser().getUsername());

        button_exit =(Button)getActivity().findViewById(R.id.btn_exit);
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        button_myCategory = getActivity().findViewById(R.id.btn_myCategory);
        button_myCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyCategoryActivity.class));
            }
        });

        button_myPaymentMethod = getActivity().findViewById(R.id.btn_myPaymentMethod);
        button_myPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyPaymentMethodActivity.class));
            }
        });
    }

}
