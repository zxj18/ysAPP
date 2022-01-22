package com.vodbyte.freetv.mvp.view.activity.detail;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.ybq.android.spinkit.SpinKitView;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.base.BaseActivity;
import com.vodbyte.freetv.base.BasePresenter;
import com.vodbyte.freetv.bean.ConfigBean;
import com.vodbyte.freetv.bean.VodBean;
import com.vodbyte.freetv.common.FocusAction;
import com.vodbyte.freetv.common.InternalFileSaveUtil;
import com.vodbyte.freetv.common.Ui;
import com.vodbyte.freetv.contract.collect.VideoCollect;
import com.vodbyte.freetv.listener.OnVodCidClickListener;
import com.vodbyte.freetv.mvp.contract.IVodDetailContract;
import com.vodbyte.freetv.mvp.model.dto.VodDetailDTO;
import com.vodbyte.freetv.mvp.presenter.VodDetailPresenter;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.freetv.utils.GsonUtil;
import com.vodbyte.freetv.utils.StartActUtil;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_DURATION;

public class VideoDetailActivity extends BaseActivity<VodDetailPresenter> implements IVodDetailContract.View {

    @BindView(R.id.detail_root)
    View detail_root;

    @BindView(R.id.top_bar_menu_root_home)
    View top_bar_menu_root_home;

    @BindView(R.id.top_bar_menu_root_search)
    View top_bar_menu_root_search;

    @BindView(R.id.detail_root_content)
    View detail_root_content;

    @BindView(R.id.detail_tv_content)
    TextView detail_tv_content;

    @BindView(R.id.detail_tv_content_more)
    TextView detail_tv_content_more;

    @BindView(R.id.detail_menu_root_collect)
    View detail_menu_root_collect;

    @BindView(R.id.detail_iv_collect)
    ImageView detail_iv_collect;

    @BindView(R.id.detail_tv_collect)
    TextView detail_tv_collect;

    @BindView(R.id.video_detail_recycler_view)
    RecyclerView video_detail_recycler_view;

    @BindView(R.id.video_detail_sub_view)
    RecyclerView video_detail_sub_view;

    @BindView(R.id.detail_iv_image)
    ImageView detail_iv_image;

    @BindView(R.id.detail_tv_title)
    TextView detail_tv_title;

    @BindView(R.id.detail_tv_video_tag)
    TextView detail_tv_video_tag;

    @BindView(R.id.detail_tv_director)
    TextView detail_tv_director;

    @BindView(R.id.detail_tv_actor)
    TextView detail_tv_actor;

    @BindView(R.id.detail_menu_root_return_error)
    View detail_menu_root_return_error;

    @BindView(R.id.detail_iv_return_error)
    ImageView detail_iv_return_error;

    @BindView(R.id.detail_tv_return_error)
    TextView detail_tv_return_error;

    @BindView(R.id.qrcode_view)
    ImageView mQrcodeView;

    @BindView(R.id.spin_kit)
    protected SpinKitView mSpinKit;

    private VideoPlayListAdapter mAdapter;
    private VodBean mVodItem;
    private int mPlayIndex = 0;
    private Context mContext;
    private RvSimpleAdapter rvSimpleAdapter;
    private VodBean.VodPlayBean.UrlBean mUrlBean;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected VodDetailPresenter getInstance() {
        return new VodDetailPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mVodItem = (VodBean) GsonUtil.gsonToBean(intent.getStringExtra("vod"),VodBean.class);
        mPresenter.getVodDetailData(mVodItem.getVid());

        rvSimpleAdapter = new RvSimpleAdapter();
        video_detail_sub_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        video_detail_sub_view.setAdapter(rvSimpleAdapter);
        showSpinKit(true);
    }

