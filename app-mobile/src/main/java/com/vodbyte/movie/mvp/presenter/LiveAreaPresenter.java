package com.vodbyte.movie.mvp.presenter;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.mvp.contract.ILiveAreaContract;
import com.vodbyte.movie.mvp.model.LiveAreaModel;
import com.vodbyte.movie.mvp.model.dto.LiveAreaDTO;
import org.jetbrains.annotations.NotNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 电视直播
 */
public class LiveAreaPresenter extends BasePresenter<ILiveAreaContract.view>
        implements ILiveAreaContract.presenter {

    private LiveAreaModel mModel;

    public LiveAreaPresenter(){
        mModel = new LiveAreaModel();
    }

    @Override
    public void getLiveAreaList() {
        mModel.getLiveAreaList(new Observer<LiveAreaDTO>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
            }

            @Override
            public void onNext(@NotNull LiveAreaDTO liveAreaDTO) {
                getMVPView().setLiveAreaList(liveAreaDTO.getData());
            }

            @Override
            public void onError(Throwable e) {
                if(isAttachView()){
                    getMVPView().showError(e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
