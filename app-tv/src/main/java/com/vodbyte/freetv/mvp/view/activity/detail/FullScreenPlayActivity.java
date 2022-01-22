package com.vodbyte.freetv.mvp.view.activity.detail;

import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.base.BaseActivity;
import com.vodbyte.freetv.base.BasePlayActivity;
import com.vodbyte.freetv.base.BasePresenter;
import com.vodbyte.freetv.bean.ConfigBean;
import com.vodbyte.freetv.utils.Utils;
import com.vodbyte.freetv.widget.component.AdControlView;
import com.vodbyte.freetv.widget.component.MyDanmakuView;
import com.vodbyte.freetv.widget.videoview.IjkVideoView;
import com.vodbyte.videocontroller.TvStandardVideoController;
import com.vodbyte.freetv.mvp.adapter.RightTitleAdapter;
import com.vodbyte.videocontroller.component.LogoView;
import com.vodbyte.videocontroller.component.TvLogoView;
import com.vodbyte.videocontroller.component.TvTitleView;
import com.vodbyte.videocontroller.component.VodControlView;
import com.vodbyte.videoplayer.exo.ExoMediaPlayerFactory;
import com.vodbyte.videoplayer.ijk.IjkPlayerFactory;
import com.vodbyte.videoplayer.player.AndroidMediaPlayerFactory;
import com.vodbyte.videoplayer.player.VideoView;
import com.vodbyte.videoplayer.util.PlayerUtils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;


/**
 * 全屏播放
 */

public class FullScreenPlayActivity extends BaseActivity {

    @BindView(R.id.player_video_view)
    protected IjkVideoView mVideoView;

    private TvStandardVideoController mController;
    private String mTitle;
    private String mUrl;

    NetThread netThread;
    private boolean isCheck = false;

    private Timer mProgressTimer;
    private Timer mTimer = new Timer();
    // 标记视频是否已经开始播放
    private boolean isPrepare = false;
    private int mVideoDuration = 0;
    private int targetPosition = -1;
    private long longTime = 0;

    private int mCurrentPosition = -1;
    private MyDanmakuView mMyDanmakuView;
    private RelativeLayout mTvRightLayout;
    private RecyclerView mTvRightRecyclerView;
    private RightTitleAdapter mTitleAdapter;

    ImageView mQrcodeView;

    @Override
    protected int setLayoutResID() {
        adaptCutoutAboveAndroidP();
        return R.layout.activity_ijk_tv_play;
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

        mQrcodeView = findViewById(R.id.qrcode_view);

        mTitle = getIntent().getStringExtra("title");
        mUrl = getIntent().getStringExtra("url");

//        mUrl = "https://wy.bigmao.top/api/GetDownUrlMu/d273758be9f14821ab72792c80e0b3e2/f07d6637a3714937b254a8d7415bd841.m3u8";
//        mTitle = "测试视频";

        mTvRightLayout = findViewById(R.id.tv_right_layout);
        mTvRightRecyclerView = findViewById(R.id.tv_right_recyclerView);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTvRightRecyclerView.setLayoutManager(linearLayoutManager);

        mTitleAdapter = new RightTitleAdapter(this, position -> {
            if (position == 0) {
                playVideo("软解");
                showToast("解码器切换至IJK软解");
            }else if (position == 1) {
                playVideo("硬解");
                showToast("解码器切换至IJK硬解");
            }
        });

        mTvRightRecyclerView.setAdapter(mTitleAdapter);
        mTitleAdapter.refresh(Arrays.asList("IJK软解","IJK硬解"));

        mVideoView = findViewById(R.id.player_video_view);
        mController = new TvStandardVideoController(this);

        if (ConfigBean.getConfig(this).getOpen_tv_ad() == 1) {
            AdControlView adControlView = new AdControlView(this);
            adControlView.setListener(new AdControlView.AdControlListener() {
                @Override
                public void onAdClick() {
                    if (mVideoView.isPlaying()) {
                        mVideoView.pause();
                    }else {
                        mVideoView.resume();;
                    }
                }

                @Override
                public void onSkipAd() {
                    if (mConfig.getOpen_tv_video_decode() == 1) {
                        playVideo("硬解");
                    }else {
                        playVideo("软解");
                    }
                }

            });
            mController.addControlComponent(adControlView);
            mVideoView.setVideoController(mController);

            //监听播放结束
            mVideoView.addOnStateChangeListener(new VideoView.SimpleOnStateChangeListener() {
                @Override
                public void onPlayStateChanged(int playState) {
                    if (playState == VideoView.STATE_PLAYBACK_COMPLETED || playState == VideoView.STATE_ERROR) {
                        if (mConfig.getOpen_tv_video_decode() == 1) {
                            playVideo("硬解");
                        }else {
                            playVideo("软解");
                        }
                    }
                }
            });

            mVideoView.setUrl(ConfigBean.getConfig(this).getTv_ad_url());
            mVideoView.start();
        }else {
            if (mConfig.getOpen_tv_video_decode() == 1) {
                playVideo("硬解");
            }else {
                playVideo("软解");
            }
        }
        // https://www.jianshu.com/p/a5cf04181f3d
        mVideoView.addPlayerOption("framedrop",10);

        startThreadTest();
    }

