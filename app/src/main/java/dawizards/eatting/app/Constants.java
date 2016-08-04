package dawizards.eatting.app;

import android.os.Environment;

/**
 * Created by WQH on 2016/8/1  20:12.
 */
public class Constants {
    public final static String bmobApplicationId = "34efd640d061e4ce64ff0e687fe64736";

    //用户发送图片的路径
    public static String PICTURE_PATH = Environment.getExternalStorageDirectory() + "/image/";

    //对于Bitmap处理时onActivityResult的RequestCode
    public static final int REQUEST_CAMERA = 0; //从相机照相的requestCode
    public static final int REQUEST_GALLEY = 1; //从相册选择的requestCode
}
