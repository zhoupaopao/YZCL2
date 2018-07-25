package com.example.yzcl.mvp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.example.yzcl.adapter.SimpleTreeeAdapter;
import com.example.yzcl.adapter.TreeListViewwAdapter;
import com.example.yzcl.content.Api;
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

/**
 * Created by Lenovo on 2018/1/16.
 */


public class CustomerChooseActivity extends BaseActivity {
    private EditText search;
    private TextView title;
    private TextView back;
    //确定
    private TextView sure;
    private ListView tree_list;
    private SharedPreferences sp = null;
    BuildBean progress;
    CustomerOrganizationBeans CustomerMBean;
    SimpleTreeeAdapter adapter;
    private List<NewFileBean> mData = new ArrayList<>();

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
    }

    private void initData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String token = sp.getString("Token", null);
        Log.i("GetCustomer", token);
        params.put("token", token);
        params.setContentEncoding("UTF-8");
        client.get(Api.getCustomer, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String json = new String(bytes, "UTF-8").toString();
                    Log.i("GetCustomer", json);
                    CustomerMBean = JSONObject.parseObject(json, CustomerOrganizationBeans.class);
                    //就从第一个开始，默认最上级是1，他的parentid是0
                    //第一条信息
                    //如果是数组需要for
                    child(0, CustomerMBean.getObject().getTree().get(0));
                    if (CustomerMBean.getObject().getTree() == null) {
                        Toast.makeText(CustomerChooseActivity.this, "没有子账户", Toast.LENGTH_SHORT).show();

                    }
                     adapter = new SimpleTreeeAdapter<NewFileBean>(tree_list, CustomerChooseActivity.this, mData, 10);

                    adapter.setOnTreeNodeClickListener(new TreeListViewwAdapter.OnTreeNodeClickListener() {


                        @Override
                        public void onClick(Node3 node, int position) {
                            Log.i("Node3onClick: ", position+"");
                            Toast.makeText(CustomerChooseActivity.this, node.getUseid(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.i("tree_list", "initListener: ");
                    tree_list.setAdapter(adapter);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                progress = DialogUIUtils.showLoading(CustomerChooseActivity.this, "加载中...", false, true, true, false);
                progress.show();

            }

            @Override
            public void onFinish() {
                super.onFinish();
                DialogUIUtils.dismiss(progress);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    private void child(int parentid, CustomerOrganizationBeans.CustomerOrganizationBean.CustomerOrganization customerMBean) {
        Log.i("pid", mData.size() + 1 + "|" + parentid + "|" + customerMBean.getId());
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


            }
        });
    }
}
