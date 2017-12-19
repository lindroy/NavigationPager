
## 1、需求分析
在饿了么首页中我们能看到这样的布局（如下图）。红框内是一个可以左右滑动的页面，每一个页面类似于九宫格，都有可供点击图标。对于这样的布局，我在网上找了很久都没有找到相关的名称，所以我这里暂时叫它导航页吧。
![饿了么首页](http://img.blog.csdn.net/20170327095341827?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTGluZHJvaWQyMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

最近公司的项目就要求我实现一个这样的布局，但是我们的图标并不是想饿了么这样是固定的，所以在饿了么的布局上还要加一个效果：在图标数目无法排满两行时，就只显示一行。比如说，我们每一页最多可以显示两行和四列，当图标的总数目小于或等于4个时就只显示出一行，第二行就不要了。这样页面就不至于留出太多的空白。

先梳理一下我们要实现的效果：
1. 大体的框架是一个可以左右滑动的页面；
2. 每一页为两行四列的矩阵；
3. 底部有和页面数目相等的导航点，且滑动到某个页面时，相应的导航点会改变颜色；
4. 单选事件：选中图标后，图标变为完全不透明。

明白需求之后，我们穿越时空看看最后实现后的布局：
![这里写图片描述](http://img.blog.csdn.net/20170327095552718?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTGluZHJvaWQyMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

图片资源大家可以在我源码中找到。

## 2、实现思路
我们已经分析了需求，现在就来分析一下怎么实现。

必须事先说明一下，我的实现方法比较暴力，也比较占资源，所以大家要是有更好的方法的话欢迎留言告诉我，我们互相学习。

首先，这是一个左右滑动的页面，所以我们可以考虑使用ViewPager我们每个页面有8个图标，也就是8个item。在图标的数目在4以下时就只显示第一行，所以布局上我们可以将四个item作为一行放置到横向的线性布局中，当第二行没有图标时就让它消失。

ViewPager中的每一页的布局都是一样，所以我们可以对其复用：将图标分组，每8个为一组（余下的不到八页也归为一组），每一组即为一页。每次滑动的页面时就加载布局，填充图标和文字。

下面的导航点就比较简单了，可以使用ViewPager的滑动监听事件进行监听，滑动到选中的页面时就改变导航点的颜色。

至于透明，我们只需要记录图标的选中状态，然后改变透明度即可。

## 3、编写布局
接下来就让我们创建项目，编写页面布局吧。

在Android Studio中创建一个NavigationPager项目，MainActivity的布局如下：
### activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@android:color/darker_gray"
    tools:context="com.lindroid.navigationpager.MainActivity">

    <com.lindroid.navigationpager.CustomViewPager
        android:background="@android:color/white"
        android:id="@+id/vp_navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        >
    </com.lindroid.navigationpager.CustomViewPager>

    <LinearLayout
        android:background="@android:color/white"
        android:layout_gravity="center_horizontal"
        android:gravity="center"
        android:id="@+id/ll_dots"
        android:orientation="horizontal"
        android:layout_below="@+id/vp_navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
    </LinearLayout>

</LinearLayout>

```

MainActivity的主布局是一个纵向的线性布局，上面一个ViewPager，下面一个横向的线性布局，用于放置导航点。这里得说一下ViewPager的特性，在不指定特定高度的情况下，ViewPager的高度是默认填充整个父布局的，我们显然不希望这样，毕竟我们还要分一行和两行两种情况呢。所以这里我在这里使用了一个可以自适应高度的自定义ViewPager：
### CustomViewPager
```java
/**
 * Created by Lindroid on 2017/3/20.
 * 自适应高度的ViewPager
 */

public class CustomViewPager extends ViewPager {
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
```
页面的布局（也就是ViewPager的子布局）可以拆分成两个横向排列的布局：
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <LinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <include layout="@layout/layout_line1"/>
        <include layout="@layout/layout_line2"/>

    </LinearLayout>

</LinearLayout>
```
layout_line1.xml和layout_line2.xml分别为第一行和第二行的布局，下面是layout_line1.xml的布局。layout_line2.xml除了控件的id名不同之外，其他的都一样，这里就不贴出来了。
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="horizontal"
    android:weightSum="4"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <LinearLayout

        android:id="@+id/ll_item1"
        android:gravity="center"
        android:padding="10dp"
        android:orientation="vertical"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:layout_height="wrap_content">

        <ImageView

            android:id="@+id/iv_icon1"
            android:layout_width="50dp"
            android:layout_height="50dp"
            android:background="@mipmap/ic_launcher_round" />

        <TextView
            android:id="@+id/tv_name1"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="图标名称" />
    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll_item2"
        android:gravity="center"
        android:padding="10dp"
        android:orientation="vertical"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:layout_height="wrap_content">

        <ImageView
            android:id="@+id/iv_icon2"
            android:layout_width="50dp"
            android:layout_height="50dp"
            android:background="@mipmap/ic_launcher_round" />

        <TextView
            android:id="@+id/tv_name2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="图标名称" />
    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll_item3"
        android:gravity="center"
        android:padding="10dp"
        android:orientation="vertical"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:layout_height="wrap_content">

        <ImageView

            android:id="@+id/iv_icon3"
            android:layout_width="50dp"
            android:layout_height="50dp"
            android:background="@mipmap/ic_launcher_round" />

        <TextView
            android:id="@+id/tv_name3"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="图标名称" />
    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll_item4"
        android:gravity="center"
        android:padding="10dp"
        android:orientation="vertical"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:layout_height="wrap_content">

        <ImageView

            android:id="@+id/iv_icon4"
            android:layout_width="50dp"
            android:layout_height="50dp"
            android:background="@mipmap/ic_launcher_round" />

        <TextView
            android:id="@+id/tv_name4"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="图标名称" />
    </LinearLayout>

</LinearLayout>
```
好了，我们所需要的布局都已经编写完毕，下面就正式开始写代码了！

## 4、编写代码

### 4.1 创建Bean类
创建一个NavigationItemBean类，用于存储图标的图片id、名称和选中状态等信息。
```java
public class NavigationItemBean {
    private int iconID;
    private String iconName;
    private boolean isSelected;

    public NavigationItemBean(int iconID, String iconName) {
        this.iconID = iconID;
        this.iconName = iconName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
```

### 4.2 设置图标资源
为了提高效率，我在初始化控件时使用了ButterKnife，大家需要它的jar包的话也可以从我的工程中复制粘贴。
```java
public class MainActivity extends AppCompatActivity  {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        for (int i = 0; i < icons.length; i++) {
            itemBeanList.add(new NavigationItemBean(icons[i], names[i]));
        }
        adapter = new NavigationAdapter(this, itemBeanList, this);
        vpNavigation.setAdapter(adapter);
    }
}
```
先将图标的id和名称存储到两个数组中，再遍历数组将资源添加到集合中。

### 4.3 创建适配器
这一步应该是整个项目中最难的一步了，所以要打起精神来了。先看下面的代码：

```java
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
        }
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
            llLine2.setVisibility(View.GONE);
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
}
```

代码有点长，但其实不难理解。首先我们创建一个NavigationAdapter继承于ViewPager专用的PageAdapter，并重写其中的几个方法。这里就要注意一个小地方了，getCount方法返回的是ViewPager的item数目，也就是页数，而不是图标的数目，可千万不能搞错了！

下面我就讲解一下比较重要的方法。首先是初始化ViewPager页面布局的方法initView：
```java
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
        }
    }
