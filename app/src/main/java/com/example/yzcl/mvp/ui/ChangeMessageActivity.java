package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2017/12/13.
 */

/**
 * 修改个人信息
 */

public class ChangeMessageActivity extends BaseActivity {
    TextView cancel;//取消
    TextView sure;//确定
    TextView title;//标题
    EditText String_text;//文本填写
    ImageView delete;//文本删除按钮
    TextView prompt;//提示

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_message);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
    }

    private void initView() {
        Intent intent=getIntent();

        cancel=findViewById(R.id.textview1);
        sure=findViewById(R.id.textview2);
        title=findViewById(R.id.title);
        delete=findViewById(R.id.delete);
        String_text=findViewById(R.id.string_text);
        prompt=findViewById(R.id.prompt);
        title.setText(intent.getStringExtra("title"));
        String_text.setText(intent.getStringExtra("StringText"));
        if(intent.getStringExtra("prompt")==""){
            prompt.setVisibility(View.INVISIBLE);
        }else{
            prompt.setVisibility(View.VISIBLE);
            prompt.setText(intent.getStringExtra("prompt"));
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String_text.setText("");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //需要提交数据
                Toast.makeText(ChangeMessageActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
