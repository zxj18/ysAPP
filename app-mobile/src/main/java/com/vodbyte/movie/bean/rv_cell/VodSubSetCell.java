package com.vodbyte.movie.bean.rv_cell;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.ChapterBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.config.ItemType;
import com.vodbyte.videoplayer.util.L;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 视频集数
 */
public class VodSubSetCell extends BaseRvCell<List<VodBean.VodPlayBean>> {

    RecyclerView mRvSubSetView;
    RvSimpleAdapter mSubSetRvAdapter;
    private List<VodBean.VodPlayBean.UrlBean> mNewList = new ArrayList<>();
    private List<VodBean.VodPlayBean.UrlBean> mOldList = new ArrayList<>();
    private List<BaseRvCell> mCellList;

    private boolean isDesc = true;//是否逆序
    private int mVodIndex = 0; //当前源
    private int mVodSubIndex = 0; //当前集数
    public OnSubSetClickViewListener mSubSetListener;

    public VodSubSetCell(List<VodBean.VodPlayBean> vodPlayBeanList) {
        super(vodPlayBeanList);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_VOD_SUB_SET_CELL;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRvViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vod_subset_cell_view,parent,false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, int position) {
        TextView mTvChangeSource = holder.getTextView(R.id.tv_change_source_tab_dir);
        TextView mUpdateLast = holder.getTextView(R.id.tv_update_last);
        ImageView mIvOrder = (ImageView) holder.getView(R.id.iv_order_fragment_tab_dir );
        TextView mTvOrder = (TextView) holder.getTextView(R.id.tv_order_fragment_tab_dir);
        mRvSubSetView = (RecyclerView) holder.getView(R.id.rv_subset_fragment_tab_dir);

        mSubSetRvAdapter = new RvSimpleAdapter();
        mRvSubSetView.setLayoutManager(new LinearLayoutManager(
                holder.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        mRvSubSetView.setAdapter(mSubSetRvAdapter);
        List<VodBean.VodPlayBean> mVodPlayBeanList = (List<VodBean.VodPlayBean>) mData;
        if (mVodPlayBeanList.size() == 0) {
            return;
        }

//        int urlsLimit = 0;
//        for (int i = 0; i < mVodPlayBeanList.size();i++)
//        {
//            if (mVodPlayBeanList.get(i).getUrls().size() > urlsLimit) {
//                mVodIndex = i;
//            }else {
//                urlsLimit = mVodPlayBeanList.get(i).getUrls().size();
//            }
//        }

        // 更新至
        if (mData.get(mVodIndex).getUrls().size() > 1) {
            mUpdateLast.setText(String.format("更新至%d集",mData.get(mVodIndex).getUrls().size()));
        }else {
            mUpdateLast.setVisibility(View.GONE);
            mVodIndex = 0;
        }

        if (mVodPlayBeanList.get(mVodIndex).getNote().isEmpty()) {
            mTvChangeSource.setText(mData.get(mVodIndex).getFrom());
        }else {
            mTvChangeSource.setText(mData.get(mVodIndex).getNote());
        }

        // 换源标题
        ArrayList<String> ChangeSourceTitles = new ArrayList<String>();
        for (int i = 0; i < mVodPlayBeanList.size(); i++) {
            if (mVodPlayBeanList.get(i).getNote().isEmpty()) {
                ChangeSourceTitles.add(mVodPlayBeanList.get(i).getFrom());
            }else {
                ChangeSourceTitles.add(mVodPlayBeanList.get(i).getNote());
            }
        }

        mTvChangeSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new XPopup.Builder(holder.getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .asCenterList(null,ChangeSourceTitles.toArray(new String[0]),
                                null, mVodIndex,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        mVodIndex = position;

                                        if (mData.get(mVodIndex).getNote().isEmpty()) {
                                            mTvChangeSource.setText(mData.get(mVodIndex).getFrom());
                                        }else {
                                            mTvChangeSource.setText(mData.get(mVodIndex).getNote());
                                        }

                                        if (mData.get(mVodIndex).getUrls().size() > 1) {
                                            mUpdateLast.setText(String.format("更新至%d集",mData.get(mVodIndex).getUrls().size()));
                                        }else {
                                            mUpdateLast.setVisibility(View.GONE);
                                        }

                                        List<VodBean.VodPlayBean.UrlBean> mUrlBeanList = mVodPlayBeanList.get(mVodIndex).getUrls();
                                        mOldList = mNewList;
                                        mNewList = mUrlBeanList;
                                        updateChapterList(mNewList,mOldList);

                                        // 自动播放第一个
                                        if (mSubSetListener != null && mUrlBeanList.size() != 0) {
                                            mSubSetListener.onClickItem(mVodIndex,mUrlBeanList.get(mVodSubIndex), mData.get(mVodIndex).getIs_p2p() == 1);
                                        }

                                    }
                                })
                        .show();
            }
        });

        // 排序按钮
        mTvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<VodBean.VodPlayBean.UrlBean> newList = new ArrayList<>(mNewList);
                Collections.reverse(newList);
                mOldList = mNewList;
                mNewList = newList;
                updateChapterList(mNewList,mOldList);
                mRvSubSetView.getLayoutManager().scrollToPosition(0);

                if (isDesc){
                    mTvOrder.setText("升序");
                    mIvOrder.setImageResource(R.drawable.svg_arrow_top);
                }else {
                    mTvOrder.setText("降序");
                    mIvOrder.setImageResource(R.drawable.svg_arrow_bottom);
                }
                isDesc = !isDesc;
            }
        });

        List<VodBean.VodPlayBean.UrlBean> mUrlBeanList = mVodPlayBeanList.get(mVodIndex).getUrls();
        if (!isDesc){
            Collections.reverse(mUrlBeanList);
        }

        mOldList = mNewList;
        mNewList = mUrlBeanList;
        updateChapterList(mNewList,mOldList);
    }

    private void updateChapterList(List<VodBean.VodPlayBean.UrlBean> newList, List<VodBean.VodPlayBean.UrlBean> oldList){
        mSubSetRvAdapter.getData().clear();
        mCellList = createChapterCellList(newList);
        mSubSetRvAdapter.getData().addAll(mCellList);
        mSubSetRvAdapter.notifyDataSetChanged();
    }

    private List<BaseRvCell> createChapterCellList(List<VodBean.VodPlayBean.UrlBean> list){
        List<BaseRvCell> cellList = new ArrayList<>();
        for (final VodBean.VodPlayBean.UrlBean urlBean : list) {
            SubSetNumCell subSetNumCell = new SubSetNumCell(urlBean);
            subSetNumCell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    mVodSubIndex = position;
                    if (mSubSetListener != null) {
                        mSubSetListener.onClickItem(mVodIndex,(VodBean.VodPlayBean.UrlBean)data,mData.get(mVodIndex).getIs_p2p() == 1);
                        for (BaseRvCell cell : cellList) {
                           SubSetNumCell cell1 = (SubSetNumCell)cell;
                           cell1.setCellBgColor(false);
                        }
                        subSetNumCell.setCellBgColor(true);
                    }
                }
            });
            cellList.add(subSetNumCell);
        }
        return cellList;
    }


    /**
     * 添加监听事件
     * @param mListener
     */
    public void setListener(OnSubSetClickViewListener mListener) {
        this.mSubSetListener = mListener;
    }

    public interface OnSubSetClickViewListener {
        void onClickItem(int vodIndex,VodBean.VodPlayBean.UrlBean urlBean,boolean isP2p);
    }

}
