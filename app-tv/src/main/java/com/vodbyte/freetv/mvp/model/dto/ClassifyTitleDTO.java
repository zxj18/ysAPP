package com.vodbyte.freetv.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.freetv.bean.ClassifyTitleBean;

import java.io.Serializable;
import java.util.List;

/**
 * 分类标题
 */
@Keep
public class ClassifyTitleDTO implements Serializable {

    private Integer code;
    private String msg;
    private List<ClassifyTitleBean> data;

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

    public List<ClassifyTitleBean> getData() {
        return data;
    }

    public void setData(List<ClassifyTitleBean> data) {
        this.data = data;
    }

}
