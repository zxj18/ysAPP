package com.vodbyte.movie.mvp.view.fragment;

import com.google.android.material.tabs.TabLayout;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseLazyFragment;
import com.vodbyte.movie.bean.ClassifyTitleBean;
import com.vodbyte.movie.bean.ConfigBean;
import com.vodbyte.movie.mvp.contract.IHomeContract;
import com.vodbyte.movie.mvp.presenter.HomePresenter;
import com.vodbyte.movie.utils.StartActUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * HomeFragment
 */
public class MainFragment extends BaseLazyFragment<HomePresenter> implements IHomeContract.View {
    @BindView(R.id.iv_search_fragment_home)
    ImageView mIvSearch;
    @BindView(R.id.iv_left_fragment_home)
    ImageView mIvLeft;
    @BindView(R.id.cl_top_fragment_home)
    ConstraintLayout mClTopLayout;
    @BindView(R.id.tv_title_fragment_home)
    TextView mTvTitle;
    @BindView(R.id.iv_bg_left_fragment_home)
    View viewBgLeft;
    @BindView(R.id.iv_bg_search_fragment_home)
    View viewBgSearch;
    @BindView(R.id.tl_top_fragment_home)
    TabLayout mTabLayout;
    @BindView(R.id.vp_container_fragment_home)
    ViewPager mVpContainer;

    private List<String> mTitleList = new ArrayList<>();
    private List<BaseLazyFragment> mFList = new ArrayList<>();
    private List<ClassifyTitleBean> mClassifyTitleBeanList;

    @Override
    protected HomePresenter getInstance() {
        return new HomePresenter();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData(Bundle bundle) {
        mTitleList.add("精选");
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setCid(0);
        mFList.add(homeFragment);

        mPresenter.getCidTitleList(true);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setListener() {
        // 搜索跳转
        mIvSearch.setOnClickListener(v -> StartActUtil.toSearchAct(mBaseActivity));
    }

    @Override
    public void setCidTitle(List<ClassifyTitleBean> list) {
        mClassifyTitleBeanList = list;
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded || mClassifyTitleBeanList == null){
            return;
        }

        if (ConfigBean.getConfig(mBaseActivity).getHome_style() == 0){
            for (int i = 0; i < mClassifyTitleBeanList.size(); i++) {
                HomeCidBlockFragment cidFragment = new HomeCidBlockFragment();
                cidFragment.setCid(mClassifyTitleBeanList.get(i).getId());
                mFList.add(cidFragment);
                mTitleList.add(mClassifyTitleBeanList.get(i).getName());
            }
        }else {
            for (int i = 0; i < mClassifyTitleBeanList.size(); i++) {
                HomeCidFragment cidFragment = new HomeCidFragment();
                cidFragment.setCid(mClassifyTitleBeanList.get(i).getId());
                mFList.add(cidFragment);
                mTitleList.add(mClassifyTitleBeanList.get(i).getName());
            }
        }


        mVpContainer.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
                return mTitleList.get(position);
            }
        });
        mTabLayout.setupWithViewPager(mVpContainer);
        mVpContainer.setOffscreenPageLimit(mFList.size());
        isLoaded = true;
    }

    @Override
    public void onError(String msg) {

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

