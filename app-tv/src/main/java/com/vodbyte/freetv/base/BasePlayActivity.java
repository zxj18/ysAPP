package com.vodbyte.freetv.base;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.vodbyte.freetv.R;
import com.vodbyte.freetv.app.Constant;
import com.vodbyte.videoplayer.player.VideoView;
import com.vodbyte.videoplayer.player.VideoViewManager;

/**
 * 页面以及播放器共有逻辑封装
 * @param <T>
 */
@SuppressLint("Registered")
public class BasePlayActivity<T extends VideoView> extends AppCompatActivity {

    protected T mVideoView;

    protected String getTitleName() {
        return getResources().getResourceName(R.string.app_name);
    }

    protected int getLayoutResId() {
        return 0;
    }

    protected View getContentView() {
        return null;
    }

    protected boolean enableBack() {
        return true;
    }

    protected VideoViewManager getVideoViewManager() {
        return VideoViewManager.instance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutResId() != 0) {
            setContentView(getLayoutResId());
        } else if (getContentView() != null) {
            setContentView(getContentView());
        }

        //标题栏设置
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getTitleName());
            if (enableBack()) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        initView();
    }

    protected void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void initView() {

    }

    /**
     * 把状态栏设成透明
     */
    protected void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
                return defaultInsets.replaceSystemWindowInsets(
                        defaultInsets.getSystemWindowInsetLeft(),
                        0,
                        defaultInsets.getSystemWindowInsetRight(),
                        defaultInsets.getSystemWindowInsetBottom());
            });
            ViewCompat.requestApplyInsets(decorView);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    protected void showToast(String msg) {
        String url = Constant.BASE_API_URL.replace("https://","").replace("http://","");
        msg = msg.replace(url,"");
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
