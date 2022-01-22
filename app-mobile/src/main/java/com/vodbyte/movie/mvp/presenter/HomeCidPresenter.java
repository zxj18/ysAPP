package com.vodbyte.movie.mvp.presenter;

import android.util.Log;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.mvp.contract.IHomeCidContract;
import com.vodbyte.movie.mvp.model.VodModel;
import com.vodbyte.movie.mvp.model.dto.HomePageDataDTO;
import com.vodbyte.movie.mvp.model.dto.HomeVodBeanListDataDTO;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HomeCidPresenter extends BasePresenter<IHomeCidContract.View> implements IHomeCidContract.Presenter {

    private VodModel mVodModel;

    public HomeCidPresenter(){
        mVodModel = new VodModel();
    }

    /**
     * 获取首页数据
     * @param cid
     */
    @Override
    public void getHomeBlockData(int cid) {
        mVodModel.getHomePageData(new Observer<HomePageDataDTO>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull HomePageDataDTO homePageDataDTO) {
                if (isAttachView()){
                    getMVPView().showHomeBlockData(homePageDataDTO.getData().getBanner_list(), homePageDataDTO.getData().getItem_list());
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
        },cid,0);
    }


    @Override
    public void getHomeVodBeanListData(int cid, int page) {
        mVodModel.getHomeVodBeanListData(new Observer<HomeVodBeanListDataDTO>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull HomeVodBeanListDataDTO dataDTO) {
                if (isAttachView()){
                    getMVPView().showHomeVodBeanListData(dataDTO.getData().getBanner_list(), dataDTO.getData().getItem_list());
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
        },cid,page);
    }
}
