package com.vodbyte.share.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.vodbyte.share.FastBlur;
import com.vodbyte.share.R;
import com.vodbyte.share.ShareManager;
import com.vodbyte.share.info.IShareInfo;
import com.vodbyte.share.templet.AbsWarpTemplateShare;
import com.vodbyte.share.way.BaseShareWay;

import java.util.List;

/**
 * 屏幕底部弹出菜单中是一个个可以点击的图片gird item
 * Created by GuDong on 2014/11/24.
 */
public class ShareView extends PopupWindow implements View.OnClickListener, View.OnTouchListener {
    /**
     * 设置要分享的内容，不同分享内容 自己实现分享内容的格式
     *
     * @param activity
     * @param shareInfo 具体分享的内容
     */
    BaseShareWay share;
    private View shareView;
    private View fullMaskView;
    private ViewStub mViewStub;
    private LinearLayout llContent;
    private GridView mGirdView;
    private TextView mTvClose;
    private int mShareItemTextColor = R.color.black;

    private Bitmap mBitmap = null;
    private Bitmap overlay = null;

    private long duration;
    private Activity mContext;
    private int mWidth;
    private int mHeight = 0;
    private OnItemClickListener mOnItemClickListener;

    public ShareView(Activity context) {
        this.mContext = context;
        setUpView(context);
        duration = 250;
    }

    public ShareView(Activity context, String title) {
        this(context);
        setTitle(title);
    }

    private void setUpView(final Activity context) {

        mWidth = ScreenUtils.getScreenWidth();
        mHeight = ScreenUtils.getScreenHeight();

        setWidth(mWidth);
        setHeight(mHeight);

        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        setTouchable(true);
        setClippingEnabled(false);
        setFocusable(true);

        shareView = View.inflate(context, R.layout.library_layout_share_view, null);
        fullMaskView = shareView.findViewById(R.id.full_mask);
        mViewStub = (ViewStub) shareView.findViewById(R.id.view_stub);
        mViewStub.inflate();
        llContent = (LinearLayout) shareView.findViewById(R.id.ll_content);
        mGirdView = (GridView) shareView.findViewById(R.id.gv_share);
        mTvClose = (TextView) shareView.findViewById(R.id.tv_close);

        setContentView(shareView);
        initListener();
    }