```
第4和第5行的代码是根据图标的数目来计算ViewPager的页数，而for循环则是有多少页我们就调用多少次View.inflate方法去创建布局。这也是`ButterKnife.bind(this, pageView)`要在for循环中执行的原因：我们使用的虽然都是同一个布局文件，但实际每次循环都会创建一个新的布局。

setPagerViewData方法则是给页面中的每个item填充数据。首先判断集合中的某一位是否有数据，有的话则设置图片和文字，反之则隐藏item。里面的代码基本都一个样子，我就摘录其中“最特别”的一段了：
```java
        if (itemBeanList.size() > pageNum * 8 + 4) {
            llItem5.setVisibility(View.VISIBLE);
            tvName5.setText(itemBeanList.get(pageNum * 8 + 4).getIconName());
            ivIcon5.setBackgroundResource(itemBeanList.get(pageNum * 8 + 4).getIconID());
            setIconAlpha(itemBeanList.get(pageNum * 8 + 4).isSelected(), ivIcon5);
        } else {
            llItem5.setVisibility(View.INVISIBLE);
            llLine2.setVisibility(View.GONE); //t图标数不大于4个时让第二行消失
        }
```

如果你对于initView方法中for循环从0开始（页码的取值从0开始）而不是从1开始感到不解，那么看了setPagerViewData方法之后相信你就会懂了。页码为0时，第一页的第一个item取到的正好是集合的0位元素；页码为1时，第二页的第一个item则是集合中的第8位（1x8+1）元素，这样，我们就可以根据具体的页码来将集合中的元素填充到页面中。这里别忘了还有高度适应的问题。回头看看我摘出来的这段代码，当集合长度不大于4时，我们不仅要隐藏第五个item，而且要让第二行的布局消失，这时，ViewPager的高度就只有一行了。但你肯定会问，第一页时去掉第二行没什么，可以到了第二页时如果集合元素排不到第二行，第二行也会消失吗？答案是不会的。ViewPager默认所有页面的高度都是一致的，就算你在第二页就去掉了第二行，它的高度也不会随着改变。

### 4.4 设置导航点
在MainActivity中，我们创建绘制导航点的方法。导航点是使用xml文件画的，灰色为未选中，绿色为选中。
```java
    private List<ImageView> dots = new ArrayList<>();
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
```
导航点的父布局是LinearLayout，所以我们使用LinearLayout中的LayoutParams给导航点设置上下左右的边距。在onCreate中调用该方法就可以绘制导航点了。因为ViewPager默认显示的是第一页，所以我们默认第一个的导航点是选中的。

除此之外，我们还需要让导航点起到导航的作用。这时候就可以去调用ViewPager的页面滑动监听了：
```java
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
```
在选中某个页面时就将对应的导航点改为绿色就可以了。

### 4.5 图标的点击事件
页面布局中的控件都是在适配器NavigationAdapter中初始化的，为了在MainActivity中能够实现它们的点击事件，我们在NavigationAdapter的构造方法中传入一个View.onClickListener的对象作为参数，只需在MainActivity继承View.OnClickListener接口，在创建NavigationAdapter时传入this即可。

当然，我们还需要在NavigationAdapter中让需要实现点击事件的控件调用setOnClickListener方法：
```java
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
```
大家想一想，initListener方法应该在哪里调用呢？这里有一个坑，由于ViewPager的页面是一页一页地绘制的，所以注册控件的点击事件也应该在for循环中，为每一页的控件都注册一遍：
```java
        for (int i = 0; i < pages; i++) {
            pageView = View.inflate(context, R.layout.page_navigation, null);
            ButterKnife.bind(this, pageView);
            setPagerViewData(i);
            itemViews.add(pageView);
            initListener();
        }
