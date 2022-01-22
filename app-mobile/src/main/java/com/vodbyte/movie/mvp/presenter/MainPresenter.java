package com.vodbyte.movie.mvp.presenter;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.mvp.contract.IMainContract;
import com.vodbyte.movie.mvp.model.VodModel;
import com.vodbyte.movie.mvp.model.dto.ClassifyTitleDTO;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainPresenter extends BasePresenter<IMainContract.View>
        implements IMainContract.Presenter {

    private VodModel mVodModel;

    public MainPresenter(){
        mVodModel = new VodModel();
    }

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
