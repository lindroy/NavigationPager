package com.lindroid.navigationpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lindroid on 2017/3/20.
 */

public class NavigationAdapter extends PagerAdapter {
    @Bind(R.id.iv_icon1)
    ImageView ivIcon1;
    @Bind(R.id.tv_name1)
    TextView tvName1;
    @Bind(R.id.ll_item1)
    LinearLayout llItem1;
    @Bind(R.id.iv_icon2)
    ImageView ivIcon2;
    @Bind(R.id.tv_name2)
    TextView tvName2;
    @Bind(R.id.ll_item2)
    LinearLayout llItem2;
    @Bind(R.id.iv_icon3)
    ImageView ivIcon3;
    @Bind(R.id.tv_name3)
    TextView tvName3;
    @Bind(R.id.ll_item3)
    LinearLayout llItem3;
    @Bind(R.id.iv_icon4)
    ImageView ivIcon4;
    @Bind(R.id.tv_name4)
    TextView tvName4;
    @Bind(R.id.ll_item4)
    LinearLayout llItem4;
    @Bind(R.id.iv_icon5)
    ImageView ivIcon5;
    @Bind(R.id.tv_name5)
    TextView tvName5;
    @Bind(R.id.ll_item5)
    LinearLayout llItem5;
    @Bind(R.id.iv_icon6)
    ImageView ivIcon6;
    @Bind(R.id.tv_name6)
    TextView tvName6;
    @Bind(R.id.ll_item6)
    LinearLayout llItem6;
    @Bind(R.id.iv_icon7)
    ImageView ivIcon7;
    @Bind(R.id.tv_name7)
    TextView tvName7;
    @Bind(R.id.ll_item7)
    LinearLayout llItem7;
    @Bind(R.id.iv_icon8)
    ImageView ivIcon8;
    @Bind(R.id.tv_name8)
    TextView tvName8;
    @Bind(R.id.ll_item8)
    LinearLayout llItem8;
    @Bind(R.id.ll_line2)
    LinearLayout llLine2;

    private Context context;
    private List<NavigationItemBean> itemBeanList;
    private List<View> itemViews;
    int pages; //总页数
    private View pageView;
    private View.OnClickListener onClickListener;

    public NavigationAdapter(Context context, List<NavigationItemBean> itemBeanList, View.OnClickListener onClickListener) {
        this.context = context;
        this.itemBeanList = itemBeanList;
        this.onClickListener = onClickListener;
        itemViews = new ArrayList<>();
        initView(itemBeanList);
    }

    /**
     * 初始化ViewPager的页面布局
     * @param list
     */
    private void initView(List<NavigationItemBean> list) {
        itemViews.clear();
        //计算ViewPager的页数
        pages = list.size() / 9;
        pages = pages + 1;
        for (int i = 0; i < pages; i++) {
            pageView = View.inflate(context, R.layout.page_navigation, null);
            ButterKnife.bind(this, pageView);
            setPagerViewData(i);
            itemViews.add(pageView);
            initListener();
        }
    }

    private void initListener() {
        llItem1.setOnClickListener(onClickListener);
        llItem2.setOnClickListener(onClickListener);
        llItem3.setOnClickListener(onClickListener);
        llItem4.setOnClickListener(onClickListener);
        llItem5.setOnClickListener(onClickListener);
        llItem6.setOnClickListener(onClickListener);
        llItem7.setOnClickListener(onClickListener);
        llItem8.setOnClickListener(onClickListener);
    }

