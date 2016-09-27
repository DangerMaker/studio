package com.example.exerciseapp.aty.organzie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.exerciseapp.model.PhotoModel;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/6.
 */
public class PhotoListAdapter extends BaseRecyclerAdapter<PhotoModel.DataBean> {

    Context mContext;
    public PhotoListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        viewHolder = new PhotoHolder(new PhotoItemView(mContext));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PhotoModel.DataBean dataBean = getItem(position);
        PhotoHolder photoHolder = (PhotoHolder) holder;
        photoHolder.itemView.addItems(dataBean);

        if (position == mItems.size() - 1) {
            if(mListener != null)
                mListener.onListEnded();
        }
    }

    protected class PhotoHolder extends RecyclerView.ViewHolder{
        public PhotoItemView itemView;

        public PhotoHolder(PhotoItemView itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }

    private ListAdapterListener mListener;

    public void setListener(ListAdapterListener mListener) {
        this.mListener = mListener;
    }

    public interface ListAdapterListener {
        void onListEnded();
    }
}
