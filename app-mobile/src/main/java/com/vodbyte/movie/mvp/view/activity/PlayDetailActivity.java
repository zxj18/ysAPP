package com.vodbyte.movie.mvp.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.TrafficStats;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cdnbye.core.p2p.EngineExceptionListener;
import com.cdnbye.core.utils.EngineException;
import com.cdnbye.sdk.P2pEngine;
import com.fiio.sdk.callBack.RewardVideoAdCallBack;
import com.fiio.sdk.view.RewardVideoLoader;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.BuildConfig;
import com.vodbyte.movie.R;
import com.vodbyte.movie.app.DefaultShareImpl;
import com.vodbyte.movie.base.BaseActivity;
import com.vodbyte.movie.base.BaseLazyFragment;
import com.vodbyte.movie.bean.ChapterBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.mvp.contract.IVodDetailContract;
import com.vodbyte.movie.mvp.model.dto.VodDetailDTO;
import com.vodbyte.movie.mvp.presenter.VodDetailPresenter;
import com.vodbyte.movie.mvp.view.fragment.VodDetail.TabCommentFragment;
import com.vodbyte.movie.mvp.view.fragment.VodDetail.TabDetailFragment;
import com.vodbyte.movie.utils.DeviceIDUtils;
import com.vodbyte.movie.utils.GsonUtil;
import com.vodbyte.movie.utils.ImageUtils;
import com.vodbyte.movie.utils.PrefsUtils;
import com.vodbyte.movie.utils.StartActUtil;
import com.vodbyte.movie.utils.VodUtil;
import com.vodbyte.movie.widget.component.MyDanmakuView;
import com.vodbyte.movie.widget.component.PlayerMonitor;
import com.vodbyte.movie.widget.videoview.IjkVideoView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import com.vodbyte.share.info.SimpleShareText;
import com.vodbyte.share.view.ShareView;
import com.vodbyte.videocontroller.StandardVideoController;
import com.vodbyte.videocontroller.component.CompleteView;
import com.vodbyte.movie.widget.component.DefinitionControlView;
import com.vodbyte.videocontroller.component.ErrorView;
import com.vodbyte.videocontroller.component.GestureView;
import com.vodbyte.videocontroller.component.LogoView;
import com.vodbyte.videocontroller.component.PrepareView;
import com.vodbyte.videocontroller.component.TitleView;
import com.vodbyte.videocontroller.component.VodControlView;
import com.vodbyte.videoplayer.player.VideoView;
import com.vodbyte.videoplayer.player.VideoViewManager;
import com.vodbyte.videoplayer.util.L;

/**
 * 播放详情
 */
