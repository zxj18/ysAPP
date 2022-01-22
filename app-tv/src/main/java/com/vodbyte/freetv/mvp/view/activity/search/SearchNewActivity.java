package com.vodbyte.freetv.mvp.view.activity.search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.base.BaseActivity;
import com.vodbyte.freetv.bean.ConfigBean;
import com.vodbyte.freetv.bean.VodBean;
import com.vodbyte.freetv.common.FocusAction;
import com.vodbyte.freetv.common.Ui;
import com.vodbyte.freetv.mvp.contract.ISearchContract;
import com.vodbyte.freetv.mvp.presenter.SearchPresenter;
import com.vodbyte.freetv.utils.StartActUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索页面
 */
public class SearchNewActivity extends BaseActivity<SearchPresenter> implements ISearchContract.View,SearchNewAdapter.OnSearchResultItemClickListener{

    @BindView(R.id.search_root_delete)
    View search_root_delete;

    @BindView(R.id.search_iv_delete)
    ImageView search_iv_delete;

    @BindView(R.id.search_tv_delete)
    TextView search_tv_delete;

    @BindView(R.id.search_root_search)
    View search_root_search;

    @BindView(R.id.search_iv_search)
    ImageView search_iv_search;

    @BindView(R.id.search_tv_search)
    TextView search_tv_search;

    @BindView(R.id.search_line)
    View search_line;

    @BindView(R.id.search_recycler_view_letter)
    RecyclerView search_recycler_view_letter;

    @BindView(R.id.search_et_keywords)
    EditText search_et_keywords;

    @BindView(R.id.search_recycler_view_result)
    RecyclerView search_recycler_view_result;

    @BindView(R.id.search_root_result)
    View search_root_result;

    @BindView(R.id.search_tv_result_title)
    TextView search_tv_result_title;

    @BindView(R.id.search_root_loading)
    View search_root_loading;

    @BindView(R.id.qrcode_view)
    ImageView mQrcodeView;

    private static final String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V"
            , "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    private List<VodBean> mResultList;
    private SearchNewAdapter mSearchNewAdapter;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_search_new;
    }

    @Override
    protected SearchPresenter getInstance() {
        return new SearchPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        search_recycler_view_letter.setLayoutManager(new GridLayoutManager(this, 6));
        search_recycler_view_letter.setAdapter(new LetterAdapter(this, letters, new OnLetterClickListener() {
            @Override
            public void onClick(int position, String letter) {
                search_et_keywords.setText(search_et_keywords.getText().toString() + letter);
                search_root_loading.setVisibility(View.VISIBLE);
                search_root_result.setVisibility(View.GONE);
                mPresenter.Search(search_et_keywords.getText().toString(),1);
            }
        }));

        search_recycler_view_result.setLayoutManager(new GridLayoutManager(this, 4));
        // 优化
        // search_recycler_view_result.setHasFixedSize(true);
        // 卡片最大缓存数量，该数量以内的卡片能保证动画效果不卡顿
        // search_recycler_view_result.setItemViewCacheSize(500);

        search_line.requestFocus();

        // 加载网络图片
        if (!ConfigBean.getConfig(this).getSearch_qrcode_url().isEmpty()) {
            Glide.with(this).load(ConfigBean.getConfig(this).getSearch_qrcode_url()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(mQrcodeView);
        }
        //
        // mQrcodeView
    }

    @Override
    protected void initListener() {

        Ui.setMenuFocusAnimator(this, search_root_delete, new FocusAction() {
            @Override
            public void onFocus() {
                search_iv_delete.setImageResource(R.drawable.ic_delete_focus);
                search_tv_delete.setTextColor(getResources().getColor(R.color.colorTextFocus));
            }

            @Override
            public void onLoseFocus() {
                search_iv_delete.setImageResource(R.drawable.ic_delete_normal);
                search_tv_delete.setTextColor(getResources().getColor(R.color.colorTextNormal));
            }
        });

        Ui.setMenuFocusAnimator(this, search_root_search, new FocusAction() {
            @Override
            public void onFocus() {
                search_iv_search.setImageResource(R.drawable.ic_search_focus);
                search_tv_search.setTextColor(getResources().getColor(R.color.colorTextFocus));
            }

            @Override
            public void onLoseFocus() {
                search_iv_search.setImageResource(R.drawable.ic_search_normal);
                search_tv_search.setTextColor(getResources().getColor(R.color.colorTextNormal));
            }
        });

    }

    @OnClick(R.id.search_root_delete)
    public void onDeleteClick() {
        search_et_keywords.setText("");
        search_tv_result_title.setText("字母或数字进行搜索");
        mSearchNewAdapter.cleanData();

        search_root_loading.setVisibility(View.GONE);
        search_root_result.setVisibility(View.VISIBLE);
        mQrcodeView.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.search_root_search)
    public void onSearchClick() {
        String keyWords = search_et_keywords.getText().toString().trim();
        mPresenter.Search(keyWords,1);

        mQrcodeView.setVisibility(View.GONE);
        search_root_loading.setVisibility(View.VISIBLE);
        search_root_result.setVisibility(View.GONE);
        search_tv_result_title.setText(keyWords + " 的搜索结果");

    }

    @Override
    public void onSearchItemClick(int position) {
        StartActUtil.toPlayDetail(this,mResultList.get(position));
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void setSearchResult(List<VodBean> list) {
        mResultList = list;
        if (list.size() == 0) {
            mQrcodeView.setVisibility(View.VISIBLE);
            search_tv_result_title.setText("“" + search_et_keywords.getText().toString().trim() + "”搜索不到相关结果");
        }else {
            mQrcodeView.setVisibility(View.GONE);
        }

        mSearchNewAdapter = new SearchNewAdapter(this, mResultList, this);
        search_recycler_view_result.setAdapter(mSearchNewAdapter);

        search_root_loading.setVisibility(View.GONE);
        search_root_result.setVisibility(View.VISIBLE);

    }

    static class LetterAdapter extends RecyclerView.Adapter<LetterViewHolder> {

        private Activity activity;

        private String[] letters;

        private OnLetterClickListener listener;

        public LetterAdapter(Activity activity, String[] letters, OnLetterClickListener listener) {
            this.activity = activity;
            this.letters = letters;
            this.listener = listener;
        }

        @NonNull
        @Override
        public LetterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_search_letter, parent, false);
            return new LetterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LetterViewHolder holder, int position) {
            holder.search_item_tv_letter.setText(letters[position]);
            holder.search_item_tv_letter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(holder.getAdapterPosition(), letters[holder.getAdapterPosition()]);
                }
            });
        }

        @Override
        public int getItemCount() {
            return letters.length;
        }
    }

    interface OnLetterClickListener {
        void onClick(int position, String letter);
    }

    static class LetterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.search_item_tv_letter)
        TextView search_item_tv_letter;

        public LetterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}