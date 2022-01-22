package com.vodbyte.movie.mvp.view.fragment.VodDetail;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vodbyte.movie.bean.rv_cell.VodBannerAdCell;
import com.vodbyte.movie.mvp.contract.IVodTabDetailContract;
import com.vodbyte.movie.mvp.model.dto.VodListDTO;
import com.vodbyte.movie.mvp.presenter.VodTabDetailPresenter;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseLazyFragment;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.bean.rv_cell.VodInfoCell;
import com.vodbyte.movie.bean.rv_cell.BookListCell;
import com.vodbyte.movie.bean.rv_cell.VodSubSetCell;
import com.vodbyte.movie.mvp.model.dto.VodDetailDTO;
import java.util.List;

import butterknife.BindView;

public class TabDetailFragment extends BaseLazyFragment<VodTabDetailPresenter>
implements IVodTabDetailContract.View{

    @BindView(R.id.rv_container_fragment_tab_detail)
    RecyclerView mRvContainer;

    private RvSimpleAdapter mAdapter;
    private VodDetailDTO mVodDetailDTO;
    public VodSubSetCell.OnSubSetClickViewListener mSubSetListener;
    public VodInfoCell.OnBtnClickViewListener mBtnListener;

    private final String TAG = "TabDetailFragment";

    public void setData(VodDetailDTO vodDetailDTO){
        mVodDetailDTO = vodDetailDTO;
        lazyLoad();
    }

    /**
     * 添加监听事件
     * @param mListener
     */
    public TabDetailFragment setSubSetListener(VodSubSetCell.OnSubSetClickViewListener mListener) {
        this.mSubSetListener = mListener;
        return this;
    }
    /**
     * 添加监听事件
     * @param mListener
     */
    public TabDetailFragment setBtnListener(VodInfoCell.OnBtnClickViewListener mListener) {
        this.mBtnListener = mListener;
        return this;
    }

    @Override
    protected VodTabDetailPresenter getInstance() {
        return new VodTabDetailPresenter();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_tab_detail;
    }

    @Override
    protected void initData(Bundle bundle) {
    }

    @Override
    protected void initView(View view) {
        mAdapter = new RvSimpleAdapter();
        mRvContainer.setLayoutManager(new LinearLayoutManager(
                mBaseActivity, LinearLayoutManager.VERTICAL,
                false));
        mRvContainer.setAdapter(mAdapter);

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded){
            return;
        }

        if (mVodDetailDTO != null){
            isLoaded = true;
            VodInfoCell vodInfoCell = new VodInfoCell(mVodDetailDTO.getData());
            VodBannerAdCell  bannerAdCell = new VodBannerAdCell();
            VodSubSetCell vodSubSetCell = new VodSubSetCell(mVodDetailDTO.getData().getVod_play_list());
            // 分集选择
            vodSubSetCell.setListener((vodIndex,urlBean,isP2p) -> {
                if (mSubSetListener != null) {
                    mSubSetListener.onClickItem(vodIndex,urlBean,isP2p);
                }
            });

            vodInfoCell.setListener((view, index) -> {
                if (mBtnListener != null) {
                    mBtnListener.onClickItem(view,index);
                }
            });

            mAdapter.add(vodInfoCell);
            mAdapter.add(bannerAdCell);
            mAdapter.add(vodSubSetCell);

            mPresenter.getVodTabData(mVodDetailDTO.getData().getVid(),mVodDetailDTO.getData().getCid());
        }
    }

    private void addItem(int type,String name,List<VodBean> list){
        if (list != null && list.size() != 0){
            mAdapter.add(new BookListCell(list,type,name));
        }
    }

    @Override
    public void setVodTabData(VodListDTO vodListDTO) {
        addItem(BookListCell.TYPE_TAB_SET_SUB_LIST_RECOMMEND,"相关推荐", vodListDTO.getData());
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }
}
