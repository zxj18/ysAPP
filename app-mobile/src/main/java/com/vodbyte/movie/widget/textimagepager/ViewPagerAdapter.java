/*
 * Copyright (c) 2015.
 * 湖南球谱科技有限公司版权所有
 * Hunan Qiupu Technology Co., Ltd. all rights reserved.
 */

package com.vodbyte.movie.widget.textimagepager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vodbyte.movie.R;
import com.vodbyte.movie.widget.photoview.PhotoView;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ViewPagerAdapter";
    private View view;
    private List<ImagePagerBean> mImages;
    private Context mContext;
    private OnViewPagerAdapterListener mListener;

    public void setListener(OnViewPagerAdapterListener listener) {
        mListener = listener;
    }

    public ViewPagerAdapter(List<ImagePagerBean> urls, Context context) {
        mImages = urls;
        mContext = context;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        view = (View) object;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public View getPrimaryItem() {
        return view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImagePagerBean bean = mImages.get(position);
        final View view = LayoutInflater.from(mContext).inflate(R.layout.text_image_view_pager_item, container, false);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.pv_image);
        photoView.enable();

//        DrawableRequestBuilder<String> thumbnailRequest = Glide
//                .with(view.getContext())
//                .load(bean.getSmallUrl())
//                .crossFade();//加载缩略图;

        try {
            Glide.with(view.getContext())
                    .load(bean.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //  .thumbnail(thumbnailRequest)//加载缩略图
                    .fitCenter()
                    //   .crossFade()
                    .error(R.mipmap.ic_launcher)
                    .into(photoView);

        }catch (Exception e) {}

        photoView.setOnClickListener(arg0 -> {
            if (mListener != null) {
                mListener.onClickListener(arg0, position);
            }
        });
        photoView.setOnLongClickListener(view1 -> {
            if (mListener != null) {
                mListener.onLongClickListener(view1, position);
            }
            return false;
        });
        container.addView(view);
        view.setId(position);
        return view;
    }

    public interface OnViewPagerAdapterListener {
        void onClickListener(View view, int position);

        void onLongClickListener(View view, int position);
    }


}