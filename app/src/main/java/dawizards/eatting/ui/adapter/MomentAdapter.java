package dawizards.eatting.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dawizards.eatting.R;
import dawizards.eatting.bean.Moment;
import dawizards.eatting.ui.adapter.base.BaseAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by WQH on 2016/8/5  20:58.
 */
public class MomentAdapter extends BaseAdapter<MomentAdapter.MomentHolder, Moment> {


    public MomentAdapter(Context mContext, List<Moment> mListData) {
        super(mContext, mListData);
    }

    public MomentAdapter(Context mContext) {
        this(mContext, null);
    }

    @Override
    protected void onBindItemDataToView(MomentHolder holder, Moment itemData) {
        holder.mAuthorName.setText(itemData.createdBy);
        holder.mMomentsTime.setText(itemData.getCreatedAt().substring(0, 11));
        holder.mMomentsContent.setText(itemData.content);
        holder.mMomentsLikeNum.setText(itemData.getAttendPeopleNum() + " 人觉得很赞");
        if (itemData.userImageUrl != null)
            ImageLoader.getInstance().displayImage(itemData.userImageUrl, holder.mAuthorImage);
    }

    @Override
    protected MomentHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new MomentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moment, parent, false));
    }

    public class MomentHolder extends BaseAdapter.BaseHolder {
        @Bind(R.id.userName)
        TextView mAuthorName;
        @Bind(R.id.userAvatar)
        CircleImageView mAuthorImage;
        @Bind(R.id.userTime)
        TextView mMomentsTime;
        @Bind(R.id.moment_content)
        TextView mMomentsContent;
        @Bind(R.id.moment_likes_num)
        TextView mMomentsLikeNum;
        @Bind(R.id.moment_like)
        ImageButton mMomentsLikeButton;

        public MomentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
