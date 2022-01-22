package com.vodbyte.freetv.mvp.view.activity.home;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.OnLoadMoreRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.base.BaseLazyAbsFragment;

import com.vodbyte.freetv.bean.ClassifyTitleBean;
import com.vodbyte.freetv.bean.VodBean;
import com.vodbyte.freetv.bean.rvc_cell.VodCell;
import com.vodbyte.freetv.listener.OnVodCidClickListener;
import com.vodbyte.freetv.mvp.contract.IClassifyContract;
import com.vodbyte.freetv.mvp.presenter.ClassifyPresenter;
import com.vodbyte.freetv.utils.StartActUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;

public class HomeVideoFragment extends BaseLazyAbsFragment<ClassifyPresenter> implements IClassifyContract.View {

    @BindView(R.id.rv_class_name_view)
    RecyclerView mRvClassNameView;
    @BindView(R.id.rv_area_view)
    RecyclerView mRvAreaView;
    @BindView(R.id.rv_lang_view)
    RecyclerView mRvLangView;
    @BindView(R.id.rv_year_view)
    RecyclerView mRvYearView;;

    RvSimpleAdapter rvClassNameSimpleAdapter = new RvSimpleAdapter();
    RvSimpleAdapter rvAreaSimpleAdapter = new RvSimpleAdapter();
    RvSimpleAdapter rvLangSimpleAdapter = new RvSimpleAdapter();
    RvSimpleAdapter rvYearSimpleAdapter = new RvSimpleAdapter();

    private String mRvClassNameData = "all";
    private String mRvAreaData = "all";
    private String mRvLangData = "all";
    private int mRvYearData = 0;
    private int mPage = 1;
    private int mCid = 0;
    private boolean mHasMoreData = true;
    private boolean mIsLoadMoreData = true;
    private ClassifyTitleBean mTitleBean;
    private LinearLayout mTopClassView;

    public void setCidInfo(ClassifyTitleBean titleBean)
    {
        mTitleBean = titleBean;
    }

    @Override
    protected ClassifyPresenter getInstance() {
        return new ClassifyPresenter();
    }

    @Override
    protected RecyclerView.LayoutManager initLayoutManger() {
        return new GridLayoutManager(mActivity,6,GridLayoutManager.VERTICAL,false);
    }

    @Override
    public void onRecyclerViewInitialized() {
    }

    /**
     * 设置头部是否显示
     */
    protected void setHeaderVisibility()
    {
        if (mTopClassView == null) return;
        if (mTopClassView.getVisibility() == View.VISIBLE) {
            mTopClassView.setVisibility(View.GONE);
        }else {
            mTopClassView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected View addTopLayout() {
        View mHeaderView = LayoutInflater.from(mBaseActivity).inflate(R.layout.top_class_view,null);
        mTopClassView = mHeaderView.findViewById(R.id.top_class_view);

        mRvClassNameView = mTopClassView.findViewById(R.id.rv_class_name_view);
        mRvAreaView  = mTopClassView.findViewById(R.id.rv_area_view);
        mRvLangView  = mTopClassView.findViewById(R.id.rv_lang_view);
        mRvYearView  = mTopClassView.findViewById(R.id.rv_year_view);


        mRvClassNameView.setLayoutManager(new LinearLayoutManager(mBaseActivity,LinearLayoutManager.HORIZONTAL,false));
        mRvAreaView.setLayoutManager(new LinearLayoutManager(mBaseActivity,LinearLayoutManager.HORIZONTAL,false));
        mRvLangView.setLayoutManager(new LinearLayoutManager(mBaseActivity,LinearLayoutManager.HORIZONTAL,false));
        mRvYearView.setLayoutManager(new LinearLayoutManager(mBaseActivity,LinearLayoutManager.HORIZONTAL,false));

        mRvClassNameView.setAdapter(rvClassNameSimpleAdapter);
        mRvAreaView.setAdapter(rvAreaSimpleAdapter);
        mRvLangView.setAdapter(rvLangSimpleAdapter);
        mRvYearView.setAdapter(rvYearSimpleAdapter);

        return mHeaderView;
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded){
            return;
        }
        if (mTitleBean != null) {
            showSpinKit(true);
           // mPresenter.getClassifyList(mPage,mTitleBean.getId());
            loadOtherClass(mTitleBean);
        }
    }


    @Override
    public void onPullRefresh() {
        showSpinKit(true);
        mPresenter.getClassifyList(mPage = 1,mTitleBean.getId());
    }

    @Override
    public void onLoadMore() {
        if (mHasMoreData) {
            showSpinKit(true);
//            mPresenter.getClassifyList(mPage += 1,mTitleBean.getId());
            mPresenter.getClassifyList(mPage += 1,mTitleBean.getId(),mRvClassNameData,mRvAreaData,mRvLangData,mRvYearData);

        }else {
            showToast("暂无更多");
        }
    }

    @Override
    public void setClassifyList(List<VodBean> list) {
        isLoaded = true;

        if (mRefreshLayout.isRefreshing() || !mIsLoadMoreData) {
            mBaseAdapter.clear();
        }

        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();

        if (list.size() > 0) {
            ArrayList<BaseRvCell> cellList = new ArrayList<>();
            for (final VodBean bean : list) {
                VodCell vodCell = new VodCell(bean);
                vodCell.setListener(new OnClickViewRvListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }

                    @Override
                    public <C> void onClickItem(C data, int position) {
                        StartActUtil.toPlayDetail(mActivity,bean);
                    }
                });

                vodCell.setLoadMoreListener((view, position) -> {
                    if ((mBaseAdapter.getData().size() - position) <= 6 ){
                        mRefreshLayout.autoLoadMore();
                    }
                });
                cellList.add(vodCell);
            }
            mBaseAdapter.addAll(cellList);
            mIsLoadMoreData = true;
        }else {
            mHasMoreData = false;
        }

