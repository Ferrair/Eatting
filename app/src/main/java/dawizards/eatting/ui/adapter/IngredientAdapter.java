package dawizards.eatting.ui.adapter;


import android.content.Context;
import android.util.Log;
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
import dawizards.eatting.bean.Ingredient;
import dawizards.eatting.ui.adapter.base.BaseAdapter;
import dawizards.eatting.ui.customview.RatioImageView;

/**
 * Created by WQH on 2016/8/5  14:26.
 */
public class IngredientAdapter extends BaseAdapter<IngredientAdapter.IngredientHolder, Ingredient> {

    private static final String TAG = "IngredientAdapter";

    public IngredientAdapter(Context mContext, List<Ingredient> mListData) {
        super(mContext, mListData);
    }

    public IngredientAdapter(Context mContext) {
        this(mContext, null);
    }

    @Override
    protected void onBindItemDataToView(IngredientHolder holder, Ingredient itemData) {
        holder.mIngredientName.setText(itemData.name);
        ImageLoader.getInstance().displayImage(itemData.imageUrl, holder.mIngredientImage);
    }

    @Override
    protected IngredientHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new IngredientHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false));
    }

    public class IngredientHolder extends BaseAdapter.BaseHolder {
        @Bind(R.id.mIngredientImage)
        RatioImageView mIngredientImage;
        @Bind(R.id.mIngredientName)
        TextView mIngredientName;

        public IngredientHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
