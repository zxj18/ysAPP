package com.vodbyte.movie.mvp.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseActivity;
import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.bean.ConfigBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.mvp.contract.IFeedBackContract;
import com.vodbyte.movie.mvp.model.dto.StringDataDTO;
import com.vodbyte.movie.mvp.presenter.FeedBackPresenter;
import com.vodbyte.movie.utils.DeviceIDUtils;
import com.vodbyte.movie.utils.GsonUtil;
import com.vodbyte.movie.widget.component.PlayerMonitor;
import com.vodbyte.movie.widget.videoview.IjkVideoView;
import com.vodbyte.videocontroller.StandardVideoController;
import com.vodbyte.videocontroller.component.GestureView;
import com.vodbyte.videocontroller.component.LiveControlView;
import com.vodbyte.videocontroller.component.TitleView;
import com.vodbyte.videoplayer.player.VideoView;
import com.vodbyte.videoplayer.util.L;

import butterknife.BindView;

/**
 * 反馈信息
 */
public class FeedBackActivity extends BaseActivity<FeedBackPresenter> implements IFeedBackContract.View {

    @BindView(R.id.iv_back_layout_top)
    ImageView mIvBack;
    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;
    @BindView(R.id.btn_feedback_submit)
    Button mFeedbackSubmit;
    @BindView(R.id.et_feedback)
    EditText mFeedbackEditText;

    private String mContent;
    private String mVid;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_feedback;
    }

    @Override
    protected FeedBackPresenter getInstance() {
        return new FeedBackPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mContent = intent.getStringExtra("content");
        mVid = intent.getStringExtra("vid");
        mFeedbackEditText.setText(mContent);
        mFeedbackEditText.setSelection(mFeedbackEditText.length());
        mFeedbackEditText.requestFocus();
    }

    @Override
    protected void initView() {
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText("反馈信息");
    }


    @Override
    protected void initListener() {
        // 返回
        mIvBack.setOnClickListener(v -> onBackPressed());
        mFeedbackSubmit.setOnClickListener(v -> {
            mPresenter.sendFeedBack(mVid,mFeedbackEditText.getText().toString(),DeviceIDUtils.getDeviceId(getBaseContext()));
        });
    }


    @Override
    public void onFeedBack(StringDataDTO bean) {
        showToast(bean.getData());
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }

    @Override
    public void onResume(){
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
