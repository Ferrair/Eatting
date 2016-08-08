package dawizards.eatting.ui.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import dawizards.eatting.R;
import dawizards.eatting.app.Constants;
import dawizards.eatting.manager.PermissionManager;
import dawizards.eatting.bean.User;
import dawizards.eatting.mvp.presenter.UserPresenter;
import dawizards.eatting.ui.activity.SearchActivity;
import dawizards.eatting.ui.adapter.FragmentAdapter;
import dawizards.eatting.ui.customview.DrawerDelegate;
import dawizards.eatting.util.IntentUtil;
import dawizards.eatting.util.ToastUtil;

/**
 * Created by WQH on 2015/11/27 19:53.
 *
 * Handle business login, and get component via callback.
 */
public abstract class BaseMain extends ToolbarActivity implements DrawerDelegate.DrawerListener, OnContentCreate {

    private static final String TAG = "BaseMain";

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    /**
     * A Delegate that holds Left-Drawer.
     */
    private DrawerDelegate mDrawerDelegate;

    private List<BaseFragment> mContentList = new ArrayList<>();

    private List<Button> mButtonList = new ArrayList<>();

    private List<String> mTitleList = new ArrayList<>();

    /*
     * User Avatar Local Path.
     */
    String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        initContentView();
        initDrawer();
    }

    /*
     * Init the Activity.
     */
    private void initComponents() {
        mContentList = onFragmentCreate();
        mButtonList = onButtonCreate();
        mTitleList = onTitleCreate();
    }

    private void initContentView() {
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mContentList));
        mViewPager.addOnPageChangeListener(new MyPageChangeListener());

        for (int i = 0; i < mButtonList.size(); i++) {
            mButtonList.get(i).setOnClickListener(new ChangeStateListener(i));
        }

        selectState(0);
    }

    // Todo : 右上角按钮 Drawer出不来
    private void initDrawer() {
        mDrawerDelegate = new DrawerDelegate(this, mToolbar, this);
        mDrawerDelegate.init();
    }

    /*
     * Create Menu and Set Listener.
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                IntentUtil.goToOtherActivity(this, SearchActivity.class);
                break;
        }
        return true;
    }

    /*
     * Upload user avatar.
     */
    @Override
    public void onAvatarClickListener() {
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
        compress();

    }

    private void compress() {
        BmobFile bmobFile = new BmobFile(new File(mFilePath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mDrawerDelegate.setAvatar(bmobFile.getFileUrl());

                    User mUser = new User();
                    mUser.setUserImage(bmobFile.getFileUrl());
                    UserPresenter userPresenter = new UserPresenter();
                    userPresenter.update(mUser);

                } else {
                    Log.i(TAG, "上传文件失败：" + e.getMessage());
                }
            }
        });
    }


    /*
     * Title.
     */
    @Override
    protected void onToolbarClick() {
        ((ScrollFragment) mContentList.get(mViewPager.getCurrentItem())).onToolbarClick();
    }


    @Override
    protected boolean canBack() {
        return false;
    }


    protected void selectStateButton(int which) {
        for (int i = 0; i < mButtonList.size(); i++) {
            if (i == which)
                mButtonList.get(i).setSelected(true);
            else
                mButtonList.get(i).setSelected(false);
        }
    }


    /**
     * Call when user scroll the ViewPager or click the Button.
     *
     * @param which which Fragment page is shown to user.
     */
    protected void selectState(int which) {
        /*
         * Change Button.
         */
        selectStateButton(which);
        /*
         * Change ViewPager.
         */
        mViewPager.setCurrentItem(which);
        /*
         * Change Title.
         */
        setTitleDynamic(mTitleList.get(which));
    }

    /*
     * System Method.
     */
    @Override
    protected void onDestroy() {
        mDrawerDelegate.destroy();
        mDrawerDelegate = null;
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null) {
            mDrawerDelegate.setName(currentUser.getUsername());
            mDrawerDelegate.setEmail(currentUser.getType() == User.UserType.CANTEEN ? currentUser.getBelongCanteen() : "学生");
            if (currentUser.getUserImage() == null) {
                mDrawerDelegate.setAvatar(R.mipmap.head);
            } else {
                mDrawerDelegate.setAvatar(currentUser.getUserImage());
            }
        }
    }


    protected class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 2) {
                selectState(mViewPager.getCurrentItem());
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
        }
    }

    protected class ChangeStateListener implements View.OnClickListener {
        int index;

        public ChangeStateListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            selectState(index);
        }
    }


}
