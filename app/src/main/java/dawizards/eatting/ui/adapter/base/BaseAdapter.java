package dawizards.eatting.ui.adapter.base;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dawizards.eatting.R;
import dawizards.eatting.ui.adapter.animation.AnimationManager;
import dawizards.eatting.ui.adapter.animation.BaseAnimation;
import dawizards.eatting.ui.adapter.event.LayoutState;
import dawizards.eatting.ui.adapter.event.OnBottomListener;
import dawizards.eatting.ui.adapter.event.OnItemClickListener;
import dawizards.eatting.ui.adapter.event.OnItemLongClickListener;
import dawizards.eatting.util.CollectionUtil;


/**
 * Created by WQH on 2016/4/11  21:07.
 * <p>
 * BaseAdapter extends RecyclerView.Adapter,subclass extends this class and do explicit works.
 * Such as bind the data to the view,that's mean show the data to the user.
 */
public abstract class BaseAdapter<Holder extends BaseAdapter.BaseHolder, DataType> extends RecyclerView.Adapter<Holder> implements Adapter<DataType> {
    private static final String TAG = "BaseAdapter";
    protected Context mContext;
    protected List<DataType> mListData;

    private boolean isOpenAnimation;
    private int mLastPosition;

    private BaseAnimation mBaseAnimation;
    private FooterViewHolder mFooterViewHolder;
    private OnBottomListener mOnBottomListener;

    /**
     * The current data page in RecyclerView.And will increase when the user scroll and loadMore.
     */
    private int mCurrentPage = 1;
    /**
     * The current state of FooterView.
     */
    private int mState = LayoutState.LOAD;

    public static final int ITEM_TYPE_FOOTER = 0;
    public static final int ITEM_TYPE_NORMAL = 1;
    /**
     * a SparseArray that stores a pair.
     * key is resId of a view.
     * value is Listener which can be triggered by click(or long click) the view in key.
     */
    protected SparseArray<OnItemClickListener<DataType>> mItemClickListener = new SparseArray<>();
    protected SparseArray<OnItemLongClickListener<DataType>> mLongItemClickListener = new SparseArray<>();

    /**
     * abstract method for subclass to bind ITEM data to the view.
     * so the subclass can show this item data by views holden by holder
     * <p>
     * NOTE: Use this method instead of @see{#onBindViewHolder} which is final in this class
     *
     * @param holder   a RecyclerView.ViewHolder that hold the view.
     * @param itemData item data from the <code>List<DataType><code/>
     */
    protected abstract void onBindItemDataToView(Holder holder, DataType itemData);

    /**
     * Create normal data ViewHolder in subclass.
     */
    protected abstract Holder onCreateHolder(ViewGroup parent, int viewType);

