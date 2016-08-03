package dawizards.eatting.ui.adapter.event;

import android.view.View;

/**
 * Created by WQH on 2016/4/11  22:47.
 */
public interface OnItemClickListener<DataType> {
    void onItemClick(View view, DataType data);
}