public class PlayDetailActivity extends BaseActivity<VodDetailPresenter>
        implements IVodDetailContract.View {

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.tabLayout_act_vod_detail)
    TabLayout mTabLayout;
    @BindView(R.id.iv_back_layout_top)
    ImageView mIvBack;
    @BindView(R.id.iv_right_layout_top)
    ImageView mIvCastScreen;
    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;
    @BindView(R.id.layout_top_act_vod_detail)
    View mTopLayout;
    @BindView(R.id.cl_top_layout_act_book_detail)
    ConstraintLayout mHeaderLayout;
    @BindView(R.id.vp_container_act_book_detail)
    ViewPager mVpContainer;
    @BindView(R.id.toolbar_act_book_detail)
    Toolbar mToolbar;
    @BindView(R.id.app_bar_layout_act_book_detail)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_layout_act_book_detail)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.player_video_view)
    protected IjkVideoView mVideoView;
    private MyDanmakuView mMyDanmakuView;

    private Context mContext;
    private List<BaseLazyFragment> mFList;
    private final String[] mTitles = {"详情"};
    private VodBean mVodItem;
    private Integer mVodIndex = 0;
    private VodBean.VodPlayBean.UrlBean mUrlBean;
    private int mPlayErrorNum = 0;
    private StandardVideoController mController;
    NetThread netThread;
    private boolean isCheck = false;
    TitleView mTitleView;
    private ShareView mShareView;
    public static RewardVideoLoader rewardVideoLoader;
    boolean isLoadAd = false;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_play_detail;
    }

    @Override
    protected VodDetailPresenter getInstance() {
        return new VodDetailPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Activity activity = this;

        Intent intent = getIntent();
        mVodItem = (VodBean) GsonUtil.gsonToBean(intent.getStringExtra("vod"),VodBean.class);
        mPresenter.getVodDetailData(mVodItem.getVid());

        mFList = new ArrayList<>();

        if (mVodItem.getVid() != 0){
            TabDetailFragment detailFragment = new TabDetailFragment();
            detailFragment.setBtnListener((view, index) -> {
                // 分享
                if (index == 3) {
                    SimpleShareText mSimpleShareText = new SimpleShareText(mVodItem.getVod_name(), mVodItem.getVod_blurb(),mConfig.getShare_url(), mVodItem.getVod_pic());
                    mShareView.setShareInfo(activity, new DefaultShareImpl(mContext, mSimpleShareText));
                    mShareView.showView(view);
                }else if (index == 0){
                    toFeedBack();
                }
            });

            detailFragment.setSubSetListener((vodIndex,urlBean,isP2p) -> {
                mVodIndex = vodIndex;

                try {
                    mUrlBean = urlBean;
                    mTitleView.setTitle(String.format("%s-%s",mVodItem.getVod_name(),urlBean.getName()));
                   // mVideoView.setUrl(urlBean.getUrl());

                    if (P2pEngine.getInstance() != null && !mConfig.getP2p_token().isEmpty() && isP2p) {
                        String parsedUrl = P2pEngine.getInstance().parseStreamUrl(urlBean.getUrl());
//                        Log.d("parsedUrl ",urlBean.getUrl());
//                        Log.d("parsedUrl ",parsedUrl);
                        mVideoView.setUrl(parsedUrl);
                    }else {
                        mVideoView.setUrl(urlBean.getUrl());
                    }

                    if (BuildConfig.isDemo) {
                        mVideoView.setUrl("https://vfx.mtime.cn/Video/2021/04/20/mp4/210420111203508147_1080.mp4");
                    }

                    if (!isLoadAd && mConfig.getAd().isOpen_ad() && !mConfig.getAd().getAd_reward_video_id().isEmpty()) {

                        new XPopup.Builder(mContext)
                                .isDestroyOnDismiss(true)
                                .asConfirm(null, "您需要观看一条广告后才能观影!",
                                        "取消", "观看",
                                        new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                loadAd();
                                            }
                                        }, null, false).show();

                    }else {
                        mVideoView.release();
                        mVideoView.start();
                        VodUtil.uploadVodEvent(mContext,"start_play",mVodItem.getVod_name(),mVodItem.getVid()+"",mUrlBean.getUrl(),mVodItem.getVod_play_list().get(mVodIndex).getFrom());
                    }

                }catch (Exception e) {
                    Log.d(TAG,e.getMessage());
                }

            });

            mFList.add(detailFragment);
