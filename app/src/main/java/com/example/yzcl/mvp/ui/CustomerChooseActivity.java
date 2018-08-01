package com.example.yzcl.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.tree.Node3;
import com.example.yzcl.R;
import com.example.yzcl.adapter.CustomerListAdapter;
import com.example.yzcl.adapter.SimpleTreeeAdapter;
import com.example.yzcl.adapter.TreeListViewwAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CustomerListBeans;
import com.example.yzcl.mvp.model.bean.CustomerManagerBean;
import com.example.yzcl.mvp.model.bean.CustomerOrganizationBeans;
import com.example.yzcl.mvp.model.bean.NewFileBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lzy.imagepicker.ImagePicker;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/1/16.
 */


public class CustomerChooseActivity extends BaseActivity {
    private EditText search;
    private TextView title;
    private TextView back;
    private boolean istree=true;
    //确定
    private TextView sure;
    private ListView tree_list;
    private SharedPreferences sp = null;
    BuildBean progress;
    CustomerOrganizationBeans CustomerMBean;
    SimpleTreeeAdapter adapter;
    CustomerListAdapter customerListAdapter;
    private EditText et_search;
    private String TAG="CustomerChooseActivity";
    private List<NewFileBean> mData = new ArrayList<>();
    private BuildBean dialog;
    private Boolean isfirst=true;
    private CustomerListBeans customerListBeans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_choose);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        try {
            initListener();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        title = findViewById(R.id.title);
        back = findViewById(R.id.textview1);
        sure = findViewById(R.id.textview2);
        tree_list = findViewById(R.id.tree_list);
        sp = getSharedPreferences("YZCL", MODE_PRIVATE);
        et_search=findViewById(R.id.et_search);
    }

    private void initData() {
        isfirst=true;
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
        String token = sp.getString("Token", null);
//        Log.i("GetCustomer", token);
//        params.put("token", token);
//        params.setContentEncoding("UTF-8");
        cn.finalteam.okhttpfinal.RequestParams params1=new cn.finalteam.okhttpfinal.RequestParams();
        params1.addFormDataPart("token",token);
        HttpRequest.get(Api.getCustomer,params1,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);

                CustomerMBean = JSONObject.parseObject(jsonObject.toString(), CustomerOrganizationBeans.class);
                //就从第一个开始，默认最上级是1，他的parentid是0
                //第一条信息
                //如果是数组需要for
                child(0, CustomerMBean.getObject().getTree().get(0));
                if (CustomerMBean.getObject().getTree() == null) {
                    Toast.makeText(CustomerChooseActivity.this, "没有子账户", Toast.LENGTH_SHORT).show();

                }
                try {
                    adapter = new SimpleTreeeAdapter<NewFileBean>(tree_list, CustomerChooseActivity.this, mData, 1);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                adapter.setOnTreeNodeClickListener(new TreeListViewwAdapter.OnTreeNodeClickListener() {


                    @Override
                    public void onClick(Node3 node, int position) {
                        Log.i("Node3onClick: ", position+"");
//                        Toast.makeText(CustomerChooseActivity.this, node.getUseid(), Toast.LENGTH_SHORT).show();
                    }
                });
                Log.i("tree_list", "initListener: ");
                tree_list.setAdapter(adapter);
                DialogUIUtils.dismiss(progress);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                DialogUIUtils.dismiss(progress);
            }

            @Override
            public void onStart() {
                super.onStart();
                progress = DialogUIUtils.showLoading(CustomerChooseActivity.this, "加载中...", false, true, true, false);
                progress.show();
            }
        });
//        client.get(Api.getCustomer, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                try {
//                    String json = new String(bytes, "UTF-8").toString();
//                    Log.i("GetCustomer", json);
//                    CustomerMBean = JSONObject.parseObject(json, CustomerOrganizationBeans.class);
//                    //就从第一个开始，默认最上级是1，他的parentid是0
//                    //第一条信息
//                    //如果是数组需要for
//                    child(0, CustomerMBean.getObject().getTree().get(0));
//                    if (CustomerMBean.getObject().getTree() == null) {
//                        Toast.makeText(CustomerChooseActivity.this, "没有子账户", Toast.LENGTH_SHORT).show();
//
//                    }
//                     adapter = new SimpleTreeeAdapter<NewFileBean>(tree_list, CustomerChooseActivity.this, mData, 1);
//
//                    adapter.setOnTreeNodeClickListener(new TreeListViewwAdapter.OnTreeNodeClickListener() {
//
//
//                        @Override
//                        public void onClick(Node3 node, int position) {
//                            Log.i("Node3onClick: ", position+"");
//                            Toast.makeText(CustomerChooseActivity.this, node.getUseid(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    Log.i("tree_list", "initListener: ");
//                    tree_list.setAdapter(adapter);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }

