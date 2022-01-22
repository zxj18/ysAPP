package com.vodbyte.freetv.mvp.view.activity.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.vodbyte.freetv.app.FreeTVApp;
import com.vodbyte.freetv.bean.ClassifyTitleBean;
import com.vodbyte.freetv.mvp.contract.IMainContract;
import com.vodbyte.freetv.mvp.presenter.MainPresenter;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.base.BaseActivity;
import com.vodbyte.freetv.common.CommonUtils;
import com.vodbyte.freetv.common.Ui;
import com.vodbyte.freetv.widget.shine.FocusBorder;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.freetv.mvp.view.activity.personal.PersonalFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 */
public class HomeActivity extends BaseActivity<MainPresenter> implements IMainContract.View,FocusBorder.OnFocusCallback {

    @BindView(R.id.tl_top_fragment_home)
    TabLayout mTabLayout;

    @BindView(R.id.home_view_pager)
    ViewPager mHomeViewPager;
    private List<Fragment> mFList = new ArrayList<>();

    private FocusBorder mFocusBorder;
    private List<String> mTitleList = new ArrayList<>();
    private List<ClassifyTitleBean> mTitleBeanList;
    private boolean mIsHeaderShow = false;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_home;
    }

    @Override
    protected MainPresenter getInstance() { return new MainPresenter(); }

    @Override
    protected void initData(Bundle savedInstanceState) {
//        mTitleList.add("我的");
//        PersonalFragment homeFragment = new PersonalFragment();
//        mFList.add(homeFragment);

        mPresenter.getCidTitleList();
    }

    protected void initView() {
        // 检查权限并动态申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionActivity.needRequestPermission(this)) {
                return;
            }
        }

        FreeTVApp.screenWidth = CommonUtils.getScreenResolutions(this)[0];
        FreeTVApp.screenHeight = CommonUtils.getScreenResolutions(this)[1];

        // 绑定流光特效回调
        mFocusBorder = new FocusBorder.Builder().asColor().shimmerColor(0x55FFFFFF).build(this);
        mFocusBorder.boundGlobalFocusListener(this);

    }

    @Override
    protected void initListener() {
        Ui.configTopBar(this);
        View top_bar_menu_root_condition = findViewById(R.id.top_bar_menu_root_condition);
        top_bar_menu_root_condition.setOnClickListener(v -> {
            mIsHeaderShow = !mIsHeaderShow;
            for (int i = 0; i < mFList.size(); i++) {
                HomeVideoFragment videoFragment = (HomeVideoFragment)mFList.get(i);
                videoFragment.setHeaderVisibility();
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            for (int i = 0; i < mFList.size(); i++) {
                HomeVideoFragment videoFragment = (HomeVideoFragment)mFList.get(i);
                videoFragment.setHeaderVisibility();
            }
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {

        if (mIsHeaderShow) {
            for (int i = 0; i < mFList.size(); i++) {
                HomeVideoFragment videoFragment = (HomeVideoFragment)mFList.get(i);
                videoFragment.setHeaderVisibility();
            }
            mIsHeaderShow = false;
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("是否确认退出？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.create().show();
    }

    @Override
    public FocusBorder.Options onFocus(View previousFocus, View currentFocus) {
        if (currentFocus != null) {
            switch (currentFocus.getId()) {
                case R.id.top_bar_menu_root_home:
                case R.id.top_bar_menu_root_search:
                case R.id.top_bar_menu_root_tv_live:
                case R.id.top_bar_menu_root_history:
                case R.id.video_root_ad:
                    mFocusBorder.setVisible(false);
                    return null;
            }
        }
        return FocusBorder.OptionsFactory.get(0);
    }

    @Override
    public void setCidTitle(List<ClassifyTitleBean> list) {
        mTitleBeanList = list;
        lazyLoad();
    }

    private void lazyLoad() {

        for (int i = 0; i < mTitleBeanList.size(); i++) {
            HomeVideoFragment videoFragment = new HomeVideoFragment();
            videoFragment.setCidInfo(mTitleBeanList.get(i));
            mFList.add(videoFragment);
            mTitleList.add(mTitleBeanList.get(i).getName());
        }

        mHomeViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        mTabLayout.setupWithViewPager(mHomeViewPager);
        mHomeViewPager.setOffscreenPageLimit(mTitleList.size());
        if (mTitleList.size() > 1) {
            mHomeViewPager.setCurrentItem(0);
        }
    }
}
