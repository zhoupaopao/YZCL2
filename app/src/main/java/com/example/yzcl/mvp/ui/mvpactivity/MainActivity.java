package com.example.yzcl.mvp.ui.mvpactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.LoginBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.utils.StatusBarUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.apache.http.Header;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;

public class MainActivity extends BaseActivity {
    //登录页面
    private Button login;
    private TextView forgetpwd;
    private CheckBox rempwd;
    private EditText username;
    private EditText password;
    private EditText yzm;
    private ImageView pic_yzm;
    private SharedPreferences sp = null;
    LoginBean loginBean;
    BuildBean dia;
    Bitmap bm;
    final String url = "http://101.37.119.32:20200/auth/v1/imagecode";
    Handler handler1;
    String TAG="MainActivity";
    String key;
    String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //最有效的标题栏，同时遇到白色背景，同时当前api不支持字体变色
        //api4.4以上可以设置标题栏颜色，但是字体设置为深色需要6.0
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .init();

        //初始化数据
        initData();
        //设置各控件的监听事件
        setonClick();

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    break;
                case 1:
                    Log.i("handleMessage", "1");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, HomePage.class);
                    startActivity(intent);
                    DialogUIUtils.dismiss(dia);
                    finish();
                    break;
            }
        }
    };
    private void setonClick() {
        pic_yzm.setOnClickListener(new View.OnClickListener() {
            //验证码点击事件
            @Override
            public void onClick(View view) {
                setpic();
            }
        });
        forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUIUtils.showAlert(MainActivity.this, null, "请联系管理员或拨打客户热线400-0000-\n0000找回密码。", "", "", "知道了", "", true, true, true, new DialogUIListener() {
                    @Override
                    public void onPositive() {
//                        showToast("onPositive");
                    }

                    @Override
                    public void onNegative() {
                        showToast("onNegative");
                    }

                }).show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            //登录，需要验证是否有账号密码，有看看是否记住密码，请求接口，成功切换页面
            @Override
            public void onClick(View view) {
                dia=DialogUIUtils.showLoading(MainActivity.this, "加载中...",false,false,false,false);
                dia.show();
//                DialogUIUtils.showMdLoading(MainActivity.this, "加载中...",true,true,true,true);
//                SharedPreferences.Editor editor=sp.edit();
//
//                editor.putString("Token","djkwMjBiNDE0MjIxZjg0bTU5MjJmNWIzYTA5OWE5NGI5ZWIzNjMxOGJlMjAxYzZkNDFhNGQ0NzJlYzE4NDQ0OWZjOTdjN2E0MzczMjQzMDk4Nw==");
//                editor.putString("UserId","a4d472ec184449fc97c7a43732430987");
//                editor.commit();
//                waitme();

                if (username.getText().toString().isEmpty()) {
                    Log.i("in", "1");
                    Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    DialogUIUtils.dismiss(dia);
                } else if (password.getText().toString().isEmpty()) {
                    Log.i("in", "2");
                    Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    DialogUIUtils.dismiss(dia);
                } else if(yzm.getText().toString().isEmpty()){
                    Log.i("in", "3");
                    Toast.makeText(MainActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    DialogUIUtils.dismiss(dia);
                }else {
                    //用户名密码都有数据，请求登录接口
                    Log.i("in", "3");
                    login(username.getText().toString().trim(), password.getText().toString().trim(),yzm.getText().toString().trim());
                }
//                SharedPreferences.Editor editor=sp.edit();
//                if(rempwd.isChecked()){
//                    //被选中，直接自动登录
//                    Log.i("canauto", "can ");
//                    editor.putString("rempwd","true");
//                }else{
//                    editor.putString("rempwd","false");
//                    Log.i("canauto", "no ");
//                }
//                editor.commit();
//                Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent();
//                intent.setClass(MainActivity.this,HomePage.class);
//                startActivity(intent);
//                finish();
            }
        });
        //键盘上面的done的点击事件
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_GO){
                    Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    private void waitme() {
        //假装执行耗时操作，这边是睡10S，不会导致oom
        //线程少无所谓，但是如果线程过多，需要使用线程池来对线程进行管理
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void showToast(CharSequence onPositive) {
        Toast.makeText(this,onPositive.toString(),Toast.LENGTH_SHORT).show();
    }

    private void login(final String name, final String psd,final String yzm) {

//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
//        params.put("username", name);
//        params.put("password", psd);
//        params.put("imagecode", yzm);
//        params.setContentEncoding("UTF-8");
//        Log.i("login: ", Api.Jsonp_GetLogin+"?username="+name+"&password="+psd+"&imagecode="+yzm);
//        client.get(Api.Jsonp_GetLogin, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                try {
//                    String json = new String(bytes, "UTF-8").toString();
//                    loginBean = JSONObject.parseObject(json, LoginBean.class);
//                    if (loginBean.isSuccess()) {
//                        SharedPreferences.Editor editor = sp.edit();
//                        editor.putString(Constant.Token, loginBean.getObject().getToken());
//                        editor.putString(Constant.userid, loginBean.getObject().getUserid());
//                        if (rempwd.isChecked()) {
//                            //被选中，记住账号
//                            Log.i("rem", "1 ");
//                            editor.putString(Constant.username,name);
//                            editor.putString(Constant.password,psd);
//                            editor.putBoolean("rempwd", true);
//                        } else {
//                            editor.putBoolean("rempwd", false);
//                            Log.i("rem", "0 ");
//                        }
//                        editor.commit();
//                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
////                        DialogUIUtils.dismiss(dia);
//                        waitme();
//                    } else {
//                        DialogUIUtils.dismiss(dia);
//                        Toast.makeText(MainActivity.this, loginBean.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Log.i("error", "1");
//            }
//        });
        RequestParams params=new RequestParams();
        params.addFormDataPart("username",name);
        params.addFormDataPart("password",psd);
        params.addFormDataPart("key",key);
        params.addFormDataPart("code",yzm);
        HttpRequest.get(Api.Jsonp_GetLogin,params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                LoginBean loginBean=JSONObject.parseObject(jsonObject.toString(),LoginBean.class);
                if(loginBean.isSuccess()){
                    //登录成功
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Constant.Token, loginBean.getObject().getToken());
                    editor.putString(Constant.userid, loginBean.getObject().getUserid());
                    if (rempwd.isChecked()) {
                            //被选中，记住账号
                            Log.i("rem", "1 ");
                            editor.putString(Constant.username,name);
                            editor.putString(Constant.password,psd);
                            editor.putBoolean("rempwd", true);
                        } else {
                            editor.putBoolean("rempwd", false);
                            Log.i("rem", "0 ");
                        }
                        editor.commit();
                    handler.sendEmptyMessage(1);
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, loginBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
                DialogUIUtils.dismiss(dia);

            }

            @Override
            public void onFinish() {
                super.onFinish();
                DialogUIUtils.dismiss(dia);

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                DialogUIUtils.dismiss(dia);

            }
        });

    }


    private void initData() {
        login = findViewById(R.id.login);
        forgetpwd = findViewById(R.id.forgetpwd);
        rempwd = findViewById(R.id.rem_pwd);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        yzm = findViewById(R.id.yzm);
        pic_yzm=findViewById(R.id.pic_yzm);
        //创建属于主线程的handler
        handler1=new Handler();
        setpic();
        final String TAG="async";

        sp = getSharedPreferences("YZCL", MODE_PRIVATE);
    }
    // 构建Runnable对象，在runnable中更新界面
    //必须在这里面处理不然会报错
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            pic_yzm.setImageBitmap(bm);
        }

    };
    public void setpic(){
        //请求图片
        RequestParams paramss=new RequestParams();
        paramss.addFormDataPart("username","");
        paramss.addFormDataPart("password","");
        HttpRequest.get(Api.Jsonp_GetLogin,paramss,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                String jsonString=jsonObject.toString();
                LoginBean loginBean=JSONObject.parseObject(jsonString,LoginBean.class);
                key=loginBean.getObject().getKey();
                image=loginBean.getObject().getImage();
                Log.i(TAG, key+"/"+image);
                bm=stringToBitmap(image);
                handler1.post(runnableUi);
            }
        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                bm=getHttpBitmap(url);
//                handler1.post(runnableUi);
//            }
//        }).start();
    }


    public Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //自动登录写在这里

//        if (sp.getBoolean("rempwd",false)) {
            //给用户名赋值
            username.setText(sp.getString(Constant.username,""));
            password.setText(sp.getString(Constant.password,""));
            //将光标移动到最后
        username.setSelection(username.length());
            rempwd.setChecked(true);
//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this, HomePage.class);
//            startActivity(intent);
//            finish();
//        } else {
//            Log.i("rempwd", "不行 ");
//        }
    }
}
