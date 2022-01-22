package com.vodbyte.movie.mvp.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseFragment;
import com.vodbyte.movie.base.BaseLazyFragment;
import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.mvp.view.fragment.BookShelf.VodShelfActivity;
import com.vodbyte.movie.utils.StartActUtil;
import com.vodbyte.movie.utils.Tools;
import com.vodbyte.movie.widget.PullScrollView;
import com.vodbyte.movie.widget.SettingItem;
import com.vodbyte.videoplayer.util.L;

import butterknife.BindView;

/**
 * 我的
 */
public class MineFragment extends BaseFragment implements PullScrollView.OnTurnListener {

    @BindView(R.id.scroll_view)
    PullScrollView mScrollView;
    @BindView(R.id.header_background)
    ImageView mHeaderBackground;
    @BindView(R.id.version)
    TextView mVersionView;
    @BindView(R.id.vod_shelf)
    SettingItem mVodShelf;

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected void initView() {

        mVodShelf.setOnSettingItemClick(() -> {
            startActivity(VodShelfActivity.class);
        });

        mScrollView.setHeader(mHeaderBackground);
        mVersionView.setText(String.format("版本号:%s",Tools.getVerName(getContext())));
    }


    @Override
    protected void setListener() {
        mScrollView.setOnTurnListener(this);
    }

    @Override
    public void onTurn() {

    }

    @Override
    public void onResume(){
        super.onResume();
        MobclickAgent.onResume(getContext());
    }

    @Override
    public void onPause(){
        super.onPause();
        MobclickAgent.onPause(getContext());
    }
}
