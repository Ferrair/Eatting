package dawizards.eatting.ui.adapter.event;

/**
 * Created by WQH on 2016/5/16  21:53.
 * Call when RecyclerView scroll to bottom.
 *
 * To unable it,do NOT call mAdapter.setOnBottomListener(this).
 */
public interface OnBottomListener {
    /**
     * @param toToLoadPage the page to be load.
     */
    void onLoadMore(int toToLoadPage);
}
