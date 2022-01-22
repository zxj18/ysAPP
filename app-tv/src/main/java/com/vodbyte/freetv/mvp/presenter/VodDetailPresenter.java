package com.vodbyte.freetv.mvp.presenter;

import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.base.BasePresenter;
import com.vodbyte.freetv.bean.VodBean;
import com.vodbyte.freetv.mvp.contract.IVodDetailContract;
import com.vodbyte.freetv.mvp.model.VodDetailModel;
import com.vodbyte.freetv.mvp.model.db.CollectBean;
import com.vodbyte.freetv.mvp.model.dto.VodDetailDTO;

import org.litepal.LitePal;

import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class VodDetailPresenter extends BasePresenter<IVodDetailContract.View>
        implements IVodDetailContract.Presenter {

    private VodDetailModel mVodDetailModel;
    private VodBean mVodBean;
    private long mVodId;

    public VodDetailPresenter(){
        mVodDetailModel = new VodDetailModel();
    }

    @Override
    public void getVodDetailData(long vodId) {
        mVodId = vodId;
        mVodDetailModel.getVodDetailData(new Observer<VodDetailDTO>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(VodDetailDTO vodDetailDTO) {
                if (isAttachView()){
                    if (vodDetailDTO.isSuccess()){
                        getMVPView().setVodDetailData(vodDetailDTO);
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
        },vodId);
    }



    @Override
    public void collect(boolean isCollect) {
        if (isCollect){
            //添加收藏
            CollectBean bean = new CollectBean();
            bean.setVodPrimaryKey(mVodBean.getPrimaryKey());
            bean.setTime(new Date());
            bean.save();
        }else {
            LitePal.where("vodPrimaryKey = ?",String.valueOf(mVodBean.getPrimaryKey()))
                    .findFirst(CollectBean.class).delete();
        }
    }
}
