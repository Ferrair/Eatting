package dawizards.eatting.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import dawizards.eatting.R;
import dawizards.eatting.app.Constants;
import dawizards.eatting.manager.PermissionManager;
import dawizards.eatting.bean.Food;
import dawizards.eatting.bean.User;
import dawizards.eatting.manager.RxBus;
import dawizards.eatting.mvp.presenter.FoodPresenter;
import dawizards.eatting.ui.base.ToolbarActivity;
import dawizards.eatting.util.ImageLoaderOptions;
import dawizards.eatting.util.ToastUtil;

public class PostFoodActivity extends ToolbarActivity {
    private static final String TAG = "PostFoodActivity";
    @Bind(R.id.food_name)
    EditText mFoodName;

    @Bind(R.id.food_price)
    EditText mFoodPrice;

    @Bind(R.id.food_image)
    ImageView mFoodImage;

    String mFilePath;
    Food mFood;
    boolean isModify = false;
    MaterialDialog mDialog;
    FoodPresenter mFoodPresenter;

    @Override
    protected int layoutId() {
        return R.layout.activity_post_food;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = new MaterialDialog.Builder(this).content(R.string.update_now).progress(true, 0).build();
        mFoodPresenter = new FoodPresenter();

        Intent mIntent = getIntent();
        //修改Food然后进行发送
        if (mIntent != null && mIntent.getBooleanExtra("flag", false)) {
            isModify = true;
            mFood = (Food) getIntent().getSerializableExtra("itemFood");
            mFoodName.setText(String.valueOf(mFood.name));
            mFoodPrice.setText(String.valueOf(mFood.price));
            ImageLoader.getInstance().displayImage(mFood.imageUrl, mFoodImage, ImageLoaderOptions.getRoundOptions(), null);
        } else
            mFood = new Food();
    }


    private boolean rightInput(String foodName, String foodPrice) {
        if (TextUtils.isEmpty(foodName)) {
            ToastUtil.showToast(getResources().getString(R.string.foodname_empty));
            return false;
        }
        if (TextUtils.isEmpty(foodPrice)) {
            ToastUtil.showToast(getResources().getString(R.string.foodprice_empty));
            return false;
        }

        if (mFoodImage.getDrawable() == ContextCompat.getDrawable(this, R.mipmap.add_photo)) {
            ToastUtil.showToast(getResources().getString(R.string.foodimage_empty));
            return false;
        }
        return true;
    }


    /**
     * Select Image from album or camera.
     */
    @OnClick(R.id.food_image)
    public void selectImage() {
        new AlertDialog.Builder(this).setItems(new String[]{"从相册选择", "照相",}, (dialog, which) -> {
            switch (which) {
                case 0:
                    if (PermissionManager.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        album();
                    } else {
                        PermissionManager.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    break;
                case 1:
                    if (PermissionManager.hasPermission(this, Manifest.permission.CAMERA)) {
                        camera();
                    } else {
                        PermissionManager.requestPermission(this, Manifest.permission.CAMERA);
                    }
                    break;
            }
        }).create().show();
    }


    /**
     * Callback for the result from requesting permissions. This method is invoked for every call on requestPermissions(android.app.Activity, String[], int).
     *
     * Note: It is possible that the permissions request interaction with the user is interrupted.
     * In this case you will receive empty permissions and results arrays which should be treated as a cancellation.
     *
     * @param requestCode  int: The request code passed in requestPermissions(android.app.Activity, String[], int)
     * @param permissions  String: The requested permissions. Never null.
     * @param grantResults int: The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            for (String permission : permissions) {
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    for (int grantResult : grantResults) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            album();
                        }
                    }
                }
                if (permission.equals(Manifest.permission.CAMERA)) {
                    for (int grantResult : grantResults) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            camera();
                        }
                    }
                }
            }
        }
    }

    public void camera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(Constants.PICTURE_PATH);
        if (!dir.exists())
            dir.mkdir();
        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        mFilePath = file.getAbsolutePath();
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(openCameraIntent, Constants.REQUEST_CAMERA);
    }

    public void album() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, Constants.REQUEST_GALLEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CAMERA:
                    showImage(mFilePath);
                    break;
                case Constants.REQUEST_GALLEY:
                    if (data == null) return;
                    if (data.getData() == null) return;
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        showImage(cursor.getString(cursor.getColumnIndex("_data")));
                        cursor.close();
                    } else
                        Log.e("PostFoodActivity ", "onActivityResult ()未查询到cursor");

                    break;
            }
        }
    }

    private void showImage(String localPath) {
        if (localPath == null || localPath.equals("null")) {
            ToastUtil.showToast(getString(R.string.image_error));
            return;
        }
        mFilePath = localPath;
        ImageLoader.getInstance().displayImage("file://" + localPath, mFoodImage, ImageLoaderOptions.getRoundOptions(), null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_food, menu);
        return true;
    }

    /**
     * When toolbar set menu.If too;bar can response the home button,MUST set it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_food:
                postFood();
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }

    /**
     * Upload file and save the new-post Food.
     */
    private void postFood() {
        String foodName = mFoodName.getText().toString();
        String foodPrice = mFoodPrice.getText().toString();

        if (rightInput(foodName, foodPrice)) {
            mDialog.show();
            BmobFile bmobFile = new BmobFile(new File(mFilePath));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        User currentUser = BmobUser.getCurrentUser(User.class);
                        mFood.name = foodName;
                        mFood.price = Float.valueOf(foodPrice);
                        mFood.belongSchool = currentUser.getBelongSchool();
                        mFood.belongCanteen = currentUser.getBelongCanteen();
                        mFood.imageUrl = bmobFile.getFileUrl();
                        mFoodPresenter.post(mFood, new FoodSaveListener(bmobFile.getFileUrl()));
                    } else {
                        Log.i(TAG, "上传文件失败：" + e.getMessage());
                    }
                }
            });
        }
    }

    class FoodSaveListener extends SaveListener<String> {
        String url;

        public FoodSaveListener(String url) {
            this.url = url;
        }

        @Override
        public void done(String var, BmobException e) {
            if (e == null) {
                RxBus.getDefault().post(mFood);
                mDialog.dismiss();
                Log.i(TAG, "上传文件成功:" + url);
                finish();
            }
        }
    }
}
