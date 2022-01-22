package com.vodbyte.videocontroller.component;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
public class TvLogoView extends FrameLayout implements IControlComponent {

    private ControlWrapper mControlWrapper;
    private RelativeLayout mTitleContainer;
    private ImageView mLogoImageView;

    public TvLogoView(@NonNull Context context) {
        super(context);
    }

    public TvLogoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TvLogoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        LayoutInflater.from(getContext()).inflate(R.layout.dkplayer_layout_tv_logo_view, this, true);
        mTitleContainer = findViewById(R.id.title_container);
        mLogoImageView = findViewById(R.id.logo);
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
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        if (playerState == VideoView.PLAYER_FULL_SCREEN) {
            if (mControlWrapper.isShowing() && !mControlWrapper.isLocked()) {
                //setVisibility(VISIBLE);
            }
        } else {
         //   setVisibility(GONE);
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
