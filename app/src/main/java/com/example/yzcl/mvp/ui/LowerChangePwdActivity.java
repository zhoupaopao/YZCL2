package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2018/1/2.
 */

public class LowerChangePwdActivity extends BaseActivity {
    Button sure;
    TextView title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lowerchangepwd);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initListener();
    }

    private void initListener() {
        title.setText("重置密码");
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("name","朱金辉");
                LowerChangePwdActivity.this.setResult(1,intent);
                finish();
            }
        });
    }

    private void initView() {
        title=findViewById(R.id.title);
        sure=findViewById(R.id.sure);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }
}
