package com.vodbyte.movie.mvp.view.activity;

import android.Manifest;
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

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.fiio.sdk.callBack.SplashAdCallBack;
import com.fiio.sdk.view.SplashAd;
import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseActivity;
import com.vodbyte.movie.bean.ConfigBean;
import com.vodbyte.movie.bean.UpdateBean;
import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.contract.IConfigContract;
import com.vodbyte.movie.mvp.presenter.ConfigPresenter;
import com.vodbyte.movie.utils.DeviceIDUtils;
import com.vodbyte.movie.utils.GsonUtil;
import com.vodbyte.movie.utils.PrefsUtils;
import com.vodbyte.movie.utils.StartActUtil;
import com.vodbyte.movie.utils.Tools;
import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends BaseActivity<ConfigPresenter> implements IConfigContract.View {

    private static final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION
            ,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE
            ,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> permissionList = new ArrayList<String>();

    FrameLayout launchAdLayout;
    boolean forwardMain = false;
    boolean adForward = false;
    SplashAd splashAd;
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

    /**
     * 加载启动屏广告
     */
    protected void loadLaunchAd() {
        launchAdLayout = (FrameLayout)findViewById(R.id.launch_ad_layout);
        splashAd = new SplashAd(this,mConfig.getAd().getAd_splash_id(),launchAdLayout,new SplashAdCallBack() {
            @Override
            public void onAdShow() {
                Log.i(TAG,"开屏广告展示了");
                findViewById(R.id.iv_logo_act_launch).setVisibility(View.GONE);
            }

            @Override
            public void onAdSkipped() {
                Log.i(TAG,"开屏广告跳过了");
            }

            @Override
            public void onAdComplete() {
                Log.i(TAG,"开屏广告播放完成");
            }

            @Override
            public void onAdClick() {
                Log.i(TAG,"开屏广告被点击了");
                adForward = true;
            }

            @Override
            public void onAdFail(String error) {
                Log.i(TAG,"开屏广告加载失败:"+error);
                mPresenter.addAdLog(DeviceIDUtils.getDeviceId(getBaseContext()),"launch",false);
                StartActUtil.toMainAct(LaunchActivity.this);
            }

            @Override
            public void onAdClose() {
                Log.i(TAG,"开屏广告关闭");
                adForward = true;
                mPresenter.addAdLog(DeviceIDUtils.getDeviceId(getBaseContext()),"launch",true);
                forwardActivity();
            }

            @Override
            public void onAdLoaded() {
                Log.i(TAG,"开屏广告缓存成功");
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int checkSelfPermission = checkSelfPermission(permission);
                if (PackageManager.PERMISSION_GRANTED == checkSelfPermission) {
                    continue;
                }
                permissionList.add(permission);
            }
            if (!permissionList.isEmpty()) {
                requestPermissions(permissionList.toArray(new String[permissionList.size()]), 1);
            } else {
                if (mConfig.getAd().isOpen_ad())
                {
                    splashAd.loadAd();
                }
            }
        }else{
            if (mConfig.getAd().isOpen_ad())
            {
                splashAd.loadAd();
            }
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void setConfig(ConfigBean config) {
        mConfig = config;
        Constant.PIC_DOMAIN_URL = config.getPic_domain();
        checkUpdate(); // 检查更新
        PrefsUtils.with(this).write("config",config.toJsonString());
    }

    @Override
    public void setAdLog(String data) {

    }

    @Override
    public void onError(String msg) {
        checkUpdate(); // 检查更新

        Log.i("11111111",msg);

        showToast(msg);
    }

    /**
     * 检查更新
     */
    protected void checkUpdate() {
        DownloadBuilder builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl(String.format("%s?ver_name=%s&ver_code=%s",Constant.BASE_API_UPDATE_URL, Tools.getVerName(this),Tools.getVerCode(this)))

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
                                    if (mConfig.getAd().isOpen_ad() && !mConfig.getAd().getAd_splash_id().isEmpty()) {
                                        loadLaunchAd();
                                    }else {
                                        StartActUtil.toMainAct(LaunchActivity.this);
                                    }
                                }
                            }
                        }catch (Exception e) {
                         //   Log.d("LaunchActivity ",e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
                        Toast.makeText(LaunchActivity.this, "request failed", Toast.LENGTH_SHORT).show();
                    }
                });

        builder.setOnCancelListener(() -> finish());
        builder.executeMission(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (mConfig.getAd().isOpen_ad())
            {
                splashAd.loadAd();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        forwardMain = true;
        MobclickAgent.onResume(this);
        forwardActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        forwardMain = false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AllenVersionChecker.getInstance().cancelAllMission();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //防止用户返回键退出APP影响曝光
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void forwardActivity(){
        if(forwardMain && adForward){
            forwardMain = false;
            StartActUtil.toMainAct(LaunchActivity.this);
        }
    }
}
