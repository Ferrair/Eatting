主要对`CoordinatorLayout`即其一些常用的组件的bug进行介绍。

`CoordinatorLayout`  `LinearLayout`不兼容
------
在`CoordinatorLayout`下面嵌套一个`AppBarLayout`后，在`AppBarLayout`下面再嵌套一个`LinearLayout`，
就会出现不兼容的情况，也就是`Toolbar`把`LinearLayout`内的`RecyclerView`上面的内容遮住。

这时候就要加一句代码在`LinearLayout`上面
```
app:layout_behavior="@string/appbar_scrolling_view_behavior"
```
即可。

`fitsSystemWindow`
--------
在做本项目的时候，出现了`Toolbar`下面与`ViewPager`有一段东西怎么也消不掉（高度正好为StatusBar高度）,具体可以参见`dawizards.eatting.ui.fragment.FoodFragment`的布局文件

原因：

在`CoordinatorLayout` `AppBarLayout`里面有下面一句代码
```
android:fitsSystemWindows="true"
```

注释上面代码就好

[Google官方文档](https://developer.android.com/reference/android/view/View.html#attr_android:fitsSystemWindows)
如下说：Boolean internal attribute to adjust view layout based on system windows such as the status bar.
If true, adjusts the padding of this view to leave space for the system windows. Will only take effect if this view is in a non-embedded activity.
(也就是忽略StatusBar，与NavigationBar)


在`StatusBar`显示图片
--------
想实现`CollapsingToolbarLayout`在StatusBar中也显示图片，具体看以参见项目中`dawizards.eatting.ui.activity.ItemFoodActivity`的布局文件

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


