package dawizards.eatting.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dawizards.eatting.R;
import dawizards.eatting.bean.Comment;
import dawizards.eatting.ui.adapter.base.BaseAdapter;
import dawizards.eatting.util.ImageLoaderOptions;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by WQH on 2016/8/3  19:48.
 */
public class CommentAdapter extends BaseAdapter<CommentAdapter.CommentsHolder, Comment> {

    public CommentAdapter(Context mContext) {
        this(mContext, null);
    }

    public CommentAdapter(Context mContext, List<Comment> mListData) {
        super(mContext, mListData);
    }

    // Todo : Query User Info.
    @Override
    protected void onBindItemDataToView(CommentsHolder holder, Comment itemData) {

        holder.createdAt.setText(itemData.getCreatedAt().substring(0, 11));
        holder.commentBy.setText(itemData.commentBy);
        holder.content.setText(itemData.content);
        if (itemData.userUrl != null)
            ImageLoader.getInstance().displayImage(itemData.userUrl, holder.avatar, ImageLoaderOptions.getOptions());
    }

    @Override
    public CommentsHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new CommentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    public static class CommentsHolder extends BaseAdapter.BaseHolder {
        @Bind(R.id.createdAt)
        TextView createdAt;
        @Bind(R.id.commentBy)
        TextView commentBy;
        @Bind(R.id.content)
        TextView content;
        @Bind(R.id.user_avatar)
        CircleImageView avatar;

        public CommentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
