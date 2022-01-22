package com.vodbyte.movie.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseActivity;
import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.mvp.view.fragment.SearchResultFragment;

import butterknife.BindView;

/**
 * 搜索结果
 */
public class SearchResultActivity extends BaseActivity {

    @BindView(R.id.iv_back_layout_top)
    ImageView mIvBack;
    @BindView(R.id.tv_title_layout_top)
    TextView mTvTitle;

    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_search_result;
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent != null){
            mTitle = intent.getStringExtra("title");
            SearchResultFragment searchResultFragment = new SearchResultFragment();
            searchResultFragment.setData(intent.getStringExtra("word"));

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_root_act_type_detail,searchResultFragment);
            transaction.commit();
        }
    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTvTitle.setText(mTitle);
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