//            @Override
//            public void onStart() {
//                super.onStart();
//                progress = DialogUIUtils.showLoading(CustomerChooseActivity.this, "加载中...", false, true, true, false);
//                progress.show();
//
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                DialogUIUtils.dismiss(progress);
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//
//            }
//        });
    }

    private void child(int parentid, CustomerOrganizationBeans.CustomerOrganizationBean.CustomerOrganization customerMBean) {
        Log.i("pid", mData.size() + 1 + "|" + parentid + "|" + customerMBean.getId());
        if(isfirst){
            isfirst=false;
            mData.clear();
        }else{

        }
        mData.add(new NewFileBean(mData.size() + 1, parentid, customerMBean.getGroup_name(), customerMBean.getId()));
        List<CustomerOrganizationBeans.CustomerOrganizationBean.CustomerOrganization> ChildCustomer = customerMBean.getChildCustomerModel();
        int pid = mData.size();
        if (ChildCustomer != null) {
            //他下面有子项
            for (int i = 0; i < ChildCustomer.size(); i++) {
                //这里是个大循环，里面有很多个小循环，如果子项里面有东西，那么pid就是上个上个的id就是size
                child(pid, ChildCustomer.get(i));
            }
        }
//        Log.i("mData", mData.toString());

    }


    private void initListener() throws IllegalAccessException {
        title.setText("选择客户");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Log.i("tree_list", mData.toString());
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先判断是否是树状
                if(istree){
                    //获取所有的id
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb1 = new StringBuilder();
                    //获取排序过的nodes
                    //如果不需要刻意直接用 mDatas既可
                    final List<Node3> allNodes = adapter.getAllNodes();
                    for (int i = 0; i < allNodes.size(); i++) {
                        if (allNodes.get(i).isChecked()){
                            sb.append(allNodes.get(i).getUseid()+",");
                            sb1.append(allNodes.get(i).getName()+",");
                        }
                    }
                    String strNodesName = sb.toString();
                    if (!TextUtils.isEmpty(strNodesName)){
                        //返回到上个界面，请求数据
                        Intent data=new Intent();
                        data.putExtra("ids",strNodesName);
                        data.putExtra("names",sb1.toString());
                        setResult(10, data);
                        finish();
//                    Toast.makeText(CustomerChooseActivity.this, strNodesName.substring(0, strNodesName.length()-1),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CustomerChooseActivity.this, "请选择至少一个客户",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb1 = new StringBuilder();
                    List<CustomerListBeans.CustomerList> nowlist=CustomerListAdapter.getlist();
                    for(int i=0;i<nowlist.size();i++){
                        if(nowlist.get(i).getIschec()){
                            //选中的话添加
                            sb.append(nowlist.get(i).getId()+",");
                            sb1.append(nowlist.get(i).getGroup_name()+",");
                        }
                    }
                    String strNodesName = sb.toString();
                    if (!TextUtils.isEmpty(strNodesName)){
                        //返回到上个界面，请求数据
                        Intent data=new Intent();
                        data.putExtra("ids",strNodesName);
                        data.putExtra("names",sb1.toString());
                        setResult(10, data);
                        finish();
//                    Toast.makeText(CustomerChooseActivity.this, strNodesName.substring(0, strNodesName.length()-1),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CustomerChooseActivity.this, "请选择至少一个客户",Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) et_search.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(CustomerChooseActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    // 搜索，进行自己要的操作...
                    if(et_search.getText().toString().trim().equals("")){
                        //如果是空的话，就显示树
                        istree=true;
//                        Toast.makeText(CustomerChooseActivity.this,"搜索字符不能为空",Toast.LENGTH_SHORT).show();
                        initData();
                    }else{
                        //不是空的话显示列表
                        if(!Constant.isNetworkConnected(CustomerChooseActivity.this)) {
                            //判断网络是否可用
                            Toast.makeText(CustomerChooseActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                        }else{
                            istree=false;
                            search_list(et_search.getText().toString().trim());
                        }

                    }

                    return true;
                }
                return false;
            }
        });
    }

    private void search_list(String trim) {
        cn.finalteam.okhttpfinal.RequestParams params=new cn.finalteam.okhttpfinal.RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("group_name",trim);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queCustMerBySearch+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                customerListBeans=JSONObject.parseObject(jsonObject.toString(),CustomerListBeans.class);
                if(customerListBeans.isSuccess()){
                    customerListAdapter=new CustomerListAdapter(CustomerChooseActivity.this,customerListBeans.getList());
                    tree_list.setAdapter(customerListAdapter);
                }else{
                    Toast.makeText(CustomerChooseActivity.this,customerListBeans.getMessage(),Toast.LENGTH_SHORT).show();
                }
                dialog.dialog.dismiss();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(CustomerChooseActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                dialog.dialog.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(CustomerChooseActivity.this,"加载中...",true,true,false,true);
                dialog.show();
            }
        });
    }
}
