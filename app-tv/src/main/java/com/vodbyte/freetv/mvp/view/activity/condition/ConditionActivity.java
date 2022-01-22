package com.vodbyte.freetv.mvp.view.activity.condition;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.api.ScrollBoundaryDecider;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.base.BaseActivity;
import com.vodbyte.freetv.bean.ClassifyTitleBean;
import com.vodbyte.freetv.bean.VodBean;
import com.vodbyte.freetv.bean.rvc_cell.CidLabelCell;
import com.vodbyte.freetv.bean.rvc_cell.VodCell;
import com.vodbyte.freetv.common.Ui;
import com.vodbyte.freetv.listener.OnVodCidClickListener;
import com.vodbyte.freetv.mvp.adapter.ColorItemsAdapter;
import com.vodbyte.freetv.mvp.contract.IClassifyContract;
import com.vodbyte.freetv.mvp.contract.ISearchContract;
import com.vodbyte.freetv.mvp.presenter.ClassifyPresenter;
import com.vodbyte.freetv.mvp.presenter.SearchPresenter;
import com.vodbyte.freetv.utils.StartActUtil;
import com.vodbyte.freetv.widget.RecyclerViewHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 筛选页面
 */
public class ConditionActivity extends BaseActivity<ClassifyPresenter> implements IClassifyContract.View {

    @BindView(R.id.rv_class_name_view)
    RecyclerView mRvClassNameView;
    @BindView(R.id.rv_area_view)
    RecyclerView mRvAreaView;
    @BindView(R.id.rv_lang_view)
    RecyclerView mRvLangView;
    @BindView(R.id.rv_year_view)
    RecyclerView mRvYearView;;

    @BindView(R.id.rv_list_view)
    RecyclerView mRvListView;;

    @BindView(R.id.base_refresh_layout)
    SmartRefreshLayout mRefreshLayout;;

    RvSimpleAdapter rvTopClassNameSimpleAdapter = new RvSimpleAdapter();
    RvSimpleAdapter rvClassNameSimpleAdapter = new RvSimpleAdapter();
    RvSimpleAdapter rvAreaSimpleAdapter = new RvSimpleAdapter();
    RvSimpleAdapter rvLangSimpleAdapter = new RvSimpleAdapter();
    RvSimpleAdapter rvYearSimpleAdapter = new RvSimpleAdapter();
    RvSimpleAdapter rvListViewAdapter = new RvSimpleAdapter();

    private String mRvClassNameData = "all";
    private String mRvAreaData = "all";
    private String mRvLangData = "all";
    private int mRvYearData = 0;
    private int mPage = 1;
    private int mCid = 0;
    private boolean mHasMoreData = true;
    private boolean mIsLoadMoreData = true;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_condition;
    }

    @Override
    protected ClassifyPresenter getInstance() {
        return new ClassifyPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        // 获取分类数据
        mPresenter.getCidTitleList();

    }

    @Override
    protected void initView() {
        Ui.configTopBar(this);

      //  mRvTopClassNameView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRvClassNameView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRvAreaView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRvLangView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRvYearView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRvListView.setLayoutManager(new GridLayoutManager(this,8,GridLayoutManager.VERTICAL,false));

     //   mRvTopClassNameView.setAdapter(rvTopClassNameSimpleAdapter);
        mRvClassNameView.setAdapter(rvClassNameSimpleAdapter);
        mRvAreaView.setAdapter(rvAreaSimpleAdapter);
        mRvLangView.setAdapter(rvLangSimpleAdapter);
        mRvYearView.setAdapter(rvYearSimpleAdapter);
        mRvListView.setAdapter(rvListViewAdapter);

    }

    @Override
    protected void initListener() {

        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(true);
        mRefreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDecider() {
            @Override
            public boolean canRefresh(View content) {
                return false;
            }

            @Override
            public boolean canLoadMore(View content) {
                return false;
            }
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mIsLoadMoreData = true;

            if (mHasMoreData) {
                mPresenter.getClassifyList(mPage,mCid,mRvClassNameData,mRvAreaData,mRvLangData,mRvYearData);
            }else {
                showToast("暂无更多");
            }
        });
    }


    @Override
    public void setClassifyList(List<VodBean> list) {

        if (!mIsLoadMoreData) {
            rvListViewAdapter.clear();
        }

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
                        StartActUtil.toPlayDetail(getApplicationContext(),bean);
                    }
                });

                vodCell.setLoadMoreListener((view, position) -> {
                    if ((rvListViewAdapter.getData().size() - position) <= 6 ){
                        //RefreshLayout.autoLoadMore();
                    }
                });
                cellList.add(vodCell);
            }
            rvListViewAdapter.addAll(cellList);
            mPage += 1;
        }else {
            mHasMoreData = false;
        }

    }

    // 分类数据获取成功
    @Override
    public void setCidTitle(List<ClassifyTitleBean> list) {

        if (list.size() > 0) {
            List<BaseRvCell> cellList = new ArrayList<>();

            for (final ClassifyTitleBean bean : list) {

                CidLabelCell cidLabelCell = new CidLabelCell(bean.getName());
                cidLabelCell.setListener(new OnClickViewRvListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }

                    @Override
                    public <C> void onClickItem(C data, int position) {
                        //加载子条件
                        loadOtherClass(list.get(position));
                        mIsLoadMoreData = false;
                    }

                });
                cellList.add(cidLabelCell);
            }
            rvTopClassNameSimpleAdapter.addAll(cellList);

            //加载子条件
            loadOtherClass(list.get(0));
        }
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
            }
        }));

        mPresenter.getClassifyList(mPage,mTitleBean.getId(),mRvClassNameData,mRvAreaData,mRvLangData,mRvYearData);
    }

    @Override
    public void onError(String msg) {

    }

}