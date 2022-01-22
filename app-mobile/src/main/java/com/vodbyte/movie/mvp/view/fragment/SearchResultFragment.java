package com.vodbyte.movie.mvp.view.fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseAbsFragment;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.bean.rv_cell.TopCell;
import com.vodbyte.movie.bean.rv_cell.VodCell;
import com.vodbyte.movie.mvp.contract.ISearchResultContract;
import com.vodbyte.movie.mvp.presenter.SearchResultPresenter;
import com.vodbyte.movie.utils.StartActUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果
 */
public class SearchResultFragment extends BaseAbsFragment<SearchResultPresenter> implements ISearchResultContract.View {

    private String mWord;
    private int mPage = 1;

    public void setData(String word){
        mWord = word;
    }

    @Override
    protected RecyclerView.LayoutManager initLayoutManger() {
        return new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected SearchResultPresenter getInstance() {
        return new SearchResultPresenter();
    }

    @Override
    public void onRecyclerViewInitialized() {
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.baseBackground));
        mPresenter.SearchWord(mWord,mPage);
    }

    @Override
    public void onPullRefresh() {
        mPage = 1;
        mPresenter.SearchWord(mWord,mPage = 1);
    }

    @Override
    public void onLoadMore() {
        mPresenter.SearchWord(mWord,mPage);
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void noMore() {
        showToast("没有更多数据了！");
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
        mRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void setSearchResult(List<VodBean> list) {
        mPage += 1;

        if (mRefreshLayout.isRefreshing()) {
            mBaseAdapter.clear();
            mPage = 1;
        }

        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();

        List<BaseRvCell> cellList = new ArrayList<>();
        for (final VodBean vodBean : list) {

            TopCell vodCell = new TopCell(vodBean);
            vodCell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    StartActUtil.toPlayDetail(mActivity, vodBean);
                }

            });
            cellList.add(vodCell);
        }
        mBaseAdapter.addAll(cellList);
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
