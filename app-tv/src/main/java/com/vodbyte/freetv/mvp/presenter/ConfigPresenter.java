package com.vodbyte.freetv.mvp.presenter;


import com.vodbyte.freetv.base.BasePresenter;
import com.vodbyte.freetv.mvp.contract.IConfigContract;
import com.vodbyte.freetv.mvp.model.ConfigModel;
import com.vodbyte.freetv.mvp.model.dto.ConfigDTO;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ConfigPresenter extends BasePresenter<IConfigContract.View> implements IConfigContract.Presenter {

    private ConfigModel mConfigModel;

    public ConfigPresenter(){
        mConfigModel = new ConfigModel();
    }

    @Override
    public void getConfig() {
        mConfigModel.getConfig(new Observer<ConfigDTO>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(@NotNull ConfigDTO configDTO) {
                if (isAttachView()){
                    getMVPView().setConfig(configDTO.getData());
                }
            }

            @Override
            public void onError(Throwable e) {
                if (isAttachView()){
                    getMVPView().onError(e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
