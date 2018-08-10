package com.example.yzcl.mvp.ui;

/**
 * Created by Lenovo on 2017/12/20.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshViewFooter;
import com.bigkoo.pickerview.OptionsPickerView;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.example.tree.Node3;
import com.example.yzcl.Listener.OnRecyclerItemClickListener;
import com.example.yzcl.R;
import com.example.yzcl.adapter.CarListAdapter1;
import com.example.yzcl.adapter.ImagePickerAdapter;
import com.example.yzcl.adapter.PostArticleImgAdapter;
import com.example.yzcl.adapter.SimpleTreeeAdapter;
import com.example.yzcl.adapter.SortAdapter;
import com.example.yzcl.adapter.TreeListViewwAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarBrandBean;
import com.example.yzcl.mvp.model.bean.CarListBean;
import com.example.yzcl.mvp.model.bean.CityBean;
import com.example.yzcl.mvp.model.bean.CustomerOrganizationBeans;
import com.example.yzcl.mvp.model.bean.NewFileBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.utils.MultiImageSelector;
import com.example.yzcl.utils.MyCallBack;
import com.example.yzcl.utils.PickImageHelper;
import com.example.yzcl.utils.TimePickerDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

import static com.dou361.dialogui.DialogUIUtils.showToast;


/**
 * 新增车辆
 */
public class AddCarActivity extends BaseActivity implements com.example.yzcl.utils.TimePickerDialog.TimePickerDialogInterface,ImagePickerAdapter.OnRecyclerViewItemClickListener,View.OnClickListener{
    public static final int IMAGE_SIZE = 9;
    RecyclerView rcvImg;
    private TextView title;
    private ImageView back;
    private ItemTouchHelper itemTouchHelper;
    private Context mContext;
    private static final int REQUEST_IMAGE = 1002;
    private List<String> originImages;//原始图片
    private List<String> dragImages;//压缩长宽后图片
    private PostArticleImgAdapter postArticleImgAdapter;
    private RelativeLayout starttime;
    private RelativeLayout endtime;
    private TextView tv_starttime;
    private TextView tv_endtime;
    final int SHOW_STARTTIME=1;
    final int SHOW_ENDTIME=2;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    AlertDialog.Builder adb;
    DatePicker dp;
    TimePicker tp;
    TextView et_starttime;
    TextView et_endtime;
    View linestart;
    View lineend;
    int isstart=1;
    //判断当前是开始还是结束，1开始2结束
    int timenew=1;
    int mYear,mMonth,mDay,mHour,mMinutes;
    private TimePickerDialog mTimePickerDialog;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数

    //详细布局
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
    private LinearLayout add_device;//添加设备
    private TextView tv_carxi;
    private EditText car_fdj;
    private EditText bz_msg;
    private EditText years;

    private LinearLayout person_msg;//个人信息页面
    private LinearLayout car_filling_msg;//点击填补后的消息
    private LinearLayout car_msg;//车辆初始信息
    private TextView tv_Filling;//点击的填补文字
    private TextView next;//下一步和绑定设备
    private TextView submit;//提交
    private RelativeLayout rl_carxi;//品牌车系
    private int nowpage=1;//当前页数，控制下一页和返回的显示

    private ArrayList<String> Pinvince = new ArrayList<>();//省
    private ArrayList<String> city = new ArrayList<>();//市
    private ArrayList<String> area = new ArrayList<>();//区
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
    private String citycode = "";
    private SharedPreferences sp;
    private String TAG="AddCarActivity";
    private BuildBean dialog;
    InputMethodManager imm;
    private EditText use_carmoney;//车辆价钱

    String car_brand_id;
    String car_brand;

