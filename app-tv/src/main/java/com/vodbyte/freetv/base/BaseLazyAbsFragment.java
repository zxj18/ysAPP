package com.vodbyte.freetv.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.vodbyte.easy_rv_adapter.fragment.AbsTvFragment;


public abstract class BaseLazyAbsFragment<P extends BasePresenter> extends AbsTvFragment {

    protected BaseActivity mBaseActivity;  //贴附的activity,Fragment中可能用到
    protected View mRootView;           //根view
    protected P mPresenter;
    protected boolean isVisible = false;
    protected boolean isPrepared = false;
    protected boolean isLoaded = false;
    private int upFocus = -1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = getInstance();
        if (mPresenter != null){
            mPresenter.attachView(this);
        }
        mRootView = super.onCreateView(inflater, container, savedInstanceState);
        isPrepared = true;
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyLoad();
    }

    protected abstract P getInstance();

    protected String getString(EditText editText){
        return editText.getText().toString();
    }

    protected void showToast(String msg) {
        Toast.makeText(mBaseActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }

    protected void startActivity(Class clazz) {
        Intent intent = new Intent(mBaseActivity, clazz);
        startActivity(intent);
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    protected void onVisible(){
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible(){
    }

}
