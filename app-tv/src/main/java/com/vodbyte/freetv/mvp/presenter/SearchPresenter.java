package com.vodbyte.freetv.mvp.presenter;


import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.base.BasePresenter;
import com.vodbyte.freetv.mvp.contract.ISearchContract;
import com.vodbyte.freetv.mvp.model.SearchModel;
import com.vodbyte.freetv.mvp.model.dto.SearchDTO;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SearchPresenter extends BasePresenter<ISearchContract.View> implements ISearchContract.Presenter {

    private SearchModel mSearchModel;

    public SearchPresenter(){
        mSearchModel = new SearchModel();
    }

    @Override
    public void Search(String key,int page) {
        mSearchModel.searchKey(new Observer<SearchDTO>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SearchDTO dto) {
                if (isAttachView()){
                    if (dto.isSuccess()){
                        getMVPView().setSearchResult(dto.getData());
                    }else {
                        getMVPView().onError(Constant.TIP_ERROR_GET_DATA);
                    }
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
        },page,key);
    }

}
