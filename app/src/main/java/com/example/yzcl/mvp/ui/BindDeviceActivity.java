package com.example.yzcl.mvp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.dou361.dialogui.DialogUIUtils.showToast;

/**
 * Created by Lenovo on 2018/8/9.
 */

public class BindDeviceActivity extends BaseActivity implements View.OnClickListener, ImagePickerAdapter.OnRecyclerViewItemClickListener {
    private TextView back;
    private TextView textview2;
    private TextView title;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 5;               //允许选择图片最大数
    private RecyclerView rcv_img;
    private RecyclerView device_list;
    private ArrayList<String> list = new ArrayList<>();
    private TextView add_device;
    private TextView device_name;
    private SharedPreferences sp;
    private String TAG = "BindDeviceActivity";
    private BuildBean dialog;
    BindDeviceAdapter bindDeviceAdapter;
    private ArrayList<BindDeviceBean.BindDeviceBeanMsg> dev_list = new ArrayList<>();

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    String jsonObject;
    Intent intent;
    JSONObject jsonObject1;
    private String url = "http://project.thinghigh.cn/index.php/api/v1/uploadTxt";

    private int imgpos = 0;
    ArrayList<String> lasturl = new ArrayList<>();
//图片返回需要处理

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
        intent = getIntent();
        title=findViewById(R.id.title);
        back = findViewById(R.id.back);
        textview2 = findViewById(R.id.textview2);
        rcv_img = findViewById(R.id.rcv_img);
        device_list = findViewById(R.id.device_list);
        device_name = findViewById(R.id.device_name);
        add_device = findViewById(R.id.add_device);
        sp = getSharedPreferences("YZCL", MODE_PRIVATE);
        initRecy();
    }

    private void initData() {
//        for(int i=0;i<30;i++){
//            list.add(i+"aa");
//        }
        jsonObject = intent.getStringExtra("jsonObject");
        jsonObject1 = JSONObject.parseObject(jsonObject);
    }

    private void initListener() {
        back.setText("取消");
        textview2.setText("完成");
        title.setText("绑定设备");
        back.setOnClickListener(this);
        textview2.setOnClickListener(this);
        add_device.setOnClickListener(this);

        bindDeviceAdapter = new BindDeviceAdapter(this, dev_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BindDeviceActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        device_list.setLayoutManager(layoutManager);
        device_list.setItemAnimator(new DefaultItemAnimator());
        device_list.setAdapter(bindDeviceAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.textview2:
                imgpos = 0;
                lasturl.clear();
                if(dev_list.size()>0){

//                    if(dev_list.size()>0){
                        //有设备
                        for (int i = 0; i < dev_list.size(); i++) {
                            //只有全部通过才能提交
                            //有设备
                            if (dev_list.get(i).getLoc() != null) {
                                Log.i(TAG, dev_list.get(i).getLoc());
                            } else {
                                Toast.makeText(BindDeviceActivity.this, "安装位置不能为空", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            if (i == dev_list.size() - 1) {
                                Toast.makeText(BindDeviceActivity.this, "可以提交", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, jsonObject1.getJSONObject("pledger").getString("card_type"));

                                if (selImageList.size() > 0) {
                                    dialog = DialogUIUtils.showLoading(BindDeviceActivity.this, "加载中...", true, true, false, true);
                                    dialog.show();
                                    uploadMultiFile(selImageList.get(imgpos).path);
                                }else{
                                    //没有添加图片，只有设备
                                    addCar();
                                }
                            }
                        }
//                    }else{
//                        //没设备
//                        addCar();
//                    }

                }else if(selImageList.size()==0){
                    //没图片没设备
                    Toast.makeText(BindDeviceActivity.this,"请至少添加一张图片或一个设备",Toast.LENGTH_SHORT).show();
                }else{
                    //有图片没设备
                    dialog = DialogUIUtils.showLoading(BindDeviceActivity.this, "加载中...", true, true, false, true);
                    dialog.show();
                    uploadMultiFile(selImageList.get(imgpos).path);
//                    for (int i = 0; i < dev_list.size(); i++) {
                        //只有全部通过才能提交
//                        if (dev_list.get(i).getLoc() != null) {
//                            Log.i(TAG, dev_list.get(i).getLoc());
//                        } else {
//                            Toast.makeText(BindDeviceActivity.this, "安装位置不能为空", Toast.LENGTH_SHORT).show();
//                            break;
//                        }
//                        if (i == dev_list.size() - 1) {
//                            Toast.makeText(BindDeviceActivity.this, "可以提交", Toast.LENGTH_SHORT).show();
//                            Log.i(TAG, jsonObject1.getJSONObject("pledger").getString("card_type"));
//                            dialog = DialogUIUtils.showLoading(BindDeviceActivity.this, "加载中...", true, true, false, true);
//                            dialog.show();
//                            if (selImageList.size() > 0) {
//                                uploadMultiFile(selImageList.get(imgpos).path);
//                            }else{
//                                //没有添加图片，只有设备
//                                addCar();
//                            }
//                        }
//                    }
                }
                break;
            case R.id.add_device:
                //添加设备
                String dev_name = device_name.getText().toString().trim();
                //获取数据
                if (dev_name.equals("")) {
                    Toast.makeText(BindDeviceActivity.this, "请输入设备名", Toast.LENGTH_SHORT).show();
                } else {
                    queDeviceMsg(dev_name);
                }

                break;
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
                    Toast.makeText(BindDeviceActivity.this, bindDeviceBean.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (bindDeviceBean.getCount() == 0) {
                    Toast.makeText(BindDeviceActivity.this, "设备名输入错误", Toast.LENGTH_SHORT).show();
                } else {
                    //判断是否已经添加了
                    boolean hasadd = false;
                    for (int i = 0; i < dev_list.size(); i++) {
                        if (dev_list.get(i).getInternalnum().equals(bindDeviceBean.getList().get(0).getInternalnum())) {
                            Toast.makeText(BindDeviceActivity.this, "请勿重复添加", Toast.LENGTH_SHORT).show();
                            hasadd = true;
                            break;
                        }
                    }
                    if (!hasadd) {
                        dev_list.add(bindDeviceBean.getList().get(0));
                        bindDeviceAdapter.notifyItemInserted(dev_list.size());
                    }


                }
                dialog.dialog.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog = DialogUIUtils.showLoading(BindDeviceActivity.this, "加载中...", true, true, false, true);
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
        Log.i("onItemClick: ", position + "21");
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
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
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
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
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
                if (images != null) {
                    for(int imgitem=0;imgitem<images.size();imgitem++){
                        Log.i("onActivityResult: ", compressImage(images.get(imgitem).path));
                    }
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }

    private void uploadMultiFile(String imgUrl) {

        String imageType = "multipart/form-data";
        File file = new File(imgUrl);//imgUrl为图片位置

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);
//        Bitmap bmp = BitmapFactory.decodeFile(imgUrl);
//        String imgba = Bitmap2StrByBase64(bmp);
//        Log.i(TAG, "uploadMultiFile: " + imgba);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                //下面这个是传送base64文件的
//                .addFormDataPart("file", "data:image/jpeg;base64,"+imgba)
//                .addFormDataPart("imagetype", imageType)
                .build();
        Request request = new Request.Builder()
                .url(Api.upload + "?token=" + sp.getString(Constant.Token, ""))
//                .url(url)
                .post(requestBody)
                .build();
        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", "onFailure: ");
//                Toast.makeText(BindDeviceActivity.this, "上传失败,请重试", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //放在UI线程弹Toast
                                        Toast.makeText(BindDeviceActivity.this, "上传失败,请重试", Toast.LENGTH_SHORT).show();

                    }
                });
                dialog.dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String htmlStr = response.body().string();
                Log.i("onResponse: ", htmlStr);
                JSONObject jsonObject2 = JSONObject.parseObject(htmlStr);

                lasturl.add("http://101.37.119.32:20209/" + jsonObject2.getJSONArray("list").getJSONObject(0).getString("fullname"));
//                Log.i("result", "http://ring.thinghigh.cn"+htmlStr);
//                com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) JSON.parse(htmlStr);
//                com.alibaba.fastjson.JSONObject datamsg = jsonObject.getJSONObject("data");
//                String img_name = datamsg.getString("path");
////                String img_name=jsonObject.getString("data");
////                Toast.makeText(ChooseUpPicActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
//                final String IMAGE_URL = img_name;
//                Log.i("result", IMAGE_URL);
                //这个是个子线程，不能在子线程里面弹出toast，需要到主线程中去

                if (imgpos == selImageList.size() - 1) {
                    //不能传了
                    dialog.dialog.dismiss();
                    //请求新增接口
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //放在UI线程弹Toast
                            addCar();
                        }
                    });

                } else {
                    imgpos = imgpos + 1;
                    uploadMultiFile(selImageList.get(imgpos).path);
                }

