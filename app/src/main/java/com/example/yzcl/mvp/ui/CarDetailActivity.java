package com.example.yzcl.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.example.yzcl.R;
import com.example.yzcl.adapter.BindDeviceAdapter;
import com.example.yzcl.adapter.EditDeviceAdapter;
import com.example.yzcl.adapter.ImagePickerAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.BindDeviceBean;
import com.example.yzcl.mvp.model.bean.CarMessageBean;
import com.example.yzcl.mvp.model.bean.CityBean;
import com.example.yzcl.mvp.model.bean.EditDeviceBean;
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
 * Created by 13126 on 2018/8/9.
 */

public class CarDetailActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener,View.OnClickListener{
    private TextView title;
    private ImageView back;
    private SharedPreferences sp;
    private String TAG="CarDetailActivity";
    private RelativeLayout rl_carxi;//品牌车系
    private int nowpage=1;//当前页数，控制下一页和返回的显示
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 5;               //允许选择图片最大数
    String car_brand_id;
    String car_brand;
    //page
    private LinearLayout page1;
    private LinearLayout page2;
    private LinearLayout page3;

    private RadioGroup car_status;
    private RadioButton status_all;
    private RadioButton status_yuqi;
    private RadioButton status_zdgz;

    private RelativeLayout rl_sex;//联系地址
    private TextView tv_sex;//联系地址
    private EditText name;//姓名
    private RelativeLayout rl_cardtype;//证件类型
    private TextView car_type;//证件类型
    private EditText card_num;//证件号码
    private EditText mobile;//手机号码
    private EditText home_address;//家庭地址
    private EditText car_vin;//车辆vin
    private EditText car_num;//车牌号
    private RelativeLayout rl_cartype;//选择车型
    private TextView tv_cartype;//车辆类型
    private EditText car_color;//车辆颜色
    private EditText mileage;//行驶里程
    private RelativeLayout rl_useyear;//使用年限
    private TextView use_age;//使用年限
    private EditText use_money;//借款金额
    private RelativeLayout belong;//所属客户
    private TextView belong_name;//所属客户名字
    private EditText con_person;//联系人
    private EditText con_phone;//联系电话
    private EditText installer_name;//安装工姓名
    private EditText installer_phone;//安装工手机号码
    private EditText installer_bz;//安装备注
    private TextView add_device;//添加设备
    private TextView tv_carxi;
    private EditText car_fdj;
    private EditText bz_msg;
    private EditText years;
    private EditText use_carmoney;//车辆价钱
    private RelativeLayout starttime;
    private RelativeLayout endtime;
    private TextView tv_starttime;
    private TextView tv_endtime;
    private TextView save;
    private RecyclerView rcvImg;
    private RecyclerView device_list;
    private EditText device_name;
    //具体的省市区
    private String sheng="";
    private String shi="";
    private String qu="";
    //临时的省市区
    private String tem_sheng="";
    private String tem_shi="";
    private String tem_qu="";
    private int level = 0;//当前省市区级别（0：省，1市，2区）
    private String ssq = "";//省市区
    private List<CityBean.NewCityBean> citysBean=new ArrayList<>();
    private List<CityBean.NewCityBean> citysBean1=new ArrayList<>();
    private List<CityBean.NewCityBean> citysBean2=new ArrayList<>();
    private ArrayList<String> Pinvince = new ArrayList<>();//省
    private ArrayList<String> city = new ArrayList<>();//市
    private ArrayList<String> area = new ArrayList<>();//区

