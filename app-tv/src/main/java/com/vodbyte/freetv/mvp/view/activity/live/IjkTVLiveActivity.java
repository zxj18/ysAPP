package com.vodbyte.freetv.mvp.view.activity.live;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.TrafficStats;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vodbyte.freetv.R;
import com.vodbyte.freetv.base.BaseActivity;
import com.vodbyte.freetv.bean.LiveAreaBean;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.freetv.mvp.adapter.RightTitleAdapter;
import com.vodbyte.freetv.mvp.contract.ILiveAreaContract;
import com.vodbyte.freetv.mvp.presenter.LiveAreaPresenter;
import com.vodbyte.freetv.mvp.view.activity.detail.FullScreenPlayActivity;
import com.vodbyte.freetv.widget.component.PlayerMonitor;
import com.vodbyte.freetv.widget.videoview.IjkVideoView;
import com.vodbyte.videocontroller.StandardVideoController;
import com.vodbyte.videocontroller.TvStandardVideoController;
import com.vodbyte.videocontroller.component.LiveControlView;
import com.vodbyte.videocontroller.component.TitleView;
import com.vodbyte.videoplayer.exo.ExoMediaPlayerFactory;
import com.vodbyte.videoplayer.player.VideoView;
import com.vodbyte.videoplayer.util.L;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

/**
 * 电视直播
 */
public class IjkTVLiveActivity extends BaseActivity<LiveAreaPresenter> implements ILiveAreaContract.view, LiveTitleAdapter.OnClickListener {

    @BindView(R.id.player_video_view)
    protected IjkVideoView mVideoView;

    @BindView(R.id.tv_live_menu)
    View tv_live_menu;

    @BindView(R.id.tv_decode_menu)
    View tv_decode_menu;

    @BindView(R.id.tv_live_recycler_view_title)
    RecyclerView tv_live_recycler_view_title;

    @BindView(R.id.tv_live_recycler_view_sub_title)
    RecyclerView tv_live_recycler_view_sub_title;

    TitleView mTitleView;

    ArrayList<String> mTitleList = new ArrayList<>();
    ArrayList<String> mSubTitleList = new ArrayList<>();

    LiveTitleAdapter mTitleAdapter;
    LiveTitleAdapter mSubTitleAdapter;
    List<LiveAreaBean> mList;
    List<String> mLiveUrlList = new ArrayList<>();

    // 切换解码
    @BindView(R.id.tv_live_decode_recycler_view)
    RecyclerView mTvDeCodeRightRecyclerView;
    RightTitleAdapter mDeCodeTitleAdapter;

    private int currentTitlePosition = 0;
    private int currentLivePosition = 0;
    private TvStandardVideoController mController;
    private String mLiveUrl;

