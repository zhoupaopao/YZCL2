package com.example.yzcl.mvp.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImagePreviewBaseActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.R;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧），ikkong （ikkong@163.com）
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：预览已经选择的图片，并可以删除, 感谢 ikkong 的提交
 * ================================================
 */
public class ImagePreviewDelActivity1 extends ImagePreviewBaseActivity1 implements View.OnClickListener {
    private boolean canedit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @SuppressLint("WrongViewCast") ImageView mBtnDel = (ImageView) findViewById(com.lzy.imagepicker.R.id.btn_del);
        mBtnDel.setOnClickListener(this);
        checkqx();
        if(canedit){
            mBtnDel.setVisibility(View.VISIBLE);
        }else{
            mBtnDel.setVisibility(View.GONE);
        }

        topBar.findViewById(com.lzy.imagepicker.R.id.btn_back).setOnClickListener(this);

        mTitleCount.setText(getString(com.lzy.imagepicker.R.string.preview_image_count, mCurrentPosition + 1, mImageItems.size()));
        //滑动ViewPager的时候，根据外界的数据改变当前的选中状态和当前的图片的位置描述文本
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                mTitleCount.setText(getString(com.lzy.imagepicker.R.string.preview_image_count, mCurrentPosition + 1, mImageItems.size()));
            }
        });
    }
    private void checkqx() {
        String list_Jurisdiction = getSharedPreferences("YZCL",MODE_PRIVATE).getString("list_Jurisdiction", "");
        String[] list_jur = list_Jurisdiction.split(",");
        for (int i = 0; i < list_jur.length; i++) {
            if (list_jur[i].equals("203")) {
                canedit = true;
            }
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == com.lzy.imagepicker.R.id.btn_del) {
            showDeleteDialog();
        } else if (id == com.lzy.imagepicker.R.id.btn_back) {
            onBackPressed();
        }
    }

    /** 是否删除此张图片 */
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("要删除这张照片吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //移除当前图片刷新界面
                mImageItems.remove(mCurrentPosition);
                if (mImageItems.size() > 0) {
                    mAdapter.setData(mImageItems);
                    mAdapter.notifyDataSetChanged();
                    mTitleCount.setText(getString(com.lzy.imagepicker.R.string.preview_image_count, mCurrentPosition + 1, mImageItems.size()));
                } else {
                    onBackPressed();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        //带回最新数据
        intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, mImageItems);
        setResult(ImagePicker.RESULT_CODE_BACK, intent);
        finish();
        super.onBackPressed();
    }

    /** 单击时，隐藏头和尾 */
    @Override
    public void onImageSingleTap() {
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, com.lzy.imagepicker.R.anim.top_out));
            topBar.setVisibility(View.GONE);
            tintManager.setStatusBarTintResource(com.lzy.imagepicker.R.color.transparent);//通知栏所需颜色
            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, com.lzy.imagepicker.R.anim.top_in));
            topBar.setVisibility(View.VISIBLE);
            tintManager.setStatusBarTintResource(com.lzy.imagepicker.R.color.status_bar);//通知栏所需颜色
            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}
