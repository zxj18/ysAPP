package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.mvp.model.VodModel;

import java.util.List;

public interface IClassifyContract {
    interface View{
        void setClassifyList(List<VodBean> list);
        void onError(String msg);
    }

    interface Presenter{
        /**
         * 获取分类数据
         * @param page 分页
         * @param cid 分类id
         * @param className 分类名称
         * @param area 地区
         * @param lang 语言
         * @param year 年代
         */
        void getClassifyList(int page,int cid,String className,String area,String lang,int year);
    }
}
