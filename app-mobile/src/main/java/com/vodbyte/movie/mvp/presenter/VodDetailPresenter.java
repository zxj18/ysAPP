package com.vodbyte.movie.mvp.presenter;

import android.util.Log;

import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.bean.ChapterBean;
import com.vodbyte.movie.mvp.model.db.CollectBean;
import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.contract.IVodDetailContract;
import com.vodbyte.movie.mvp.model.VodDetailModel;
import com.vodbyte.movie.mvp.model.dto.StringDataDTO;
import com.vodbyte.movie.mvp.model.dto.VodDetailDTO;
import com.vodbyte.movie.mvp.model.service.ApiService;
import com.vodbyte.movie.utils.RetrofitUtil;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class VodDetailPresenter extends BasePresenter<IVodDetailContract.View>
        implements IVodDetailContract.Presenter {

    private VodDetailModel mVodDetailModel;
    private VodBean mVodBean;
    private long mVodId;

    public VodDetailPresenter(){
        mVodDetailModel = new VodDetailModel();
    }


    @Override
    public void addAdLog(String uuid, String adType, boolean status) {
        mVodDetailModel.addAdLog(new Observer<StringDataDTO>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(@NotNull StringDataDTO dataDTO) {
                if (isAttachView()){
                    getMVPView().setAdLog(dataDTO.getData());
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
        },uuid,adType,status);
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

                        try {
                            VodBean vodBean = LitePal
                                    .where("vid = ?",vodDetailDTO.getData().getVid()+"")
                                    .findFirst(VodBean.class);
                            if (vodBean == null){
                                vodBean = vodDetailDTO.getData();
                                vodBean.save();
                            }else {
                                VodBean vodBean1 = new VodBean();
                                vodBean1 = vodDetailDTO.getData();
                                vodBean1.saveOrUpdate("vid = ?",vodDetailDTO.getData().getVid() + "");
                            }
                        }catch (Exception e) {
                            Log.d("1111",e.getMessage());
                        }

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

    public void addHistory(VodBean vodBean) {
        mVodDetailModel.addHistory(vodBean);
    }
}
