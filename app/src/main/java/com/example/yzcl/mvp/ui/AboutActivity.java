package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

/**
 * Created by Lenovo on 2017/12/12.
 */

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity {
    ImageView back;
    TextView title;
    TextView bbh;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
//        ImmersionBar.with(this).init();
//        ImmersionBar.with(this)
//                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
//                .statusBarColor(R.color.title_color)
//                .init();
//        ImmersionBar.with(this)
//                .statusBarColor(R.color.title_color)
//                .init();
        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //获取版本号
    private String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

    private void initView() throws Exception {
        bbh=findViewById(R.id.bbh);
        back=findViewById(R.id.back);
//        title=findViewById(R.id.title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bbh.setText("当前版本号：V"+getVersionName().toString());
//        title.setText("关于我们");
        PgyUpdateManager.register(AboutActivity.this, "com.example.yzcl.zhou",
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {

                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        Log.i("resultresult",result);
                        new AlertDialog.Builder(AboutActivity.this)
                                .setTitle("更新")
                                .setMessage("系统检测到您的版本过低，请更新")
                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {

                                    @Override

                                    public void onClick(DialogInterface dialog, int which) {

                                    }

                                })
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                startDownloadTask(
                                                        AboutActivity.this,
                                                        appBean.getDownloadURL());

                                            }
                                        }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        Toast.makeText(AboutActivity.this, "已经是最新的版本", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
