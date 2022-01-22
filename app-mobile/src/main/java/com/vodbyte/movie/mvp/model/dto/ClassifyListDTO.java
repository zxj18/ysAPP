package com.vodbyte.movie.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.movie.bean.ClassifyTitleBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.mvp.model.VodModel;

import java.io.Serializable;
import java.util.List;

/**
 * 分类数据
 */
@Keep
public class ClassifyListDTO implements Serializable {

    private Integer code;
    private String msg;
    private List<VodBean> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<VodBean> getData() {
        return data;
    }

    public void setData(List<VodBean> data) {
        this.data = data;
    }

}
