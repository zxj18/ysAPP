package com.vodbyte.easy_rv_adapter.fragment;

import android.app.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vodbyte.easy_rv_adapter.R;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsFragment extends Fragment {

    public static final String TAG = "AbsFragment";
    protected RecyclerView mRecyclerView;
    protected RvSimpleAdapter mBaseAdapter;
    private FrameLayout mToolbarContainer;
    protected SmartRefreshLayout mRefreshLayout;
    protected Activity mActivity;
    protected List<BaseRvCell> mData;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_base_fragment_layout,null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mData = new ArrayList<>();
        mRefreshLayout = view.findViewById(R.id.base_refresh_layout);
        mToolbarContainer = view.findViewById(R.id.toolbar_container);
        mRecyclerView = view.findViewById(R.id.base_fragment_rv);
        mRecyclerView.setLayoutManager(initLayoutManger());
        mBaseAdapter = initAdapter();
        mRecyclerView.setAdapter(mBaseAdapter);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> onPullRefresh());
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> onLoadMore());

        View toolbarView = addTopLayout();
        if(toolbarView != null && mToolbarContainer != null){
            mToolbarContainer.addView(toolbarView);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRecyclerViewInitialized();
    }

    protected View customLoadMoreView(){
        //如果需要自定义LoadMore View,子类实现这个方法
        return null;
    }

    /**
     * 子类可以自己指定Adapter,如果不指定默认RVSimpleAdapter
     * @return
     */
    protected RvSimpleAdapter initAdapter(){
        return new RvSimpleAdapter();
    }

    /**
     * 子类自己指定RecyclerView的LayoutManager,如果不指定，默认为LinearLayoutManager,VERTICAL 方向
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected RecyclerView.LayoutManager initLayoutManger(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        return manager;
    }

    /**
     * 添加顶部自定义布局
     * @param
     */
    public View addTopLayout(){
        //如果需要在RecyclerView顶部添加一些布局，则在这里返回
        return null;
    }

    /**
     *RecyclerView 初始化完毕，可以在这个方法里绑定数据
     */
    public abstract void onRecyclerViewInitialized();

    /**
     * 下拉刷新
     */
    public abstract void onPullRefresh();

    /**
     * 上拉加载更多
     */
    public abstract void onLoadMore();
}