    @Override
    protected void initListener() {

        Ui.configTopBar(this);

        setContentFocusAnimator(this, detail_root_content, new FocusAction() {
            @Override
            public void onFocus() {
                detail_tv_content.setTextColor(getResources().getColor(R.color.colorTextFocus));
                detail_tv_content_more.setTextColor(getResources().getColor(R.color.colorTextFocus));
            }

            @Override
            public void onLoseFocus() {
                detail_tv_content.setTextColor(getResources().getColor(R.color.colorTextNormal));
                detail_tv_content_more.setTextColor(getResources().getColor(R.color.colorTextNormal));
            }
        });

        Ui.setViewFocusScaleAnimator(detail_menu_root_collect, new FocusAction() {
            @Override
            public void onFocus() {
                detail_tv_collect.setTextColor(getResources().getColor(R.color.colorTextFocus));
                detail_menu_root_collect.setBackground(getResources().getDrawable(R.drawable.bg_common_menu_focus));
                if (detail_tv_collect.getText().equals("收藏")) {
                    detail_iv_collect.setImageResource(R.drawable.ic_collected_focus);
                } else {
                    detail_iv_collect.setImageResource(R.drawable.ic_not_collected_focus);
                }
            }

            @Override
            public void onLoseFocus() {
                detail_menu_root_collect.setBackground(getResources().getDrawable(R.drawable.bg_common_menu_normal));
                detail_tv_collect.setTextColor(getResources().getColor(R.color.colorTextNormal));
                if (detail_tv_collect.getText().equals("收藏")) {
                    detail_iv_collect.setImageResource(R.drawable.ic_collected_normal);
                } else {
                    detail_iv_collect.setImageResource(R.drawable.ic_not_collected_normal);
                }
            }
        });

        Ui.setViewFocusScaleAnimator(detail_menu_root_return_error, new FocusAction() {
            @Override
            public void onFocus() {
                detail_menu_root_return_error.setBackground(getResources().getDrawable(R.drawable.bg_common_menu_focus));
                detail_iv_return_error.setImageResource(R.drawable.ic_return_error_focus);
                detail_tv_return_error.setTextColor(getResources().getColor(R.color.colorTextFocus));
            }

            @Override
            public void onLoseFocus() {
                detail_menu_root_return_error.setBackground(getResources().getDrawable(R.drawable.bg_common_menu_normal));
                detail_iv_return_error.setImageResource(R.drawable.ic_return_error_normal);
                detail_tv_return_error.setTextColor(getResources().getColor(R.color.colorTextNormal));
            }
        });

    }


    @Override
    public void setVodDetailData(VodDetailDTO vodDetailData) {
        mVodItem = vodDetailData.getData();
        rvSimpleAdapter.clear();
        showSpinKit(false);

        if (vodDetailData.getData().getVod_play_list() != null && vodDetailData.getData().getVod_play_list().size() > 0)
        {

           rvSimpleAdapter.addAll(vodDetailData.getData().getVodSubSourceCell(new OnVodCidClickListener() {
               @Override
               public void onViewClick(View view, int position) {

               }

               @Override
               public void onDataClick(String data, int position) {
                    showToast("切换至:" + data);
                    mPlayIndex = position;
                    refreshUrlBeanList(position);
               }
           }));
           //
           refreshUrlBeanList(mPlayIndex);
        }
    }

