package com.example.yzcl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.MyCarListActivity;

import java.util.ArrayList;

import library.ISlideAdapter;
import library.OnClickSlideItemListener;
import library.SlideAdapterImpl;
import library.SlideTouchView;

/**
 * Created by Lenovo on 2018/5/16.
 */

public class MyRecycleAdpter extends RecyclerView.Adapter<MyRecycleAdpter.ItemViewHolder> implements ISlideAdapter {
    private SlideAdapterImpl mSlideAdapterImpl;
    private ArrayList<String>list;
    private MyCarListActivity context;
    public MyRecycleAdpter(MyCarListActivity context, ArrayList list){
        this.list=list;
        this.context=context;
    }
    public MyRecycleAdpter() {
        mSlideAdapterImpl = new SlideAdapterImpl(){
            @Override
            public int[] getBindOnClickViewsIds() {
                return MyRecycleAdpter.this.getBindOnClickViewsIds();
            }
        };
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //使用这句，item的等比布局会失效
//        View view=View.inflate(parent.getContext(),R.layout.item_text_line,null);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_line,parent,false);
        ItemViewHolder holder =new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.text1.setText(list.get(position));
        //这样设置点击事件貌似会比较消耗性能
        holder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"test",Toast.LENGTH_SHORT).show();
                holder.button1.setText("tet");
            }
        });
        holder.ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,list.get(position),Toast.LENGTH_SHORT).show();
                list.set(position,"222");
                //不刷新整个页面，只单独刷新这一项
                notifyItemChanged(position);
            }
        });
//        bindSlidePosition(holder.mSlideTouchView, position);
    }
    public void setupRecyclerView(RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    closeAll();
                }
            });
        }
    }


//    private RecyclerView.ViewHolder newViewHolder(View view) {
//        TextView text1;
//        text1=view.findViewById(android.R.id.text1);
//        return null;
//    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void bindSlideState(View slideTouchView) {
        mSlideAdapterImpl.bindSlideState(slideTouchView);
    }

    @Override
    public void bindSlidePosition(View slideTouchView, int pos) {
        mSlideAdapterImpl.bindSlidePosition(slideTouchView, pos);
    }

    @Override
    public void setOnClickSlideItemListener(OnClickSlideItemListener listener) {
        mSlideAdapterImpl.setOnClickSlideItemListener(listener);
    }

    @Override
    public int[] getBindOnClickViewsIds() {
        return new int[0];
    }

    @Override
    public void closeAll() {
//            mSlideAdapterImpl.closeAll();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView text1;
        private Button button1;
        private LinearLayout ll1;
         SlideTouchView mSlideTouchView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            text1=itemView.findViewById(R.id.tv1);
            button1=itemView.findViewById(R.id.bt1);
            ll1=itemView.findViewById(R.id.ll1);
            mSlideTouchView = (SlideTouchView) itemView.findViewById(R.id.mSlideTouchView);
//            bindSlideState(mSlideTouchView);
        }
    }
}
