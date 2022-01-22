package com.vodbyte.movie.mvp.view.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseActivity;
import com.vodbyte.movie.bean.SearchHistoryBean;
import com.vodbyte.movie.bean.HotKeyBean;
import com.vodbyte.movie.bean.rv_cell.SearchHistoryCell;
import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.contract.ISearchContract;
import com.vodbyte.movie.mvp.presenter.SearchPresenter;
import com.vodbyte.movie.utils.StartActUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 搜索页面
 */
public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchContract.View {

    @BindView(R.id.iv_back_act_search)
    ImageView mIvBack;
    @BindView(R.id.iv_right_act_search)
    ImageView mIvSearch;
    @BindView(R.id.et_input_act_search)
    EditText mEtInput;
    @BindView(R.id.tfl_hot_keys_act_search)
    TagFlowLayout mTagFlowLayout;
    @BindView(R.id.rv_history_act_search)
    RecyclerView mRvHistory;

    private RvSimpleAdapter mHistoryAdapter;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_search;
    }

    @Override
    protected SearchPresenter getInstance() {
        return new SearchPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPresenter.getHotKeys();
        mPresenter.getHistoryList();
    }

    @Override
    protected void initView() {
        mHistoryAdapter = new RvSimpleAdapter();
        mRvHistory.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false));
        mRvHistory.setAdapter(mHistoryAdapter);
    }

    @Override
    public void onBackPressed() {
        List<SearchHistoryBean> list = new ArrayList<>();
        for (BaseRvCell baseRvCell : mHistoryAdapter.getData()) {
            list.add((SearchHistoryBean) baseRvCell.mData);
        }
        mPresenter.saveHistoryList(list);
        super.onBackPressed();
    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(getString(mEtInput))){
                    StartActUtil.toSearchResultAct(
                            SearchActivity.this,
                            getString(mEtInput),
                            getString(mEtInput));
                    int p =-1;
                    for (int j = 0; j < mHistoryAdapter.getData().size(); j++) {
                        SearchHistoryCell searchHistoryCell = (SearchHistoryCell) mHistoryAdapter.getData().get(j);
                        if (searchHistoryCell.mData.getKey().equals(getString(mEtInput))){
                            p = j;
                            break;
                        }
                    }
                    if (p != -1){
                        mHistoryAdapter.remove(p);
                    }
                    Log.d("TTT", "add history -> p="+p);
                    mHistoryAdapter.add(0,createCell(new SearchHistoryBean(getString(mEtInput))));
                }
            }
        });
    }

    @Override
    public void setHotKeys(final List<HotKeyBean> list) {
        final TagAdapter<HotKeyBean> tagAdapter = new TagAdapter<HotKeyBean>(list) {
            @Override
            public View getView(FlowLayout parent, int position, HotKeyBean hotKeyBean) {
                TextView tv = (TextView) LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.hotkey_tagflowlayout,
                                mTagFlowLayout,
                                false);
                tv.setText(hotKeyBean.getKey());
                tv.setBackgroundResource(hotKeyBean.getColorResId());
                return tv;
            }
        };
        mTagFlowLayout.setAdapter(tagAdapter);
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                HotKeyBean keyBean = tagAdapter.getItem(position);
                StartActUtil.toSearchResultAct(
                        SearchActivity.this,
                        keyBean.getKey(),
                        keyBean.getKey());
                return true;
            }
        });
    }

    @Override
    public void setHistoryList(List<SearchHistoryBean> list) {
        mHistoryAdapter.clear();
        List<BaseRvCell> cellList = new ArrayList<>();
        for (SearchHistoryBean bean : list) {
            BaseRvCell cell = createCell(bean);
            cellList.add(cell);
        }
        mHistoryAdapter.addAll(cellList);
    }

    @NonNull
    private BaseRvCell createCell(final SearchHistoryBean bean) {
        BaseRvCell cell = new SearchHistoryCell(bean);
        cell.setListener(new OnClickViewRvListener() {
            @Override
            public void onClick(View view, int position) {
                switch (view.getId()){
                    case R.id.iv_delete_cell_search_history:
                        mHistoryAdapter.remove(position);
                        break;
                }
            }

            @Override
            public <C> void onClickItem(C data, int position) {
                StartActUtil.toSearchResultAct(
                        SearchActivity.this,
                        bean.getKey(),
                        bean.getKey());
            }
        });
        return cell;
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
