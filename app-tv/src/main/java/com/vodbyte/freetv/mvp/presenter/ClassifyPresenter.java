package com.vodbyte.freetv.mvp.presenter;


import android.util.Log;

import com.vodbyte.freetv.base.BasePresenter;
import com.vodbyte.freetv.mvp.contract.IClassifyContract;
import com.vodbyte.freetv.mvp.model.VodModel;
import com.vodbyte.freetv.mvp.model.dto.ClassifyListDTO;
import com.vodbyte.freetv.mvp.model.dto.ClassifyTitleDTO;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ClassifyPresenter extends BasePresenter<IClassifyContract.View> implements IClassifyContract.Presenter {

    private VodModel mVodModel;

    public ClassifyPresenter(){
        mVodModel = new VodModel();
    }

    /**
     * 获取分类id
     */
    @Override
    public void getCidTitleList() {
        mVodModel.getKindTitleList(new Observer<ClassifyTitleDTO>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(@NotNull ClassifyTitleDTO classifyTitleDTO) {
                if (isAttachView()){
                    getMVPView().setCidTitle(classifyTitleDTO.getData());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getClassifyList(int page,int cid) {
        mVodModel.getClassifyBeanList2(new Observer<ClassifyListDTO>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull ClassifyListDTO classifyListDTO) {
                if (isAttachView()) {
                    getMVPView().setClassifyList(classifyListDTO.getData());
                }
            }

            @Override
            public void onError(@NotNull Throwable e) {
                if (isAttachView()) {
                    getMVPView().onError(e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        }, page, cid);
    }


    /**
     * 获取分类数据
     * @param page 分页
     * @param cid 分类id
     * @param className 分类名称
     * @param area 地区
     * @param lang 语言
     * @param year 年代
     */
    @Override
    public void getClassifyList(int page,int cid, String className, String area, String lang, int year) {
        mVodModel.getClassifyBeanList(new Observer<ClassifyListDTO>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull ClassifyListDTO classifyListDTO) {
                if (isAttachView()){
                    getMVPView().setClassifyList(classifyListDTO.getData());
                }
            }

            @Override
            public void onError(@NotNull Throwable e) {
                if (isAttachView()){
                    getMVPView().onError(e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        },page,cid,className,area,lang,year);
    }
}
