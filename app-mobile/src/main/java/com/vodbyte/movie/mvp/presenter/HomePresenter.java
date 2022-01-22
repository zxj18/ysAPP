package com.vodbyte.movie.mvp.presenter;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.mvp.contract.IHomeContract;
import com.vodbyte.movie.mvp.model.VodModel;
import com.vodbyte.movie.mvp.model.dto.ClassifyTitleDTO;
import com.vodbyte.movie.mvp.model.dto.HomePageDataDTO;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HomePresenter extends BasePresenter<IHomeContract.View> implements IHomeContract.Presenter {

    private VodModel mVodModel;

    public HomePresenter(){
        mVodModel = new VodModel();
    }

    /**
     * 获取分类数据
     */
    @Override
    public void getCidTitleList(boolean isHome) {
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
        },isHome);
    }

}
