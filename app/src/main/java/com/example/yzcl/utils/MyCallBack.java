package com.example.yzcl.utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.yzcl.R;
import com.example.yzcl.adapter.PostArticleImgAdapter;
import com.example.yzcl.mvp.ui.MyApplication;

import java.util.Collections;
import java.util.List;

/**
 * Created by Lenovo on 2017/12/25.
 */

public class MyCallBack extends ItemTouchHelper.Callback {
    private int dragFlags;  //压缩后的图片
    private int swipeFlags;
    private PostArticleImgAdapter adapter;
    private List<String> images;//图片经过压缩处理
    private List<String> originImages;//图片没有经过处理，这里传这个进来是为了使原图片的顺序与拖拽顺序保持一致
    private boolean up;//手指抬起标记位
    public MyCallBack(PostArticleImgAdapter adapter, List<String> images, List<String> originImages) {
        this.adapter = adapter;
        this.images = images;
        this.originImages = originImages;
    }

    /**
     * 设置item是否处理拖拽事件和滑动事件，以及拖拽和滑动操作的方向
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //判断recycleView的布局管理数据
        if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
            //设置能拖拽的方向
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;//0则不响应事件
        }
        return makeMovementFlags(dragFlags,swipeFlags);

    }

    /**
     * 当用户从item原来的位置拖动可以拖动的item到新位置的过程中调用
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition=viewHolder.getAdapterPosition();//得到item原来的position
        int toPosition=target.getAdapterPosition();//得到目标的position
        if(toPosition==images.size()-1||images.size()-1==fromPosition){
            return true;
        }
        if(fromPosition<toPosition){
            //如果往后移动
            for(int i=fromPosition;i<toPosition;i++){
                Collections.swap(images,i,i+1);
                Collections.swap(originImages,i,i+1);
            }
        }else{
            //往前移动
            for(int i=fromPosition;i>toPosition;i--){
                Collections.swap(images,i,i-1);
                Collections.swap(originImages,i,i-1);
            }
        }
        adapter.notifyItemMoved(fromPosition,toPosition);
        return true;
    }

    /**
     * 设置是否支持长按拖拽
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    /**
     * 当用户与item的交互结束并且item也完成了动画时调用
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        adapter.notifyDataSetChanged();
        initData();
    }

    /**
     * 重置
     */
    private void initData() {
        if(dragListener!=null){
            dragListener.deleteState(false);
            dragListener.dragState(false);
        }
    }
    public int getPixelById(int dimensionId){
        return MyApplication.getInstance().getResources().getDimensionPixelSize(dimensionId);
    }

    /**
     * 自定义拖动与滑动交互
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(null==dragListener){
            return;
        }
        if(dY>=(recyclerView.getHeight()//整个列表的高度
                -viewHolder.itemView.getBottom()//item底部距离recycleView顶部高度
                -getPixelById(R.dimen.article_post_delete))){//拖到删除处
            dragListener.deleteState(true);
            if(up){
                //在删除处放手，就删除item
                viewHolder.itemView.setVisibility(View.INVISIBLE);//先设置不可见，如果不设置的话，会看到viewHolder返回到原位置时才消失，因为remove会在viewHolder动画执行完成后才将viewHoldere删除
                originImages.remove(viewHolder.getAdapterPosition());
                images.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                initData();
                return;
            }
        }else{
            //没有到删除处
            if(View.INVISIBLE==viewHolder.itemView.getVisibility()){
                dragListener.deleteState(false);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    /**
     * 当长按选中item的时候（拖拽开始的时候）调用
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(ItemTouchHelper.ACTION_STATE_DRAG==actionState&&dragListener!=null){
            dragListener.dragState(true);

        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 设置手指离开后ViewHolder的动画时间，在用户手指离开后调用
     * @param recyclerView
     * @param animationType
     * @param animateDx
     * @param animateDy
     * @return
     */
    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        up=true;
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    /**
     * 应该是拖拽监听
     */
    private DragListener dragListener;
  void setDragListener(DragListener dragListener){
        this.dragListener=dragListener;
    }
    interface DragListener{
        /**
         * 用户是否将item拖拽到删除处，根据状态改变颜色
         * @param delete
         */
        void deleteState(boolean delete);

        /**
         * 是否处于拖拽状态
         * @param start
         */
        void dragState(boolean start);
    }
}
