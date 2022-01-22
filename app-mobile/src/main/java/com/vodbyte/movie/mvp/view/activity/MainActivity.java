package com.vodbyte.movie.mvp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fiio.sdk.callBack.InterstitialAdCallBack;
import com.fiio.sdk.view.InterstitialAdLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseActivity;
import com.vodbyte.movie.base.BaseFragment;
import com.vodbyte.movie.base.BaseLazyFragment;
import com.vodbyte.movie.bean.ClassifyTitleBean;
import com.vodbyte.movie.mvp.view.fragment.MainFragment;
import com.vodbyte.movie.mvp.view.fragment.LiveFragment;
import com.vodbyte.movie.widget.NoScrollViewPager;
import com.vodbyte.movie.mvp.contract.IMainContract;
import com.vodbyte.movie.mvp.presenter.MainPresenter;
import com.vodbyte.movie.mvp.view.fragment.MineFragment;
import com.vodbyte.movie.mvp.view.fragment.TypeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainContract.View {

    @BindView(R.id.vp_container_act_main)
    NoScrollViewPager mVpContainer;
    @BindView(R.id.bnv_menus_act_main)
    BottomNavigationView mBnvBottom;
    private List<Fragment> mFList;
    InterstitialAdLoader interactionLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getCidTitleList(){
        mPresenter.getCidTitleList(false);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter getInstance() {
        return new MainPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mFList = new ArrayList<>();
        mFList.add(new MainFragment());
        mFList.add(new TypeFragment());
        mFList.add(new LiveFragment());
        mFList.add(new MineFragment());
    }

    @Override
    protected void initView() {

        mVpContainer.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFList.get(position);
            }

            @Override
            public int getCount() {
                return mFList.size();
            }
        });
        // 预加载页面数量
        mVpContainer.setOffscreenPageLimit(3);
        //禁用左右滚动
        mVpContainer.setScroll(false);

        // 加载广告
        new Handler().postDelayed(() -> showAd(),1500);
    }

    @Override
    protected void initListener() {
        mBnvBottom.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_homepage:
                    mVpContainer.setCurrentItem(0);
                    return true;
                case R.id.menu_type:
                    mVpContainer.setCurrentItem(1);
                    return true;
                case R.id.menu_tv:
                    mVpContainer.setCurrentItem(2);
                    return true;
                case R.id.menu_mine:
                    mVpContainer.setCurrentItem(3);
                    return true;
            }
            return false;
        });
    }


    @Override
    public void setCidTitle(List<ClassifyTitleBean> list) {
        ((TypeFragment)(mFList.get(1))).setCidTitleList(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 加载插屏（半屏）
     */
    private void showAd(){
        if (mConfig != null && mConfig.getAd().isOpen_ad() && !mConfig.getAd().getAd_half_screen_id().isEmpty()) {
            interactionLoader = new InterstitialAdLoader(this,mConfig.getAd().getAd_half_screen_id(),new InterstitialAdCallBack() {

                @Override
                public void onAdClick() {
                    Log.i(TAG,"插屏广告-被点击");
                }

                @Override
                public void onAdFail(String error) {
                    Log.i(TAG,"插屏广告-error = "+error);
                }

                @Override
                public void onAdLoaded() {
                    // 插屏缓存完成
                    Log.i(TAG,"插屏广告-缓存完成");
                    interactionLoader.showAd();
                }

                @Override
                public void onAdShow() {
                    Log.i(TAG,"插屏广告-展示");
                }

                @Override
                public void onAdClose() {
                    Log.i(TAG,"插屏广告-关闭");
                }

                @Override
                public void onAdVideoStart() {

                }

                @Override
                public void onAdVideoComplete() {
                    Log.i(TAG,"插屏广告-视频播放完成");
                }

            });
            interactionLoader.loadAd();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
