package com.vodbyte.movie.mvp.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseLazyFragment;
import com.vodbyte.movie.bean.BannerBean;
import com.vodbyte.movie.bean.ItemBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.bean.rv_cell.VodCell;
import com.vodbyte.movie.listener.OnVodClickListener;
import com.vodbyte.movie.mvp.contract.IHomeCidContract;
import com.vodbyte.movie.mvp.presenter.HomeCidPresenter;
import com.vodbyte.movie.utils.StartActUtil;
import com.vodbyte.movie.widget.ListenOffsetYNestedScrollView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * HomeCidFragment
 */
public class HomeCidFragment extends BaseLazyFragment<HomeCidPresenter> implements IHomeCidContract.View {
    @BindView(R.id.banner_fragment_home)
    Banner mBanner;
    @BindView(R.id.rv_cid_vod_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout_fragment_home)
    SmartRefreshLayout mRefreshLayout;
    List<BannerBean> mBannerList;
    RvSimpleAdapter mRvSimpleAdapter;

    private int mCid = 0;
    private int mPage = 1;

    public void setCid(int cid){
        mCid = cid;
    }

    @Override
    protected HomeCidPresenter getInstance() {
        return new HomeCidPresenter();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_cid_home;
    }

    @Override
    protected void initData(Bundle bundle) {
    }

    @Override
    protected void initView(View view) {
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mRvSimpleAdapter.clear();
            mPresenter.getHomeVodBeanListData(mCid,1);
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mPresenter.getHomeVodBeanListData(mCid,mPage);
        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(
                mBaseActivity, 3,
                GridLayoutManager.VERTICAL,
                false));
        mRvSimpleAdapter = new RvSimpleAdapter();
        mRecyclerView.setAdapter(mRvSimpleAdapter);
    }

    @Override
    protected void setListener() {
//        mRefreshLayout.autoRefresh(1000 * 8);
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded){
            return;
        }
        mPresenter.getHomeVodBeanListData(mCid,mPage);
        isLoaded = true;
    }

    @Override
    public void showHomeVodBeanListData(List<BannerBean> bannerList, List<VodBean> vodBeanList) {

        mBannerList = bannerList;
        isLoaded = true;

        if (vodBeanList.size() > 0) {
            mPage += 1;
        }

        if (mRefreshLayout.isRefreshing()){
            mRvSimpleAdapter.clear();
            mPage = 2;
        }

        initBanner(bannerList);
        addAllVodItem(vodBeanList);
        
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }


    @Override
    public void showHomeBlockData(List<BannerBean> bannerList, List<ItemBean> itemBeanList) {

    }

    /**
     * 添加分类小说
     */
    private void addAllVodItem( List<VodBean> vodBeanList) {

        List<BaseRvCell> vodCellList = new ArrayList<>();;

        for (final VodBean vodBean : vodBeanList) {

            VodCell vodCell = new VodCell(vodBean);
            vodCell.setGrid(true);

            vodCell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    StartActUtil.toPlayDetail(mBaseActivity,(VodBean) data);
                }
            });
            vodCellList.add(vodCell);
        }

        mRvSimpleAdapter.addAll(vodCellList);
    }

    /**
     * 初始化Banner
     * @param bannerList
     */
    private void initBanner(final List<BannerBean> bannerList) {
        List<String> imageUrlList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        for (BannerBean bannerBean : bannerList) {
            imageUrlList.add(bannerBean.getImageUrl());
            titleList.add(bannerBean.getTitle());
        }
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        mBanner.setImages(imageUrlList);
        mBanner.setBannerTitles(titleList);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                StartActUtil.toPlayDetail(mBaseActivity,mBannerList.get(position).getVod());
            }
        });
        mBanner.start();
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
        mRefreshLayout.finishRefresh();
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

