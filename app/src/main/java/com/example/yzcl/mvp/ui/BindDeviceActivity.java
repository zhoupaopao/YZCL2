package com.example.yzcl.mvp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshViewFooter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.example.yzcl.R;
import com.example.yzcl.adapter.BindDeviceAdapter;
import com.example.yzcl.adapter.CarListAdapter1;
import com.example.yzcl.adapter.EasyRecycleAdapter;
import com.example.yzcl.adapter.ImagePickerAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.BindDeviceBean;
import com.example.yzcl.mvp.model.bean.CarListBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

import static com.dou361.dialogui.DialogUIUtils.showToast;

/**
 * Created by Lenovo on 2018/8/9.
 */

public class BindDeviceActivity extends BaseActivity implements View.OnClickListener,ImagePickerAdapter.OnRecyclerViewItemClickListener{
    private TextView back;
    private TextView textview2;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 5;               //允许选择图片最大数
    private RecyclerView rcv_img;
    private RecyclerView device_list;
    private ArrayList<String>list=new ArrayList<>();
    private TextView add_device;
    private TextView device_name;
    private SharedPreferences sp;
    private String TAG="BindDeviceActivity";
    private BuildBean dialog;
    BindDeviceAdapter bindDeviceAdapter;
    private ArrayList<BindDeviceBean.BindDeviceBeanMsg>dev_list=new ArrayList<>();

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    String jsonObject;
    Intent intent;
    JSONObject jsonObject1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        intent=getIntent();
        back=findViewById(R.id.back);
        textview2=findViewById(R.id.textview2);
        rcv_img=findViewById(R.id.rcv_img);
        device_list=findViewById(R.id.device_list);
        device_name=findViewById(R.id.device_name);
        add_device=findViewById(R.id.add_device);
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        initRecy();
    }

    private void initData() {
//        for(int i=0;i<30;i++){
//            list.add(i+"aa");
//        }
        jsonObject=intent.getStringExtra("jsonObject");
        jsonObject1=JSONObject.parseObject(jsonObject);
    }

    private void initListener() {
        back.setText("取消");
        textview2.setText("完成");
        back.setOnClickListener(this);
        textview2.setOnClickListener(this);
        add_device.setOnClickListener(this);

        bindDeviceAdapter=new BindDeviceAdapter(this,dev_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BindDeviceActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        device_list.setLayoutManager(layoutManager);
        device_list.setItemAnimator( new DefaultItemAnimator());
        device_list.setAdapter(bindDeviceAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.textview2:
                for (int i=0;i<dev_list.size();i++){
                    //只有全部通过才能提交
                    if(dev_list.get(i).getLoc()!=null){
                        Log.i(TAG, dev_list.get(i).getLoc());
                    }else{
                        Toast.makeText(BindDeviceActivity.this,"安装位置不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(i==dev_list.size()-1){
                        Toast.makeText(BindDeviceActivity.this,"可以提交",Toast.LENGTH_SHORT).show();
                        Log.i(TAG, jsonObject1.getJSONObject("pledger").getString("card_type"));
//
//                        //可以请求接口
//                        RequestParams params=new RequestParams();
//                        params.addHeader("Content-Type","application/json");
//                        JSONObject jsonObject=new JSONObject();
//                        JSONObject carjsonObject=new JSONObject();
//                        JSONObject pledgerjsonObject=new JSONObject();
//                        carjsonObject.put("vin",car_vin.getText().toString().trim());
//                        carjsonObject.put("is_new_car",1);
//                        carjsonObject.put("mileage",mileage.getText().toString().trim());
//                        carjsonObject.put("car_brand",tv_carxi.getText().toString().trim());
//                        carjsonObject.put("car_no",car_num.getText().toString().trim());
//                        carjsonObject.put("car_value",use_carmoney.getText().toString().trim());
//                        carjsonObject.put("color",car_color.getText().toString().trim());
//                        //发动机号tv_starttime
//
//                        carjsonObject.put("engine",car_fdj.getText().toString().trim());
//                        carjsonObject.put("remark",bz_msg.getText().toString().trim());
//                        //车辆用途
//                        switch (tv_starttime.getText().toString()){
//                            case "小型车":
//                                carjsonObject.put("car_type","1");
//                                break;
//                            case "紧凑车":
//                                carjsonObject.put("car_type","2");
//                                break;
//                            case "中型车":
//                                carjsonObject.put("car_type","3");
//                                break;
//                            case "中型SUV":
//                                carjsonObject.put("car_type","4");
//                                break;
//                            case "中大型车":
//                                carjsonObject.put("car_type","5");
//                                break;
//                            case "中大型SUV":
//                                carjsonObject.put("car_type","6");
//                                break;
//                            case "皮卡":
//                                carjsonObject.put("car_type","7");
//                                break;
//                            case "其他":
//                                carjsonObject.put("car_type","0");
//                                break;
//                        }
//                        switch (tv_endtime.getText().toString()){
//                            case "自用":
//                                carjsonObject.put("use_prop","1");
//                                break;
//                            case "非营运":
//                                carjsonObject.put("use_prop","3");
//                                break;
//                            case "营运":
//                                carjsonObject.put("use_prop","2");
//                                break;
//                            case "其他":
//                                carjsonObject.put("use_prop","0");
//                                break;
//                        }
//                        //使用年限
//                        carjsonObject.put("used_age",years.getText().toString().trim());
//                        jsonObject.put("car",carjsonObject);
//                        jsonObject.put("group_id",sp.getString(Constant.Group_id,""));
//                        pledgerjsonObject.put("card_type",1);
//                        pledgerjsonObject.put("name",name.getText().toString().trim());
//                        pledgerjsonObject.put("phone",mobile.getText().toString().trim());
////                    JSONObject pledgerjsonObject1=new JSONObject();
//                        JSONArray pledgerListjsonObject=new JSONArray();
//                        JSONObject locjsonObject=new JSONObject();
//                        locjsonObject.put("province",sheng);
//                        locjsonObject.put("city",shi);
//                        locjsonObject.put("district",qu);
//                        locjsonObject.put("address",home_address.getText().toString().trim());
//                        locjsonObject.put("type",1);
//                        locjsonObject.put("lat",null);
//                        locjsonObject.put("lng",null);
//                        pledgerListjsonObject.add(locjsonObject);
//                        pledgerjsonObject.put("pledger_loc",pledgerListjsonObject);
//                        pledgerjsonObject.put("card_type","1");//写死是身份证
//                        pledgerjsonObject.put("idcard",card_num.getText().toString().trim());
//                        pledgerjsonObject.put("name",name.getText().toString().trim());
//                        pledgerjsonObject.put("phone",mobile.getText().toString().trim());
//
//                        jsonObject.put("pledger",pledgerjsonObject);
//
//                        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
//                        HttpRequest.post(Api.add+"?token="+sp.getString(Constant.Token,""), params, new JsonHttpRequestCallback() {
//                            @Override
//                            protected void onSuccess(Headers headers, JSONObject jsonObject) {
//                                super.onSuccess(headers, jsonObject);
//                                Log.i("jsonObject", jsonObject.toString());
//                                if(jsonObject.getBoolean("success")){
//                                    ///成功
//                                    Toast.makeText(AddCarActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }else{
//                                    Toast.makeText(AddCarActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
//                                }
//                                dialog.dialog.dismiss();
//                            }
//
//                            @Override
//                            public void onFailure(int errorCode, String msg) {
//                                super.onFailure(errorCode, msg);
//                            }
//
//                            @Override
//                            public void onStart() {
//                                super.onStart();
//                                dialog= DialogUIUtils.showLoading(AddCarActivity.this,"加载中...",true,true,false,true);
//                                dialog.show();
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                super.onFinish();
//                            }
//                        });
                    }
                }

                break;
            case R.id.add_device:
                //添加设备
                String dev_name=device_name.getText().toString().trim();
                //获取数据
                if(dev_name.equals("")){
                    Toast.makeText(BindDeviceActivity.this,"请输入设备名",Toast.LENGTH_SHORT).show();
                }else{
                    queDeviceMsg(dev_name);
                }

                break;
        }
    }

    private void queDeviceMsg(final String dev_name) {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("groupids",sp.getString(Constant.Group_id,""));
        jsonObject.put("internalnum",dev_name);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getDeviceMess+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                BindDeviceBean bindDeviceBean=JSONObject.parseObject(jsonObject.toString(),BindDeviceBean.class);
                if(!bindDeviceBean.isSuccess()){
                    Toast.makeText(BindDeviceActivity.this,bindDeviceBean.getMessage(),Toast.LENGTH_SHORT).show();
                }else if(bindDeviceBean.getCount()==0){
                    Toast.makeText(BindDeviceActivity.this,"设备名输入错误",Toast.LENGTH_SHORT).show();
                }else{
                    //判断是否已经添加了
                    boolean hasadd=false;
                    for(int i=0;i<dev_list.size();i++){
                        if(dev_list.get(i).getInternalnum().equals(bindDeviceBean.getList().get(0).getInternalnum())){
                            Toast.makeText(BindDeviceActivity.this,"请勿重复添加",Toast.LENGTH_SHORT).show();
                            hasadd=true;
                            break;
                        }
                    }
                    if(!hasadd){
                        dev_list.add(bindDeviceBean.getList().get(0));
                        bindDeviceAdapter.notifyItemInserted(dev_list.size());
                    }


                }
                dialog.dialog.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(BindDeviceActivity.this,"加载中...",true,true,false,true);
                dialog.show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }

    private void initRecy() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        rcv_img.setLayoutManager(new GridLayoutManager(this, 4));
        rcv_img.setHasFixedSize(true);
        rcv_img.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("onItemClick: ",position+"21" );
        //要进行application注册
        switch (position) {

            case IMAGE_ITEM_ADD:
                List<TieBean> strings = new ArrayList<TieBean>();
                strings.add(new TieBean("拍照"));
                strings.add(new TieBean("相册"));
                DialogUIUtils.showSheet(BindDeviceActivity.this, strings, "取消", Gravity.BOTTOM, true, true, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
//                        showToast(text + "---" + position);
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(BindDeviceActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(BindDeviceActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onBottomBtnClick() {
                        showToast("取消");
                    }
                }).show();
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS,true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }
    //------------------图片相关-----------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {//从相册选择完图片
//            //压缩图片
//            new Thread(new MyRunnable(AddCarActivity.this, data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT),
//                    originImages, dragImages, myHandler, true)).start();
//        }
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null){
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null){
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }
}