    private void initListener() {
        shareView.findViewById(R.id.tv_close).setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.full_mask || event.getAction() == KeyEvent.ACTION_DOWN) {
            hideView();
        }
        return true;
    }

    public void setTitle(String title) {
        TextView tvTitle = (TextView) shareView.findViewById(R.id.tv_share_title);
        tvTitle.setText(title);
        tvTitle.setVisibility(!TextUtils.isEmpty(title) ? View.VISIBLE : View.GONE);
    }

    public void setTitleColor(int titleColor) {
        TextView tvTitle = (TextView) shareView.findViewById(R.id.tv_share_title);
        tvTitle.setTextColor(tvTitle.getResources().getColor(titleColor));
    }

    /**
     * 显示view
     *
     * @param anchor
     */
    public void showView(View anchor) {
        showView(anchor, 0);
    }

    public void showView(View anchor, int bottomMargin) {

        //设置背景
        setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur(mContext)));
        shareView.setVisibility(View.VISIBLE);
        shareViewAnimator(mHeight, 0, 0.0f, 0.5f);

        showAtLocation(anchor, Gravity.BOTTOM, 0, bottomMargin);
    }

    /**
     * 隐藏view
     */
    public void hideView() {
        if (isShowing()) {
            shareViewAnimator(0, mHeight, 0.5f, 0.0f);
        }
    }

    private void shareViewAnimator(final float start, final float end, final float startAlpha, final float endAlpha) {
        shareViewTranslationAnimator(start, end);
        shareViewAlphaAnimator(startAlpha, endAlpha);
//        shareViewRotationAnimator(0f,30f);
    }

    private void shareViewTranslationAnimator(final float start, final float end) {
        if (Build.VERSION.SDK_INT >= 11) {
            ObjectAnimator bottomAnim = ObjectAnimator.ofFloat(llContent, "translationY", start, end);
            bottomAnim.setDuration(duration);
            bottomAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
                    if (start < end) {
                        //close ShareView
                        shareView.setVisibility(View.GONE);
                        dismiss();
                    } else {//show ShareView
                    }
                }
            });
            bottomAnim.start();
        }
    }

    private void shareViewAlphaAnimator(final float startAlpha, final float endAlpha) {
        if (Build.VERSION.SDK_INT >= 11) {
            ObjectAnimator bottomAnimAlpha = ObjectAnimator.ofFloat(fullMaskView, "alpha", startAlpha, endAlpha);
            bottomAnimAlpha.setDuration(duration);
            bottomAnimAlpha.start();
        }
    }

    public void setAnimatorDuration(long duration) {
        this.duration = duration;
    }

    private void shareViewRotationAnimator(final float startAngle, final float endAngle) {
        if (Build.VERSION.SDK_INT >= 11) {
            ObjectAnimator bottomAnimAlpha = ObjectAnimator.ofFloat(mTvClose, "rotation", startAngle, endAngle);
            bottomAnimAlpha.setDuration(duration);
            bottomAnimAlpha.start();
        }
    }

    /**
     * 系统分享
     *
     * @param activity
     * @param shareInfo
     */
    public void setSystemShareInfo(final Activity activity, AbsWarpTemplateShare shareInfo) {
        final List<IShareInfo> listInfo = shareInfo.getListInfo();
        final List<BaseShareWay> list = ShareManager.getShareWay(activity);
        mGirdView.setAdapter(new ShareAdapter(activity, list));
        mGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                share = list.get(position);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.itemClick(share, listInfo.get(position));
                } else {
                    share.onSystemShare(listInfo.get(position));
                }
            }
        });
        initHeight();
    }

    /**
     * api接口分享
     *
     * @param activity
     * @param shareInfo
     */
    public void setShareInfo(final Activity activity, AbsWarpTemplateShare shareInfo) {
        final List<IShareInfo> listInfo = shareInfo.getListInfo();
        final List<BaseShareWay> list = ShareManager.getShareWay(activity);
        ShareAdapter shareAdapter = new ShareAdapter(activity, list);
        mGirdView.setAdapter(shareAdapter);

        shareAdapter.setItemClickListener ((position) -> {
            share = list.get(position);
            if (mOnItemClickListener != null) {
                mOnItemClickListener.itemClick(share, listInfo.get(position));
            } else {
                share.onShare(listInfo.get(position));
            }

        });
        initHeight();
    }

    /**
     * 系统分享监听分享图点击
     *
     * @param activity
     * @param shareInfo
     * @param onItemClickListener
     */
    public void setSystemShareInfo(Activity activity, AbsWarpTemplateShare shareInfo, OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        this.setSystemShareInfo(activity, shareInfo);
    }

    /**
     * 分享监听分享图点击
     *
     * @param activity
     * @param shareInfo
     * @param onItemClickListener
     */
    public void setShareInfo(Activity activity, AbsWarpTemplateShare shareInfo, OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        this.setShareInfo(activity, shareInfo);
    }

    public interface OnItemClickListener {
        public void itemClick(BaseShareWay baseShareWay, IShareInfo shareInfo);
    }

    private void initHeight() {
        mHeight = getViewHeight(llContent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_close) {
            hideView();
        }
    }

    /**
     * 设置分享的背景图片
     *
     * @param shareBackground
     */
    public void setShareBackground(int shareBackground) {
        shareView.setBackgroundResource(shareBackground);
    }

    public void setShareItemTextColor(int shareItemTextColor) {
        this.mShareItemTextColor = shareItemTextColor;
    }

    public void setOnActivityResult(int requestCode, int resultCode, Intent data) {
        share.onActivityResult(requestCode, resultCode, data);
    }

    private int getViewHeight(View mView) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        mView.measure(w, h);
        return mView.getMeasuredHeight();
    }

    /**
     * 点击透明背景对应的监听事件处理 默认点击背景会隐藏整个dialog
     */
    private class OnClickCloseIcon implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            hideView();
        }
    }

    public static class ShareAdapter extends BaseAdapter {
        private List<BaseShareWay> mData;
        private Context mContext;
        private LayoutInflater mInflater;
        private OnItemClickListener mOnItemClickListener;

        public ShareAdapter(Context context, List<BaseShareWay> data) {
            mContext = context;
            mData = data;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tView = (TextView) mInflater.inflate(R.layout.library_share_item, null);
            BaseShareWay info = (BaseShareWay) getItem(position);
            String label = info.getTitle();
            Drawable icon = mContext.getResources().getDrawable(info.getResIcon());
            tView.setText(label);
            tView.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
            tView.setTextColor(mContext.getResources().getColor(R.color.black));
            tView.setClickable(true);

            tView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.itemClick(position);
                }
            });

            return tView;
        }

        public void setItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public interface OnItemClickListener {
             void itemClick(int position);
        }
    }

    /**
     * 高斯模糊
     *
     * @param context 上下文
     * @return
     */
    private Bitmap blur(Activity context) {
        if (null != overlay) {
            return overlay;
        }
        long startMs = System.currentTimeMillis();

        View view = context.getWindow().getDecorView();

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        mBitmap = view.getDrawingCache();

        float scaleFactor = 8;//图片缩放比例；
        float radius = 10;//模糊程度

        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int)(height / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
//        Log.i(TAG, "blur time is:" + (System.currentTimeMillis() - startMs));
        return overlay;
    }


    public void destroy() {
        if (null != overlay) {
            overlay.recycle();
            overlay = null;
            System.gc();
        }
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
    }
}
