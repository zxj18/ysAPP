package com.vodbyte.movie.base;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vodbyte.movie.bean.ConfigBean;
import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.utils.ActivityCollectorUtil;

import butterknife.ButterKnife;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    protected String TAG;
    protected T mPresenter;
    protected ConfigBean mConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusTextColor(true);
        TAG = this.getClass().getSimpleName();
        ActivityCollectorUtil.addActivity(this);
        setContentView(setLayoutResID());
        ButterKnife.bind(this);
        mConfig = ConfigBean.getConfig(this);

        mPresenter = getInstance();
        if (mPresenter !=null){
            mPresenter.attachView(this);
        }
        initData(savedInstanceState);
        initView();
        initListener();
    }

    /**
     * 设置状态栏字体颜色
     * @param isDark
     */
    protected void setStatusTextColor(boolean isDark){
        if (isDark){
            //黑色字体
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }else {
            //白色字体
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        ActivityCollectorUtil.removeActivity(this);
        super.onDestroy();
    }

    protected abstract int setLayoutResID();

    protected abstract T getInstance();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initView();

    protected abstract void initListener();

    public void showToast(String text) {
        String url = Constant.BASE_API_URL.replace("https://","").replace("http://","");
        text = text.replace(url,"");
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected static String getString(EditText et) {
        return et.getText().toString();
    }



}