```
好了，现在可以到MainActivity中实现监听事件了：
```java
    @Override
    public void onClick(View v) {
        int pageNum = vpNavigation.getCurrentItem();
        Log.e("Tag", "viewPager.getCurrentItem()=" + vpNavigation.getCurrentItem());
        switch (v.getId()) {
            case R.id.ll_item1:
                Toast.makeText(this, itemBeanList.get(pageNum * 8 + 0).getIconName(), Toast.LENGTH_SHORT).show();
                initSelected();
                itemBeanList.get(pageNum * 8 + 0).setSelected(true);
                adapter.refresh(itemBeanList);
                break;
            case R.id.ll_item2:
                Toast.makeText(this, itemBeanList.get(pageNum * 8 + 1).getIconName(), Toast.LENGTH_SHORT).show();
                initSelected();
                itemBeanList.get(pageNum * 8 + 1).setSelected(true);
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
```
刷新Adapter时我是重新调用了initView方法，并且传入了修改后的数据源。下面是在Adapter中刷新的方法：
```java
    public void refresh(List<NavigationItemBean> itemBeanList){
        initView(itemBeanList);
        notifyDataSetChanged();
    }
```

运行一下，点击，咦，怎么没有反应呢？PageAdapter这里有一个坑了：notifyDataSetChanged只有在ViewPager的页数发生变化才会刷新页面，单是某一个页面的数据发生变化时是没用的。但是不用着急，我们可以重写getItemPosition方法，让其返回POSITION_NONE，强迫viewpager重绘所有的页面。

```java
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
```
运行一下，点击，发现可以完美实现我们要的效果了。

### 4.6 双击取消选中
实现双击取消选中的效果并不难，我们只需在点击每一个图标之前都判断一下当前的选择状态，如果当前为选中的状态，则将状态改为未选中，反之，则改为选中。示例代码如下：
```java
                if (itemBeanList.get(pageNum * 8 + 0).isSelected()) {
                    initSelected();
                    itemBeanList.get(pageNum * 8 + 0).setSelected(false);
                } else {
                    initSelected();
                    itemBeanList.get(pageNum * 8 + 0).setSelected(true);
                }
```
我在代码中只设置了前面两项，大家如果想实现所有图标的双击选中事件的话就自己动一下手吧！

## 5、总结
这个功能我前前后后花了不少时间，在参考别人代码的基础上总算是实现了，水平有限，如果博客中有错漏的地方，欢迎批评指正。我的写法还是太过简单粗暴，不够精巧，而且对资源的占用也比较大，如果你有更好的方法，欢迎交流学习。听说ViewPager+GridVie也可以实现同样的效果，接下来我再研究一下，看能不能再写一篇博客吧！

最后是源码下载地址：
[GitHub](https://github.com/Lindroy/NavigationPager "GitHub")
[CSDN](http://download.csdn.net/detail/lindroid20/9794887 "CSDN")