        showSpinKit(false);
    }

    @Override
    public void setCidTitle(List<ClassifyTitleBean> list) {

    }

    /**
     * 加载筛选条件
     * @param mTitleBean
     */
    private void loadOtherClass(ClassifyTitleBean mTitleBean)
    {
        mCid = mTitleBean.getId();

        rvClassNameSimpleAdapter.clear();
        rvAreaSimpleAdapter.clear();
        rvLangSimpleAdapter.clear();
        rvYearSimpleAdapter.clear();

        if (mTitleBean.getExtend().getClassName().size() == 2) {
            mRvClassNameView.setVisibility(View.GONE);
        }else {
            mRvClassNameView.setVisibility(View.VISIBLE);
        }

        if (mTitleBean.getExtend().getArea().size() == 2) {
            mRvAreaView.setVisibility(View.GONE);
        }else {
            mRvAreaView.setVisibility(View.VISIBLE);
        }

//        if (mTitleBean.getExtend().getLang().size() == 2) {
        mRvLangView.setVisibility(View.GONE);
//        }else {
//            mRvLangView.setVisibility(View.VISIBLE);
//        }

        if (mTitleBean.getExtend().getYear().size() == 2) {
            mRvYearView.setVisibility(View.GONE);
        }else {
            mRvYearView.setVisibility(View.VISIBLE);
        }

        // 其他条件
        rvClassNameSimpleAdapter.addAll(mTitleBean.getVodCidCellList(mTitleBean.getExtend().getClassName(), new OnVodCidClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                for (int i = 0;i < mRvClassNameView.getChildCount();i ++) {
                    mRvClassNameView.getChildAt(i).setBackgroundResource(R.drawable.video_detail_content_normal);
                }
                view.setBackgroundResource(R.drawable.video_detail_content_focus);
            }

            @Override
            public void onDataClick(String data,int position) {
                if (data.contains("全部:")) {
                    mRvClassNameData = "all";
                }else {
                    mRvClassNameData = data;
                }
                mIsLoadMoreData = false;
                mPresenter.getClassifyList(mPage,mTitleBean.getId(),mRvClassNameData,mRvAreaData,mRvLangData,mRvYearData);
            }
        }));
        rvAreaSimpleAdapter.addAll(mTitleBean.getVodCidCellList(mTitleBean.getExtend().getArea(), new OnVodCidClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                for (int i = 0;i < mRvAreaView.getChildCount();i ++) {
                    mRvAreaView.getChildAt(i).setBackgroundResource(R.drawable.video_detail_content_normal);
                }
                view.setBackgroundResource(R.drawable.video_detail_content_focus);
            }

            @Override
            public void onDataClick(String data,int position) {
                if (data.contains("全部:")) {
                    mRvAreaData = "all";
                }else {
                    mRvAreaData = data;
                }
                mIsLoadMoreData = false;
                mPresenter.getClassifyList(mPage,mTitleBean.getId(),mRvClassNameData,mRvAreaData,mRvLangData,mRvYearData);
            }
        }));
        rvLangSimpleAdapter.addAll(mTitleBean.getVodCidCellList(mTitleBean.getExtend().getLang(), new OnVodCidClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                for (int i = 0;i < mRvLangView.getChildCount();i ++) {
                    mRvLangView.getChildAt(i).setBackgroundResource(R.drawable.video_detail_content_normal);
                }
                view.setBackgroundResource(R.drawable.video_detail_content_focus);
            }

            @Override
            public void onDataClick(String data,int position) {
                if (data.contains("全部:")) {
                    mRvLangData = "all";
                }else {
                    mRvLangData = data;
                }
                mIsLoadMoreData = false;
                mPresenter.getClassifyList(mPage,mTitleBean.getId(),mRvClassNameData,mRvAreaData,mRvLangData,mRvYearData);
            }
        }));
        rvYearSimpleAdapter.addAll(mTitleBean.getVodCidCellList(mTitleBean.getExtend().getYear(), new OnVodCidClickListener() {
            @Override
            public void onDataClick(String data,int position) {
                if (data.contains("全部:")) {
                    mRvYearData = 0;
                }else {
                    mRvYearData = Integer.parseInt(data);
                }
                mIsLoadMoreData = false;
                mPresenter.getClassifyList(mPage,mTitleBean.getId(),mRvClassNameData,mRvAreaData,mRvLangData,mRvYearData);
            }

            @Override
            public void onViewClick(View view, int position) {
                for (int i = 0;i < mRvYearView.getChildCount();i ++) {
                    mRvYearView.getChildAt(i).setBackgroundResource(R.drawable.video_detail_content_normal);
                }
                view.setBackgroundResource(R.drawable.video_detail_content_focus);
            }
        }));

        mPresenter.getClassifyList(mPage,mTitleBean.getId(),mRvClassNameData,mRvAreaData,mRvLangData,mRvYearData);
    }


    @Override
    public void onError(String msg) {
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
        showSpinKit(false);
        showToast(msg);
    }
}
