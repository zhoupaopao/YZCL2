package com.example.yzcl.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/2/2.
 */

/**
 * 最新的车辆管理界面
 */
public class CarManagerRealActivity extends BaseActivity {
    private ImageView back;
    private TextView AddCar;
    private TextView title;
    private Spinner spinner;
    ArrayList<String>data_list;
    private RelativeLayout choose_customer;
    private RelativeLayout car_list;
    private RelativeLayout jq_list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager_real);
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
        AddCar=findViewById(R.id.textview2);
        spinner=findViewById(R.id.spinner);
        car_list=findViewById(R.id.car_list);
        choose_customer=findViewById(R.id.choose_customer);
        jq_list=findViewById(R.id.jq_list);
    }

    private void initData() {
        data_list=new ArrayList<>();
        data_list.add("全部");
        data_list.add("有线");
        data_list.add("无线");
    }

    private void initListener() {
        title.setText("车辆管理");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        AddCar.setText("新增车辆");
        AddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(CarManagerRealActivity.this,AddCarActivity.class);
                startActivity(intent);
            }
        });
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.drop_list,data_list);
        adapter.setDropDownViewResource(R.layout.drop_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("onItemSelected", data_list.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        choose_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(CarManagerRealActivity.this,CustomerChooseActivity.class);
                startActivity(intent);
            }
        });
        car_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(CarManagerRealActivity.this,MyCarListActivity.class);
                startActivity(intent);
            }
        });
        jq_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("jq_list", "onClick: ");
                Intent intent=new Intent();
                intent.setClass(CarManagerRealActivity.this,JieQingActivity.class);
                startActivity(intent);
            }
        });
    }
}
