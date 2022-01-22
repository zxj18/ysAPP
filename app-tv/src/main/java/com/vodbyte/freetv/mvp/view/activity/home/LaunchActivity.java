package com.vodbyte.freetv.mvp.view.activity.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.base.BaseActivity;
import com.vodbyte.freetv.bean.ConfigBean;
import com.vodbyte.freetv.bean.UpdateBean;
import com.vodbyte.freetv.mvp.contract.IConfigContract;
import com.vodbyte.freetv.mvp.presenter.ConfigPresenter;
import com.vodbyte.freetv.utils.AppTools;
import com.vodbyte.freetv.utils.GsonUtil;
import com.vodbyte.freetv.utils.PrefsUtils;
import com.vodbyte.freetv.utils.StartActUtil;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends BaseActivity<ConfigPresenter> implements IConfigContract.View {


    private ConfigBean mConfig;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_launch;
    }

    @Override
    protected ConfigPresenter getInstance() {
        return new ConfigPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPresenter.getConfig();
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initListener() {

    }

    @Override
    public void setConfig(ConfigBean config) {
        mConfig = config;
        checkUpdate(); // 检查更新
        PrefsUtils.with(this).write("config",config.toJsonString());
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
        checkUpdate(); // 检查更新
        if (msg.contains("reset")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("网络异常是否退出重试？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.create().show();
        }
    }

    /**
     * 检查更新
     */
    protected void checkUpdate() {
        DownloadBuilder builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl(String.format("%s?ver_name=%s&ver_code=%s", Constant.BASE_API_UPDATE_URL, AppTools.getVerName(),AppTools.getVerCode()))

                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(DownloadBuilder downloadBuilder, String result) {
                        try {
                            UpdateBean update = GsonUtil.gsonToBean(result, UpdateBean.class);
                            if (update.getCode() == 200) {
                                UIData uiData = UIData.create();
                                uiData.setTitle(update.getData().getTitle());
                                uiData.setDownloadUrl(update.getData().getUrl());
                                uiData.setContent(Html.fromHtml(update.getData().getContent()).toString());
                                return uiData;
                            }else {
                                if (mConfig != null) {
                                    StartActUtil.toMainAct(LaunchActivity.this);
                                }
                            }
                        }catch (Exception e) {
                           // Log.d("LaunchActivity ",e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
                        showToast(message);
                    }
                });

        builder.setOnCancelListener(() -> finish());
        builder.executeMission(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AllenVersionChecker.getInstance().cancelAllMission();
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
