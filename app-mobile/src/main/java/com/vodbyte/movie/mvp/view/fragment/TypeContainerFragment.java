package com.vodbyte.movie.mvp.view.fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseLazyAbsFragment;
import com.vodbyte.movie.bean.ClassifyTitleBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.bean.rv_cell.VodCell;
import com.vodbyte.movie.listener.OnVodCidClickListener;
import com.vodbyte.movie.mvp.contract.IClassifyContract;
import com.vodbyte.movie.mvp.presenter.ClassifyPresenter;
import com.vodbyte.movie.utils.StartActUtil;

import java.util.ArrayList;
import java.util.List;

public class TypeContainerFragment extends BaseLazyAbsFragment<ClassifyPresenter> implements IClassifyContract.View {

    private ClassifyTitleBean mTitleBean;
    private View mHeaderView;
    private RecyclerView mRvClassNameView;
    private RecyclerView mRvAreaView;
    private RecyclerView mRvLangView;
    private RecyclerView mRvYearView;;

    private String mRvClassNameData = "all";
    private String mRvAreaData = "all";
    private String mRvLangData = "all";
    private int mRvYearData = 0;
    private int mPage = 1;

    public void setClassifyTitle(ClassifyTitleBean titleBean ){
        mTitleBean = titleBean;
    }

    @Override
    protected RecyclerView.LayoutManager initLayoutManger() {
        return new GridLayoutManager(mActivity,3,GridLayoutManager.VERTICAL,false);
    }


    @Override
    public void onPullRefresh() {
        mPresenter.getClassifyList(1,mTitleBean.getId(),mRvClassNameData,mRvAreaData,mRvLangData,mRvYearData);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getClassifyList(mPage,mTitleBean.getId(),mRvClassNameData,mRvAreaData,mRvLangData,mRvYearData);
    }

    @Override
    public View addTopLayout() {

        mHeaderView = LayoutInflater.from(mBaseActivity).inflate(R.layout.classify_header_view,null);

        mRvClassNameView = mHeaderView.findViewById(R.id.rv_class_name_view);
        mRvAreaView = mHeaderView.findViewById(R.id.rv_area_view);
        mRvLangView = mHeaderView.findViewById(R.id.rv_lang_view);
        mRvYearView = mHeaderView.findViewById(R.id.rv_year_view);

        mRvClassNameView.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL,false));
        mRvAreaView.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL,false));
        mRvLangView.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL,false));
        mRvYearView.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL,false));

        RvSimpleAdapter rvClassNameSimpleAdapter = new RvSimpleAdapter();
        RvSimpleAdapter rvAreaSimpleAdapter = new RvSimpleAdapter();
        RvSimpleAdapter rvLangSimpleAdapter = new RvSimpleAdapter();
        RvSimpleAdapter rvYearSimpleAdapter = new RvSimpleAdapter();

        mRvClassNameView.setAdapter(rvClassNameSimpleAdapter);
        mRvAreaView.setAdapter(rvAreaSimpleAdapter);
        mRvLangView.setAdapter(rvLangSimpleAdapter);
        mRvYearView.setAdapter(rvYearSimpleAdapter);

        rvClassNameSimpleAdapter.addAll(mTitleBean.getVodCidCellList(mTitleBean.getExtend().getClassName(), new OnVodCidClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                for (int i = 0;i < mRvClassNameView.getChildCount();i ++) {
                    mRvClassNameView.getChildAt(i).setBackgroundColor(Color.parseColor("#00000000"));
                }
                view.setBackgroundColor(Color.parseColor("#2C7BF1"));
            }

            @Override
            public void onDataClick(String data,int position) {
                if (data.contains("类型:")) {
                    mRvClassNameData = "all";
                }else {
                    mRvClassNameData = data;
                }
                mRefreshLayout.autoRefresh();
            }
        }));
        rvAreaSimpleAdapter.addAll(mTitleBean.getVodCidCellList(mTitleBean.getExtend().getArea(), new OnVodCidClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                for (int i = 0;i < mRvAreaView.getChildCount();i ++) {
                    mRvAreaView.getChildAt(i).setBackgroundColor(Color.parseColor("#00000000"));
                }
                view.setBackgroundColor(Color.parseColor("#2C7BF1"));
            }

            @Override
            public void onDataClick(String data,int position) {
                if (data.contains("地区:")) {
                    mRvAreaData = "all";
                }else {
                    mRvAreaData = data;
                }
                mRefreshLayout.autoRefresh();
            }
        }));
        rvLangSimpleAdapter.addAll(mTitleBean.getVodCidCellList(mTitleBean.getExtend().getLang(), new OnVodCidClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                for (int i = 0;i < mRvLangView.getChildCount();i ++) {
                    mRvLangView.getChildAt(i).setBackgroundColor(Color.parseColor("#00000000"));
                }
                view.setBackgroundColor(Color.parseColor("#2C7BF1"));
            }

            @Override
            public void onDataClick(String data,int position) {
                if (data.contains("语言:")) {
                    mRvLangData = "all";
                }else {
                    mRvLangData = data;
                }
                mRefreshLayout.autoRefresh();
            }
        }));
        rvYearSimpleAdapter.addAll(mTitleBean.getVodCidCellList(mTitleBean.getExtend().getYear(), new OnVodCidClickListener() {
            @Override
            public void onDataClick(String data,int position) {
                if (data.contains("年代:")) {
                    mRvYearData = 0;
                }else {
                    mRvYearData = Integer.parseInt(data);
                }
                mRefreshLayout.autoRefresh();
            }

            @Override
            public void onViewClick(View view, int position) {
                for (int i = 0;i < mRvYearView.getChildCount();i ++) {
                    mRvYearView.getChildAt(i).setBackgroundColor(Color.parseColor("#00000000"));
                }
                view.setBackgroundColor(Color.parseColor("#2C7BF1"));
            }
        }));
        mRvLangView.setVisibility(View.GONE);
        return mHeaderView;
    }

    @Override
    public void onRecyclerViewInitialized() {

    }

    @Override
    protected ClassifyPresenter getInstance() {
        return new ClassifyPresenter();
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded){
            return;
        }
        if (mTitleBean != null){
            mBaseAdapter.showLoading();
            mPresenter.getClassifyList(mPage,mTitleBean.getId(),mRvClassNameData,mRvAreaData,mRvLangData,mRvYearData);
        }
    }

    @Override
    public void setClassifyList(List<VodBean> list) {
        isLoaded = true;
        mBaseAdapter.hideLoading();

        if (mRefreshLayout.isRefreshing()){
            mBaseAdapter.clear();
            mPage = 2;
        }else {
            if (list.size() > 0) {
                mPage += 1;
            }
        }

        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
        List<BaseRvCell> cellList = new ArrayList<>();
        for (final VodBean bean : list) {

            VodCell vodCell = new VodCell(bean);
            vodCell.setGrid(true);

            vodCell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    StartActUtil.toPlayDetail(mActivity,bean);
                }

            });
            cellList.add(vodCell);
        }

//        mBaseAdapter.showHeaderView(mHeaderView);
        mBaseAdapter.addAll(cellList);
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
        mRefreshLayout.finishLoadMore();
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
