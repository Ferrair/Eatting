package dawizards.eatting.ui.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import dawizards.eatting.R;
import dawizards.eatting.ui.activity.SettingActivity;
import dawizards.eatting.util.IntentUtil;


/**
 * Created by WQH on 2016/4/11  18:30.
 * <p>
 * Drawer for user on the left of the screen,this class is a delegate to reduce the coupling of the <code>BaseMain<code/>
 */
public class DrawerDelegate {
    private Toolbar toolbar;
    private Activity activity;

    private DrawerListener drawerListener;

    protected Drawer drawer;
    protected AccountHeader header;
    protected ProfileDrawerItem profileDrawerItem;

    public DrawerDelegate(@NonNull Activity activity, @NonNull Toolbar toolbar, @NonNull DrawerListener listener) {
        this.activity = activity;
        this.toolbar = toolbar;
        this.drawerListener = listener;
    }

    public void init() {
        header = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.mipmap.background)
                .addProfiles(profileDrawerItem = new ProfileDrawerItem().withName("未登录"))
                .withOnAccountHeaderListener((view, profile, current) -> {
                    drawerListener.onAvatarClickListener();
                    return true;
                })
                .build();

        final PrimaryDrawerItem setting = new PrimaryDrawerItem().withIcon(FontAwesome.Icon.faw_cogs).withName("设置");
        drawer = new DrawerBuilder()
                .withToolbar(toolbar)
                .withActivity(activity)
                .withDisplayBelowStatusBar(false)
                .withDrawerItems(drawerListener.onDrawerMenuCreate())
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    if (drawerItem == setting) {
                        IntentUtil.goToOtherActivity(activity, SettingActivity.class);
                        return true;
                    }
                    return drawerListener.onDrawerMenuSelected(view, position, drawerItem);
                })
                .withAccountHeader(header)
                .build();
        drawer.addStickyFooterItem(setting);

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                ImageLoader.getInstance().displayImage(uri.toString(), imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                ImageLoader.getInstance().cancelDisplayTask(imageView);
            }
        });
    }

    public void setName(String name) {
        profileDrawerItem.withName(name);
        header.updateProfile(profileDrawerItem);
    }

    public void setAvatar(String url) {
        profileDrawerItem.withIcon(url);
        header.updateProfile(profileDrawerItem);
    }

    public void setAvatar(@DrawableRes int resId) {
        profileDrawerItem.withIcon(resId);
        header.updateProfile(profileDrawerItem);
    }

    public void setHeaderBackground(String url) {
        header.setHeaderBackground(new ImageHolder(url));
    }

    public void setHeaderBackground(@DrawableRes int resId) {
        header.setHeaderBackground(new ImageHolder(resId));
    }

    public void setEmail(String email) {
        profileDrawerItem.withEmail(email);
        header.updateProfile(profileDrawerItem);
    }

    public void destroy() {
        header.clear();
        header = null;
        profileDrawerItem = null;
        drawerListener = null;
        activity = null;
        toolbar = null;
        drawer = null;
    }

    /**
     * A drawer listener which can create AND listen a item in the parent drawer
     */
    public interface DrawerListener {
        boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem);

        @NonNull
        List<IDrawerItem> onDrawerMenuCreate();

        void onAvatarClickListener();

    }
}
