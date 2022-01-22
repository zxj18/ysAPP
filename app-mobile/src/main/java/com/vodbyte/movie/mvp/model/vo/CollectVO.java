package com.vodbyte.movie.mvp.model.vo;

import androidx.annotation.Keep;

import com.vodbyte.movie.base.BaseBookShelfVO;
import com.vodbyte.movie.bean.VodBean;

import java.util.Objects;

@Keep
public class CollectVO extends BaseBookShelfVO implements Cloneable {
    private VodBean vodBean;
    public CollectVO(VodBean vodBean) {
        this.vodBean = vodBean;
    }


    @Override
    public CollectVO clone(){
        CollectVO vo = null;
        try {
            vo = (CollectVO) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return vo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectVO collectVO = (CollectVO) o;
        return Objects.equals(vodBean.getVid(), collectVO.vodBean.getVid());
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
