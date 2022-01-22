package com.vodbyte.movie.mvp.presenter;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.mvp.contract.IGirlsContract;
import com.vodbyte.movie.mvp.contract.ILiveAreaContract;
import com.vodbyte.movie.mvp.model.GirlsModel;
import com.vodbyte.movie.mvp.model.LiveAreaModel;
import com.vodbyte.movie.mvp.model.dto.GirlsDTO;
import com.vodbyte.movie.mvp.model.dto.LiveAreaDTO;
import org.jetbrains.annotations.NotNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 电视直播
 */
public class GirlsPresenter extends BasePresenter<IGirlsContract.view>
        implements IGirlsContract.presenter {

    private GirlsModel mModel;

    public GirlsPresenter(){
        mModel = new GirlsModel();
    }

    @Override
    public void getGirlsList(int page) {
        mModel.getGirlsList(new Observer<GirlsDTO>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
            }

            @Override
            public void onNext(@NotNull GirlsDTO girlsDTO) {
                getMVPView().setGirlsList(girlsDTO.getData());
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
        },page);
    }
}
