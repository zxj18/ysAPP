package com.vodbyte.videocontroller.component;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vodbyte.videocontroller.R;
import com.vodbyte.videoplayer.controller.ControlWrapper;
import com.vodbyte.videoplayer.controller.IControlComponent;
import com.vodbyte.videoplayer.player.VideoView;
import com.vodbyte.videoplayer.util.PlayerUtils;

/**
 * logo
 */
public class LogoView extends FrameLayout implements IControlComponent {

    private ControlWrapper mControlWrapper;
    private RelativeLayout mTitleContainer;
    private ImageView mLogoImageView;

    public LogoView(@NonNull Context context) {
        super(context);
    }

    public LogoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LogoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        LayoutInflater.from(getContext()).inflate(R.layout.dkplayer_layout_logo_view, this, true);
        mTitleContainer = findViewById(R.id.title_container);
        mLogoImageView = findViewById(R.id.logo);
        mLogoImageView.setVisibility(View.GONE);

    }

    public void setLogo(int resId) {
        mLogoImageView.setImageResource(resId);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        mControlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {
        //只在全屏时才有效
        if (!mControlWrapper.isFullScreen()) return;
        setVisibility(VISIBLE);
        mLogoImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
            case VideoView.STATE_START_ABORT:
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
            case VideoView.STATE_PLAYBACK_COMPLETED:
                setVisibility(GONE);
                mLogoImageView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        if (playerState == VideoView.PLAYER_FULL_SCREEN) {
            setVisibility(VISIBLE);
            mLogoImageView.setVisibility(View.VISIBLE);
        } else {
            setVisibility(GONE);
            mLogoImageView.setVisibility(View.GONE);
        }

        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity != null && mControlWrapper.hasCutout()) {
            int orientation = activity.getRequestedOrientation();
            int cutoutHeight = mControlWrapper.getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                mTitleContainer.setPadding(0, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                mTitleContainer.setPadding(cutoutHeight, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                mTitleContainer.setPadding(0, 0, cutoutHeight, 0);
            }
        }
    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {
        if (isLocked) {
          //  setVisibility(GONE);
        } else {
        //    setVisibility(VISIBLE);
        }
    }

}
