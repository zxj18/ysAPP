package com.vodbyte.movie.mvp.presenter;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.mvp.contract.IClassifyContract;
import com.vodbyte.movie.mvp.model.VodModel;
import com.vodbyte.movie.mvp.model.dto.ClassifyListDTO;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ClassifyPresenter extends BasePresenter<IClassifyContract.View> implements IClassifyContract.Presenter {

    private VodModel mVodModel;

    public ClassifyPresenter(){
        mVodModel = new VodModel();
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
