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
在做本项目的时候，出现了`Toolbar`下面与`ViewPager`有一段东西怎么也消不掉（高度正好为StatusBar高度）
原因：

在`CoordinatorLayout` `AppBarLayout`里面有下面一句代码
```
android:fitsSystemWindows="true"
```

注释上面代码就好


同时：要想进入沉浸式的StatusBar在`Toolbar`中加入
```
android:fitsSystemWindows="true"
```
就好

[Google官方文档](https://developer.android.com/reference/android/view/View.html#attr_android:fitsSystemWindows)
如下说：Boolean internal attribute to adjust view layout based on system windows such as the status bar.
If true, adjusts the padding of this view to leave space for the system windows. Will only take effect if this view is in a non-embedded activity.
(也就是忽略StatusBar，与NavigationBar)