//                Handler handler = new Handler(Looper.getMainLooper());
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //放在UI线程弹Toast
//                        Toast.makeText(BindDeviceActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
        });
    }

    /**
     * 图片压缩-质量压缩
     *
     * @param filePath 源图片路径
     * @return 压缩后的路径
     */

    public static String compressImage(String filePath) {

        //原文件
        File oldFile = new File(filePath);


        //压缩文件路径 照片路径/
        String targetPath = oldFile.getPath();
        int quality = 50;//压缩比例0-100
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        //在部分手机里面会有自动选择，如小米8
        int degree = readPictureDegree(filePath);//获取相片拍摄角度
//
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotaingImageView(degree,bm);
        }
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return filePath;
        }
        return outputFile.getPath();
    }
    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    /**
     * 旋转图片
     * @param angle 被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private void addCar() {

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < lasturl.size(); i++) {
            JSONObject imgjsonObject = new JSONObject();
            imgjsonObject.put("imgurl", lasturl.get(i));
            jsonArray.add(imgjsonObject);
        }
        jsonObject1.getJSONObject("car").put("carimage", jsonArray);

        JSONArray jsonArray1 = new JSONArray();
        for (int i = 0; i < dev_list.size(); i++) {
            JSONObject devjsonObject = new JSONObject();
            devjsonObject.put("deviceid", dev_list.get(i).getDeviceid());
            devjsonObject.put("install_part", dev_list.get(i).getLoc());
            jsonArray1.add(devjsonObject);
        }
        jsonObject1.put("device", jsonArray1);

        //可以请求接口
        RequestParams params = new RequestParams();
        params.addHeader("Content-Type", "application/json");

        params.setRequestBody(MediaType.parse("application/json"), jsonObject1.toString());
        String jsonstr=jsonObject1.toString();
        HttpRequest.post(Api.add + "?token=" + sp.getString(Constant.Token, ""), params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i("jsonObject", jsonObject.toString());
                if (jsonObject.getBoolean("success")) {
                    ///成功
                    Toast.makeText(BindDeviceActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent data=new Intent();
                    setResult(10, data);
                    finish();
                } else {
                    Toast.makeText(BindDeviceActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                dialog = DialogUIUtils.showLoading(BindDeviceActivity.this, "加载中...", true, true, false, true);
                dialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