    /**
     * 根据页码来填充数据
     * @param pageNum
     */
    public void setPagerViewData(int pageNum) {
        if (itemBeanList.size() > pageNum * 8 + 0) {
            llItem1.setVisibility(View.VISIBLE);
            tvName1.setText(itemBeanList.get(pageNum * 8 + 0).getIconName());
            ivIcon1.setBackgroundResource(itemBeanList.get(pageNum * 8 + 0).getIconID());
            setIconAlpha(itemBeanList.get(pageNum * 8 + 0).isSelected(), ivIcon1);
        } else {
            llItem1.setVisibility(View.INVISIBLE);
        }

        if (itemBeanList.size() > pageNum * 8 + 1) {
            llItem2.setVisibility(View.VISIBLE);
            tvName2.setText(itemBeanList.get(pageNum * 8 + 1).getIconName());
            ivIcon2.setBackgroundResource(itemBeanList.get(pageNum * 8 + 1).getIconID());
            setIconAlpha(itemBeanList.get(pageNum * 8 + 1).isSelected(), ivIcon2);
        } else {
            llItem2.setVisibility(View.INVISIBLE);
        }

        if (itemBeanList.size() > pageNum * 8 + 2) {
            llItem3.setVisibility(View.VISIBLE);
            tvName3.setText(itemBeanList.get(pageNum * 8 + 2).getIconName());
            ivIcon3.setBackgroundResource(itemBeanList.get(pageNum * 8 + 2).getIconID());
            setIconAlpha(itemBeanList.get(pageNum * 8 + 2).isSelected(), ivIcon3);
        } else {
            llItem3.setVisibility(View.INVISIBLE);
        }

        if (itemBeanList.size() > pageNum * 8 + 3) {
            llItem4.setVisibility(View.VISIBLE);
            tvName4.setText(itemBeanList.get(pageNum * 8 + 3).getIconName());
            ivIcon4.setBackgroundResource(itemBeanList.get(pageNum * 8 + 3).getIconID());
            setIconAlpha(itemBeanList.get(pageNum * 8 + 3).isSelected(), ivIcon4);
        } else {
            llItem4.setVisibility(View.INVISIBLE);
        }

        if (itemBeanList.size() > pageNum * 8 + 4) {
            llItem5.setVisibility(View.VISIBLE);
            tvName5.setText(itemBeanList.get(pageNum * 8 + 4).getIconName());
            ivIcon5.setBackgroundResource(itemBeanList.get(pageNum * 8 + 4).getIconID());
            setIconAlpha(itemBeanList.get(pageNum * 8 + 4).isSelected(), ivIcon5);
        } else {
            llItem5.setVisibility(View.INVISIBLE);
            llLine2.setVisibility(View.GONE); //t图标数不大于4个时让第二行消失
        }

        if (itemBeanList.size() > pageNum * 8 + 5) {
            llItem6.setVisibility(View.VISIBLE);
            tvName6.setText(itemBeanList.get(pageNum * 8 + 5).getIconName());
            ivIcon6.setBackgroundResource(itemBeanList.get(pageNum * 8 + 5).getIconID());
            setIconAlpha(itemBeanList.get(pageNum * 8 + 5).isSelected(), ivIcon6);
        } else {
            llItem6.setVisibility(View.INVISIBLE);
        }

        if (itemBeanList.size() > pageNum * 8 + 6) {
            llItem7.setVisibility(View.VISIBLE);
            tvName7.setText(itemBeanList.get(pageNum * 8 + 6).getIconName());
            ivIcon7.setBackgroundResource(itemBeanList.get(pageNum * 8 + 6).getIconID());
            setIconAlpha(itemBeanList.get(pageNum * 8 + 6).isSelected(), ivIcon7);
        } else {
            llItem7.setVisibility(View.INVISIBLE);
        }

        if (itemBeanList.size() > pageNum * 8 + 7) {
            llItem8.setVisibility(View.VISIBLE);
            tvName8.setText(itemBeanList.get(pageNum * 8 + 7).getIconName());
            ivIcon8.setBackgroundResource(itemBeanList.get(pageNum * 8 + 7).getIconID());
            setIconAlpha(itemBeanList.get(pageNum * 8 + 7).isSelected(), ivIcon8);
        } else {
            llItem8.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置图片的透明度
     * @param isSelected
     * @param imageView
     */
    public void setIconAlpha(boolean isSelected, ImageView imageView) {
        if (isSelected) {
            imageView.setAlpha(1.0f);
        } else {
            imageView.setAlpha(0.4f);
        }
    }

    @Override
    public int getCount() {
        return pages;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(itemViews.get(position));
        return itemViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public void refresh(List<NavigationItemBean> itemBeanList){
        initView(itemBeanList);
        notifyDataSetChanged();
    }

    /**
     * 使Adapter能够刷新布局
     */
    private int mChildCount = 0;
    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }
    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
