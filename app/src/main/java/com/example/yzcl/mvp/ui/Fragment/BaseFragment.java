package com.example.yzcl.mvp.ui.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;


import com.example.yzcl.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseFragment extends Fragment {
    protected View mMainView;
    protected static ArrayList<Map<String, Object>> mlistItems;
    protected Context mContext;
    protected ListView mListView;
    static {
        mlistItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 20; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", "name#" + i);
            map.put("sex", i % 2 == 0 ? "male" : "female");
            mlistItems.add(map);
        }
    }

    public BaseFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_one, container, false);
        mListView = (ListView) mMainView.findViewById(R.id.list);
        SimpleAdapter adapter = new SimpleAdapter(mContext, mlistItems,
                R.layout.listview_item, new String[] {
                        "name", "sex"
                }, new int[] {
                        R.id.name, R.id.download
                });
        mListView.setAdapter(adapter);
        return mMainView;
    }


    public void setMarginTop(int page){
        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParam.setMargins(0, page, 0, 0);
        mListView.setLayoutParams(layoutParam);
        mListView.invalidate();
    }
}
