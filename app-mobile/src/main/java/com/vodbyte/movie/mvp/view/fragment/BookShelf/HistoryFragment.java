package com.vodbyte.movie.mvp.view.fragment.BookShelf;


import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.movie.base.BaseBookShelfTabFragment;
import com.vodbyte.movie.bean.rv_cell.HistoryCell;
import com.vodbyte.movie.mvp.contract.IHistoryContract;
import com.vodbyte.movie.mvp.model.vo.HistoryVO;
import com.vodbyte.movie.mvp.presenter.HistoryPresenter;
import com.vodbyte.movie.utils.StartActUtil;
import com.vodbyte.movie.utils.diffutil.HistoryDiffUtil;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends BaseBookShelfTabFragment<HistoryPresenter>
        implements IHistoryContract.View {

    private OnClickViewRvListener mOnHistoryItemListener;

    private List<BaseRvCell> createHistoryCellList(List<HistoryVO> list){
        List<BaseRvCell> cellList = new ArrayList<>();
        for (final HistoryVO vo : list) {
            BaseRvCell cell = new HistoryCell(vo);
            cell.setListener(mOnHistoryItemListener);
            cellList.add(cell);
        }
        return cellList;
    }

    @Override
    protected HistoryPresenter getInstance() {
        return new HistoryPresenter();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded){
            return;
        }
        mPresenter.refresh();
        isLoaded = true;
    }

    @Override
    public void onRecyclerViewInitialized() {
        mOnHistoryItemListener = new OnClickViewRvListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public <C> void onClickItem(C data, int position) {
                StartActUtil.toPlayDetail(mActivity,((HistoryVO)data).getVodBean());
            }
        };
    }

    @Override
    protected RecyclerView.LayoutManager initLayoutManger() {
        return new GridLayoutManager(mBaseActivity,3);
    }

    @Override
    public void onPullRefresh() {
        mRefreshLayout.setEnableLoadMore(true);
        mPresenter.refresh();
    }

    @Override
    public void onLoadMore() {
        mPresenter.getHistoryData();
    }

    @Override
    public void setHistoryData(List<HistoryVO> oldList, List<HistoryVO> newList) {
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
        mBaseAdapter.setDataNoNotify(createHistoryCellList(newList));
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new HistoryDiffUtil(oldList,newList));
        diffResult.dispatchUpdatesTo(mBaseAdapter);
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }

    @Override
    public void noMore() {
        mRefreshLayout.setEnableLoadMore(false);
        showToast("没有更多数据了！");
    }

    @Override
    public void onStartEdit() {
        addBottomCheckLayout();
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        mPresenter.startEdit();
    }

    @Override
    public void onEndEdit() {
        removerBottomCheckLayout();
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadMore(true);
        mPresenter.endEdit();
    }

    @Override
    public void onSelectAll(boolean isSelect) {
        mPresenter.selectAll(isSelect);
    }

    @Override
    public void onDelete() {
        mPresenter.deleteBooks();
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
