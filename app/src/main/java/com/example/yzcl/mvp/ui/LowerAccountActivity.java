package com.example.yzcl.mvp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzcl.R;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2018/1/2.
 */

public class LowerAccountActivity extends Activity {
    RelativeLayout changepwd;
    TextView name;
    TextView edit;
    TextView email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loweraccount);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initListener();
    }



    @SuppressLint("WrongViewCast")
    private void initView() {
        changepwd=findViewById(R.id.changepwd);
        name=findViewById(R.id.name);
        edit=findViewById(R.id.textview2);
        email=findViewById(R.id.email);
        Intent getintent=getIntent();
//        String email=getintent.getStringExtra("email");
//        if(email!=null){
//            Toast.makeText(LowerAccountActivity.this,email,Toast.LENGTH_SHORT).show();
//        }
    }
    private void initListener() {
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(LowerAccountActivity.this,LowerChangePwdActivity.class);
                startActivityForResult(intent,1);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(LowerAccountActivity.this,LowerEditAccountActivity.class);
                startActivityForResult(intent,2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data==null){

            return;

        }
        switch (requestCode) {
            case 1:
                String newname=data.getStringExtra("name");
                name.setText(newname);
                break;
            case 2:
                //执行编辑操作后赋值
                String email=data.getStringExtra("email");
                Toast.makeText(LowerAccountActivity.this,email,Toast.LENGTH_SHORT).show();
                break;
        }



    }
}
