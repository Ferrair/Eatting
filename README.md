# 简介

饭点`V2.0`源码（`V1.0`git仓库已删除）。

其开发环境如下：
 - `JDK8`
 - `Android SDK 24`

所用到的第三方库

 - `com.android.support:appcompat-v7:24.2.1`
 - `cn.bmob.android:bmob-sdk:3.5.0`
 - `com.github.clans:fab:1.6.4`
 - `de.hdodenhof:circleimageview:2.0.0`
 - `com.jakewharton:butterknife:7.0.1`
 - `com.afollestad.material-dialogs:core:0.8.6.2`
 - `com.mikepenz:fontawesome-typeface:4.5.0.1@aa`
 - `com.cleveroad:slidingtutorial:0.9.5`
 - `com.mikepenz:materialdrawer:5.1.1@aar`


# 截图(所有的图片见`screenshot`文件夹)

![APP截图](https://github.com/Ferrair/Eatting/blob/master/screenshot/Screenshot_2016-08-08-15-41-54.png?raw=true)

![APP截图](https://github.com/Ferrair/Eatting/blob/master/screenshot/Screenshot_2016-08-08-15-47-52.png?raw=true)




# 一些讲解

`Activity`之间的继承关系
----
在本项目中，所有的`Activity`都会继承`dawizards.eatting.ui.base.BaseActivity`,这个基类只提供了一个抽象方法，用于子类来解析`layoutID`

```
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int layoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
```

然后，一般`Activity`可以都会有`Toolbar`,这时，就会有`ToolbarActivity`,这时，我们就会去写一个含有`Toolbar`的基类.里面的方法都是比较的简单的。但是要注意，在**子类的xml文件里面`Toolbar`的id必须是`toolbar`.**。该类也提供了一个钩子方法，在点击`Toolbar`时调用（一般是回到顶部）.
```
/**
 * Created by DA Wizards on 2015/11/10.
 *
 * ToolbarActivity for all activity that have a Toolbar, provides an Toolbar that liked some hooked-method.
 */
public abstract class ToolbarActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;


    /*
     * Whether this Activity can back by click left-top back-button.
     */
    protected boolean canBack() {
        return true;
    }

    // A hook method that do something when click the Toolbar.
    protected void onToolbarClick() {
        //Do nothing here.
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) throws RuntimeException {
        super.onCreate(savedInstanceState);
        //Toolbar
        if (mToolbar == null) {
            throw new RuntimeException("Toolbar must be set id as 'toolbar'");
        }
        setSupportActionBar(mToolbar);
        mToolbar.setOnClickListener(view -> onToolbarClick());

        if (canBack()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    //Set the home button listener which can finish this activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Change Toolbar title in Runtime.
     */
    public void setTitleDynamic(String title) {
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
    }
}

```
然后，有的`Activity`可以滚动的话，其内部要维护一个`RecyclerView`,这里有一个`ScrollActivity`。没什么要说的，在子类的xml文件中**`SwipeRefreshLayout`的id必须是`swipeRefreshLayout`,`RecyclerView `的id必须是`recyclerView`.**
```
/**
 * Created by DA Wizards on 2015/11/10.
 *
 * A Activity that have a SwipeRefreshLayout(with layout id swipeRefreshLayout) and a RecyclerView(with layout id recyclerView)
 * NOTE: in subclass's XML file,those layout's is MUST be the same as above.
 */
public abstract class ScrollActivity extends ToolbarActivity implements CanScroll {
    private static final String TAG = "ScrollActivity";
    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @Override
    public boolean canRefresh() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mRefreshLayout == null)
            throw new NullPointerException("This Activity must have a SwipeRefreshLayout with id:swipeRefreshLayout");
        if (mRecyclerView == null)
            throw new NullPointerException("This Activity must have a RecyclerView with id:recyclerView");

        mRefreshLayout.setEnabled(canRefresh());
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            onRefreshDelayed();
            if (mRefreshLayout != null) {
                mRefreshLayout.setRefreshing(false);
            } else {
                Log.e(TAG, "onRefresh: mRefreshLayout is null");
            }
        }, 2000);
    }

    /**
     * All the ScrollActivity can scroll to top when click the Toolbar.
     */
    @Override
    protected void onToolbarClick() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        } else {
            Log.e(TAG, "onRefresh: mRefreshLayout is null");
        }
    }
}
```

`BaseMain`的复用
----

在`BaseMain`中，向子类提供了下面几个抽象方法(这些方法都在`dawizards.eatting.ui.base.OnContentCreate`中辣):
```
      List<BaseFragment> onFragmentCreate();

      List<Button> onButtonCreate();

      List<String> onTitleCreate();
```
子类继承`BaseMain`实现上面的方法，就可以了。


`Adapter`
----
这里的`Adapter`可以看我写的这一篇博文,[传送门](http://blog.csdn.net/haveferrair/article/details/51242604)



# 开发中遇到的坑

1.`CoordinatorLayout`  `LinearLayout`不兼容
------
在`CoordinatorLayout`下面嵌套一个`AppBarLayout`后，在`AppBarLayout`下面再嵌套一个`LinearLayout`，
就会出现不兼容的情况，也就是`Toolbar`把`LinearLayout`内的`RecyclerView`上面的内容遮住。

这时候就要加一句代码在`LinearLayout`上面
```
app:layout_behavior="@string/appbar_scrolling_view_behavior"
```
即可。

2.`fitsSystemWindow`
--------
在做本项目的时候，出现了`Toolbar`下面与`ViewPager`有一段东西怎么也消不掉（高度正好为`StatusBar`高度）,具体可以参见`dawizards.eatting.ui.fragment.FoodFragment`的布局文件

原因：

在`CoordinatorLayout` `AppBarLayout`里面有下面一句代码
```
android:fitsSystemWindows="true"
```

注释上面代码就好

[Google官方文档](https://developer.android.com/reference/android/view/View.html#attr_android:fitsSystemWindows)
如下说：
 > Boolean internal attribute to adjust view layout based on system windows such as the status bar.
If true, adjusts the padding of this view to leave space for the system windows. Will only take effect if this view is in a non-embedded activity.

(也就是忽略`StatusBar`，与`NavigationBar`)


3.在`StatusBar`显示图片
--------
想实现`CollapsingToolbarLayout`在`StatusBar`中也显示图片，具体看以参见项目中`dawizards.eatting.ui.activity.ItemFoodActivity`的布局文件

1.定义自己的style(style-v21)
```
<style name="AppTheme.NoActionBar.Transparent" parent="AppTheme.NoActionBar">
        <item name="android:statusBarColor">@android:color/transparent</item>
    </style>
```
(style)
```
 <style name="AppTheme.NoActionBar.Transparent" parent="AppTheme.NoActionBar">
        <item name="colorPrimaryDark">@android:color/transparent</item>
    </style>
```
2.设置当前的布局文件为这个主题
```
 <activity
         android:name=".ui.activity.ItemFoodActivity"
         android:theme="@style/AppTheme.NoActionBar.Transparent"/>
 <activity
```
3.这样之后，会有一个问题，一开始进去的时候，`StatusBar`是原来的颜色，在滑动的时候会慢慢消失
这个时候，在`ImageView`添加下面一句话：
```
android:fitsSystemWindows="true"
```

# `ToDo List`
 - 自动更新
 - 由于服务器用的是`Bmob`,所以导致代码数据获取模块与`Controller`没有分离，所以可以将这2个模块解耦。
 - 图片压缩,图片没有压缩就进行上传！！（小心流量）
 - 图片选择（在图库中选择多个图片），可以利用下面这个开源库`https://github.com/DroidNinja/Android-FilePicker`
# 联系我
`QQ: 1906362072`

`Mail : hellowangqiang@gmail.com`





