package com.vodbyte.movie.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.core.DrawerPopupView;
import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.LiveAreaBean;
import com.vodbyte.movie.bean.rv_cell.LiveChangeSourceCell;
import com.vodbyte.movie.utils.StartActUtil;
import com.vodbyte.videocontroller.component.LiveControlView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ListDrawerPopupView extends DrawerPopupView {

    private RecyclerView mRvContainer;
    private Context mContext;
    private RvSimpleAdapter mAdapter;
    LiveAreaBean.LiveBean mLiveBean;
    public OnBtnClickViewListener mBtnListener;

    public ListDrawerPopupView(@NonNull Context context, LiveAreaBean.LiveBean liveBean) {
        super(context);
        mContext = context;
        mLiveBean = liveBean;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.right_list_drawer;
    }

    @Override
    protected void onCreate() {
        mAdapter = new RvSimpleAdapter();
        mRvContainer = findViewById(R.id.recyclerView);
        mRvContainer.setLayoutManager(new LinearLayoutManager(
                mContext,
                LinearLayoutManager.VERTICAL,
                false));
        mRvContainer.setAdapter(mAdapter);

        List<BaseRvCell> cellList = new ArrayList<>();
        for (final LiveAreaBean.LiveBean.LiveItemBean bean : mLiveBean.getContent()) {
            BaseRvCell cell = new LiveChangeSourceCell(bean);
            cell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {
                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    if (mBtnListener != null) {
                        mBtnListener.onClickItem((LiveAreaBean.LiveBean.LiveItemBean) data);
                    }
                }
            });
            cellList.add(cell);
        }
        mAdapter.addAll(cellList);
    }

    /**
     * 添加监听事件
     * @param mListener
     */
    public ListDrawerPopupView setListener(OnBtnClickViewListener mListener) {
        this.mBtnListener = mListener;
        return this;
    }

    public interface OnBtnClickViewListener {
        void onClickItem(LiveAreaBean.LiveBean.LiveItemBean bean);
    }
}
