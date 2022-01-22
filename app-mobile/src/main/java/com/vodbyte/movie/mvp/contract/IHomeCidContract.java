package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.bean.BannerBean;
import com.vodbyte.movie.bean.ClassifyTitleBean;
import com.vodbyte.movie.bean.ItemBean;
import com.vodbyte.movie.bean.VodBean;

import java.util.List;

public interface IHomeCidContract {

    interface Presenter{
        void getHomeBlockData(int cid);
        void getHomeVodBeanListData(int cid,int page);
    }

    interface View{
        void showHomeVodBeanListData(List<BannerBean> bannerList, List<VodBean> vodBeanList);
        void showHomeBlockData(List<BannerBean> bannerList, List<ItemBean> itemBeanList);
        void onError(String msg);
    }
}
