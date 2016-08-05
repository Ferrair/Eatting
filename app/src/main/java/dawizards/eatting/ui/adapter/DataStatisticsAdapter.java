package dawizards.eatting.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dawizards.eatting.R;
import dawizards.eatting.bean.Food;
import dawizards.eatting.ui.adapter.base.BaseAdapter;

/**
 * Created by Administrator on 2015/11/18.
 */
public class DataStatisticsAdapter extends BaseAdapter<DataStatisticsAdapter.DataStatisticsHolder, Food> {

    public DataStatisticsAdapter(Context mContext, List<Food> mListData) {
        super(mContext, mListData);
    }

    public DataStatisticsAdapter(Context mContext) {
        this(mContext, null);
    }

    @Override
    protected void onBindItemDataToView(DataStatisticsHolder holder, Food itemData) {
        holder.mFoodName.setText(itemData.name);
        holder.mFoodNum.setText("预定人数：" + itemData.getAttendPeopleNum());
    }

    @Override
    protected DataStatisticsHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new DataStatisticsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false));
    }

    class DataStatisticsHolder extends BaseAdapter.BaseHolder {

        @Bind(R.id.mFoodNum)
        TextView mFoodNum;

        @Bind(R.id.mFoodName)
        TextView mFoodName;

        public DataStatisticsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