    private BuildBean dialog;
    private String car_id="";
    private CarMessageBean carMessageBean;
    private EditDeviceAdapter editDeviceAdapter;
    ArrayList<EditDeviceBean.EditDeviceBeanMsg>list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        list=new ArrayList<>();
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        Intent intent=getIntent();
        car_id=intent.getStringExtra("carid");
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        page1=findViewById(R.id.page1);
        page2=findViewById(R.id.page2);
        page3=findViewById(R.id.page3);
        car_status=findViewById(R.id.car_status);
        status_all=findViewById(R.id.status_all);
        status_yuqi=findViewById(R.id.status_yuqi);
        status_zdgz=findViewById(R.id.status_zdgz);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        rl_cardtype=findViewById(R.id.rl_cardtype);
        car_type=findViewById(R.id.car_type);
        card_num=findViewById(R.id.card_num);
        rl_sex=findViewById(R.id.rl_sex);
        tv_sex=findViewById(R.id.tv_sex);
        home_address=findViewById(R.id.home_address);

//第二个页面
        car_vin=findViewById(R.id.car_vin);
        car_num=findViewById(R.id.car_num);
        rl_carxi=findViewById(R.id.rl_carxi);
        tv_carxi=findViewById(R.id.tv_carxi);
        car_fdj=findViewById(R.id.car_fdj);
        car_color=findViewById(R.id.car_color);
        years=findViewById(R.id.years);
        mileage=findViewById(R.id.mileage);
        use_carmoney=findViewById(R.id.use_carmoney);
        use_money=findViewById(R.id.use_money);
        starttime=findViewById(R.id.starttime);
        tv_starttime=findViewById(R.id.tv_starttime);
        endtime=findViewById(R.id.endtime);
        tv_endtime=findViewById(R.id.tv_endtime);
        bz_msg=findViewById(R.id.bz_msg);

        //第三页
        add_device=findViewById(R.id.add_device);
        device_name=findViewById(R.id.device_name);
        device_list=findViewById(R.id.device_list);
        rcvImg=findViewById(R.id.rcv_img);
        save=findViewById(R.id.save);