    String group_id="";
    private List<CityBean.NewCityBean> citysBean=new ArrayList<>();
    private List<CityBean.NewCityBean> citysBean1=new ArrayList<>();
    private List<CityBean.NewCityBean> citysBean2=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //由于键盘显示会把提交按钮顶上去
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_addcar);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();

        initView();
        initData();
        initListener();
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
                DialogUIUtils.showSheet(AddCarActivity.this, strings, "取消", Gravity.BOTTOM, true, true, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
//                        showToast(text + "---" + position);
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(AddCarActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(AddCarActivity.this, ImageGridActivity.class);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                if(nowpage==1){
                    finish();
                }else{
                    //返回第一页
                    person_msg.setVisibility(View.VISIBLE);
                    car_msg.setVisibility(View.GONE);
                    tv_Filling.setVisibility(View.VISIBLE);
                    car_filling_msg.setVisibility(View.GONE);
                    next.setText("下一页");
                    submit.setVisibility(View.GONE);
                    nowpage=1;
                }
                break;
            case R.id.rl_sex:
                 imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                level = 0;
                ssq = "";
                OptionsPickerView pvOptions = new OptionsPickerView.Builder(AddCarActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
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
            case R.id.rl_cardtype:
                //可以换成底部的
                //CHU
                ///kk
                final ArrayList<String>cardtypee = new ArrayList<>();
                cardtypee.add("身份证");
//                cardtypee.add("组织机构代码");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                OptionsPickerView pickerView = new OptionsPickerView.Builder(AddCarActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
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
            case R.id.rl_cartype:
                break;
            case R.id.next:
                //下一步
                if(nowpage==1){
                    if(name.getText().toString().trim().equals("")){
                        //是空的话
                        Toast.makeText(AddCarActivity.this,"姓名不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                        person_msg.setVisibility(View.GONE);
                        car_msg.setVisibility(View.VISIBLE);
                        next.setText("绑定设备");
                        submit.setVisibility(View.VISIBLE);
                        nowpage=2;
                    }

                }else{
                    //第二页了
                    //需要去绑定设备
                    if(car_vin.getText().toString().trim().length()!=17){
                        Toast.makeText(AddCarActivity.this,"请填写17位车架号",Toast.LENGTH_SHORT).show();
                    }else if(mobile.getText().toString().trim().length()!=0&&mobile.getText().toString().trim().length()!=11){
                        Toast.makeText(AddCarActivity.this,"请填写11位手机号",Toast.LENGTH_SHORT).show();
                    }else if(card_num.getText().toString().trim().length()!=0&&card_num.getText().toString().trim().length()!=18){
                        Toast.makeText(AddCarActivity.this,"请填写18位身份证号",Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent=new Intent();
                        intent.setClass(AddCarActivity.this,BindDeviceActivity.class);
                        JSONObject jsonObject=new JSONObject();
                        JSONObject carjsonObject=new JSONObject();
                        JSONObject pledgerjsonObject=new JSONObject();
                        carjsonObject.put("vin",car_vin.getText().toString().trim());
                        carjsonObject.put("is_new_car",1);
                        carjsonObject.put("mileage",mileage.getText().toString().trim());
                        carjsonObject.put("car_brand",tv_carxi.getText().toString().trim());
                        carjsonObject.put("car_no",car_num.getText().toString().trim());
                        carjsonObject.put("car_value",use_carmoney.getText().toString().trim());
                        carjsonObject.put("color",car_color.getText().toString().trim());
                        //发动机号tv_starttime

                        carjsonObject.put("engine",car_fdj.getText().toString().trim());
                        carjsonObject.put("remark",bz_msg.getText().toString().trim());
                        //车辆用途
                        switch (tv_starttime.getText().toString()){
                            case "小型车":
                                carjsonObject.put("car_type","1");
                                break;
                            case "紧凑车":
                                carjsonObject.put("car_type","2");
                                break;
                            case "中型车":
                                carjsonObject.put("car_type","3");
                                break;
                            case "中型SUV":
                                carjsonObject.put("car_type","4");
                                break;
                            case "中大型车":
                                carjsonObject.put("car_type","5");
                                break;
                            case "中大型SUV":
                                carjsonObject.put("car_type","6");
                                break;
                            case "皮卡":
                                carjsonObject.put("car_type","7");
                                break;
                            case "其他":
                                carjsonObject.put("car_type","0");
                                break;
                        }
                        switch (tv_endtime.getText().toString()){
                            case "自用":
                                carjsonObject.put("use_prop","1");
                                break;
                            case "非营运":
                                carjsonObject.put("use_prop","3");
                                break;
                            case "营运":
                                carjsonObject.put("use_prop","2");
                                break;
                            case "其他":
                                carjsonObject.put("use_prop","0");
                                break;
                        }
                        //使用年限
                        carjsonObject.put("used_age",years.getText().toString().trim());
                        jsonObject.put("car",carjsonObject);
                        jsonObject.put("group_id",sp.getString(Constant.Group_id,""));
                        pledgerjsonObject.put("card_type",1);
                        pledgerjsonObject.put("name",name.getText().toString().trim());
                        pledgerjsonObject.put("phone",mobile.getText().toString().trim());
//                    JSONObject pledgerjsonObject1=new JSONObject();
                        JSONArray pledgerListjsonObject=new JSONArray();
                        JSONObject locjsonObject=new JSONObject();
                        locjsonObject.put("province",sheng);
                        locjsonObject.put("city",shi);
                        locjsonObject.put("district",qu);
                        locjsonObject.put("address",home_address.getText().toString().trim());
                        locjsonObject.put("type",1);
                        locjsonObject.put("lat",null);
                        locjsonObject.put("lng",null);
                        pledgerListjsonObject.add(locjsonObject);
                        pledgerjsonObject.put("pledger_loc",pledgerListjsonObject);
                        pledgerjsonObject.put("card_type","1");//写死是身份证
                        pledgerjsonObject.put("idcard",card_num.getText().toString().trim());
                        pledgerjsonObject.put("name",name.getText().toString().trim());
                        pledgerjsonObject.put("phone",mobile.getText().toString().trim());

                        jsonObject.put("pledger",pledgerjsonObject);

                        intent.putExtra("jsonObject",jsonObject.toString());
                        startActivityForResult(intent,10);
                    }
                }

                break;
            case R.id.tv_Filling:
                //张开填补的信息
                tv_Filling.setVisibility(View.GONE);
                car_filling_msg.setVisibility(View.VISIBLE);


                break;
            case R.id.rl_carxi:
                //选择品牌车系
                Intent intent = new Intent();
                intent.setClass(AddCarActivity.this, BrandListActivity.class);
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
                 pickerView = new OptionsPickerView.Builder(AddCarActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
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
                pickerView = new OptionsPickerView.Builder(AddCarActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
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
            case R.id.submit:
                //提交，不加设备的那种
                if(car_vin.getText().toString().trim().length()!=17){
                    Toast.makeText(AddCarActivity.this,"请填写17位车架号",Toast.LENGTH_SHORT).show();
                }else if(mobile.getText().toString().trim().length()!=0&&mobile.getText().toString().trim().length()!=11){
                    Toast.makeText(AddCarActivity.this,"请填写11位手机号",Toast.LENGTH_SHORT).show();
                }else if(card_num.getText().toString().trim().length()!=0&&card_num.getText().toString().trim().length()!=18){
                    Toast.makeText(AddCarActivity.this,"请填写18位身份证号",Toast.LENGTH_SHORT).show();
                }else{
                    //可以请求接口
                    RequestParams params=new RequestParams();
                    params.addHeader("Content-Type","application/json");
                    JSONObject jsonObject=new JSONObject();
                    JSONObject carjsonObject=new JSONObject();
                    JSONObject pledgerjsonObject=new JSONObject();
                    carjsonObject.put("vin",car_vin.getText().toString().trim());
                    carjsonObject.put("is_new_car",1);
                    carjsonObject.put("mileage",mileage.getText().toString().trim());
                    carjsonObject.put("car_brand",tv_carxi.getText().toString().trim());
                    carjsonObject.put("car_no",car_num.getText().toString().trim());
                    carjsonObject.put("car_value",use_carmoney.getText().toString().trim());
                    carjsonObject.put("color",car_color.getText().toString().trim());
                    //发动机号tv_starttime

                    carjsonObject.put("engine",car_fdj.getText().toString().trim());
                    carjsonObject.put("remark",bz_msg.getText().toString().trim());
                    //车辆用途
                    switch (tv_starttime.getText().toString()){
                        case "小型车":
                            carjsonObject.put("car_type","1");
                            break;
                        case "紧凑车":
                            carjsonObject.put("car_type","2");
                            break;
                        case "中型车":
                            carjsonObject.put("car_type","3");
                            break;
                        case "中型SUV":
                            carjsonObject.put("car_type","4");
                            break;
                        case "中大型车":
                            carjsonObject.put("car_type","5");
                            break;
                        case "中大型SUV":
                            carjsonObject.put("car_type","6");
                            break;
                        case "皮卡":
                            carjsonObject.put("car_type","7");
                            break;
                        case "其他":
                            carjsonObject.put("car_type","0");
                            break;
                    }
                    switch (tv_endtime.getText().toString()){
                        case "自用":
                            carjsonObject.put("use_prop","1");
                            break;
                        case "非营运":
                            carjsonObject.put("use_prop","3");
                            break;
                        case "营运":
                            carjsonObject.put("use_prop","2");
                            break;
                        case "其他":
                            carjsonObject.put("use_prop","0");
                            break;
                    }
                    //使用年限
                    carjsonObject.put("used_age",years.getText().toString().trim());
                    jsonObject.put("car",carjsonObject);
                    jsonObject.put("group_id",sp.getString(Constant.Group_id,""));
                    pledgerjsonObject.put("card_type",1);
                    pledgerjsonObject.put("name",name.getText().toString().trim());
                    pledgerjsonObject.put("phone",mobile.getText().toString().trim());
//                    JSONObject pledgerjsonObject1=new JSONObject();
                    JSONArray pledgerListjsonObject=new JSONArray();
                    JSONObject locjsonObject=new JSONObject();
                    locjsonObject.put("province",sheng);
                    locjsonObject.put("city",shi);
                    locjsonObject.put("district",qu);
                    locjsonObject.put("address",home_address.getText().toString().trim());
                    locjsonObject.put("type",1);
                    locjsonObject.put("lat",null);
                    locjsonObject.put("lng",null);
                    pledgerListjsonObject.add(locjsonObject);
                    pledgerjsonObject.put("pledger_loc",pledgerListjsonObject);
                    pledgerjsonObject.put("card_type","1");//写死是身份证
                    pledgerjsonObject.put("idcard",card_num.getText().toString().trim());
                    pledgerjsonObject.put("name",name.getText().toString().trim());
                    pledgerjsonObject.put("phone",mobile.getText().toString().trim());

                    jsonObject.put("pledger",pledgerjsonObject);

                    params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
                    HttpRequest.post(Api.add+"?token="+sp.getString(Constant.Token,""), params, new JsonHttpRequestCallback() {
                        @Override
                        protected void onSuccess(Headers headers, JSONObject jsonObject) {
                            super.onSuccess(headers, jsonObject);
                            Log.i("jsonObject", jsonObject.toString());
                            if(jsonObject.getBoolean("success")){
                                ///成功
                                Toast.makeText(AddCarActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(AddCarActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                            dialog.dialog.dismiss();
                        }

                        @Override
                        public void onFailure(int errorCode, String msg) {
                            super.onFailure(errorCode, msg);
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            dialog= DialogUIUtils.showLoading(AddCarActivity.this,"加载中...",true,true,false,true);
                            dialog.show();
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                        }
                    });
                }
                break;

        }
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
                    OptionsPickerView pickerView = new OptionsPickerView.Builder(AddCarActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
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
                    OptionsPickerView pickerView = new OptionsPickerView.Builder(AddCarActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            //当点击的时候触发的事件
                            ssq = ssq + area.get(options1);
                            tem_qu=area.get(options1);
                            tv_sex.setText(ssq);
                            citycode = citysBean2.get(options1).getCode();
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

    static class MyRunnable implements Runnable {

        List<String> images;
        List<String> originImages;
        List<String> dragImages;
        Handler handler;
        boolean add;//是否为添加图片
        private final Context mContext;

        public MyRunnable(Context context, List<String> images, List<String> originImages, List<String> dragImages, Handler handler, boolean add) {
            this.images = images;
            this.originImages = originImages;
            this.dragImages = dragImages;
            this.handler = handler;
            this.add = add;
            mContext = context;
        }

        @Override
        public void run() {
            int addIndex = originImages.size() - 1;
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).contains(MyApplication.getInstance().getString(R.string.glide_plus_icon_string))) {//说明是添加图片按钮
                    //跳出单个循环，开始下一个
                    continue;
                }
                Bitmap bitmap;
                try {
                    bitmap = PickImageHelper.getImageSampleOutput(mContext, Uri.fromFile(new File(images.get(i))));
                    Bitmap thumbNail = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
                    String fileName = PickImageHelper.GenerateNameWithUUID();
                    File file = PickImageHelper.createFileFromBitmap(mContext, fileName, thumbNail);
                    if (!add) {
                        images.set(i, file.getPath());
                    } else {//添加图片，要更新
                        dragImages.add(addIndex, file.getPath());
                        originImages.add(addIndex++, file.getPath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError ome) {
                    ome.printStackTrace();
                }
            }
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }

    private void initListener() {
        back.setOnClickListener(this);
        tv_Filling.setOnClickListener(this);
        next.setOnClickListener(this);
        rl_cardtype.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_carxi.setOnClickListener(this);
        starttime.setOnClickListener(this);
        submit.setOnClickListener(this);
        //自定义时间段选择器
//        starttime.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View view) {
//
//                //这个是年月日选择器
////                timenew=1;
////                Calendar ca=Calendar.getInstance();
////                mYear=ca.get(Calendar.YEAR);
////                mMonth=ca.get(Calendar.MONTH);
////                mDay=ca.get(Calendar.DAY_OF_MONTH);
////                mHour=ca.get(Calendar.HOUR_OF_DAY);
////                mMinutes=ca.get(Calendar.MINUTE);
////                showDialog(SHOW_STARTTIME);
//                //年月日时分一体时间选择器
//                //其他的单一的被禁用了
//
////                mTimePickerDialog = new TimePickerDialog(AddCarActivity.this);
////                mTimePickerDialog.showDateAndTimePickerDialog();
//                //尝试自定义时间选择器
//                adb=new AlertDialog.Builder(AddCarActivity.this);
//                adb.setTitle("选择时间");
////                View pickerview=initPicker();
////                adb.setView(pickerview);
////                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                            //不同的android版本对应不同的方法
////                            mMinutes=tp.getMinute();
////                            mHour=tp.getHour();
////                        }else{
////                            mMinutes=tp.getCurrentMinute();
////                            mHour=tp.getCurrentHour();
////                        }
////                        mYear=dp.getYear();
////                        mMonth=dp.getMonth();
////                        mDay=dp.getDayOfMonth();
////                        tv_starttime.setText(new StringBuffer().append(mYear).append("-").append(mMonth).append("-").append(mDay).append(" ").append(mHour).append(":").append(mMinutes));
////                    }
////                });
////                adb.show();
//                //同页面开始时间结束时间范围
//                View datafromto=initdatafromto();
//                adb.setView(datafromto);
////                dp.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
////                    @Override
////                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
////                        if(isstart==1){
////                            et_endtime.setText(new StringBuffer().append(i).append("-").append(i1).append("-").append(i2));
////                        }
////                            et_starttime.setText(new StringBuffer().append(i).appnd("-").append(i1).append("-").append(i2));
////                        }else{e
//                Calendar ca=Calendar.getInstance();
////                    }
////                });
//                dp.init(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
//                    @Override
//                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
//                        if(isstart==1){
//                            et_starttime.setText(new StringBuffer().append(i).append("-").append(i1).append("-").append(i2));
//                        }else{
//                            et_endtime.setText(new StringBuffer().append(i).append("-").append(i1).append("-").append(i2));
//                        }
//                    }
//                });
//                et_starttime.setOnClickListener(new View.OnClickListener() {
//                    @SuppressLint("ResourceAsColor")
//                    @Override
//                    public void onClick(View view) {
//                        isstart=1;
//                        et_starttime.setTextColor(0xff0000ff);
//                        linestart.setBackgroundColor(0xff0000ff);
//                        et_endtime.setTextColor(R.color.black);
//                        lineend.setBackgroundColor(R.color.black);
//
//                        et_starttime.setText(new StringBuffer().append(dp.getYear()).append("-").append(dp.getMonth()).append("-").append(dp.getDayOfMonth()));
//                    }
//                });
//                et_endtime.setOnClickListener(new View.OnClickListener() {
//                    @SuppressLint("ResourceAsColor")
//                    @Override
//                    public void onClick(View view) {
//                        isstart=0;
//                        et_starttime.setTextColor(R.color.black);
//                        linestart.setBackgroundColor(R.color.black);
//                        et_endtime.setTextColor(0xff0000ff);
//                        lineend.setBackgroundColor(0xff0000ff);
//                        et_endtime.setText(new StringBuffer().append(dp.getYear()).append("-").append(dp.getMonth()).append("-").append(dp.getDayOfMonth()));
//                    }
//                });
//                adb.show();
//            }
//        });
        //结束时间设置
        endtime.setOnClickListener(this);
//        endtime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                timenew=2;
//                Calendar ca=Calendar.getInstance();
//                mYear=ca.get(Calendar.YEAR);
//                mMonth=ca.get(Calendar.MONTH);
//                mDay=ca.get(Calendar.DAY_OF_MONTH);
//                mHour=ca.get(Calendar.HOUR_OF_DAY);
//                mMinutes=ca.get(Calendar.MINUTE);
//                showDialog(SHOW_STARTTIME);
//            }
//        });
    }

    @SuppressLint("ResourceAsColor")
    private View initdatafromto() {
        View inflate=LayoutInflater.from(AddCarActivity.this).inflate(R.layout.layout_date_from_to,null);
        Calendar ca=Calendar.getInstance();
        dp=inflate.findViewById(R.id.datap);
        et_starttime=inflate.findViewById(R.id.et_starttime);
        et_endtime=inflate.findViewById(R.id.et_endtime);
        linestart=inflate.findViewById(R.id.startline);
        lineend=inflate.findViewById(R.id.endline);
        dp.updateDate(ca.get(Calendar.YEAR),ca.get(Calendar.MONTH),ca.get(Calendar.DAY_OF_MONTH));
        et_starttime.setTextColor(0xff0000ff);
        linestart.setBackgroundColor(0xff0000ff);
        et_endtime.setTextColor(R.color.black);
        lineend.setBackgroundColor(R.color.black);
        et_starttime.setText(new StringBuffer().append(ca.get(Calendar.YEAR)).append("-").append(ca.get(Calendar.MONTH)).append("-").append(ca.get(Calendar.DAY_OF_MONTH)));
        return inflate;
    }

    private View initPicker() {
        View inflater= LayoutInflater.from(AddCarActivity.this).inflate(R.layout.layout_timepicker,null);
        dp=inflater.findViewById(R.id.dp);
        tp=inflater.findViewById(R.id.tp);
        dp.updateDate(2016,2,22);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //当前版本大于23
            //不同的android版本对应不同的方法
            tp.setMinute(30);
            tp.setHour(3);
        }else{
            tp.setCurrentHour(2);
            tp.setCurrentMinute(20);
        }
        tp.setIs24HourView(true);
        return  inflater;
    }


    private void initData() {
        //请求车辆品牌和地址接口
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
                dialog.dialog.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(AddCarActivity.this,"加载中...",true,true,false,true);
                dialog.show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
//        if (originImages == null) {//原始图片
//            originImages = new ArrayList<>();
//        }
//        mContext = getApplicationContext();
//        //添加按钮图片资源
//        String plusPath = getString(R.string.glide_plus_icon_string) + getPackageInfo(mContext).packageName + "/mipmap/" + R.mipmap.mine_btn_plus;
//        Log.i("plusPath", plusPath);
//        dragImages = new ArrayList<>();//压缩图片
//        originImages.add(plusPath);//添加按键，超过9张时在adapter中隐藏
//        dragImages.addAll(originImages);
//        new Thread(new MyRunnable(this, dragImages, originImages, dragImages, myHandler, false)).start();//开启线程，在新线程中去压缩图片
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return new PackageInfo();
    }
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<Activity> reference;

        public MyHandler(Activity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AddCarActivity activity = (AddCarActivity) reference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        activity.postArticleImgAdapter.notifyDataSetChanged();
                        break;
                }
            }
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
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case SHOW_STARTTIME:
                final DatePickerDialog dpd=new DatePickerDialog(this,null,mYear,mMonth,mDay);
                //这边暂时没有用，我就简单的按照年月日来弄,不需要时分
//                dpd.setButton3("时间", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i("时间","时间");
//                        showDialog(SHOW_ENDTIME);
//                    }
//                });
                //给确定按钮增加点击事件
                dpd.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //获取弹出框上面的时间
                        DatePicker dp=dpd.getDatePicker();
                        mYear=dp.getYear();
                        mMonth=dp.getMonth();
                        mDay=dp.getDayOfMonth();
                        //展示
                        display();
                    }
                });
//                dpd.setButton("时间", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        showDialog(SHOW_ENDTIME);
//                    }
//                });
                return dpd;
            case SHOW_ENDTIME:
//                final TimePickerDialog tpd=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                        mHour=i;
//                        mMinutes=i1;
//                    }
//                }, 0, 0, true);

                break;
        }
        return null;

    }

    private void display() {
        int month=mMonth+1;
        String month_new="";
        String day="";
        if(month<10){
            month_new="0"+month;
        }else{
            month_new=month+"";
        }
        if(mDay<10){
            day="0"+mDay;
        }else{
            day=mDay+"";
        }
        if(timenew==1){
            tv_starttime.setText(new StringBuffer().append(mYear).append("-").append(month_new).append("-").append(day));
        }else if(timenew==2){
            tv_endtime.setText(new StringBuffer().append(mYear).append("-").append(month_new).append("-").append(day));
        }

    }

    private void initView() {
        person_msg=findViewById(R.id.person_msg);
        rcvImg=findViewById(R.id.rcv_img);
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        title.setText("新增车辆");
        starttime=findViewById(R.id.starttime);
        endtime=findViewById(R.id.endtime);
        tv_starttime=findViewById(R.id.tv_starttime);
        tv_endtime=findViewById(R.id.tv_endtime);
        rl_sex=findViewById(R.id.rl_sex);//性别
        tv_sex=findViewById(R.id.tv_sex);//性别
        name=findViewById(R.id.name);//姓名
        rl_cardtype=findViewById(R.id.rl_cardtype);//证件类型
        car_type=findViewById(R.id.car_type);//证件类型
        card_num=findViewById(R.id.card_num);//证件号码
        mobile=findViewById(R.id.mobile);//手机号码
        home_address=findViewById(R.id.home_address);//家庭地址
        car_vin=findViewById(R.id.car_vin);//车辆vin
        car_num=findViewById(R.id.car_num);//车牌号
        rl_cartype=findViewById(R.id.rl_cartype);//选择车型
        tv_cartype=findViewById(R.id.tv_cartype);//车辆类型
        car_color=findViewById(R.id.car_color);//车辆颜色
        mileage=findViewById(R.id.mileage);//行驶里程
        use_age=findViewById(R.id.use_age);//使用年限
        use_money=findViewById(R.id.use_money);//借款金额
        belong=findViewById(R.id.belong);//所属客户
        belong_name=findViewById(R.id.belong_name);//所属客户名字
        con_person=findViewById(R.id.con_person);//联系人
        con_phone=findViewById(R.id.con_phone);//联系电话
        installer_name=findViewById(R.id.installer_name);//安装工姓名
        installer_phone=findViewById(R.id.installer_phone);//安装工手机号码
        installer_bz=findViewById(R.id.installer_bz);//安装备注
        add_device=findViewById(R.id.add_device);//添加设备
        person_msg=findViewById(R.id.person_msg);
        car_filling_msg=findViewById(R.id.car_filling_msg);
        car_msg=findViewById(R.id.car_msg);
        tv_Filling=findViewById(R.id.tv_Filling);
        next=findViewById(R.id.next);
        submit=findViewById(R.id.submit);
        rl_carxi=findViewById(R.id.rl_carxi);
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        tv_carxi=findViewById(R.id.tv_carxi);
        use_carmoney=findViewById(R.id.use_carmoney);
        car_fdj=findViewById(R.id.car_fdj);
        bz_msg=findViewById(R.id.bz_msg);
        years=findViewById(R.id.years);
        //初始化图片recycle
//        initRcv();
        initRecy();
//        List<TieBean> strings = new ArrayList<TieBean>();
//        strings.add(new TieBean("1"));
//        strings.add(new TieBean("2"));
//        strings.add(new TieBean("3"));
//        DialogUIUtils.showSheet(AddCarActivity.this, strings, "", Gravity.CENTER, true, true, new DialogUIItemListener() {
//            @Override
//            public void onItemClick(CharSequence text, int position) {
////                showToast(text);
//            }
//        }).show();
    }

    private void initRecy() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        rcvImg.setLayoutManager(new GridLayoutManager(this, 4));
        rcvImg.setHasFixedSize(true);
        rcvImg.setAdapter(adapter);
    }

    private void initRcv() {
        //压缩过的图片
        postArticleImgAdapter=new PostArticleImgAdapter(mContext,dragImages);
        rcvImg.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        rcvImg.setAdapter(postArticleImgAdapter);
        MyCallBack myCallBack=new MyCallBack(postArticleImgAdapter,dragImages,originImages);
        itemTouchHelper=new ItemTouchHelper(myCallBack);
itemTouchHelper.attachToRecyclerView(rcvImg);
//事件监听
        rcvImg.addOnItemTouchListener(new OnRecyclerItemClickListener(rcvImg) {

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                if (originImages.get(vh.getAdapterPosition()).contains(getString(R.string.glide_plus_icon_string))) {//打开相册
                    Log.i("打开相册", "onItemClick: ");
                    MultiImageSelector.create()
                            .showCamera(true)
                            .count(IMAGE_SIZE - originImages.size() + 1)
                            .multi()
                            .start(AddCarActivity.this, REQUEST_IMAGE);
                } else {
                    Toast.makeText(mContext, "Review", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                //如果item不是最后一个，则执行拖拽
                if (vh.getLayoutPosition() != dragImages.size() - 1) {
                    itemTouchHelper.startDrag(vh);
                }
            }
        });

//        myCallBack.setDragListener(new MyCallBack.DragListener() {
//            @Override
//            public void deleteState(boolean delete) {
//                if (delete) {
//                    tv.setBackgroundResource(R.color.holo_red_dark);
//                    tv.setText(getResources().getString(R.string.post_delete_tv_s));
//                } else {
//                    tv.setText(getResources().getString(R.string.post_delete_tv_d));
//                    tv.setBackgroundResource(R.color.holo_red_light);
//                }
//            }
//
//            @Override
//            public void dragState(boolean start) {
//                if (start) {
//                    tv.setVisibility(View.VISIBLE);
//                } else {
//                    tv.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    private  DatePickerDialog.OnDateSetListener dpdListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            //只要点击任何datepick上的按钮都会执行这个
            //如果没有取消按钮的话就能用这个
            //对于取消按钮可以不执行这个，在listener里面写null
        }
    };

    //确定按钮
    @Override
    public void positiveListener() {
        String newmonth="";
        String newday="";
        String newhour="";
        String newminutes="";
        int hour = mTimePickerDialog.getHour();
        int minute = mTimePickerDialog.getMinute();
        int month=mTimePickerDialog.getMonth();
        int day=mTimePickerDialog.getDay();
        if(month<10){
            newmonth="0"+month;
        }else{
            newmonth=month+"";
        }
        if(day<10){
            newday="0"+day;
        }else{
            newday=""+day;
        }
        if(hour<10){
            newhour="0"+hour;
        }else{
            newhour=""+hour;
        }
        if(minute<10){
            newminutes="0"+minute;
        }else{
            newminutes=""+minute;
        }
        tv_starttime.setText(mTimePickerDialog.getYear()+"-"+newmonth+"-"+newday+"  "+newhour+":"+newminutes);
        Log.i("=====","=======year======"+mTimePickerDialog.getYear());
        Log.i("=====","=======getMonth======"+mTimePickerDialog.getMonth());
        Log.i("=====","=======getDay======"+mTimePickerDialog.getDay());
        Log.i("=====","=======getHour======"+mTimePickerDialog.getHour());
        Log.i("=====","=======getMinute======"+mTimePickerDialog.getMinute());
    }

    /**
     * 取消按钮
     */
    @Override
    public void negativeListener() {

    }


}
