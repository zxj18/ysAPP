package com.vodbyte.freetv.mvp.view.activity.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.vodbyte.freetv.app.FreeTVApp;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.base.BaseActivity;
import com.vodbyte.freetv.base.BasePresenter;
import com.vodbyte.freetv.bean.VodBean;
import com.vodbyte.freetv.common.InternalFileSaveUtil;
import com.vodbyte.freetv.common.Ui;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.freetv.utils.StartActUtil;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoHistoryActivity extends BaseActivity implements VideoHistoryAdapter.OnHistoryItemClickListener {

    @BindView(R.id.video_history_recycler_view)
    RecyclerView video_history_recycler_view;
    private LinkedList<VodBean> historyList;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_video_history;
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        historyList = (LinkedList<VodBean>) InternalFileSaveUtil.getInstance(this).get("video_history");
        video_history_recycler_view.setLayoutManager(new GridLayoutManager(this, 6) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return FreeTVApp.screenHeight / 2;
            }
        });
        if (historyList != null) {
            VideoHistoryAdapter adapter = new VideoHistoryAdapter(this, historyList, this);
            video_history_recycler_view.setAdapter(adapter);
        }
    }

    @Override
    protected void initView() {
        Ui.configTopBar(this, "选中视频时菜单键可删除");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onHistoryItemClick(int position) {
        VodBean vodBean = historyList.get(position);
        StartActUtil.toPlayDetail(this,vodBean);
    }

    @Override
    public void onHistoryItemMenuClick(int position) {
        historyList.remove(position);
        InternalFileSaveUtil.getInstance(this).put("video_history", historyList);
        VideoHistoryAdapter adapter = new VideoHistoryAdapter(this, historyList, this);
        video_history_recycler_view.setAdapter(adapter);
    }
}
