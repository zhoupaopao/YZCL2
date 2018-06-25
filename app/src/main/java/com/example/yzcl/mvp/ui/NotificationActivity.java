package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.fanwe.lib.switchbutton.ISDSwitchButton;
import com.fanwe.lib.switchbutton.SDSwitchButton;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2017/12/12.
 */

public class NotificationActivity extends BaseActivity {
    SDSwitchButton sb1;
    SDSwitchButton sb2;
    SDSwitchButton sb3;
    SDSwitchButton sb4;
    ImageView back;
    TextView title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
    }

    private void initView() {
        sb1=findViewById(R.id.sb1);
        sb2=findViewById(R.id.sb2);
        sb3=findViewById(R.id.sb3);
        sb4=findViewById(R.id.sb4);
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        title.setText("消息通知");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        sb1.isChecked()//是否被选中

            /**
             * 设置选中状态
             *
             * @param checked        true-选中，false-未选中
             * @param anim           是否需要动画
             * @param notifyCallback 是否需要通知回调
             */
            sb1.setChecked(true,false,false);
        sb1.setOnCheckedChangedCallback(new ISDSwitchButton.OnCheckedChangedCallback() {
            @Override
            public void onCheckedChanged(boolean checked, SDSwitchButton view) {
                Log.i("checked", checked+"");
            }
        });
    }
}
