package com.example.exerciseapp.aty.organzie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.organzie.picview.ImageScanActivity2;
import com.example.exerciseapp.model.PhotoModel;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.utils.UiUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/6.
 */
public class PhotoItemView extends RelativeLayout implements View.OnClickListener {

    PhotoModel.DataBean data;
    String[] images;
    protected int item_width, item_height, margin;

    @Bind(R.id.date)
    TextView mChannelName;

    @Bind(R.id.layout_grid)
    GridLayout mGridView;

    public PhotoItemView(Context context) {
        super(context);
        init();
    }

    public PhotoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_photo_list, this);
        ButterKnife.bind(this, view);
        float screen_width = ScreenUtils.getScreenWidth(getContext());
        margin = UiUtils.dip2px(getContext(), 4);
        item_width = (int) ((screen_width - margin * 5) / 4f);
        item_height = item_width;
    }

    public void addItems(PhotoModel.DataBean data) {
        this.data = data;

        List<String> picUrlList = new ArrayList<>();
        for (int i = 0; i < data.getPic_list().size(); i++) {
            picUrlList.add(data.getPic_list().get(i).getPic_path());
        }

        images =new String[picUrlList.size()];
        picUrlList.toArray(images);
        mGridView.removeAllViews();
        int count = 0;
        for (PhotoModel.DataBean.PicListBean item : data.getPic_list()) {
            View convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.photo, this.mGridView, false);

//            GenericDraweeHierarchy hierarchy = imageview.getHierarchy();
//            hierarchy.setPlaceholderImage(R.drawable.placeholder_banner);
//            imageview.setBackgroundResource(R.drawable.default_loading_bg);
            GridLayout.LayoutParams lp = (GridLayout.LayoutParams)convertView.getLayoutParams();
            convertView.setOnClickListener(this);
            convertView.setTag(count);
            lp.width = this.item_width;
            lp.height = this.item_height;
            lp.setMargins(this.margin / 2, 0, this.margin / 2, this.margin);
            mGridView.addView(convertView);
            setItemData(convertView, item);
            count ++ ;
        }
        mChannelName.setText(data.getDate());
    }

    public void setItemData(View view, PhotoModel.DataBean.PicListBean bean) {
        ((SimpleDraweeView)view.findViewById(R.id.item_img)).setImageURI(Uri.parse(bean.getPic_thumb_path()));
    }

    @Override
    public void onClick(View view) {
        String [] s = images;
        int a = (int)view.getTag();
        Intent intent = ImageScanActivity2.newIntent(getContext(), images, (int)view.getTag(), 0,200);
        getContext().startActivity(intent);

    }
}
