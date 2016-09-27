package com.example.exerciseapp.aty.organzie.picview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.organzie.mutipick.BitmapCache;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.request.ImageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



public class ImageScanActivity2 extends AppCompatActivity implements View.OnClickListener {

    public ViewPager mViewPager;
    public ImageScanAdapter1 mAdapter;
    public List<View> mViews = new ArrayList<View>();
    public TextView txtIndex;
    public LayoutInflater mInflater;

    public String[] images;
    public int mCurrentIndex = 1;
    List<String> mUrls = new ArrayList<>();
    private TextView mDelete;
    private int flag;
    private List<String> list;
    private int requestCode;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(setContentRes());
        findViewById();
        setListener();
        mInflater = LayoutInflater.from(this);
        processData();
    }

    protected int setContentRes(){
        return R.layout.activity_image_scan;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        processData();
    }

    /**
     * @param context
     * @param imageids
     * @param index 从0开始
     * @return
     */
    public static Intent newIntent(Context context, String[] imageids, int index) {
        Intent intent = new Intent(context, ImageScanActivity2.class);
        intent.putExtra("imageids", imageids);
        intent.putExtra("index", index);
        return intent;
    }
    public static Intent newIntent(Context context, String[] imageids, int index,int flag,int requestCode) {
        Intent intent = new Intent(context, ImageScanActivity2.class);
        intent.putExtra("imageids", imageids);
        intent.putExtra("index", index);
        intent.putExtra("flag",flag);
        intent.putExtra("requestCode",requestCode);
        return intent;
    }

    private void processData() {
        Intent intent = getIntent();
        images = intent.getStringArrayExtra("imageids");
        list = new LinkedList<>();
        mCurrentIndex = intent.getIntExtra("index", 0);
        flag = intent.getIntExtra("flag",0);
        requestCode = intent.getIntExtra("requestCode",0);
        if (images!=null&&mCurrentIndex>=images.length){
            mCurrentIndex=images.length-1;
        }
        if (mCurrentIndex<0){
            mCurrentIndex=0;
        }
        if (images != null && images.length > 0) {
            txtIndex.setText(mCurrentIndex+1 + "/" + images.length);
            for(String s : images){
                list.add(s);
            }
            mUrls = Arrays.asList(images);
            mAdapter = new ImageScanAdapter1();
            mViewPager.setAdapter(mAdapter);
            mViewPager.setCurrentItem(mCurrentIndex);
            if(flag == 1 || flag == 2){	//进来的是单选
//				if(mDelete != null)
                mDelete.setVisibility(View.VISIBLE);
            }
            if(mDelete != null)
                mDelete.setOnClickListener(this);

        }
    }

    public void findViewById() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
        txtIndex = (TextView) findViewById(R.id.index);
        mDelete = (TextView) findViewById(R.id.delete);
    }

    public void setListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                txtIndex.setText(String.valueOf(arg0 + 1 + "/" + mAdapter.getCount()));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        mViewPager.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }
    public class ImageScanAdapter1 extends SimplePagerAdapter<String> {


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public View getItemView(ViewGroup container, int position) {

            View view = LayoutInflater.from(ImageScanActivity2.this).inflate(R.layout.image_scan_item,container,false);
            final PhotoView image = (PhotoView) view
                    .findViewById(R.id.photo_view);
            final ProgressBar pb = (ProgressBar) view
                    .findViewById(R.id.progress);

            if(list.get(position).startsWith("http")) {
                pb.setVisibility(View.VISIBLE);
                ImagePipeline mImagePipeline = Fresco.getImagePipeline();
                ImageRequest request = ImageRequest.fromUri(list.get(position));
                DataSource dataSource =
                        mImagePipeline.fetchDecodedImage(request, this);

                dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                         @Override
                                         public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                             // 你可以直接在这里使用Bitmap，没有别的限制要求，也不需要回收
                                             image.setImageBitmap(bitmap);
                                             pb.setVisibility(View.INVISIBLE);
                                             Log.e("onNewResultImpl", "image");
                                         }

                                         @Override
                                         public void onFailureImpl(DataSource dataSource) {
                                             pb.setVisibility(View.INVISIBLE);
                                         }
                                     },
                        UiThreadImmediateExecutorService.getInstance());
            }else{
                image.setImageBitmap(BitmapCache.revitionImageSize(list.get(position)));
            }
            return view;
        }

    }

    public class SimplePagerAdapter<T> extends PagerAdapter {

        private List<T> list = new ArrayList<T>();

        public SimplePagerAdapter() {
        }

        public SimplePagerAdapter(List<T> list) {
            setList(list);
        }

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            if (list == null) return;
            this.list = list;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            if (list != null && list.size() > 0) {
                return list.size();
            } else {
                return 0;
            }
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getItemView(container, position);
            container.addView(view);
            return view;
        }

        public View getItemView(ViewGroup container, int position) {

            return new View(container.getContext());
        }

        public T getItem(int position) {
            return list.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }


}