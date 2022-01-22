package com.vodbyte.movie.mvp.presenter;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.contract.ISearchResultContract;
import com.vodbyte.movie.mvp.model.SearchResultModel;
import com.vodbyte.movie.mvp.model.dto.SearchDTO;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SearchResultPresenter extends BasePresenter<ISearchResultContract.View> implements ISearchResultContract.Presenter {

    private SearchResultModel mSearchResultModel;

    public SearchResultPresenter(){
        mSearchResultModel = new SearchResultModel();
    }

    @Override
    public void SearchWord(String word,int page) {
        mSearchResultModel.searchWord(new Observer<SearchDTO>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SearchDTO dto) {
                if (isAttachView()){
                    if (dto.isSuccess()){
                        if (dto.getData().size() > 0) {
                            getMVPView().setSearchResult(dto.getData());
                        }else {
                            if (isAttachView()){
                                getMVPView().noMore();
                            }
                        }
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
        },word,page);
    }

}