    /**
     * 刷新播放列表
     * @param index
     */
    private void refreshUrlBeanList(final int index)
    {
        VodBean.VodPlayBean playBean = mVodItem.getVod_play_list().get(index);

        if (playBean.getQrcode_url().isEmpty()) {
            mQrcodeView.setVisibility(View.GONE);
        }else {
            mQrcodeView.setVisibility(View.VISIBLE);
            Glide.with(this).load(playBean.getQrcode_url()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(mQrcodeView);
        }

        mAdapter = new VideoPlayListAdapter(this, playBean.getUrls(), new VideoPlayListAdapter.OnPartClickListener() {
            @Override
            public void onPartClick(int position) {
                try {
                    mUrlBean = mVodItem.getVod_play_list().get(index).getUrls().get(position);
                    saveHistory();
                    StartActUtil.toPlayPlayActivity(mContext,String.format("%s-%s",mVodItem.getVod_name(),mUrlBean.getName()),mUrlBean.getUrl());
                }catch (Exception e) {
                    // Log.d("eee",e.getMessage());
                }
            }
        });
        video_detail_recycler_view.setAdapter(mAdapter);
    }

    protected void initView() {
        mContext = this;

        top_bar_menu_root_home.setNextFocusDownId(R.id.video_detail_sub_view);
        top_bar_menu_root_search.setNextFocusDownId(R.id.video_detail_sub_view);

        int[] bgArray = new int[]{R.drawable.bg_detail_1, R.drawable.bg_detail_2, R.drawable.bg_detail_3, R.drawable.bg_detail_4};
        detail_root.setBackground(getResources().getDrawable(bgArray[(int) (Math.random() * 4)]));

        video_detail_recycler_view.setLayoutManager(new GridLayoutManager(this, 8));

        try {
            // 加载图片和名称
            detail_tv_title.setText(mVodItem.getVod_name());
            Glide.with(this).load(mVodItem.getVod_pic()).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).into(detail_iv_image);

            // 收藏判断
//        LinkedList<VideoCollect> videoCollectList = (LinkedList<VideoCollect>) InternalFileSaveUtil.getInstance(this).get("video_collect");
//        if (videoCollectList != null) {
////            for (int i = 0; i < videoCollectList.size(); i++) {
////                if (videoCollectList.get(i).getVideo().getVod_name().equals(mVodItem.getVod_name())
////                        && videoCollectList.get(i).getVideoEngine().equals(Model.getData().getVideoEngine(this))) {
////                    detail_tv_collect.setText("取消收藏");
////                    detail_iv_collect.setImageResource(R.drawable.ic_not_collected_normal);
////                    break;
////                }
////            }
//        }

            String tag = mVodItem.getVod_area() + " · " + mVodItem.getVod_year() + " · " + mVodItem.getVod_class();
            if (mVodItem.getVod_lang() != null && !mVodItem.getVod_lang().isEmpty()) {
                tag = tag + " · " + mVodItem.getVod_lang();
            }
            detail_tv_video_tag.setText("地区：" + tag);
            detail_tv_director.setText("导演：" + mVodItem.getVod_director());
            detail_tv_actor.setText("主演：" + mVodItem.getVod_actor());
            detail_tv_content.setText("简介：" + mVodItem.getVod_blurb());
        }catch (Exception e){

        }
    }

    @OnClick(R.id.detail_menu_root_return_error)
    public void onReturnClick() {

    }

    @OnClick(R.id.detail_menu_root_collect)
    public void onCollectClick() {
        LinkedList<VideoCollect> videoCollectList = (LinkedList<VideoCollect>) InternalFileSaveUtil.getInstance(this).get("video_collect");
        if (videoCollectList == null) {
            videoCollectList = new LinkedList<>();
        }

        if (detail_tv_collect.getText().equals("收藏")) {
            // 收藏
            VideoCollect videoCollect = new VideoCollect();
            videoCollect.setVideo(mVodItem);
            //videoCollect.setVideoEngine(Model.getData().getVideoEngine(this));

            videoCollectList.add(0, videoCollect);
            if (videoCollectList.size() > 1) {
//                for (int i = 1; i < videoCollectList.size(); i++) {
//                    if (videoCollectList.get(i).getVideo().getVod_name().equals(mVodItem.getVod_name())
//                            && videoCollectList.get(i).getVideoEngine().equals(Model.getData().getVideoEngine(this))) {
//                        videoCollectList.remove(i);
//                        break;
//                    }
//                }
            }

            if (videoCollectList.size() > Constant.VIDEO_COLLECTION_NUM) {
                videoCollectList.remove(videoCollectList.size() - 1);
            }
            InternalFileSaveUtil.getInstance(this).put("video_collect", videoCollectList);

            detail_tv_collect.setText("取消收藏");
            detail_iv_collect.setImageResource(R.drawable.ic_not_collected_focus);
        } else {
            // 取消收藏
            for (int i = 1; i < videoCollectList.size(); i++) {
//                if (videoCollectList.get(i).getVideo().getVod_name().equals(mVodItem.getVod_name())
//                        && videoCollectList.get(i).getVideoEngine().equals(Model.getData().getVideoEngine(this))) {
//                    videoCollectList.remove(i);
//                    break;
//                }
            }
            InternalFileSaveUtil.getInstance(this).put("video_collect", videoCollectList);

            detail_tv_collect.setText("收藏");
            detail_iv_collect.setImageResource(R.drawable.ic_collected_focus);
        }


    }


