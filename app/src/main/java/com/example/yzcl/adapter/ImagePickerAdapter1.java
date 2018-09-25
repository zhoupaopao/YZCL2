package com.example.yzcl.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.AddCarActivity;
import com.example.yzcl.mvp.ui.CarDetailActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：ikkong （ikkong@163.com），修改 jeasonlzy（廖子尧）
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：微信图片选择的Adapter, 感谢 ikkong 的提交
 * ================================================
 */
public class ImagePickerAdapter1 extends RecyclerView.Adapter<ImagePickerAdapter1.SelectedPicViewHolder> {
    private int maxImgCount;
    private CarDetailActivity mContext;
    private List<String> mData;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener listener;
    private boolean isAdded;   //是否额外添加了最后一个图片
    private int nowpos=0;
    private boolean canedit=false;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setImages(List<String> data) {
        mData = new ArrayList<>(data);
        if (getItemCount() < maxImgCount) {
            mData.add(new String());
            isAdded = true;
        } else {
            isAdded = false;
        }
        Log.i("setImages", "setImages: ");
        notifyDataSetChanged();
    }

    public List<String> getImages() {
        //由于图片未选满时，最后一张显示添加图片，因此这个方法返回真正的已选图片
        if (isAdded) return new ArrayList<>(mData.subList(0, mData.size() - 1));
        else return mData;
    }

    public ImagePickerAdapter1(CarDetailActivity mContext, List<String> data, int maxImgCount) {
        this.mContext = mContext;
        this.maxImgCount = maxImgCount;
        this.mInflater = LayoutInflater.from(mContext);
        nowpos=0;
        setImages(data);
        checkqx();
    }
    private void checkqx() {
        String list_Jurisdiction = mContext.getSharedPreferences("YZCL",mContext.MODE_PRIVATE).getString("list_Jurisdiction", "");
        String[] list_jur = list_Jurisdiction.split(",");
        for (int i = 0; i < list_jur.length; i++) {
            if (list_jur[i].equals("203")) {
                canedit = true;
            }
        }
    }

    @Override
    public SelectedPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedPicViewHolder(mInflater.inflate(R.layout.list_item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedPicViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedPicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv_img;
        private int clickPosition;

        public SelectedPicViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
        }

        public void bind(final int position) {
            //设置条目的点击事件
            itemView.setOnClickListener(this);
            //根据条目位置设置图片
            String item = mData.get(position);
            if (isAdded && position == getItemCount() - 1) {
                iv_img.setImageResource(R.drawable.selector_image_add);
                if(!canedit){
                    iv_img.setVisibility(View.GONE);
                }
                clickPosition = AddCarActivity.IMAGE_ITEM_ADD;
            } else {
                Glide.with(mContext).load(item).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.i("bind: ", position+"");
                        nowpos=nowpos+1;
                        int allnum=getImages().size();
                        if(allnum==nowpos){
                            Log.i("bind321123213: ", position+"");
                            mContext.dismisdia();
                        }

                        return false;
                    }
                }).into(iv_img);
//                ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, item, iv_img, 0, 0);
//                Log.i("bind: ", position+"");
                clickPosition = position;
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, clickPosition);
        }
    }
}