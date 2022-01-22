package com.vodbyte.movie.mvp.presenter;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.mvp.contract.ITopContract;
import com.vodbyte.movie.mvp.model.VodModel;
import com.vodbyte.movie.mvp.model.dto.TopVodDTO;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class TopPresenter extends BasePresenter<ITopContract.View>
        implements ITopContract.Presenter {

    private VodModel mVodModel;

    public TopPresenter(){
        mVodModel = new VodModel();
    }

    @Override
    public void getTopData(int page) {
        mVodModel.getTopVodBeanListData(new Observer<TopVodDTO>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(TopVodDTO topVodDTO) {
                if (isAttachView()){
                    getMVPView().setTopData(topVodDTO.getData());
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

        },page);
    }
}
