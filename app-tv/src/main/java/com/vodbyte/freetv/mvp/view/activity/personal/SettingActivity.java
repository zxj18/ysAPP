package com.vodbyte.freetv.mvp.view.activity.personal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vodbyte.freetv.BuildConfig;
import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.common.FocusAction;
import com.vodbyte.freetv.common.Ui;
import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.setting_tv_video_weiduo)
    TextView setting_tv_video_weiduo;

    @BindView(R.id.setting_tv_video_cms)
    TextView setting_tv_video_cms;

    @BindView(R.id.setting_tv_video_ok)
    TextView setting_tv_video_ok;

    @BindView(R.id.setting_tv_video_zd)
    TextView setting_tv_video_zd;

    @BindView(R.id.setting_tv_player_native)
    TextView setting_tv_player_native;

    @BindView(R.id.setting_tv_player_tbs)
    TextView setting_tv_player_tbs;

    @BindView(R.id.setting_tv_player_ijkplayer)
    TextView setting_tv_player_ijkplayer;

    @BindView(R.id.setting_tv_player_exo)
    TextView setting_tv_player_exo;

    @BindView(R.id.setting_tv_version_update)
    TextView setting_tv_version_update;

    @BindView(R.id.setting_tv_clear_cache)
    TextView setting_tv_clear_cache;

    @BindView(R.id.setting_tv_new_version)
    TextView setting_tv_new_version;

    @BindView(R.id.setting_iv_new_version)
    ImageView setting_iv_new_version;

    private TextView currentVideoEngine;
    private TextView currentPlayerEngine;


    private boolean isVideoEngineChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initView();

        refreshCache();
    }

    private void initView() {
        // 版本号
        setting_tv_version_update.setText("当前版本 : " + BuildConfig.VERSION_NAME);

//        switch (Model.getData().getVideoEngine(this)) {
//            case VIDEO_1:
//                currentVideoEngine = setting_tv_video_weiduo;
//                setting_tv_video_weiduo.requestFocus();
//                break;
//            case VIDEO_2:
//                currentVideoEngine = setting_tv_video_cms;
//                setting_tv_video_cms.requestFocus();
//                break;
//            case VIDEO_3:
//                currentVideoEngine = setting_tv_video_ok;
//                setting_tv_video_ok.requestFocus();
//                break;
//            case VIDEO_4:
//                currentVideoEngine = setting_tv_video_zd;
//                setting_tv_video_zd.requestFocus();
//                break;
//        }

//        switch (Model.getData().getPlayerEngine(this)) {
//            case PLAY_1:
//                setting_tv_player_native.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
//                currentPlayerEngine = setting_tv_player_native;
//                break;
//            case PLAY_2:
//                setting_tv_player_tbs.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
//                currentPlayerEngine = setting_tv_player_tbs;
//                break;
//            case PLAY_3:
//                setting_tv_player_ijkplayer.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
//                currentPlayerEngine = setting_tv_player_ijkplayer;
//                break;
//            case PLAY_4:
//                setting_tv_player_exo.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
//                currentPlayerEngine = setting_tv_player_exo;
//        }

//        if (Model.getData().isAutoLogin(this)) {
//            setting_tv_auto_login_on.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
//            currentAutoLogin = setting_tv_auto_login_on;
//        } else {
//            setting_tv_auto_login_off.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
//            currentAutoLogin = setting_tv_auto_login_off;
//        }

        Ui.setViewFocusScaleAnimator(setting_tv_video_weiduo, new FocusAction() {
            @Override
            public void onFocus() {
                setting_tv_video_weiduo.setTextColor(getResources().getColor(R.color.colorPersonalFocus));
            }

            @Override
            public void onLoseFocus() {
                if (setting_tv_video_weiduo == currentVideoEngine) {
                    setting_tv_video_weiduo.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
                } else {
                    setting_tv_video_weiduo.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
                }
            }
        });
        Ui.setViewFocusScaleAnimator(setting_tv_video_cms, new FocusAction() {
            @Override
            public void onFocus() {
                setting_tv_video_cms.setTextColor(getResources().getColor(R.color.colorPersonalFocus));
            }

            @Override
            public void onLoseFocus() {
                if (setting_tv_video_cms == currentVideoEngine) {
                    setting_tv_video_cms.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
                } else {
                    setting_tv_video_cms.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
                }
            }
        });
        Ui.setViewFocusScaleAnimator(setting_tv_video_ok, new FocusAction() {
            @Override
            public void onFocus() {
                setting_tv_video_ok.setTextColor(getResources().getColor(R.color.colorPersonalFocus));
            }

            @Override
            public void onLoseFocus() {
                if (setting_tv_video_ok == currentVideoEngine) {
                    setting_tv_video_ok.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
                } else {
                    setting_tv_video_ok.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
                }
            }
        });
        Ui.setViewFocusScaleAnimator(setting_tv_video_zd, new FocusAction() {
            @Override
            public void onFocus() {
                setting_tv_video_zd.setTextColor(getResources().getColor(R.color.colorPersonalFocus));
            }

            @Override
            public void onLoseFocus() {
                if (setting_tv_video_zd == currentVideoEngine) {
                    setting_tv_video_zd.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
                } else {
                    setting_tv_video_zd.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
                }
            }
        });
        Ui.setViewFocusScaleAnimator(setting_tv_player_native, new FocusAction() {
            @Override
            public void onFocus() {
                setting_tv_player_native.setTextColor(getResources().getColor(R.color.colorPersonalFocus));
            }

            @Override
            public void onLoseFocus() {
                if (setting_tv_player_native == currentPlayerEngine) {
                    setting_tv_player_native.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
                } else {
                    setting_tv_player_native.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
                }
            }
        });
        Ui.setViewFocusScaleAnimator(setting_tv_player_tbs, new FocusAction() {
            @Override
            public void onFocus() {
                setting_tv_player_tbs.setTextColor(getResources().getColor(R.color.colorPersonalFocus));
            }

            @Override
            public void onLoseFocus() {
                if (setting_tv_player_tbs == currentPlayerEngine) {
                    setting_tv_player_tbs.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
                } else {
                    setting_tv_player_tbs.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
                }
            }
        });
        Ui.setViewFocusScaleAnimator(setting_tv_player_ijkplayer, new FocusAction() {
            @Override
            public void onFocus() {
                setting_tv_player_ijkplayer.setTextColor(getResources().getColor(R.color.colorPersonalFocus));
            }

            @Override
            public void onLoseFocus() {
                if (setting_tv_player_ijkplayer == currentPlayerEngine) {
                    setting_tv_player_ijkplayer.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
                } else {
                    setting_tv_player_ijkplayer.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
                }
            }
        });
        Ui.setViewFocusScaleAnimator(setting_tv_player_exo, new FocusAction() {
            @Override
            public void onFocus() {
                setting_tv_player_exo.setTextColor(getResources().getColor(R.color.colorPersonalFocus));
            }

            @Override
            public void onLoseFocus() {
                if (setting_tv_player_exo == currentPlayerEngine) {
                    setting_tv_player_exo.setTextColor(getResources().getColor(R.color.colorPersonalCurrent));
                } else {
                    setting_tv_player_exo.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
                }
            }
        });
        Ui.setViewFocusScaleAnimator(setting_tv_version_update, new FocusAction() {
            @Override
            public void onFocus() {
                setting_tv_version_update.setTextColor(getResources().getColor(R.color.colorPersonalFocus));
            }

            @Override
            public void onLoseFocus() {
                setting_tv_version_update.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
            }
        });
        Ui.setViewFocusScaleAnimator(setting_tv_clear_cache, new FocusAction() {
            @Override
            public void onFocus() {
                setting_tv_clear_cache.setTextColor(getResources().getColor(R.color.colorPersonalFocus));
            }

            @Override
            public void onLoseFocus() {
                setting_tv_clear_cache.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
            }
        });

        if (Constant.FEATURE_2) {
            setting_tv_player_native.setVisibility(View.VISIBLE);
        } else {
            setting_tv_player_native.setVisibility(View.GONE);
        }
        if (Constant.FEATURE_3) {
            setting_tv_player_ijkplayer.setVisibility(View.VISIBLE);
        } else {
            setting_tv_player_ijkplayer.setVisibility(View.GONE);
        }
        if (Constant.FEATURE_4) {
            setting_tv_player_exo.setVisibility(View.VISIBLE);
        } else {
            setting_tv_player_exo.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkUpdate();
    }

    @Override
    public void onBackPressed() {
        if (isVideoEngineChange) {
        }
        finish();
    }

    @OnClick(R.id.setting_tv_video_weiduo)
    public void onWeiduoResClick() {
       // Model.getData().setVideoEngine(this, VIDEO_1);
        currentVideoEngine = setting_tv_video_weiduo;
        defaultOtherVideoEngine(setting_tv_video_weiduo);
        isVideoEngineChange = true;
    }

    @OnClick(R.id.setting_tv_video_cms)
    public void onCMSResCLick() {
      //  Model.getData().setVideoEngine(this, VIDEO_2);
        currentVideoEngine = setting_tv_video_cms;
        defaultOtherVideoEngine(setting_tv_video_cms);
        isVideoEngineChange = true;
    }

    @OnClick(R.id.setting_tv_video_ok)
    public void onOKResClick() {
      //  Model.getData().setVideoEngine(this, VIDEO_3);
        currentVideoEngine = setting_tv_video_ok;
        defaultOtherVideoEngine(setting_tv_video_ok);
        isVideoEngineChange = true;
    }

    @OnClick(R.id.setting_tv_video_zd)
    public void onZDResClick() {
       // Model.getData().setVideoEngine(this, VIDEO_4);
        currentVideoEngine = setting_tv_video_zd;
        defaultOtherVideoEngine(setting_tv_video_zd);
        isVideoEngineChange = true;
    }

    @OnClick(R.id.setting_tv_player_native)
    public void onNativePlayerClick() {
       // Model.getData().setPlayerEngine(this, PLAY_1);
        currentPlayerEngine = setting_tv_player_native;
        defaultOtherPlayerEngine(setting_tv_player_native);
    }

    @OnClick(R.id.setting_tv_player_tbs)
    public void onTbsPlayerClick() {
      //  Model.getData().setPlayerEngine(this, PLAY_2);
        currentPlayerEngine = setting_tv_player_tbs;
        defaultOtherPlayerEngine(setting_tv_player_tbs);
    }

    @OnClick(R.id.setting_tv_player_ijkplayer)
    public void onIjkPlayerClick() {
      //  Model.getData().setPlayerEngine(this, PLAY_3);
        currentPlayerEngine = setting_tv_player_ijkplayer;
        defaultOtherPlayerEngine(setting_tv_player_ijkplayer);
    }

    @OnClick(R.id.setting_tv_player_exo)
    public void onEXOPlayerClick() {
      //  Model.getData().setPlayerEngine(this, PLAY_4);
        currentPlayerEngine = setting_tv_player_exo;
        defaultOtherPlayerEngine(setting_tv_player_exo);
    }

    @OnClick(R.id.setting_tv_version_update)
    public void onVersionUpdateClick() {
//        if (newVersion) {
//            Intent intent = new Intent(this, UpdateActivity.class);
//            startActivity(intent);
//
//            EventBus.getDefault().postSticky(new UpdateEvent("版本更新至 " + mBaseDataBean.getHBbb(), mBaseDataBean.getHBnr(), mBaseDataBean.getHBxz()));
//        } else {
//            Toast.makeText(this, "当前版本已经是最新", Toast.LENGTH_SHORT).show();
//        }
    }

    @OnClick(R.id.setting_tv_clear_cache)
    public void onClearCacheClick() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("缓存清理中");
        progressDialog.show();

        DisposableObserver<Boolean> observer = new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    progressDialog.dismiss();
                    Toast.makeText(SettingActivity.this, "清理完成", Toast.LENGTH_SHORT).show();
                    refreshCache();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable
                .create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                        deleteFilesByDirectory(getCacheDir());
                        Glide.get(SettingActivity.this).clearDiskCache();
                        emitter.onNext(true);
                    }
                })
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


    }

    public void refreshCache() {
        try {
            long cacheSize = getFolderSize(getCacheDir());

            String text = "";
            if (cacheSize < 1024) {
                text = cacheSize  + " B";
            } else if (cacheSize < 1024 * 1024) {
                text = String.format("%.2f", ((double)cacheSize / 1024)) + " KB";
            } else if (cacheSize < 1024 * 1024 * 1024) {
                text = String.format("%.2f", ((double)cacheSize / 1024 / 1024)) + " MB";
            } else {
                text = String.format("%.2f", ((double)cacheSize / 1024 / 1024 / 1024)) + " GB";
            }
            setting_tv_clear_cache.setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File value : fileList) {
                if (value.isDirectory()) {
                    size = size + getFolderSize(value);
                } else {
                    size = size + value.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    public void checkUpdate() {

    }

    private void defaultOtherVideoEngine(TextView textView) {
        if (setting_tv_video_weiduo != textView) {
            setting_tv_video_weiduo.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
        }
        if (setting_tv_video_cms != textView) {
            setting_tv_video_cms.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
        }
        if (setting_tv_video_ok != textView) {
            setting_tv_video_ok.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
        }
        if (setting_tv_video_zd != textView) {
            setting_tv_video_zd.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
        }
    }

    private void defaultOtherPlayerEngine(TextView textView) {
        if (setting_tv_player_native != textView) {
            setting_tv_player_native.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
        }
        if (setting_tv_player_tbs != textView) {
            setting_tv_player_tbs.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
        }
        if (setting_tv_player_ijkplayer != textView) {
            setting_tv_player_ijkplayer.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
        }
        if (setting_tv_player_exo != textView) {
            setting_tv_player_exo.setTextColor(getResources().getColor(R.color.colorPersonalNormal));
        }
    }
}
