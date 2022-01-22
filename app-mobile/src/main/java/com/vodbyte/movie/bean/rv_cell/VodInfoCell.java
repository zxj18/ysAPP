package com.vodbyte.movie.bean.rv_cell;

import android.annotation.SuppressLint;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.config.ItemType;
import com.vodbyte.movie.utils.AnimateUtil;
import com.vodbyte.movie.utils.VodUtil;
import com.vodbyte.movie.widget.foldtext.SpannableFoldTextView;

/**
 * 视频详情
 */
public class VodInfoCell extends BaseRvCell<VodBean> implements View.OnClickListener {

    public OnBtnClickViewListener mBtnListener;
    private boolean isCollected = false;

    public VodInfoCell(VodBean vodBean) {
        super(vodBean);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_VOD_INFO_CELL;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRvViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_vod_info,parent,false));
    }

    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, int position) {
        TextView mTitleView = holder.getTextView(R.id.tv_title_view);
        TextView mScoreView = holder.getTextView(R.id.tv_score_view);
        TextView mClassView = holder.getTextView(R.id.tv_class_view);
        SpannableFoldTextView mContentView = (SpannableFoldTextView)holder.getView(R.id.tv_content_view);

        Button mDownloadView = holder.getButton(R.id.btn_download);
        Button mCollectView = holder.getButton(R.id.btn_collect);
        Button mFeedbackView = holder.getButton(R.id.btn_feedback);
        Button mShareView = holder.getButton(R.id.btn_share);

        mDownloadView.setOnClickListener(this);
        mCollectView.setOnClickListener(this);
        mFeedbackView.setOnClickListener(this);
        mShareView.setOnClickListener(this);

        try {
            mTitleView.setText(Html.fromHtml(mData.getVod_name()));
            mContentView.setText(Html.fromHtml(String.format("%s",mData.getVod_blurb())));
            mScoreView.setText(mData.getVod_score());
            if (isCollected = VodUtil.isVodCollect(mData.getVid())) {
                mCollectView.setText(R.string.yet_collect);
            }else {
                mCollectView.setText(R.string.collect);
            }
            mClassView.setText(String.format("%s/%s/%s",mData.getVod_year(),mData.getVod_area(),mData.getVod_class()));
        }catch (Exception e) {
            //Log.d("error",e.getMessage());
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (mBtnListener != null){
            switch (v.getId())
            {
                case R.id.btn_feedback:
                    mBtnListener.onClickItem(v,0);
                    break;
                case R.id.btn_download:
                    mBtnListener.onClickItem(v,1);
                    break;
                case R.id.btn_collect:
                    Button button = (Button)v;
                    AnimateUtil.likeAnimate(v,isCollected);
                    isCollected = !isCollected;
                    VodUtil.vodCollect(isCollected,mData.getVid());

                    if (isCollected) {
                        button.setText(R.string.yet_collect);
                    }else {
                        button.setText(R.string.collect);
                    }

                    mBtnListener.onClickItem(v,2);
                    break;
                case R.id.btn_share:
                    mBtnListener.onClickItem(v,3);
                    break;
            }
        }
    }

    /**
     * 添加监听事件
     * @param mListener
     */
    public void setListener(OnBtnClickViewListener mListener) {
        this.mBtnListener = mListener;
    }

    public interface OnBtnClickViewListener {
        void onClickItem(View view,int index);
    }
}