        initRecy();
    }

    private void initData() {
        //请求品牌
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id","");
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getAddress+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                CityBean cityBean=JSONObject.parseObject(jsonObject.toString(),CityBean.class);
                citysBean=cityBean.getList();
                for(int i=0;i<citysBean.size();i++){
                    Pinvince.add(citysBean.get(i).getName());
                }

                getCarMessageById();

            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(CarDetailActivity.this,"加载中...",true,true,false,true);
                dialog.show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }
    private void getCarMessageById(){
        //获取车辆情况
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id",car_id);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getCarMessageById+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                carMessageBean=JSONObject.parseObject(jsonObject.toString(),CarMessageBean.class);
                if(carMessageBean.isSuccess()){
                    CarMessageBean.CarMessage carMessage=carMessageBean.getObject();
                    name.setText(carMessage.getPledger().getName());
                    mobile.setText(carMessage.getPledger().getPhone());
                    car_type.setText("身份证");
                    card_num.setText(carMessage.getPledger().getIdcard());
                    if(carMessage.getPledger().getPledger_loc().get(0).getProvince().equals("")||carMessage.getPledger().getPledger_loc().get(0).getProvince()==null){
                        tv_sex.setText("请选择");
                    }else{
                        tv_sex.setText(carMessage.getPledger().getPledger_loc().get(0).getProvince()+carMessage.getPledger().getPledger_loc().get(0).getCity()+carMessage.getPledger().getPledger_loc().get(0).getDistrict());
                    }
                    home_address.setText(carMessage.getPledger().getPledger_loc().get(0).getAddress());

                    //第二页
                    car_vin.setText(carMessage.getVin());
                    car_num.setText(carMessage.getCar_no());
                    tv_carxi.setText(carMessage.getCar_brand());
                    car_fdj.setText(carMessage.getEngine());
                    car_color.setText(carMessage.getColor());
                    years.setText(carMessage.getUsed_age()+"");
                    mileage.setText(carMessage.getMileage()+"");
                    //这个先不写
                    use_carmoney.setText(carMessage.getCar_brand());
                    use_money.setText(carMessage.getCar_brand());

                    if(carMessage.getCar_type()==0){
                        tv_starttime.setText("请选择");
                    }else if(carMessage.getCar_type()==1){
                        tv_starttime.setText("小型车");
                    }else if(carMessage.getCar_type()==2){
                        tv_starttime.setText("紧凑车");
                    }else if(carMessage.getCar_type()==3){
                        tv_starttime.setText("中型车");
                    }else if(carMessage.getCar_type()==4){
                        tv_starttime.setText("中型SUV");
                    }else if(carMessage.getCar_type()==5){
                        tv_starttime.setText("中大型车");
                    }else if(carMessage.getCar_type()==6){
                        tv_starttime.setText("中大型SUV");
                    }else if(carMessage.getCar_type()==7){
                        tv_starttime.setText("皮卡");
                    }else{
                        tv_starttime.setText("请选择");
                    }
                    if (carMessage.getUse_prop()==0){
                        tv_endtime.setText("请选择");
                    }else if(carMessage.getUse_prop()==1){
                        tv_endtime.setText("自用");
                    }else if(carMessage.getUse_prop()==2){
                        tv_endtime.setText("营运");
                    }else if(carMessage.getUse_prop()==3){
                        tv_endtime.setText("非营运");
                    }else{
                        tv_endtime.setText("请选择");
                    }
                    bz_msg.setText(carMessage.getRemark());
                }else{
                    Toast.makeText(CarDetailActivity.this,carMessageBean.getMessage(),Toast.LENGTH_SHORT).show();
                }
                queCarGpsInfo();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(CarDetailActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                dialog.dialog.dismiss();
            }
        });
    }

    private void queCarGpsInfo() {
        ///获取车辆下的设备情况
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("car_id",car_id);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queCarGpsInfo+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                EditDeviceBean editDeviceBean=JSONObject.parseObject(jsonObject.toString(),EditDeviceBean.class);
                list=editDeviceBean.getList();
                editDeviceAdapter = new EditDeviceAdapter(CarDetailActivity.this, list);
                LinearLayoutManager layoutManager = new LinearLayoutManager(CarDetailActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                device_list.setLayoutManager(layoutManager);
                device_list.setItemAnimator(new DefaultItemAnimator());
                device_list.setAdapter(editDeviceAdapter);
                dialog.dialog.dismiss();

            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(CarDetailActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                dialog.dialog.dismiss();
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(this);
        rl_cardtype.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_carxi.setOnClickListener(this);
        starttime.setOnClickListener(this);
        endtime.setOnClickListener(this);
        add_device.setOnClickListener(this);
        title.setText("新增车辆");
        car_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.i(TAG, "onCheckedChanged: "+i);
                switch (i){
                    case R.id.status_all:
                        nowpage=1;
                        page1.setVisibility(View.VISIBLE);
                        page2.setVisibility(View.GONE);
                        page3.setVisibility(View.GONE);
                        break;
                    case R.id.status_yuqi:
                        nowpage=2;
                        page1.setVisibility(View.GONE);
                        page2.setVisibility(View.VISIBLE);
                        page3.setVisibility(View.GONE);
                        break;
                    case R.id.status_zdgz:
                        nowpage=3;
                        page1.setVisibility(View.GONE);
                        page2.setVisibility(View.GONE);
                        page3.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_cardtype:
                //证件类型
                final ArrayList<String> cardtypee = new ArrayList<>();
                cardtypee.add("身份证");
//                cardtypee.add("组织机构代码");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                OptionsPickerView pickerView = new OptionsPickerView.Builder(CarDetailActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //当点击的时候触发的事件
                        car_type.setText(cardtypee.get(options1));


                    }
                }).setTitleText("").setDividerColor(Color.BLUE)
                        .setTextColorCenter(Color.GRAY)
                        .setContentTextSize(18)
                        .setOutSideCancelable(false).build();
                pickerView.setPicker(cardtypee);
                pickerView.show();
                break;
            case R.id.rl_sex:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                level = 0;
                ssq = "";
                OptionsPickerView pvOptions = new OptionsPickerView.Builder(CarDetailActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //点击确定的时候，获取当前的信息
                        String pinid = citysBean.get(options1).getId();
                        ssq = ssq + Pinvince.get(options1);
                        level = level + 1;
                        tem_sheng=Pinvince.get(options1);
                        showopv(pinid);
                    }
                }).setTitleText("").setDividerColor(Color.BLUE)
                        .setTextColorCenter(Color.GRAY)
                        .setContentTextSize(18)
                        .setOutSideCancelable(false)
                        .build();
                pvOptions.setPicker(Pinvince);//三级选择器
                pvOptions.show();
                break;
            case R.id.rl_carxi:
                //选择品牌车系
                Intent intent = new Intent();
                intent.setClass(CarDetailActivity.this, BrandListActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.starttime:
                final ArrayList<String>cardtypeee = new ArrayList<>();
                cardtypeee.add("小型车");
                cardtypeee.add("紧凑车");
                cardtypeee.add("中型车");
                cardtypeee.add("中型SUV");
                cardtypeee.add("中大型车");
                cardtypeee.add("中大型SUV");
                cardtypeee.add("皮卡");
                cardtypeee.add("其他");
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                pickerView = new OptionsPickerView.Builder(CarDetailActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //当点击的时候触发的事件
                        tv_starttime.setText(cardtypeee.get(options1));


                    }
                }).setTitleText("").setDividerColor(Color.BLUE)
                        .setTextColorCenter(Color.GRAY)
                        .setContentTextSize(18)
                        .setOutSideCancelable(false).build();
                pickerView.setPicker(cardtypeee);
                pickerView.show();
                break;
            case R.id.endtime:
                final ArrayList<String>foruser = new ArrayList<>();
                foruser.add("自用");
                foruser.add("非营运");
                foruser.add("营运");
                foruser.add("其他");
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                pickerView = new OptionsPickerView.Builder(CarDetailActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //当点击的时候触发的事件
                        tv_endtime.setText(foruser.get(options1));


                    }
                }).setTitleText("").setDividerColor(Color.BLUE)
                        .setTextColorCenter(Color.GRAY)
                        .setContentTextSize(18)
                        .setOutSideCancelable(false).build();
                pickerView.setPicker(foruser);
                pickerView.show();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.add_device:
                //请求添加接口，并重新展现列表
                //添加设备
                String dev_name = device_name.getText().toString().trim();
                //获取数据
                if (dev_name.equals("")) {
                    Toast.makeText(CarDetailActivity.this, "请输入设备名", Toast.LENGTH_SHORT).show();
                } else {
                    queDeviceMsg(dev_name);
                }
        }
    }
    private void queDeviceMsg(final String dev_name) {
        RequestParams params = new RequestParams();
        params.addHeader("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupids", sp.getString(Constant.Group_id, ""));
        jsonObject.put("internalnum", dev_name);
        params.setRequestBody(MediaType.parse("application/json"), jsonObject.toString());
        HttpRequest.post(Api.getDeviceMess + "?token=" + sp.getString(Constant.Token, ""), params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                BindDeviceBean bindDeviceBean = JSONObject.parseObject(jsonObject.toString(), BindDeviceBean.class);
                if (!bindDeviceBean.isSuccess()) {
                    Toast.makeText(CarDetailActivity.this, bindDeviceBean.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dialog.dismiss();
                } else if (bindDeviceBean.getCount() == 0) {
                    Toast.makeText(CarDetailActivity.this, "设备名输入错误", Toast.LENGTH_SHORT).show();
                    dialog.dialog.dismiss();
                } else {
                    //可以添加的话请求接口
                    achieveDevList(bindDeviceBean.getList().get(0).getDeviceid());
                }

            }

            @Override
            public void onStart() {
                super.onStart();
                dialog = DialogUIUtils.showLoading(CarDetailActivity.this, "加载中...", true, true, false, true);
                dialog.show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }

    private void achieveDevList(String deviceid) {
        //获取最新的设备列表
        RequestParams params = new RequestParams();
        params.addHeader("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", car_id);
        jsonObject.put("deviceids", deviceid);
        params.setRequestBody(MediaType.parse("application/json"), jsonObject.toString());
        HttpRequest.post(Api.addDevice + "?token=" + sp.getString(Constant.Token, ""), params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                EditDeviceBean editDeviceBean=JSONObject.parseObject(jsonObject.toString(),EditDeviceBean.class);
                list.clear();
                list.addAll(editDeviceBean.getList());
                editDeviceAdapter.notifyDataSetChanged();
                dialog.dialog.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }
    public void getInstallPart(String deviceidd,String install_part){
        RequestParams params = new RequestParams();
        params.addHeader("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("carid", car_id);
        jsonObject.put("deviceid", deviceidd);
        jsonObject.put("install_part", install_part);
        params.setRequestBody(MediaType.parse("application/json"), jsonObject.toString());
        HttpRequest.post(Api.getInstallPart + "?token=" + sp.getString(Constant.Token, ""), params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                if(jsonObject.getBoolean("success")){
                    //添加成功
                    Toast.makeText(CarDetailActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CarDetailActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }

    private void showopv(String pinid) {
        city.clear();
        area.clear();
        citysBean1.clear();
        citysBean2.clear();
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id",pinid);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getAddress+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                CityBean cityBean=JSONObject.parseObject(jsonObject.toString(),CityBean.class);
                ArrayList<CityBean.NewCityBean>jsonArray=cityBean.getList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (level == 1) {
                        CityBean.NewCityBean newCityBean = jsonArray.get(i);
                        citysBean1.add(newCityBean);
                        city.add(newCityBean.getName());


                    } else if (level == 2) {
                        CityBean.NewCityBean newCityBean = jsonArray.get(i);
                        citysBean2.add(newCityBean);
                        area.add(newCityBean.getName());

                    }
                }
                if (level == 1) {
                    OptionsPickerView pickerView = new OptionsPickerView.Builder(CarDetailActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            //当点击的时候触发的事件
                            String pinid = citysBean1.get(options1).getId();
                            level = level + 1;
                            tem_shi=city.get(options1);
                            ssq = ssq + city.get(options1);
                            showopv(pinid);

                        }
                    }).setTitleText("").setDividerColor(Color.BLUE)
                            .setTextColorCenter(Color.GRAY)
                            .setContentTextSize(18)
                            .setOutSideCancelable(false).build();
                    pickerView.setPicker(city);
                    pickerView.show();
                } else if (level == 2) {
                    OptionsPickerView pickerView = new OptionsPickerView.Builder(CarDetailActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            //当点击的时候触发的事件
                            ssq = ssq + area.get(options1);
                            tem_qu=area.get(options1);
                            tv_sex.setText(ssq);
//                            citycode = citysBean2.get(options1).getCode();
                            //直接一次性赋值
                            sheng=tem_sheng;
                            shi=tem_shi;
                            qu=tem_qu;
                        }
                    }).setTitleText("").setDividerColor(Color.BLUE)
                            .setTextColorCenter(Color.GRAY)
                            .setContentTextSize(18)
                            .setOutSideCancelable(false).build();
                    pickerView.setPicker(area);
                    pickerView.show();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });


    }
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
        } else if (resultCode == 3) {
            //车型选择返回
            if (data != null && requestCode == 2) {
                car_brand=data.getStringExtra("brand_name");
                car_brand_id=data.getStringExtra("brand_id");
                tv_carxi.setText(car_brand);
            }
        }else if (resultCode == 10) {
            //新增成功返回
            finish();

        }
    }
    private void initRecy() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        rcvImg.setLayoutManager(new GridLayoutManager(this, 4));
        rcvImg.setHasFixedSize(true);
        rcvImg.setAdapter(adapter);
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
                DialogUIUtils.showSheet(CarDetailActivity.this, strings, "取消", Gravity.BOTTOM, true, true, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
//                        showToast(text + "---" + position);
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(CarDetailActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(CarDetailActivity.this, ImageGridActivity.class);
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
}