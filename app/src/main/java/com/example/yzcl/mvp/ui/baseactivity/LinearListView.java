package com.example.yzcl.mvp.ui.baseactivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.LinkedList;
import java.util.List;

/**
 * 能够很好解决listview上面有个头并且随之滚动
 * 只需要在整个滚动的布局外面嵌套一层这个
 */

public class LinearListView extends LinearLayout {

	private List<View> mViewList;
	private int mPosition;
	
	private LinearLayout mHeadLayout;
	private LinearLayout mFooterLayout;
	private LinearLayout mContentLayout;
	private ScrollView mScrollView;
	private AbsListView.LayoutParams mListLayoutParams;
	private ViewGroup.LayoutParams mViewGroupLayoutParams;
	
	public LinearListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public LinearListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LinearListView(Context context) {
		this(context, null);
	}
	
	private void init() {
		if (getOrientation() == HORIZONTAL) {
			throw new IllegalArgumentException("the ScrollListView only support VERTICAL");
		}
		mViewList = new LinkedList<View>();
		mListLayoutParams = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mViewGroupLayoutParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	@Override
	public void setOrientation(int orientation) {
		if (orientation == VERTICAL)
			super.setOrientation(orientation);
		else
			throw new IllegalArgumentException("the ScrollListView only support VERTICAL");
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int count = getChildCount();
		if (!(count == 1 && getChildAt(0) == mScrollView 
				|| count == 2 && getChildAt(0) == mScrollView && getChildAt(1) instanceof ListView)) {
			View curView = null;
			mPosition = count;
			/**
			 * 找出childView中的ListView的索引位置，如果不存在，则position的数值为count
			 */
			for (int i = 0; i < count; i++) {
				curView = getChildAt(i);
				if (curView instanceof ListView) {
					/**
					 * 如果position不等于0的时候还发现有其余的childView属于ListView的实例
					 * 那么则说明该布局存在多个ListView，会造成布局异常，应该直接抛错
					 */
					if (mPosition < count)
						throw new IllegalStateException("the ScrollListView can't contain more than two ListView");
					mPosition = i;
				}
				mViewList.add(curView);
			}
			removeAllViews();
		}
		/**
		 * 处理后的情况，直接退出view处理
		 */
		if (count == 1 && getChildAt(0) == mScrollView) {
			return;
		}
		if (count == 2 && getChildAt(0) == mScrollView && getChildAt(1) instanceof ListView
				&& ( getListViewVisible((ListView)getChildAt(1)) && mScrollView.getVisibility() != View.VISIBLE
				|| !getListViewVisible((ListView)getChildAt(1)) && mScrollView.getVisibility() == View.VISIBLE) ) {
			return;
		}
		
		ListView listView = null;
		if (mPosition < mViewList.size()) {
			listView = (ListView) mViewList.get(mPosition);
		}
		checkHeadAndFooterView(listView);
		
		if (listView == null || listView.getVisibility() != View.VISIBLE || getCurrentAdapter(listView) == null) {
			mScrollView.setVisibility(View.VISIBLE);
			mContentLayout.removeAllViews();
			mHeadLayout.removeAllViews();
			mFooterLayout.removeAllViews();
			for (View v : mViewList) {
				if (!(v instanceof ListView))
					mContentLayout.addView(v);
			}
		}
		else {
			mScrollView.setVisibility(View.GONE);
			mContentLayout.removeAllViews();
			mHeadLayout.removeAllViews();
			mFooterLayout.removeAllViews();
			for (int i = 0; i < mPosition; i++) {
				if (mViewList.get(i) != mHeadLayout)
					mHeadLayout.addView(mViewList.get(i));
			}
			mHeadLayout.requestLayout();
			for (int i = mViewList.size() - 1; i > mPosition; i--) {
				if (mViewList.get(i) != mFooterLayout)
					mFooterLayout.addView(mViewList.get(i), 0);
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private void checkHeadAndFooterView(ListView listView) {
		if (mHeadLayout == null) {
			mHeadLayout = new LinearLayout(getContext());
			mHeadLayout.setOrientation(LinearLayout.VERTICAL);
			mHeadLayout.setLayoutParams(mListLayoutParams);
		}
		if (mFooterLayout == null) {
			mFooterLayout = new LinearLayout(getContext());
			mFooterLayout.setOrientation(LinearLayout.VERTICAL);
			mFooterLayout.setLayoutParams(mListLayoutParams);
		}
		if (mContentLayout == null) {
			mContentLayout = new LinearLayout(getContext());
			mContentLayout.setOrientation(LinearLayout.VERTICAL);
			mContentLayout.setLayoutParams(mViewGroupLayoutParams);
		}
		if (mScrollView == null) {
			mScrollView = new ScrollView(getContext());
			mScrollView.setLayoutParams(mViewGroupLayoutParams);
		}
		if (listView != null) {
			ListAdapter adapter = null;
			if (listView.getAdapter() != null && !(listView.getAdapter() instanceof HeaderViewListAdapter)) {
				adapter = listView.getAdapter();
				listView.setAdapter(null);
			}
			listView.removeHeaderView(mHeadLayout);
			listView.removeFooterView(mFooterLayout);
			listView.addHeaderView(mHeadLayout);
			listView.addFooterView(mFooterLayout);
			if (adapter != null)
				listView.setAdapter(adapter);
		}
		if (mContentLayout.getParent() == null) {
			addView(mScrollView);
			mScrollView.addView(mContentLayout);
		}
		if (listView != null && listView.getParent() == null)
			addView(listView);
	}
	
	
	private boolean getListViewVisible(ListView listView) {
		boolean result = listView.getVisibility() == View.VISIBLE && getCurrentAdapter(listView) != null;
		return result;
	}
	
	
	private ListAdapter getCurrentAdapter(ListView listView) {
		ListAdapter adapter = listView.getAdapter();
		if (adapter != null && (adapter instanceof HeaderViewListAdapter))
			adapter = ((HeaderViewListAdapter)adapter).getWrappedAdapter();
		return adapter;
	}
}