//            mFList.add(new TabCommentFragment());
        }

        //判断是否需要加载广告
        long currentTime = System.currentTimeMillis() / 1000;
        if (mConfig.getAd().isOpen_ad()) {
            if ((PrefsUtils.with(this).readLong("isLoadAdTime") + mConfig.getAd().getAd_reward_video_time()) < currentTime) {
                PrefsUtils.with(this).writeLong("isLoadAdTime",currentTime);
                isLoadAd = false;
            }else {
                isLoadAd = true;
            }
        }

    }

    @Override
    protected void initView() {
        mContext = this;
        mShareView = new ShareView(this, "分享");

        VideoViewManager.instance().add(mVideoView,"PlayDetailActivity");

        mTopLayout.setBackgroundResource(R.color.transparent);
        mIvCastScreen.setVisibility(View.VISIBLE);
        mIvBack.setImageResource(R.drawable.ic_title_white_back);
        setSupportActionBar(mToolbar);

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            mCollapsingToolbarLayout.setTitle("");

            if (verticalOffset <= -mHeaderLayout.getHeight() / 2){
                setStatusTextColor(true);
                mIvBack.setImageResource(R.drawable.ic_title_white_red);
                mIvCastScreen.setImageResource(R.drawable.cast_screen_icon_red);
                mTvTitle.setVisibility(View.VISIBLE);
            }else {
                setStatusTextColor(false);
                mIvBack.setImageResource(R.drawable.ic_title_white_back);
                mIvCastScreen.setImageResource(R.drawable.cast_screen_icon_white);
                mTvTitle.setVisibility(View.INVISIBLE);
            }

        });
        mVpContainer.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFList.get(position);
            }

            @Override
            public int getCount() {
                return mFList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        mVpContainer.setOffscreenPageLimit(0);
        mTabLayout.setupWithViewPager(mVpContainer);


        // 播放器相关
        mTitleView = new TitleView(this); //标题栏
        mController = new StandardVideoController(this);
        //根据屏幕方向自动进入/退出全屏
        mController.setEnableOrientation(false);
        // logo
        LogoView logoView = new LogoView(this);
        logoView.setLogo(R.mipmap.logo);
        mController.addControlComponent(logoView);

        PrepareView prepareView = new PrepareView(this);//准备播放界面
        if (!isLoadAd && mConfig.getAd().isOpen_ad() && !mConfig.getAd().getAd_reward_video_id().isEmpty()) {
            prepareView.setListener(view -> {
                new XPopup.Builder(mContext)
                        .isDestroyOnDismiss(true)
                        .asConfirm(null, "您需要观看一条广告后才能观影!",
                                "取消", "观看",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        loadAd();
                                    }
                                }, null, false).show();
            });
        }
        prepareView.setClickStart();


        ImageView thumb = prepareView.findViewById(R.id.thumb);//封面图
