package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2017/12/12.
 */
/*
客服热线
 */

public class CustomerServiceActivity extends BaseActivity {
    RelativeLayout callmobile;
    ImageView back;
    TextView title;
    TextView number;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerservice);
//        ImmersionBar.with(this)
//                .statusBarColor(R.color.title_color)
//                .init();
        initView();
    }

    private void initView() {
//        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        number=findViewById(R.id.number);
        callmobile=findViewById(R.id.callmobile);
//        title.setText("客服热线");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        callmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number2=number.getText().toString();
                Uri callUri = Uri.parse("tel:" + number2);
                Intent intent = new Intent(Intent.ACTION_DIAL, callUri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
