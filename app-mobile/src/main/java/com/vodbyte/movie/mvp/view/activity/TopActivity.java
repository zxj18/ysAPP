package com.vodbyte.movie.mvp.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseActivity;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.bean.rv_cell.TopCell;
import com.vodbyte.movie.bean.rv_cell.VodCell;
import com.vodbyte.movie.mvp.contract.ITopContract;
import com.vodbyte.movie.mvp.presenter.TopPresenter;
import com.vodbyte.movie.utils.StartActUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TopActivity extends BaseActivity<TopPresenter> implements ITopContract.View {

    @BindView(R.id.rv_top_layout_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout_top)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;
    @BindView(R.id.iv_back_layout_top)
    ImageView mIvBack;

    private RvSimpleAdapter mBaseAdapter;
    private int mPage = 1;
    private Context mContext;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_top;
    }

    @Override
    protected TopPresenter getInstance() {
        return new TopPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTvTitle.setText(R.string.top);
    }

    @Override
    protected void initView() {
        mContext = this;

        mBaseAdapter = new RvSimpleAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mBaseAdapter);
        mPresenter.getTopData(mPage);
    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(v -> onBackPressed());
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mPresenter.getTopData(mPage);
        });
    }


    @Override
    public void setTopData(List<VodBean> list) {
        if (mRefreshLayout.isRefreshing()){
            mBaseAdapter.clear();
        }
        mRefreshLayout.finishRefresh();

        List<BaseRvCell> baseRvCellList = new ArrayList<>();

        for (final VodBean bean : list) {
            TopCell vodCell = new TopCell(bean);
            vodCell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    StartActUtil.toPlayDetail(mContext,bean);
                }

            });
            baseRvCellList.add(vodCell);
        }
        mBaseAdapter.addAll(baseRvCellList);
    }

    @Override
    public void onError(String msg) {
        mRefreshLayout.finishRefresh();
        showToast(msg);
    }
}
