package dawizards.eatting.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import dawizards.eatting.R;
import dawizards.eatting.bean.Food;
import dawizards.eatting.bean.User;
import dawizards.eatting.ui.adapter.base.BaseAdapter;
import dawizards.eatting.util.ImageLoaderOptions;

/**
 * Created by WQH on 2016/8/2  22:08.
 */
public class FoodAdapter extends BaseAdapter<FoodAdapter.FoodHolder, Food> {

    private User currentUser;
    /*
     * Edit Model.
     */
    private boolean edit = false;

    public FoodAdapter(Context mContext, List<Food> mListData) {
        super(mContext, mListData);
        currentUser = BmobUser.getCurrentUser(User.class);
    }

    public void enterEdit() {
        edit = true;
    }

    public void exitEdit() {
        edit = false;
    }

    public FoodAdapter(Context mContext) {
        this(mContext, null);
    }

    @Override
    protected void onBindItemDataToView(FoodHolder holder, Food itemData) {
        ImageLoader.getInstance().displayImage(itemData.imageUrl, holder.mFoodImage, ImageLoaderOptions.getRoundOptions(), null);
        holder.mFoodName.setText("品名:" + itemData.name);
        holder.mFoodPrice.setText("价格:" + itemData.price);
        holder.mFoodLikeNum.setText("点赞人数：" + String.valueOf(itemData.getLikePeopleNum()));
        if (itemData.getUpdatedAt() != null)
            holder.mFoodTime.setText(itemData.getUpdatedAt().substring(0, 11));
        /*
         * Hide the schedule button when currentUser is Canteen.
         */
        if (currentUser.getType() == User.UserType.CANTEEN) {
            holder.mFoodButton.setVisibility(View.GONE);
        }

        /*
         * Set the text on schedule button depend on whether the student schedule the food.
         */
        if (currentUser.getType() == User.UserType.STUDENT) {
            holder.mFoodButton.setVisibility(View.VISIBLE);

            if (itemData.isAttend(BmobUser.getCurrentUser(User.class))) {
                holder.mFoodButton.setText(mContext.getString(R.string.food_scheduled));
            } else {
                holder.mFoodButton.setText(mContext.getString(R.string.food_schedule));
            }
        }

        if (edit) {
            holder.mButtonSelect.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected FoodHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new FoodHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false));
    }

    public static class FoodHolder extends BaseAdapter.BaseHolder {
        @Bind(R.id.food_image)
        ImageView mFoodImage;
        @Bind(R.id.food_name)
        TextView mFoodName;
        @Bind(R.id.food_price)
        TextView mFoodPrice;
        @Bind(R.id.food_like_num)
        TextView mFoodLikeNum;
        @Bind(R.id.food_schedule)
        Button mFoodButton;
        @Bind(R.id.food_layout)
        View mFoodLayout;
        @Bind(R.id.food_time)
        TextView mFoodTime;
        @Bind(R.id.food_select)
        RadioButton mButtonSelect;

        public FoodHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
