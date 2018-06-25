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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.yzcl.Listener.OnRecyclerItemClickListener;
import com.example.yzcl.R;
import com.example.yzcl.adapter.PostArticleImgAdapter;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.utils.MultiImageSelector;
import com.example.yzcl.utils.MyCallBack;
import com.example.yzcl.utils.PickImageHelper;
import com.example.yzcl.utils.TimePickerDialog;
import com.gyf.barlibrary.ImmersionBar;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;


/**
 * 新增车辆
 */
public class AddCarActivity extends BaseActivity implements com.example.yzcl.utils.TimePickerDialog.TimePickerDialogInterface{
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //由于键盘显示会把提交按钮顶上去
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_addcar);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initData();
        initView();

        initListener();
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //开始时间设置
        starttime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                //这个是年月日选择器
//                timenew=1;
//                Calendar ca=Calendar.getInstance();
//                mYear=ca.get(Calendar.YEAR);
//                mMonth=ca.get(Calendar.MONTH);
//                mDay=ca.get(Calendar.DAY_OF_MONTH);
//                mHour=ca.get(Calendar.HOUR_OF_DAY);
//                mMinutes=ca.get(Calendar.MINUTE);
//                showDialog(SHOW_STARTTIME);
                //年月日时分一体时间选择器
                //其他的单一的被禁用了

//                mTimePickerDialog = new TimePickerDialog(AddCarActivity.this);
//                mTimePickerDialog.showDateAndTimePickerDialog();
                //尝试自定义时间选择器
                adb=new AlertDialog.Builder(AddCarActivity.this);
                adb.setTitle("选择时间");
//                View pickerview=initPicker();
//                adb.setView(pickerview);
//                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            //不同的android版本对应不同的方法
//                            mMinutes=tp.getMinute();
//                            mHour=tp.getHour();
//                        }else{
//                            mMinutes=tp.getCurrentMinute();
//                            mHour=tp.getCurrentHour();
//                        }
//                        mYear=dp.getYear();
//                        mMonth=dp.getMonth();
//                        mDay=dp.getDayOfMonth();
//                        tv_starttime.setText(new StringBuffer().append(mYear).append("-").append(mMonth).append("-").append(mDay).append(" ").append(mHour).append(":").append(mMinutes));
//                    }
//                });
//                adb.show();
                //同页面开始时间结束时间范围
                View datafromto=initdatafromto();
                adb.setView(datafromto);
//                dp.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
//                    @Override
//                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
//                        if(isstart==1){
//                            et_endtime.setText(new StringBuffer().append(i).append("-").append(i1).append("-").append(i2));
//                        }
//                            et_starttime.setText(new StringBuffer().append(i).appnd("-").append(i1).append("-").append(i2));
//                        }else{e
                Calendar ca=Calendar.getInstance();
//                    }
//                });
                dp.init(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                        if(isstart==1){
                            et_starttime.setText(new StringBuffer().append(i).append("-").append(i1).append("-").append(i2));
                        }else{
                            et_endtime.setText(new StringBuffer().append(i).append("-").append(i1).append("-").append(i2));
                        }
                    }
                });
                et_starttime.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view) {
                        isstart=1;
                        et_starttime.setTextColor(0xff0000ff);
                        linestart.setBackgroundColor(0xff0000ff);
                        et_endtime.setTextColor(R.color.black);
                        lineend.setBackgroundColor(R.color.black);

                        et_starttime.setText(new StringBuffer().append(dp.getYear()).append("-").append(dp.getMonth()).append("-").append(dp.getDayOfMonth()));
                    }
                });
                et_endtime.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view) {
                        isstart=0;
                        et_starttime.setTextColor(R.color.black);
                        linestart.setBackgroundColor(R.color.black);
                        et_endtime.setTextColor(0xff0000ff);
                        lineend.setBackgroundColor(0xff0000ff);
                        et_endtime.setText(new StringBuffer().append(dp.getYear()).append("-").append(dp.getMonth()).append("-").append(dp.getDayOfMonth()));
                    }
                });
                adb.show();
            }
        });
        //结束时间设置
        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timenew=2;
                Calendar ca=Calendar.getInstance();
                mYear=ca.get(Calendar.YEAR);
                mMonth=ca.get(Calendar.MONTH);
                mDay=ca.get(Calendar.DAY_OF_MONTH);
                mHour=ca.get(Calendar.HOUR_OF_DAY);
                mMinutes=ca.get(Calendar.MINUTE);
                showDialog(SHOW_STARTTIME);
            }
        });
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
        if (originImages == null) {//原始图片
            originImages = new ArrayList<>();
        }
        mContext = getApplicationContext();
        //添加按钮图片资源
        String plusPath = getString(R.string.glide_plus_icon_string) + getPackageInfo(mContext).packageName + "/mipmap/" + R.mipmap.mine_btn_plus;
        Log.i("plusPath", plusPath);
        dragImages = new ArrayList<>();//压缩图片
        originImages.add(plusPath);//添加按键，超过9张时在adapter中隐藏
        dragImages.addAll(originImages);
        new Thread(new MyRunnable(this, dragImages, originImages, dragImages, myHandler, false)).start();//开启线程，在新线程中去压缩图片
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
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {//从相册选择完图片
            //压缩图片
            new Thread(new MyRunnable(AddCarActivity.this, data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT),
                    originImages, dragImages, myHandler, true)).start();
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
        rcvImg=findViewById(R.id.rcv_img);
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        title.setText("新增车辆");
        starttime=findViewById(R.id.starttime);
        endtime=findViewById(R.id.endtime);
        tv_starttime=findViewById(R.id.tv_starttime);
        tv_endtime=findViewById(R.id.tv_endtime);
        //初始化图片recycle
        initRcv();
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
