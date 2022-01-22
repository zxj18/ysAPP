package com.vodbyte.movie.mvp.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseLazyFragment;
import com.vodbyte.movie.bean.BannerBean;
import com.vodbyte.movie.bean.ItemBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.listener.OnVodClickListener;
import com.vodbyte.movie.mvp.contract.IHomeCidContract;
import com.vodbyte.movie.mvp.presenter.HomeCidPresenter;
import com.vodbyte.movie.utils.StartActUtil;
import com.vodbyte.movie.widget.ListenOffsetYNestedScrollView;
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
public class HomeCidBlockFragment extends BaseLazyFragment<HomeCidPresenter> implements IHomeCidContract.View {
    @BindView(R.id.ll_scroll_inner_fragment_home)
    LinearLayout mLLScrollContainer;
    @BindView(R.id.banner_fragment_home)
    Banner mBanner;
    @BindView(R.id.scrollView_fragment_home)
    ListenOffsetYNestedScrollView mScrollView;
    @BindView(R.id.refreshLayout_fragment_home)
    SmartRefreshLayout mRefreshLayout;
    List<BannerBean> mBannerList;

    private int mCid = 0;

    public void setCid(int cid){
        mCid = cid;
    }

    @Override
    protected HomeCidPresenter getInstance() {
        return new HomeCidPresenter();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_cid_block__home;
    }

    @Override
    protected void initData(Bundle bundle) {
    }

    @Override
    protected void initView(View view) {
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> mPresenter.getHomeBlockData(mCid));
    }

    @Override
    protected void setListener() {
        mRefreshLayout.autoRefresh(1000 * 8);
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded){
            return;
        }
        mPresenter.getHomeBlockData(mCid);
        isLoaded = true;
    }

    @Override
    public void showHomeVodBeanListData(List<BannerBean> bannerList, List<VodBean> vodBeanList) {

    }

    @Override
    public void showHomeBlockData(List<BannerBean> bannerList, List<ItemBean> itemBeanList) {
        if (mLLScrollContainer.getChildCount() > 1){
            mLLScrollContainer.removeViews(1,mLLScrollContainer.getChildCount() - 1);
        }
        mBannerList = bannerList;
        initBanner(bannerList);
        addAllVodItem(itemBeanList);
        mRefreshLayout.finishRefresh();
    }

    /**
     * 添加分类小说项
     * @param itemBeanList
     */
    private void addAllVodItem(List<ItemBean> itemBeanList) {
        for (ItemBean bean : itemBeanList) {
            if (bean.getVod_list().size() > 0) {

                //初始化View
                View rootView = LayoutInflater.from(mBaseActivity).inflate(R.layout.cell_item, null);
                TextView tvTitle = rootView.findViewById(R.id.tv_title_cell_item);
                RecyclerView rvVodList = rootView.findViewById(R.id.rv_book_cell_item);
                tvTitle.setText(bean.getTitle());

                rvVodList.setLayoutManager(new GridLayoutManager(
                        mBaseActivity, 3,
                        GridLayoutManager.VERTICAL,
                        false));
                RvSimpleAdapter rvSimpleAdapter = new RvSimpleAdapter();
                rvVodList.setAdapter(rvSimpleAdapter);

                rvSimpleAdapter.addAll(bean.getVodCellList(new OnVodClickListener() {
                    @Override
                    public void onClick(VodBean vodBean) {
                        StartActUtil.toPlayDetail(mBaseActivity, vodBean);
                    }
                }));
                //添加进布局
                mLLScrollContainer.addView(rootView);
            }
        }
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
}