    private static void setContentFocusAnimator(Activity activity, View view, FocusAction action) {
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                view.setBackground(activity.getResources().getDrawable(R.drawable.video_detail_content_focus));
                if (action != null) {
                    action.onFocus();
                }

                ValueAnimator animatorFirst = ValueAnimator.ofPropertyValuesHolder(
                        PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.02f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.2f)
                ).setDuration(ANIMATION_ZOOM_IN_DURATION);
                ValueAnimator animatorSecond = ValueAnimator.ofPropertyValuesHolder(
                        PropertyValuesHolder.ofFloat("scaleX", 1.02f, 1.01f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.2f, 1.1f)
                ).setDuration(ANIMATION_ZOOM_OUT_DURATION);

                animatorFirst.addUpdateListener(animation -> {
                    if (view.isFocused()) {
                        view.setScaleX((float) animation.getAnimatedValue("scaleX"));
                        view.setScaleY((float) animation.getAnimatedValue("scaleY"));
                    } else {
                        animatorFirst.cancel();
                    }
                });
                animatorSecond.addUpdateListener(animation -> {
                    if (view.isFocused()) {
                        view.setScaleX((float) animation.getAnimatedValue("scaleX"));
                        view.setScaleY((float) animation.getAnimatedValue("scaleY"));
                    } else {
                        animatorFirst.cancel();
                    }
                });
                animatorFirst.start();
                animatorSecond.setStartDelay(ANIMATION_ZOOM_IN_DURATION);
                animatorSecond.start();
            } else {
                view.setBackground(null);
                if (action != null) {
                    action.onLoseFocus();
                }
                ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(
                        PropertyValuesHolder.ofFloat("scaleX", 1.01f, 1.0f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.1f, 1.0f)
                ).setDuration(ANIMATION_ZOOM_IN_DURATION);
                animator.addUpdateListener(animation -> {
                    view.setScaleX((float) animation.getAnimatedValue("scaleX"));
                    view.setScaleY((float) animation.getAnimatedValue("scaleY"));
                });
                animator.start();
            }
        });
    }

    @Override
    public void onError(String msg) {
        showSpinKit(false);
        showToast(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void showSpinKit(boolean isShow)
    {
        mSpinKit.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 保存历史
     */
    private void saveHistory() {
        LinkedList<VodBean> historyList = (LinkedList<VodBean>) InternalFileSaveUtil.getInstance(this).get("video_history");
        if (historyList == null) {
            historyList = new LinkedList<>();
        }
        historyList.add(0, mVodItem);
        // 同名视频只添加去除之前的历史
        if (historyList.size() > 1) {
            for (int i = 1 ; i < historyList.size() ; i++) {
                if (historyList.get(i).getVod_name().equals(mVodItem.getVod_name())) {
                    historyList.remove(i);
                    break;
                }
            }
        }
        if (historyList.size() > Constant.VIDEO_HISTORY_NUM) {
            historyList.remove(historyList.size() - 1);
        }
        InternalFileSaveUtil.getInstance(this).put("video_history", historyList);
    }

}
