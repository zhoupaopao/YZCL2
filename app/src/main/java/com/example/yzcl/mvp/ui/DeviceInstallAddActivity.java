package com.example.yzcl.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2018/8/9.
 */

public class DeviceInstallAddActivity extends BaseActivity implements View.OnClickListener{
    private EditText other;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private RadioButton rb6;
    private RadioButton rb7;
    private TextView title;
    private ImageView back;
    private int nowrb=1;
    private String TAG="DeviceInstallAddActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_installadd);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        other=findViewById(R.id.other);
        rb1=findViewById(R.id.rb1);
        rb2=findViewById(R.id.rb2);
        rb3=findViewById(R.id.rb3);
        rb4=findViewById(R.id.rb4);
        rb5=findViewById(R.id.rb5);
        rb6=findViewById(R.id.rb6);
        rb7=findViewById(R.id.rb7);

    }

    private void initData() {

    }

    private void initListener() {
        title.setText("安装位置");
        back.setOnClickListener(this);
        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);
        rb3.setOnClickListener(this);
        rb4.setOnClickListener(this);
        rb5.setOnClickListener(this);
        rb6.setOnClickListener(this);
        rb7.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.rb1:
                uncheck(1);
                break;
            case R.id.rb2:
                uncheck(2);
                break;
            case R.id.rb3:
                uncheck(3);
                break;
            case R.id.rb4:
                uncheck(4);
                break;
            case R.id.rb5:
                uncheck(5);
                break;
            case R.id.rb6:
                uncheck(6);
                break;
            case R.id.rb7:
                uncheck(7);
                break;
        }
    }

    private void uncheck(int rb) {
        //取消当前选中
        if(rb==nowrb){
            //两个相同不做处理
        }else{
            switch (nowrb){
                case 1:
                    rb1.setChecked(false);
                    break;
                case 2:
                    rb2.setChecked(false);
                    break;
                case 3:
                    rb3.setChecked(false);
                    break;
                case 4:
                    rb4.setChecked(false);
                    break;
                case 5:
                    rb5.setChecked(false);
                    break;
                case 6:
                    rb6.setChecked(false);
                    break;
                case 7:
                    rb7.setChecked(false);
                    break;
            }
            nowrb=rb;
        }
    }
}
