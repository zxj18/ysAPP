package com.vodbyte.movie.mvp.presenter;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.mvp.contract.IConfigContract;
import com.vodbyte.movie.mvp.contract.IFeedBackContract;
import com.vodbyte.movie.mvp.model.ConfigModel;
import com.vodbyte.movie.mvp.model.FeedBackModel;
import com.vodbyte.movie.mvp.model.dto.ConfigDTO;
import com.vodbyte.movie.mvp.model.dto.StringDataDTO;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FeedBackPresenter extends BasePresenter<IFeedBackContract.View> implements IFeedBackContract.Presenter {

    private FeedBackModel mFeedBackModel;

    public FeedBackPresenter(){
        mFeedBackModel = new FeedBackModel();
    }

    @Override
    public void sendFeedBack(String vid,String content,String uuid) {
        mFeedBackModel.sendFeedBack(new Observer<StringDataDTO>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(@NotNull StringDataDTO stringDataDTO) {
                if (isAttachView()){
                    getMVPView().onFeedBack(stringDataDTO);
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
        },vid,content,uuid);
    }
}
