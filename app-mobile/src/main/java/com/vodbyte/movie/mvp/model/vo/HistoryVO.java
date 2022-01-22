package com.vodbyte.movie.mvp.model.vo;

import androidx.annotation.Keep;

import com.vodbyte.movie.base.BaseBookShelfVO;
import com.vodbyte.movie.bean.VodBean;

import java.util.Objects;

@Keep
public class HistoryVO extends BaseBookShelfVO implements Cloneable {
    private VodBean vodBean;
    public HistoryVO(VodBean vodBean) {
        this.vodBean = vodBean;
    }


    @Override
    public HistoryVO clone(){
        HistoryVO vo = null;
        try {
            vo = (HistoryVO) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return vo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryVO historyVO = (HistoryVO) o;
        return Objects.equals(vodBean.getVid(), historyVO.vodBean.getVid());
    }


    @Override
    public int hashCode() {
        return Objects.hash(vodBean.getVid());
    }

    public VodBean getVodBean() {
        return vodBean;
    }

    public void setVodBean(VodBean vodBean) {
        this.vodBean = vodBean;
    }
}