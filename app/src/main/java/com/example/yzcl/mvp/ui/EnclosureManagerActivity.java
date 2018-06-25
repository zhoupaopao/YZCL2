package com.example.yzcl.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.Fragment.FragmentOne;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/1/29.
 */

/**
 * 上拉隐藏标题栏，但是不隐藏导航栏
 */
public class EnclosureManagerActivity extends FragmentActivity {
//    private TextView title;
//    private TextView add_enclosure;
//    private ImageView back;
//    private ListView list_enclosure;
//    List<String> arraydata=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encloure_manager);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, new FragmentOne());
        fragmentTransaction.commit();
//        ImmersionBar.with(this)
//                .statusBarColor(R.color.title_color)
//                .init();
        initView();
        initData();
        initListener();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean dispatchTouchEvent(MotionEvent ev);
    }
    private void initView() {
//        title=findViewById(R.id.title);
//        back=findViewById(R.id.back);
//        add_enclosure=findViewById(R.id.textview2);
//        list_enclosure=findViewById(R.id.list_encloure);
    }

    private void initData() {
//        title.setText("围栏管理");
//        add_enclosure.setText("添加围栏");
//        for(int i=0;i<50;i++){
//            arraydata.add("item"+i);
//        }
    }

    private void initListener() {
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        add_enclosure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent();
//                intent.setClass(EnclosureManagerActivity.this,EnclosureActivity.class);
//                startActivity(intent);
//            }
//        });
//        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arraydata);
//        list_enclosure.setAdapter(adapter);
    }
}
