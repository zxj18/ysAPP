package com.vodbyte.movie.mvp.view.fragment.BookShelf;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseActivity;
import com.vodbyte.movie.base.BaseBookShelfTabFragment;
import com.vodbyte.movie.base.BaseLazyFragment;
import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.widget.SettingItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 收藏或者历史
 */
public class VodShelfActivity extends BaseActivity {

    @BindView(R.id.tl_top_fragment_bookshelf)
    TabLayout mTabLayout;
    @BindView(R.id.vp_container_fragment_bookshelf)
    ViewPager mVpContainer;
    @BindView(R.id.tv_edit_fragment_bookshelf)
    TextView mTvEdit;

    @BindView(R.id.iv_back)
    ImageView mIvBack;

    private List<BaseBookShelfTabFragment> mFList;
    private List<EditStatus> mEditStatusList;
    private int mCurrentPage;

    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected void initView() {
        mFList = new ArrayList<>(2);
        mFList.add(new HistoryFragment());
        mFList.add(new CollectFragment());
        mEditStatusList = new ArrayList<>(2);
        mEditStatusList.add(new EditStatus(false));
        mEditStatusList.add(new EditStatus(false));

        mVpContainer.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            String[] titles = new String[]{"历史","收藏"};

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
                return titles[position];
            }
        });
        mVpContainer.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mVpContainer);
    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(v -> {
            finish();
        });

        mTvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditStatus editStatus = mEditStatusList.get(mCurrentPage);
                if (!editStatus.isEdit()){
                    mFList.get(mCurrentPage).onStartEdit();
                    mTvEdit.setText("完成");
                }else {
                    mFList.get(mCurrentPage).onEndEdit();
                    mTvEdit.setText("编辑");
                }
                editStatus.setEdit(!editStatus.isEdit());
            }
        });
        mVpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                mTvEdit.setText(mEditStatusList.get(position).getText());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 编辑管理
     */
    class EditStatus {
        private boolean isEdit;

        public EditStatus(boolean isEdit) {
            this.isEdit = isEdit;
        }

        public String getText() {
            return isEdit?"完成":"编辑";
        }

        public boolean isEdit() {
            return isEdit;
        }

        public void setEdit(boolean edit) {
            isEdit = edit;
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
