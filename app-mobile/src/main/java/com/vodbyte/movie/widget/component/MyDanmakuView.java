package com.vodbyte.movie.widget.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.vodbyte.movie.R;
import com.vodbyte.movie.widget.CenteredImageSpan;

import com.vodbyte.videoplayer.BuildConfig;
import com.vodbyte.videoplayer.controller.ControlWrapper;
import com.vodbyte.videoplayer.controller.IControlComponent;
import com.vodbyte.videoplayer.player.VideoView;
import com.vodbyte.videoplayer.util.PlayerUtils;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.Duration;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class MyDanmakuView extends DanmakuView implements IControlComponent {

    private DanmakuContext mContext;
    private BaseDanmakuParser mParser;

    public MyDanmakuView(@NonNull Context context) {
        super(context);
    }

    public MyDanmakuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDanmakuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(2.5f) // 设置滚动速度
                .setScaleTextSize(1.8f)  //文本缩放
                .setMaximumLines(null)
                .preventOverlapping(null)
                .setDanmakuMargin(40).setMarginTop(240);
        mParser = new BaseDanmakuParser() {
            @Override
            protected IDanmakus parse() {
                return new Danmakus();
            }
        };
        setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                start();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
      //  showFPS(BuildConfig.DEBUG);
        enableDanmakuDrawingCache(true);
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {

    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
                release();
                break;
            case VideoView.STATE_PREPARING:
                if (isPrepared()) {
                    restart();
                }
                prepare(mParser, mContext);
                break;
            case VideoView.STATE_PLAYING:
                if (isPrepared() && isPaused()) {
                    resume();
                }
                break;
            case VideoView.STATE_PAUSED:
                if (isPrepared()) {
                    pause();
                }
                break;
            case VideoView.STATE_PLAYBACK_COMPLETED:
                clear();
                clearDanmakusOnScreen();
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {

    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {

    }

    /**
     * 发送文字弹幕
     *
     * @param text   弹幕文字
     */
    public void sendDanmaku(String text) {
        removeAllDanmakus(true);

        mContext.setCacheStuffer(new SpannedCacheStuffer(), null);
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null) {
            return;
        }

        danmaku.text = text;
        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = false;
        danmaku.setTime(getCurrentTime() + 1200);
        danmaku.textSize = PlayerUtils.sp2px(getContext(), 24);
        danmaku.textColor = Color.YELLOW;
        danmaku.textShadowColor = Color.GRAY;

        addDanmaku(danmaku);
    }


}