    NetThread netThread;
    private boolean isCheck = false;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_ijk_tv_live;
    }

    @Override
    protected LiveAreaPresenter getInstance() {
        return new LiveAreaPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPresenter.getLiveAreaList();
    }

    @Override
    protected void initView() {

        tv_live_recycler_view_title.setLayoutManager(new LinearLayoutManager(this));
        tv_live_recycler_view_sub_title.setLayoutManager(new LinearLayoutManager(this));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(FLAG_KEEP_SCREEN_ON);

        mVideoView.addPlayerOption("framedrop",10);

        // 播放器相关
        mTitleView = new TitleView(this); //标题栏
        mTitleView.setIsLive(true);

        mController  = new TvStandardVideoController(this);
        //根据屏幕方向自动进入/退出全屏
        mController.setEnableOrientation(false);

        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new LiveControlView(this).setListener((view, index) -> {
            if (index == 0) {


            }

        }));//直播控制条


        //根据是否为直播决定是否需要滑动调节进度
        mController.setCanChangePosition(false);
        mController.addControlComponent(new PlayerMonitor());
        //如果你不想要UI，不要设置控制器即可
        mVideoView.setVideoController(mController);
        //播放状态监听
        mVideoView.addOnStateChangeListener(mOnStateChangeListener);

        //切换解码
        mTvDeCodeRightRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mDeCodeTitleAdapter = new RightTitleAdapter(this, position -> {
            mVideoView.release();
            if (position == 0) {
                mVideoView.setEnableMediaCodec(false);
                showToast("解码器切换至IJK软解");
            } else {
                mVideoView.setEnableMediaCodec(true);
                showToast("解码器切换至IJK硬解");
            }
            tv_decode_menu.setVisibility(View.INVISIBLE);
            mVideoView.setUrl(mLiveUrl);
            mVideoView.start();
        });

        mTvDeCodeRightRecyclerView.setAdapter(mDeCodeTitleAdapter);
        mDeCodeTitleAdapter.refresh(Arrays.asList("IJK软解","IJK硬解"));
    }


    @Override
    public void setLiveAreaList(List<LiveAreaBean> list) {
        mList = list;

        try {
            for(LiveAreaBean areaBean : list) {
                for (LiveAreaBean.LiveBean liveBean : areaBean.getData()) {
                    mLiveUrlList.add(liveBean.getContent().get(0).getUrl());
                }
            }
        }catch (Exception e){}

        for(LiveAreaBean areaBean : list) {
            mTitleList.add(areaBean.getTitle());
        }

        if (list.size() > 0) {
            for(LiveAreaBean.LiveBean liveBean : list.get(0).getData()) {
                mSubTitleList.add(liveBean.getTitle());
            }
        }

        mTitleAdapter = new LiveTitleAdapter(this,mTitleList, LiveTitleAdapter.TYPE.TITLE,this);
        mSubTitleAdapter = new LiveTitleAdapter(this,mSubTitleList, LiveTitleAdapter.TYPE.SUB_TITLE,this);

        tv_live_recycler_view_title.setAdapter(mTitleAdapter);
        tv_live_recycler_view_sub_title.setAdapter(mSubTitleAdapter);
        // 默认开启硬解

        if (mConfig.getOpen_tv_video_decode() == 1) {
             mVideoView.setEnableMediaCodec(true);
        }else {
             mVideoView.setEnableMediaCodec(false);
        }

        try {
            LiveAreaBean.LiveBean mLiveBean = mList.get(currentTitlePosition).getData().get(0);
            mLiveUrl = mLiveBean.getContent().get(0).getUrl();
            mVideoView.setUrl(mLiveUrl);
            mVideoView.start();
        }catch (Exception e){}
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
                    break;
                case VideoView.STATE_PLAYING:
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
        startThreadTest();
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (tv_live_menu.getVisibility() == View.VISIBLE || tv_decode_menu.getVisibility() == View.VISIBLE) {
                    tv_live_menu.setVisibility(View.INVISIBLE);
                    tv_decode_menu.setVisibility(View.INVISIBLE);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(IjkTVLiveActivity.this);
                    builder.setMessage("退出电视直播？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create().show();
                }
                return true;
        }

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            tv_decode_menu.setVisibility(View.VISIBLE);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_ENTER ||  keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            tv_live_menu.setVisibility(View.VISIBLE);
            return true;
        }

       try {

           if (tv_live_menu.getVisibility() != View.VISIBLE) {
               if (tv_decode_menu.getVisibility() == View.VISIBLE) {
                   return true;
               }

               // 按键上
               if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                   if (currentLivePosition <= mLiveUrlList.size()) {
                       currentLivePosition -= 1;
                   }

                   if (currentLivePosition == mLiveUrlList.size() || currentLivePosition < 0) {
                       currentLivePosition = mLiveUrlList.size() - 1;
                   }

                   mVideoView.release();
                   mLiveUrl = mLiveUrlList.get(currentLivePosition);
                   mVideoView.setUrl(mLiveUrl);
                   mVideoView.start();
                   return true;
               }
               // 按键下
               if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                   if (currentLivePosition <= mLiveUrlList.size()) {
                       currentLivePosition += 1;
                   }
                   if (currentLivePosition == mLiveUrlList.size()) {
                       currentLivePosition = 0;
                   }

                   mVideoView.release();
                   mLiveUrl = mLiveUrlList.get(currentLivePosition);
                   mVideoView.setUrl(mLiveUrl);
                   mVideoView.start();
                   return true;
               }
           }
       }catch (Exception e){}


        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onTitleFocus(int position) {
        mSubTitleList.clear();

        for(LiveAreaBean.LiveBean liveBean : mList.get(position).getData()) {
            mSubTitleList.add(liveBean.getTitle());
        }

        mSubTitleAdapter.refresh(mSubTitleList);
        currentTitlePosition = position;
    }

    @Override
    public void onSubTitleClick(int position) {
        tv_live_menu.setVisibility(View.GONE);
        try {
            mVideoView.release();
            LiveAreaBean.LiveBean mLiveBean = mList.get(currentTitlePosition).getData().get(position);
            mLiveUrl = mLiveBean.getContent().get(0).getUrl();
            mVideoView.setUrl(mLiveUrl);
            mVideoView.start();
        }catch (Exception e){}
    }


    @Override
    public void showError(String msg) {
        showToast(msg);
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
