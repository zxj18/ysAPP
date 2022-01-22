package com.vodbyte.freetv.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vodbyte.easy_rv_adapter.fragment.AbsFragment;

public abstract class BaseAbsFragment<P extends BasePresenter> extends AbsFragment {

    protected P mPresenter;

    protected abstract P getInstance();

    protected void showToast(String msg){
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = getInstance();
        if (mPresenter != null){
            mPresenter.attachView(this);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }

}
