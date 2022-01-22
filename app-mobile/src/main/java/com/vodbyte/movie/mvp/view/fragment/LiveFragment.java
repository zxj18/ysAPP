package com.vodbyte.movie.mvp.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseLazyFragment;
import com.vodbyte.movie.bean.LiveAreaBean;
import com.vodbyte.movie.mvp.adapter.LiveAreaLeftAdapter;
import com.vodbyte.movie.mvp.adapter.LiveAreaRightAdapter;
import com.vodbyte.movie.mvp.contract.ILiveAreaContract;
import com.vodbyte.movie.mvp.presenter.LiveAreaPresenter;
import com.vodbyte.movie.utils.StartActUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;


public class LiveFragment extends BaseLazyFragment<LiveAreaPresenter> implements ILiveAreaContract.view {

    @BindView(R.id.lv_tv_left_view)
    ListView mLeftListView;
    @BindView(R.id.lv_tv_right_view)
    ListView mRightListView;
    @BindView(R.id.refreshLayout_live_listview)
    SmartRefreshLayout mRefreshLayout;

    private View lastView;
    private List<LiveAreaBean> mLiveAreaList;
    private List<LiveAreaBean.LiveBean> mLiveBeanList;
    private LiveAreaLeftAdapter mLeftAdapter;
    private LiveAreaRightAdapter mRightAdapter;

    @Override
    protected LiveAreaPresenter getInstance() {
        return new LiveAreaPresenter();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_tab_live;
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected void setListener() {
        //左侧类型点击对应右侧商品
        mLeftListView.setOnItemClickListener((parent, view, position, id) -> {
            if (lastView != null) {
                //上次选中的view变回灰色
                lastView.setBackgroundColor(getResources().getColor(R.color.frament_tab_color));
            }
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            lastView = view;
            mLiveBeanList = mLiveAreaList.get(position).getData();
            mRightAdapter = new LiveAreaRightAdapter(getActivity(),mLiveBeanList);
            mRightListView.setAdapter(mRightAdapter);
        });

        mRightListView.setOnItemClickListener((parent, view, position, id) -> {
            LiveAreaBean.LiveBean liveBean =  mLiveBeanList.get(position);
            StartActUtil.toLivePlayDetail(getActivity(),liveBean);
        });

        mRefreshLayout.setOnRefreshListener(refreshLayout -> lazyLoad());
    }

    @Override
    protected void lazyLoad() {
        mPresenter.getLiveAreaList();
    }

    protected void initView(View view) {
        mRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void setLiveAreaList(List<LiveAreaBean> list) {
        mLiveAreaList = list;
        mLeftAdapter = new LiveAreaLeftAdapter(getActivity(), mLiveAreaList);
        mLeftListView.setAdapter(mLeftAdapter);
        mRefreshLayout.finishRefresh();

        try {
            mLiveBeanList = mLiveAreaList.get(0).getData();
            mRightAdapter = new LiveAreaRightAdapter(getActivity(),mLiveBeanList);
            mRightListView.setAdapter(mRightAdapter);
        }catch (Exception e){}
    }

    @Override
    public void showError(String msg) {
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
