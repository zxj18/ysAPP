package com.vodbyte.movie.mvp.presenter;

import android.util.Log;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.mvp.contract.IConfigContract;
import com.vodbyte.movie.mvp.contract.IHomeContract;
import com.vodbyte.movie.mvp.model.ConfigModel;
import com.vodbyte.movie.mvp.model.VodModel;
import com.vodbyte.movie.mvp.model.dto.ClassifyTitleDTO;
import com.vodbyte.movie.mvp.model.dto.ConfigDTO;
import com.vodbyte.movie.mvp.model.dto.StringDataDTO;

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

    @Override
    public void addAdLog(String uuid,String adType,boolean status) {
        mConfigModel.addAdLog(new Observer<StringDataDTO>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(@NotNull StringDataDTO dataDTO) {
                if (isAttachView()){
                    getMVPView().setAdLog(dataDTO.getData());
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
        },uuid,adType,status);
    }
}
