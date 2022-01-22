package com.vodbyte.movie.mvp.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseLazyFragment;
import com.vodbyte.movie.bean.ClassifyTitleBean;
import com.vodbyte.movie.mvp.presenter.ClassifyPresenter;
import com.vodbyte.movie.mvp.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 分类
 */
public class TypeFragment extends BaseLazyFragment{

    @BindView(R.id.tl_top_fragment_type)
    TabLayout mTabLayout;
    @BindView(R.id.vp_container_fragment_type)
    ViewPager mVpContainer;

    private List<ClassifyTitleBean> mKindTitleList;
    private List<TypeContainerFragment> mFList;

    public void setCidTitleList(List<ClassifyTitleBean> list){
        mKindTitleList = list;
        lazyLoad();
    }

    @Override
    protected ClassifyPresenter getInstance() {
        return new ClassifyPresenter();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_type;
    }

    @Override
    protected void initData(Bundle bundle) {
        lazyLoad();
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded){
            return;
        }

        if (mKindTitleList != null){
            mFList = new ArrayList<>();
            for (int i = 0; i < mKindTitleList.size(); i++) {
                TypeContainerFragment f = new TypeContainerFragment();
                f.setClassifyTitle(mKindTitleList.get(i));
                mFList.add(f);
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
                    ClassifyTitleBean titleBean = mKindTitleList.get(position);
                    return titleBean.getName();
                }
            });
            mTabLayout.setupWithViewPager(mVpContainer);
            mVpContainer.setOffscreenPageLimit(mFList.size());
            isLoaded = true;
        }else {
            ((MainActivity)(mBaseActivity)).getCidTitleList();
        }
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
