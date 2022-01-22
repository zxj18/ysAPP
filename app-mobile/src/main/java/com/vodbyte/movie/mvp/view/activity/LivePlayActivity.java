package com.vodbyte.movie.mvp.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseActivity;
import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.bean.LiveAreaBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.utils.GsonUtil;
import com.vodbyte.movie.utils.StartActUtil;
import com.vodbyte.movie.widget.ListDrawerPopupView;
import com.vodbyte.movie.widget.component.PlayerMonitor;
import com.vodbyte.movie.widget.videoview.IjkVideoView;

import butterknife.BindView;
import com.vodbyte.videocontroller.StandardVideoController;
import com.vodbyte.videocontroller.component.GestureView;
import com.vodbyte.videocontroller.component.LiveControlView;
import com.vodbyte.videocontroller.component.TitleView;
import com.vodbyte.videoplayer.player.VideoView;
import com.vodbyte.videoplayer.util.L;

import java.text.DecimalFormat;

public class LivePlayActivity extends BaseActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.player_video_view)
    protected IjkVideoView mVideoView;
    TitleView mTitleView;
    LiveAreaBean.LiveBean mLiveBean;
    private StandardVideoController mController;

    NetThread netThread;
    private boolean isCheck = false;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_live_play_detail;
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mLiveBean = (LiveAreaBean.LiveBean) GsonUtil.gsonToBean(intent.getStringExtra("live"), LiveAreaBean.LiveBean.class);
        if (mLiveBean == null) {
            finish();
        }
    }

    @Override
    protected void initView() {
        Context context = this;
        // 播放器相关
        mTitleView = new TitleView(this); //标题栏
        mTitleView.setIsLive(true);

        mController = new StandardVideoController(this);
        //根据屏幕方向自动进入/退出全屏
        mController.setEnableOrientation(false);

        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new LiveControlView(this).setListener((view, index) -> {
            if (index == 0) {

                new XPopup.Builder(context)
                        .dismissOnTouchOutside(true) //点击额外区域关闭
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .popupPosition(PopupPosition.Right)//右边
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .asCustom(new ListDrawerPopupView(context,mLiveBean).setListener(new ListDrawerPopupView.OnBtnClickViewListener() {
                            @Override
                            public void onClickItem(LiveAreaBean.LiveBean.LiveItemBean bean) {
                                mTitleView.setTitle(String.format("%s-%s",mLiveBean.getTitle(),bean.getTitle()));
                                mVideoView.setUrl(bean.getUrl());
                                mVideoView.replay(true);
                                mVideoView.start();
                            }
                        })).show();

            }

        }));//直播控制条

        GestureView gestureControlView = new GestureView(this);//滑动控制视图
        mController.addControlComponent(gestureControlView);

        //根据是否为直播决定是否需要滑动调节进度
        mController.setCanChangePosition(false);
        mController.addControlComponent(new PlayerMonitor());
        //如果你不想要UI，不要设置控制器即可
        mVideoView.setVideoController(mController);
        //播放状态监听
        mVideoView.addOnStateChangeListener(mOnStateChangeListener);

        if (mLiveBean.getContent().size() > 0) {

            try {
                LiveAreaBean.LiveBean.LiveItemBean mData = mLiveBean.getContent().get(0);

                if (mData.getUrl().startsWith("http")) {
                    mTitleView.setTitle(String.format("%s-%s",mLiveBean.getTitle(),mData.getTitle()));
                    mVideoView.setUrl(mData.getUrl());
                    mVideoView.start();
                    mVideoView.startFullScreen();
                }

            }catch (Exception e){

            }
        }


        startThreadTest();
    }

    private VideoView.OnStateChangeListener mOnStateChangeListener = new VideoView.SimpleOnStateChangeListener() {
        @Override
        public void onPlayerStateChanged(int playerState) {
            switch (playerState) {
                case VideoView.PLAYER_NORMAL://小屏
                    break;
                case VideoView.PLAYER_FULL_SCREEN://全屏
                    break;
            }
        }

        @Override
        public void onPlayStateChanged(int playState) {
            switch (playState) {
                case VideoView.STATE_IDLE:
                    break;
                case VideoView.STATE_PREPARING:
                    break;
                case VideoView.STATE_PREPARED:
                    break;
                case VideoView.STATE_PLAYING:
                    //需在此时获取视频宽高
                    int[] videoSize = mVideoView.getVideoSize();
                    L.d("视频宽：" + videoSize[0]);
                    L.d("视频高：" + videoSize[1]);
                    break;
                case VideoView.STATE_PAUSED:
                    break;
                case VideoView.STATE_BUFFERING:
                    break;
                case VideoView.STATE_BUFFERED:
                    break;
                case VideoView.STATE_PLAYBACK_COMPLETED:
                    break;
                case VideoView.STATE_ERROR:
                    break;
            }
        }
    };


    @Override
    protected void initListener() {

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
