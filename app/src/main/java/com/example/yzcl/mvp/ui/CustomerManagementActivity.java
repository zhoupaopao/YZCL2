package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tree.Node;
import com.example.yzcl.R;
import com.example.yzcl.adapter.SimpleTreeAdapter;
import com.example.yzcl.adapter.TreeListViewAdapter;
import com.example.yzcl.mvp.model.bean.ChildAccountBean;
import com.example.yzcl.mvp.model.bean.FileBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/11/29.
 */

public class CustomerManagementActivity extends Activity {
    //账号管理
    private ListView tree_list;
    private List<FileBean>mDatas=new ArrayList<FileBean>();
    SimpleTreeAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_management);
        initview();
        initdata();
    }



    private void initview() {
        tree_list=findViewById(R.id.tree_list);
    }
    private void initdata() {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("userid","3ab403f00d324cb9807dfbb7d8bd63ee");
        params.put("tokenstring","erATMAHTJWA5sH78eZBYhyNsk1IjdEV1hNYFzEXWCyzyuRf+tMNmhgnCDGXyAtJ1FQHai3McsvJ2K+sPbHUyesVK6i30fXNi9WbSu9XAKxbpd3fFUYPwO+EAGRGobS7BBMffS8T9BxNVsKPrHsV/6FqgJM62rP0vd0gaUZL0I80=");
        params.setContentEncoding("UTF-8");
        client.post("http://m1api.chetxt.com:8083/Customer.asmx/GetRecursiveUserByUserID", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                try {
                    String json=new String(bytes,"UTF-8");
                    Log.i("111111->", json);
                    ChildAccountBean childAccountBean= com.alibaba.fastjson.JSONObject.parseObject(json.toString(),ChildAccountBean.class);
//                    if(childAccountBean.getResult())
                    child(0,childAccountBean);
                    if(childAccountBean.getChildren()==null){
                        Toast.makeText(CustomerManagementActivity.this,"没有子账户",Toast.LENGTH_SHORT).show();

                    }
                    mAdapter = new SimpleTreeAdapter<FileBean>(tree_list, CustomerManagementActivity.this, mDatas, 10);
                    mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                        @Override
                        public void onClick(Node node, int position) {
                            Toast.makeText(CustomerManagementActivity.this,node.getUseid(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    tree_list.setAdapter(mAdapter);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    private void child(int parentId,ChildAccountBean childAccountBean) {
        mDatas.add(new FileBean(mDatas.size()+1,parentId,childAccountBean.getCustomerName(),
                childAccountBean.getAleaQty(),childAccountBean.getTempQty(),childAccountBean.getUserID()));//将当前节点（父节点）加入树
        parentId=mDatas.size();
        List<ChildAccountBean>childs=childAccountBean.getChildren();
        if(childs!=null){
            //代表他下面有子项
            for(int i=0;i<childs.size();i++){
                child(parentId,childs.get(i));
            }
        }
    }
}