    @Override
    protected void initListener() {

    }

    /**
     * 播放正片
     */
    private void playVideo(String factory) {
        mVideoView.release();
        mController.removeAllControlComponent();

        mController = new TvStandardVideoController(this);
        TvTitleView titleView = new TvTitleView(this);
        titleView.setTitle(mTitle);
        mController.addControlComponent(titleView);

        TvLogoView logoView = new TvLogoView(this);
        logoView.setLogo(R.mipmap.logo);
        mController.addControlComponent(logoView);

        VodControlView mVodControlView = new VodControlView(this);
        mVodControlView.findViewById(R.id.fullscreen).setVisibility(View.GONE);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mVodControlView.findViewById(R.id.total_time).getLayoutParams();
        lp.rightMargin = PlayerUtils.dp2px(this, 16);

        mController.addControlComponent(mVodControlView);
        // 弹幕
        mMyDanmakuView = new MyDanmakuView(this);
        mController.addControlComponent(mMyDanmakuView);

        mVideoView.addOnStateChangeListener(new VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                if (playState == VideoView.STATE_PREPARED) {
                    if (!ConfigBean.getConfig(FullScreenPlayActivity.this).getPlay_note_info().equals("")) {
                        mMyDanmakuView.sendDanmaku(ConfigBean.getConfig(FullScreenPlayActivity.this).getPlay_note_info());
                    }
                }
                // 播放失败显示下载APP
                if (playState == VideoView.STATE_ERROR) {
                    if (!ConfigBean.getConfig(getBaseContext()).getError_qrcode_url().isEmpty()) {
                        mQrcodeView.setVisibility(View.VISIBLE);
                        try {
                            Glide.with(getBaseContext()).load(ConfigBean.getConfig(getBaseContext()).getError_qrcode_url()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(mQrcodeView);
                        }catch (Exception e) {

                        }
                    }
                }else {
                    mQrcodeView.setVisibility(View.GONE);
                }
            }
        });

        if (factory.equals("软解")) {
            mVideoView.setEnableMediaCodec(false);
        }else if (factory.equals("硬解")) {
            mVideoView.setEnableMediaCodec(true);
        }


        // 展开菜单屏蔽其他操作
        if (mTvRightLayout.getVisibility() == View.VISIBLE) {
            mTvRightLayout.setVisibility(View.GONE);
        }

        mVideoView.setVideoController(mController);
        mVideoView.setUrl(mUrl);
        mVideoView.start();
    }

    private void adaptCutoutAboveAndroidP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (!mController.isShowing()) {
            mController.show();
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
            if (event.getRepeatCount() == 0){
                longTime = System.currentTimeMillis();
                mController.stopProgress();
                mController.stopFadeOut();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        isPrepare = mVideoView.isPlaying();
        mVideoDuration = (int) mVideoView.getDuration() / 1000;

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mTvRightLayout.getVisibility() == View.GONE) {
                mTvRightLayout.setVisibility(View.VISIBLE);
            }else {
                mTvRightLayout.setVisibility(View.GONE);
            }
            return true;
        }
        // 展开菜单屏蔽其他操作
        if (mTvRightLayout.getVisibility() == View.VISIBLE) {
           return true;
        }


        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER ||
                keyCode == KeyEvent.KEYCODE_ENTER) {
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
            }else {
                mVideoView.resume();
            }
        }

        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (!isPrepare) {
                return false;
            }
            if (System.currentTimeMillis() - longTime < 50){
                return true;
            } else {
                mVideoView.seekTo(mVideoView.getCurrentPosition() - 200 * (System.currentTimeMillis() - longTime));
            }
            mController.startProgress();
            mController.startFadeOut();

            return false;
        }

        if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (!isPrepare) {
                return false;
            }

            if (System.currentTimeMillis() - longTime < 50){
                return true;
            } else {
                mVideoView.seekTo(mVideoView.getCurrentPosition() + 200 * (System.currentTimeMillis() - longTime));
            }

            mController.startProgress();
            mController.startFadeOut();

            return false;
        }

        return super.onKeyUp(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThreadTest();

        if (mVideoView != null) {
            mVideoView.release();
        }

        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
        if (mProgressTimer != null) {
            mProgressTimer.cancel();
            mProgressTimer.purge();
        }
    }


    @Override
    public void onBackPressed() {
        if (!mController.isLocked()) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
