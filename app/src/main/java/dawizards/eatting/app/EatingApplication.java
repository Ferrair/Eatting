package dawizards.eatting.app;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.bmob.v3.Bmob;
import dawizards.eatting.util.ToastUtil;

/**
 * Created by WQH on 2016/8/1  20:04.
 */
public class EatingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initToast();
        initBmobSDK();
        initImageLoader(getApplicationContext());
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(50 * 1024 * 1024)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initToast() {
        ToastUtil.register(getApplicationContext());
    }

    private void initBmobSDK() {
        Bmob.initialize(getApplicationContext(), Constants.bmobApplicationId);
    }
}
