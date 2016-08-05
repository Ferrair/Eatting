package dawizards.eatting.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dawizards.eatting.R;
import dawizards.eatting.bean.Select;
import dawizards.eatting.ui.adapter.base.BaseAdapter;
import dawizards.eatting.util.ImageLoaderOptions;

/**
 * Created by WQH on 2016/8/5  13:21.
 */
public class SelectAdapter extends BaseAdapter<SelectAdapter.SelectHolder, Select> {


    public SelectAdapter(Context mContext, List<Select> mListData) {
        super(mContext, mListData);
    }

    @Override
    protected void onBindItemDataToView(SelectHolder holder, Select itemData) {
        holder.mOptionsName.setText(itemData.name);
        if (itemData.url == null)
            holder.mOptionsImage.setVisibility(View.GONE);
        else
            ImageLoader.getInstance().displayImage(itemData.url, holder.mOptionsImage, ImageLoaderOptions.getRoundOptions(), null);
    }

    @Override
    protected SelectHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new SelectHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select, parent, false));
    }

    public static class SelectHolder extends BaseAdapter.BaseHolder {

        @Bind(R.id.mOptionsImage)
        ImageView mOptionsImage;
        @Bind(R.id.mOptionsName)
        TextView mOptionsName;

        public SelectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
