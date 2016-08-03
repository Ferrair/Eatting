package dawizards.eatting.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import dawizards.eatting.R;

public class ImageLoaderOptions {
    public static DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(false) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                        // .decodingOptions(android.graphics.BitmapFactory.Options
                        // decodingOptions)//设置图片的解码配置
                .considerExifParams(true)
                        // 设置图片下载前的延迟
                .delayBeforeLoading(100)//int
                        // delayInMillis为你设置的延迟时间
                        // 设置图片加入缓存前，对bitmap进行设置
                        // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
                .build();
    }

    public static DisplayImageOptions getRoundOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(false) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                // .decodingOptions(android.graphics.BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(5)) //圆角
                // 设置图片下载前的延迟
                .delayBeforeLoading(100)//int
                // delayInMillis为你设置的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
                .build();
    }
}
