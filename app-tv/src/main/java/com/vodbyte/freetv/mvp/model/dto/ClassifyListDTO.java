package com.vodbyte.freetv.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.freetv.bean.VodBean;

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
