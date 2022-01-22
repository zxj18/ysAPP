package com.vodbyte.movie.mvp.presenter;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.bean.ChapterBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.contract.IVodDetailContract;
import com.vodbyte.movie.mvp.contract.IVodTabDetailContract;
import com.vodbyte.movie.mvp.model.VodDetailModel;
import com.vodbyte.movie.mvp.model.db.CollectBean;
import com.vodbyte.movie.mvp.model.dto.VodDetailDTO;
import com.vodbyte.movie.mvp.model.dto.VodListDTO;

import org.litepal.LitePal;

import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class VodTabDetailPresenter extends BasePresenter<IVodTabDetailContract.View>
        implements IVodTabDetailContract.Presenter {

    private VodDetailModel mVodDetailModel;

    public VodTabDetailPresenter(){
        mVodDetailModel = new VodDetailModel();
    }

    @Override
    public void getVodTabData(long vodId,int cid) {
        mVodDetailModel.getVodRelevanceList(new Observer<VodListDTO>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(VodListDTO vodListDTO) {
                if (isAttachView()){
                    if (vodListDTO.isSuccess()){
                        getMVPView().setVodTabData(vodListDTO);
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
        },vodId,cid);
    }

}
