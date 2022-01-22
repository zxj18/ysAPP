package com.vodbyte.movie.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseActivity;
import com.vodbyte.movie.bean.GirlBean;
import com.vodbyte.movie.bean.rv_cell.GirlCell;
import com.vodbyte.movie.mvp.contract.IGirlsContract;
import com.vodbyte.movie.mvp.presenter.GirlsPresenter;
import com.vodbyte.movie.widget.textimagepager.ImagePagerBean;
import com.vodbyte.movie.widget.textimagepager.ImageViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GirlListActivity extends BaseActivity<GirlsPresenter> implements IGirlsContract.view{

    @BindView(R.id.rv_girl_layout_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout_girls)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;
    @BindView(R.id.iv_back_layout_top)
    ImageView mIvBack;

    private RvSimpleAdapter mBaseAdapter;
    private int mPage = 1;


    @Override
    protected int setLayoutResID() {
        return R.layout.activity_girl;
    }

    @Override
    protected GirlsPresenter getInstance() {
        return new GirlsPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTvTitle.setText(R.string.girl);
    }

    @Override
    protected void initView() {
        mBaseAdapter = new RvSimpleAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, 2,
                GridLayoutManager.VERTICAL,
                false));
        mRecyclerView.setAdapter(mBaseAdapter);
        mRefreshLayout.setEnableLoadMore(true);

        mPresenter.getGirlsList(mPage);
    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(v -> onBackPressed());
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mPresenter.getGirlsList(mPage);
        });
        mRefreshLayout.setOnRefreshListener(refreshLayout -> mPresenter.getGirlsList(mPage = 1));
    }

    @Override
    public void setGirlsList(List<GirlBean> list) {
        mPage += 1;

        if (mRefreshLayout.isRefreshing()){
            mBaseAdapter.clear();
            mPage = 1;
        }
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();

        List<BaseRvCell> baseRvCellList = new ArrayList<>();

        for (final GirlBean bean : list) {
            GirlCell girlCell = new GirlCell(bean);
            girlCell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    GirlBean girlBean = (GirlBean)data;
                    List<ImagePagerBean> mList = new ArrayList<>();
                    for (String url : girlBean.getUrls()) {
                        ImagePagerBean imagePagerBean = new ImagePagerBean(url,girlBean.getTitle(),url);
                        mList.add(imagePagerBean);
                    }
                    try {
                        ImageViewPager.start(GirlListActivity.this, (ArrayList<ImagePagerBean>) mList, 0);
                    }catch (Exception e){
                    }
                }

            });
            baseRvCellList.add(girlCell);
        }
        mBaseAdapter.addAll(baseRvCellList);
    }

    @Override
    public void showError(String msg) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        showToast(msg);
    }

}
