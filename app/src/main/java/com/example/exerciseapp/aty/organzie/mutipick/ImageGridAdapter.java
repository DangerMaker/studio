package com.example.exerciseapp.aty.organzie.mutipick;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.organzie.picview.ImageScanActivity2;

import java.util.LinkedList;
import java.util.List;



public class ImageGridAdapter extends BaseAdapter {

	private final int maxImages;
	private final int number;
	public LinkedList<String> mPaths = new LinkedList<>();
	Activity act;
	List<ImageItem> dataList;
	public static BitmapCache cache;
	BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
							  Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals(imageView.getTag())) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	};

	private int selectTotal = 0;
	private final String[] images;

	public ImageGridAdapter(Activity act, List<ImageItem> list, int maxImages, int number) {
		this.act = act;
		this.maxImages = maxImages;
		this.number = number;
		dataList = list;
		mPaths.clear();
		this.images = new String[dataList.size()];
		for(int i = 0;i<dataList.size();i++){
			this.images[i] = dataList.get(i).imagePath;
			if(mPaths.contains(dataList.get(i).imagePath)){
				dataList.get(i).isSelected = true;
			}
		}
		cache = new BitmapCache();
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView text;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(act, R.layout.item_image_grid, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView
					.findViewById(R.id.isselected);
			holder.text = (TextView) convertView
					.findViewById(R.id.item_image_grid_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final ImageItem item = dataList.get(position);

		holder.iv.setTag(item.imagePath);
		cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
				callback);

		holder.selected.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String path = dataList.get(position).imagePath;
				if ((selectTotal+number) < maxImages) {
					item.isSelected = !item.isSelected;
					if (item.isSelected) {
						holder.selected	.setImageResource(R.mipmap.pictures_selected);
						holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
						selectTotal++;
						mPaths.add(path);
					} else if (!item.isSelected) {
						holder.selected.setImageResource(R.mipmap.picture_unselected);
						holder.text.setBackgroundColor(0x00000000);
						selectTotal--;
						mPaths.remove(path);
					}
				} else if ((selectTotal+number) >= maxImages) {
					if (item.isSelected == true) {
						item.isSelected = !item.isSelected;
						holder.selected.setImageResource(R.mipmap.picture_unselected);
						holder.text.setBackgroundColor(0x00000000);
						selectTotal--;
						mPaths.remove(path);
					} else {
//						EventBus.getDefault().post(new ImageMaxEvent());
					}
				}
			}
		});
		if (item.isSelected) {
			holder.selected.setImageResource(R.mipmap.pictures_selected);
			holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
		} else {
			holder.selected.setImageResource(R.mipmap.picture_unselected);
			holder.text.setBackgroundColor(0x00000000);
		}
		holder.iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = ImageScanActivity2.newIntent(act,images,position);
				act.startActivity(intent);
			}
		});
		return convertView;
	}

	public List<String> getPaths(){
		return mPaths;
	}
}
