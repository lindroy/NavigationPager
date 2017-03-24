package com.lindroid.navigationpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.vp_navigation)
    CustomViewPager vpNavigation;
    @Bind(R.id.ll_dots)
    LinearLayout llDots;
    private NavigationAdapter adapter;
    private List<NavigationItemBean> itemBeanList = new ArrayList<>();
    private List<ImageView> dots = new ArrayList<>();

    //图标id数组
    private int[] icons = {R.mipmap.phone,
            R.mipmap.browser,
            R.mipmap.messages,
            R.mipmap.contacts,
            R.mipmap.camera,
            R.mipmap.gallery,
            R.mipmap.calendar,
            R.mipmap.calculator,
            R.mipmap.settings,
            R.mipmap.mail,
            R.mipmap.maps,
            R.mipmap.music,
            R.mipmap.movie};

    //图标名称数组
    private String[] names = {"电话",
            "浏览器",
            "信息",
            "联系人",
            "照相",
            "图库",
            "日历",
            "计算器",
            "设置",
            "邮箱",
            "地图",
            "音乐",
            "电影"};

//    //图标id数组
//    private int[] icons = {R.mipmap.phone,
//            R.mipmap.browser,
//            R.mipmap.messages,
//            R.mipmap.contacts,
//    };

//    //图标名称数组
//    private String[] names = {"电话",
//            "浏览器",
//            "信息",
//            "联系人"
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 绘制导航点
     */
    private void initDot() {
        for (int i = 0; i < (itemBeanList.size() / 9 + 1); i++) {
            Log.e("Tag", itemBeanList.size() + "");
            ImageView imageView = new ImageView(this);
            //创建一个ImageView来放置圆点，并确定ImageView的宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20, 20);
            params.leftMargin = 10;
            params.rightMargin = 10;
            params.topMargin = 5;
            params.bottomMargin = 10;
            imageView.setLayoutParams(params);
            dots.add(imageView);
            //由于刚进去引导页时出现的是第一个引导页，故此时的导航点设置为红色
            if (i == 0) {
                dots.get(i).setBackgroundResource(R.drawable.dot_green);
            } else {
                dots.get(i).setBackgroundResource(R.drawable.dot_gray);
            }
            llDots.addView(imageView);
        }
    }

    private void initData() {
        for (int i = 0; i < icons.length; i++) {
            itemBeanList.add(new NavigationItemBean(icons[i], names[i]));
        }
        adapter = new NavigationAdapter(this, itemBeanList, this);
        vpNavigation.setAdapter(adapter);
        if (itemBeanList.size() > 8) {
            initDot();
        }
        /**
         * 滑动监听事件
         */
        vpNavigation.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("Tag", "position=" + position);
                for (int i = 0; i < dots.size(); i++) {
                    if (i == position) {
                        dots.get(i).setBackgroundResource(R.drawable.dot_green);
                    } else {
                        dots.get(i).setBackgroundResource(R.drawable.dot_gray);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int pageNum = vpNavigation.getCurrentItem();
        Log.e("Tag", "viewPager.getCurrentItem()=" + vpNavigation.getCurrentItem());
        switch (v.getId()) {
            case R.id.ll_item1:
                Toast.makeText(this, itemBeanList.get(pageNum * 8 + 0).getIconName(), Toast.LENGTH_SHORT).show();
                if (itemBeanList.get(pageNum * 8 + 0).isSelected()) {
                    initSelected();
                    itemBeanList.get(pageNum * 8 + 0).setSelected(false);
                } else {
                    initSelected();
                    itemBeanList.get(pageNum * 8 + 0).setSelected(true);
                }
                adapter.refresh(itemBeanList);
                break;
            case R.id.ll_item2:
                Toast.makeText(this, itemBeanList.get(pageNum * 8 + 1).getIconName(), Toast.LENGTH_SHORT).show();
                if (itemBeanList.get(pageNum * 8 + 1).isSelected()) {
                    initSelected();
                    itemBeanList.get(pageNum * 8 + 1).setSelected(false);
                } else {
                    initSelected();
                    itemBeanList.get(pageNum * 8 + 1).setSelected(true);
                }
                adapter.refresh(itemBeanList);
                break;
            case R.id.ll_item3:
                Toast.makeText(this, itemBeanList.get(pageNum * 8 + 2).getIconName(), Toast.LENGTH_SHORT).show();
                initSelected();
                itemBeanList.get(pageNum * 8 + 2).setSelected(true);
                adapter.refresh(itemBeanList);
                break;
            case R.id.ll_item4:
                Toast.makeText(this, itemBeanList.get(pageNum * 8 + 3).getIconName(), Toast.LENGTH_SHORT).show();
                initSelected();
                itemBeanList.get(pageNum * 8 + 3).setSelected(true);
                adapter.refresh(itemBeanList);
                break;
            case R.id.ll_item5:
                Toast.makeText(this, itemBeanList.get(pageNum * 8 + 4).getIconName(), Toast.LENGTH_SHORT).show();
                initSelected();
                itemBeanList.get(pageNum * 8 + 4).setSelected(true);
                adapter.refresh(itemBeanList);
                break;
            case R.id.ll_item6:
                Toast.makeText(this, itemBeanList.get(pageNum * 8 + 5).getIconName(), Toast.LENGTH_SHORT).show();
                initSelected();
                itemBeanList.get(pageNum * 8 + 5).setSelected(true);
                adapter.refresh(itemBeanList);
                break;
            case R.id.ll_item7:
                Toast.makeText(this, itemBeanList.get(pageNum * 8 + 6).getIconName(), Toast.LENGTH_SHORT).show();
                initSelected();
                itemBeanList.get(pageNum * 8 + 6).setSelected(true);
                adapter.refresh(itemBeanList);
                break;
            case R.id.ll_item8:
                Toast.makeText(this, itemBeanList.get(pageNum * 8 + 7).getIconName(), Toast.LENGTH_SHORT).show();
                initSelected();
                itemBeanList.get(pageNum * 8 + 7).setSelected(true);
                adapter.refresh(itemBeanList);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化图标的选择状态，全部设置为未选中状态
     */
    private void initSelected() {
        for (int i = 0; i < itemBeanList.size(); i++) {
            itemBeanList.get(i).setSelected(false);
        }
    }
}
