package com.vodbyte.movie.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.vodbyte.movie.config.Constant;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
    protected BaseActivity mBaseActivity;  //贴附的activity,Fragment中可能用到
    protected View mRootView;           //根view
    Unbinder mUnbinder;
    protected P mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (BaseActivity) getActivity();
    }

    protected abstract P getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutResourceId(), container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);
        mPresenter = getInstance();
        if (mPresenter != null){
            mPresenter.attachView(this);
        }
        initData(getArguments());
        initView();
        setListener();
        return mRootView;
    }

    protected abstract int setLayoutResourceId();

    /**
     * 初始化数据
     *
     * @param bundle 接收到的从其他地方传递过来的数据
     */
    protected abstract void initData(Bundle bundle);

    //初始化View
    protected abstract void initView();

    //设置监听事件
    protected abstract void setListener();

    protected String getString(EditText editText){
        return editText.getText().toString();
    }

    protected void showToast(String msg) {
        String url = Constant.BASE_API_URL.replace("https://","").replace("http://","");
        msg = msg.replace(url,"");
        Toast.makeText(mBaseActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }

    protected void startActivity(Class clazz) {
        Intent intent = new Intent(mBaseActivity, clazz);
        startActivity(intent);
    }
}