    public BaseAdapter(Context mContext, List<DataType> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (ITEM_TYPE_FOOTER == viewType) {
            if (mFooterViewHolder == null) {
                mFooterViewHolder = new FooterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_footer_load_more, parent, false));
            }
            return (Holder) mFooterViewHolder;
        }
        return onCreateHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mListData.size()) {
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public final void onBindViewHolder(Holder holder, int position) {
        if (position == mListData.size()) {
            mFooterViewHolder.bind();
        } else {
            final DataType itemData = mListData.get(position);
            onBindItemDataToView(holder, itemData);
            bindListener(holder, itemData);
            bindAnimation(holder);
        }
    }

    /**
     * Bind animation to item view when the isOpenAnimation is ON.
     *
     * @param holder a RecyclerView.ViewHolder that hold the view.That's means the
     *               holder can add animation.
     */
    private void bindAnimation(Holder holder) {
        if (isOpenAnimation) {
            if (holder.getLayoutPosition() > mLastPosition) {
                for (Animator anim : mBaseAnimation.getAnimators(holder.itemView)) {
                    anim.setDuration(500).start();
                    anim.setInterpolator(new LinearInterpolator());
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * Bind OnItemClickListener and OnItemLongClickListener on the holder if exists
     *
     * @param holder   a RecyclerView.ViewHolder that hold the view.That's means the
     *                 holder can add Listener
     * @param itemData item data from the <code>List<DataType><code/>
     */
    private void bindListener(Holder holder, DataType itemData) {
        for (int i = 0; i < mItemClickListener.size(); ++i) {
            int resId = mItemClickListener.keyAt(i);
            holder.getView(resId).setOnClickListener(view -> mItemClickListener.get(resId).onItemClick(view, itemData));
        }

        for (int i = 0; i < mLongItemClickListener.size(); ++i) {
            int resId = mLongItemClickListener.keyAt(i);
            holder.getView(resId).setOnLongClickListener(view -> {
                mLongItemClickListener.get(resId).onItemLongClick(view, itemData);
                // return true means this view consume this action.So will not dispatch this action.
                return true;
            });
        }
    }

    /**
     * When the adapter add/delete a item,animate to user by the param <param>type<param> in <code>@AnimationManager.AnimationType</code>
     */
    public void openAnimation(@AnimationManager.AnimationType int type) {
        this.isOpenAnimation = true;
        mBaseAnimation = AnimationManager.get(type);
    }

    /**
     * Because of the Footer-View,So the count MUST contains the Footer-View.
     */
    @Override
    public int getItemCount() {
        if (mListData == null)
            return 0;
        return mListData.size() + 1;
    }

    @Override
    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    @Override
    public void fill(List<DataType> newDataList) {
        if (this.mListData == null) {
            this.mListData = newDataList;
            return;
        }
        mListData.clear();
        mListData.addAll(newDataList);
        notifyDataSetChanged();
    }

    @Override
    public void fill(DataType newData) {
        int index = mListData.indexOf(newData);
        mListData.remove(index);
        mListData.add(index, newData);
        notifyItemChanged(index);
    }


    @Override
    public void addAtTail(DataType data) {
        this.addOne(data, mListData.size() - 1);
    }

    @Override
    public void addAtHead(DataType data) {
        this.addOne(data, 0);
    }

    public List<DataType> getAllData() {
        return mListData;
    }

    @Override
    public void addOne(DataType data, int position) {
        this.mListData.add(position, data);
        notifyItemInserted(position);
        if (position != mListData.size() - 1) {
            notifyItemRangeChanged(position, mListData.size() - position);
        }
    }

    @Override
    public void removeOne(DataType item) {
        notifyItemRemoved(this.mListData.indexOf(item));
        this.mListData.remove(item);
    }

    @Override
    public void removeOne(int position) {
        this.mListData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void removeAll() {
        this.mListData.clear();
        notifyDataSetChanged();
    }

    @Override
    public DataType getOne(int which) {
        return mListData.get(which);
    }

    public void setOnItemClickListener(@IdRes int resId, OnItemClickListener<DataType> mOnItemClickListener) {
        mItemClickListener.append(resId, mOnItemClickListener);
    }

    public void setOnItemLongClickListener(@IdRes int resId, OnItemLongClickListener<DataType> mOnItemLongClickListener) {
        mLongItemClickListener.append(resId, mOnItemLongClickListener);
    }

    /**
     * Trigger that this Adapter can LoadMore.
     * So if do not call this method,and the adapter will not call OnBottomListener().
     */
    public void setOnBottomListener(OnBottomListener mOnBottomListener) {
        this.mOnBottomListener = mOnBottomListener;
    }

    public void setLoadState(@LayoutState.State int state) {
        this.mState = state;
        if (mFooterViewHolder != null)
            mFooterViewHolder.bind();
    }

    public abstract static class BaseHolder extends RecyclerView.ViewHolder {

        public BaseHolder(View itemView) {
            super(itemView);
        }

        /**
         * find a View by given resId in parent view container.
         */
        @SuppressWarnings("unchecked")
        public <T extends View> T getView(@IdRes int resId) {
            return (T) itemView.findViewById(resId);
        }
    }

    /**
     * A Footer-View holds in recyclerView's footer.
     * This view can show 3 states:
     * -- LayoutState.LOAD : the data is loading from server.
     * -- LayoutState.FINISHED : the data have loaded from server.
     * --  LayoutState.GONE : don't show this view.
     */
    public class FooterViewHolder extends BaseHolder {
        @Bind(R.id.footerText)
        TextView footerText;
        @Bind(R.id.footerProgressBar)
        ProgressBar footerProgressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            switch (mState) {
                case LayoutState.LOAD:
                    loadMore();
                    if (mOnBottomListener != null) {
                        mOnBottomListener.onLoadMore(++mCurrentPage);
                    }
                    break;
                case LayoutState.FINISHED:
                    noMore();
                    break;
                case LayoutState.GONE:
                    hide();
                    break;
            }
        }

        private void loadMore() {
            show();
            if (footerProgressBar.getVisibility() != View.VISIBLE) {
                footerProgressBar.setVisibility(View.VISIBLE);
            }
            footerText.setText(R.string.footer_load_more);
        }

        private void noMore() {
            show();
            if (footerProgressBar.getVisibility() != View.GONE) {
                footerProgressBar.setVisibility(View.GONE);
            }
            footerText.setText(R.string.footer_load_finish);
        }

        private void hide() {
            if (itemView.getVisibility() != View.GONE) {
                itemView.setVisibility(View.GONE);
            }
        }

        private void show() {
            if (itemView.getVisibility() != View.VISIBLE) {
                itemView.setVisibility(View.VISIBLE);
            }
        }
    }
}
