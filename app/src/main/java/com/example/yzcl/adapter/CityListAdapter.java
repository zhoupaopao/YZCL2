package com.example.yzcl.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yzcl.R;

import java.util.List;

/**
 * Created by Lenovo on 2018/3/19.
 */

public class CityListAdapter extends ArrayAdapter<String> {
    private List<String> listTag = null;
    public CityListAdapter(Context context, List<String> objects, List<String> tags) {
        super(context, 0, objects);
        this.listTag = tags;
    }

    @Override
    public boolean isEnabled(int position) {
        if(listTag.contains(getItem(position))){
            return false;
        }
        return super.isEnabled(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
//        if(listTag.contains(getItem(position))){
//            view = LayoutInflater.from(getContext()).inflate(R.layout.citylist_tag, null);
//        }else{
//            view = LayoutInflater.from(getContext()).inflate(R.layout.citylist_item, null);
//        }
//        TextView textView = (TextView) view.findViewById(R.id.group_list_item_text);
//        textView.setText(getItem(position));
        return view;
    }
}
