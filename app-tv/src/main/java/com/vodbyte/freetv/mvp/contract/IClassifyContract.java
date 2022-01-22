package com.vodbyte.freetv.mvp.contract;

import com.vodbyte.freetv.bean.ClassifyTitleBean;
import com.vodbyte.freetv.bean.VodBean;

import java.util.List;

public interface IClassifyContract {
    interface View{
        void setClassifyList(List<VodBean> list);
        void setCidTitle(List<ClassifyTitleBean> list);
        void onError(String msg);
    }

    interface Presenter{
        void getClassifyList(int page,int cid);
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
        void getCidTitleList();
    }
}
