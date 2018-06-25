package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.example.yzcl.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/3/19.
 */

public class NewCityListActivity extends Activity {
    private List<String> list = new ArrayList<String>();
    private List<String> listTag = new ArrayList<String>();
    private String cityTags[];
    private ListView listView1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcitylist);getData();
//        listView1 = (ListView) findViewById(R.id.listView1);
//        listView1.setAdapter(new CityListAdapter(NewCityListActivity.this, list, listTag));
//        listView1.setOnItemClickListener(this);
//        ListView listView2=(ListView)findViewById(R.id.listView2);
//        listView2.setAdapter(new MyBaseAdapter(NewCityListActivity.this,cityTags));
//        listView2.setOnItemClickListener(this);
    }
    public void getData() {
        String city_name_list[] = NewCityListActivity.this.getResources()
                .getStringArray(R.array.city_description_list);
        String city_list_tag[] = NewCityListActivity.this.getResources()
                .getStringArray(R.array.city_group_list);
        cityTags = new String[] { "热门", "A", "B", "C", "D", "E", "F", "G", "H", "J",
                "K", "L", "M", "N", "Q", "S", "T", "W", "X", "Y", "Z" };
        int listsize[] = { 0, 19, 5, 6, 9, 7, 1, 3, 6, 13, 13, 5, 8, 5, 7, 7,
                10, 6, 11, 7, 11, 9 };

        for (int j = 1; j < listsize.length; j++) {
            list.add(cityTags[j - 1]);
            listTag.add(cityTags[j - 1]);
            listsize[j] = listsize[j - 1] + listsize[j];
            for (int i = listsize[j - 1]; i < listsize[j]; i++) {
                list.add(city_name_list[i]);
                // System.out.println(city_list_tag[i]);
                listTag.add(city_list_tag[i]);
            }
        }
    }
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,
//                            long id) {
//        // TODO Auto-generated method stub
//        switch (parent.getId()) {
//            case R.id.listView1:
//                boolean  isLetter=false;
//                for(int i=0;i<cityTags.length;i++){
//                    if(cityTags[i].equals(listTag.get(position))){
//                        isLetter=true;
//                        break;
//                    }
//                }
//                if(!isLetter){
//                    Toast.makeText(this, listTag.get(position), Toast.LENGTH_SHORT).show();
//
//                }
//
//                break;
//            case R.id.listView2:
//                for(int i=0;i<listTag.size();i++){
//                    if(cityTags[position].equals(listTag.get(i))){
//                        listView1.setSelection(i);
//                        break;
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }


}