//        thumb.setImageResource(R.mipmap.thumb_player);
        Glide.with(this).load(mVodItem.getVod_pic()).diskCacheStrategy(DiskCacheStrategy.ALL).into(thumb);

        mController.addControlComponent(prepareView);
        mController.addControlComponent(new CompleteView(this));//自动完成播放界面
        mController.addControlComponent(new ErrorView(this).setListener(view -> {
            // 判断是否解析
            if (mVodItem.getVod_play_list().get(mVodIndex).getJiexi_count() > 0) {

                // 换源标题
                ArrayList<String> titles = new ArrayList<String>();
                for (int i = 0; i < mVodItem.getVod_play_list().get(mVodIndex).getJiexi_count(); i++) {
                    titles.add(String.format("高清接口%d",i + 1));
                }

                new XPopup.Builder(mContext)
                        .hasShadowBg(true)
                            .hasBlurBg(true)
                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .asBottomList("请选择解析接口",titles.toArray(new String[0]),
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                mVideoView.release();
                                mTitleView.setTitle(String.format("%s-%s",mVodItem.getVod_name(),mUrlBean.getName()));
                                mVideoView.setUrl(String.format("%s?jiexi=%d",mUrlBean.getUrl(),position));
                                mVideoView.start();
                            }
                        }).show();

            }else {
                if (mPlayErrorNum <= 3) {
                    new AlertDialog.Builder(mContext)
                            .setMessage("视频播放失败是否反馈？")
                            .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                            .setPositiveButton("确定", (dialog, which) -> toFeedBack()).create().show();
                }else {
                    mPlayErrorNum = 0;
                }

                mPlayErrorNum += 1;
            }

        }));//错误界面

        mController.addControlComponent(mTitleView);

        VodControlView vodControlView = new VodControlView(this);//点播控制条
        mController.addControlComponent(vodControlView);
        // 弹幕
        mMyDanmakuView = new MyDanmakuView(this);
        mController.addControlComponent(mMyDanmakuView);

        GestureView gestureControlView = new GestureView(this);//滑动控制视图
        mController.addControlComponent(gestureControlView);
        //根据是否为直播决定是否需要滑动调节进度
        mController.setCanChangePosition(true);
        mController.addControlComponent(new PlayerMonitor());
        mVideoView.setVideoController(mController);
        mVideoView.addOnStateChangeListener(mOnStateChangeListener);

        mTvTitle.setText(mVodItem.getVod_name());
        mTitleView.setTitle(mVodItem.getVod_name());

    }

    private VideoView.OnStateChangeListener mOnStateChangeListener = new VideoView.SimpleOnStateChangeListener() {

        @Override
        public void onPlayStateChanged(int playState) {
            switch (playState) {
                case VideoView.STATE_IDLE:
                    break;
                case VideoView.STATE_PREPARING:
                    break;
                case VideoView.STATE_PREPARED:
                    if (!mConfig.getPlay_note_info().equals("")) {
                        mMyDanmakuView.sendDanmaku(mConfig.getPlay_note_info());
                    }
                    break;
                case VideoView.STATE_ERROR:
                    VodUtil.uploadVodEvent(mContext,"play_error",mVodItem.getVod_name(),mVodItem.getVid()+"",mUrlBean.getUrl(),mVodItem.getVod_play_list().get(mVodIndex).getFrom());
                    break;
            }
        }
    };


    @Override
    protected void initListener() {
        // 投屏
        mIvCastScreen.setOnClickListener(v -> {
            try {
                if (BuildConfig.isDemo){
                    mUrlBean.setUrl("https://vfx.mtime.cn/Video/2021/04/20/mp4/210420111203508147_1080.mp4");
                }
                StartActUtil.toDlnaView(mContext,mUrlBean.getName(),mUrlBean.getUrl());
            }catch (Exception e) {}
        });
        // 返回
        mIvBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void setAdLog(String data) {

    }

    @Override
    public void setVodDetailData(VodDetailDTO vodDetailDTO) {
        mVodItem = vodDetailDTO.getData();
        try {
            ((TabDetailFragment)(mFList.get(0))).setData(vodDetailDTO);

            if (P2pEngine.getInstance() != null && !mConfig.getP2p_token().isEmpty() && mVodItem.getVod_play_list().get(mVodIndex).getIs_p2p() == 1) {
                String parsedUrl = P2pEngine.getInstance().parseStreamUrl(getPlayItem(mVodIndex,0).getUrl());
                mVideoView.setUrl(parsedUrl);
            }else {
                mVideoView.setUrl(getPlayItem(mVodIndex,0).getUrl());
            }

            if (BuildConfig.isDemo) {
                mVideoView.setUrl("https://vfx.mtime.cn/Video/2021/04/20/mp4/210420111203508147_1080.mp4");
            }

        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
    }


    private VodBean.VodPlayBean.UrlBean getPlayItem(Integer vodIndex,Integer subIndex){
        List<VodBean.VodPlayBean> vodPlayBeanList = mVodItem.getVod_play_list();
        if (vodPlayBeanList.size() == 0) return null;
        VodBean.VodPlayBean vodPlayBean  = vodPlayBeanList.get(vodIndex);
        mPresenter.addHistory(mVodItem);
        mUrlBean = vodPlayBean.getUrls().get(subIndex);
        return mUrlBean;
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }

    public void loadAd() {
        rewardVideoLoader = new RewardVideoLoader(this,mConfig.getAd().getAd_reward_video_id(), new RewardVideoAdCallBack() {

            @Override
            public void onAdShow() {
              //  Log.i(TAG, "激励视频广告展示");
            }

            @Override
            public void onAdVideoCache() {
             //   Log.i(TAG, "激励视频广告收到数据");
                rewardVideoLoader.showAd();
            }

            @Override
            public void onAdClick() {
              //  Log.i(TAG, "激励视频广告被点击");
            }

            @Override
            public void onAdClose() {
               // Log.i(TAG, "激励视频广告关闭");
                if (isLoadAd) {
                    mVideoView.start();
                }
            }

            @Override
            public void onAdFail(String error) {
                //Log.i(TAG, "激励视频广告加载失败:" + error);
                mPresenter.addAdLog(DeviceIDUtils.getDeviceId(getBaseContext()),"play_detail",false);
                mVideoView.start();
            }

            @Override
            public void onAdVideoComplete() {
                Log.i(TAG, "激励视频广告播放完成");
            }

            @Override
            public void onReward(String trans_id) {
                isLoadAd = true;
              //  Log.i(TAG, "可以发放奖励了");
                if (mConfig.getAd().isOpen_ad()) {
                    PrefsUtils.with(mContext).writeLong("isLoadAdTime", System.currentTimeMillis() / 1000);
                    mPresenter.addAdLog(DeviceIDUtils.getDeviceId(getBaseContext()),"play_detail",true);
                }
            }
        });
        isLoadAd = false;
        rewardVideoLoader.setOrientation(RewardVideoLoader.VERTICAL);
        rewardVideoLoader.loadAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startThreadTest();
        MobclickAgent.onResume(this);
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThreadTest();
        if (mVideoView != null) {
            mVideoView.release();
        }
    }

    @Override
    public void onBackPressed() {
        if (mVideoView == null || !mVideoView.onBackPressed()) {
            super.onBackPressed();
        }
    }


    private void toFeedBack()
    {
        try {
            List<VodBean.VodPlayBean> vodPlayBeanList = mVodItem.getVod_play_list();
            VodBean.VodPlayBean vodPlayBean  = vodPlayBeanList.get(mVodIndex);
            String content = String.format("视频:%s\r\n播放源:%s\n视频序列:%s\n\r视频播放异常! ",mVodItem.getVod_name(),vodPlayBean.getNote(),mUrlBean.getName());
            StartActUtil.toFeedBack(mContext,mVodItem.getVid() + "",content);
        }catch (Exception e)
        {
           // Log.d("111",e.getMessage());
        }
    }



    public void startThreadTest() {
        //开始监测网速
        if(!isCheck){
            netThread = new NetThread();
            isCheck = true;
            netThread.start();
        }
    }

    public void stopThreadTest() {
        //关闭检测网速
        if(isCheck){
            isCheck = false;
            netThread=null;
        }
    }

    public class NetThread extends Thread {

        private boolean isFirst=false;
        private long rxtxTotal = 0;
        private int time;
        private double rxtxSpeed = 1.0f;
        private boolean isNetBad = false;
        private DecimalFormat showFloatFormat = new DecimalFormat("0.00");

        @Override
        public void run() {
            do {
                long tempSum = TrafficStats.getTotalRxBytes()+ TrafficStats.getTotalTxBytes();
                if (isFirst) {
                    rxtxTotal = tempSum;
                    isFirst = false;
                }
                long rxtxLast = tempSum - rxtxTotal;
                double tempSpeed = rxtxLast * 1000 / 1000;
                rxtxTotal = tempSum;
                if ((tempSpeed / 1024d) < 20 && (rxtxSpeed / 1024d) < 20) {
                    time += 1;
                } else {
                    time = 0;
                }
                rxtxSpeed = tempSpeed;
                // 更新网速显示
                runOnUiThread(() -> mController.setNetSpeedText(showFloatFormat.format(tempSpeed / 1024d) + "KB/s"));

                if (time >= 5) {//连续五次检测网速都小于20kb/s  断定网速很差.
                    isNetBad = true;
                    //  Log.i("testren", "==网速差 " + isNetBad);
                    time = 0; //重新检测
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //   Log.i("testren","=========睡眠发生异常");
                    e.printStackTrace();
                }
            }while(isCheck);

        }
    }


}
