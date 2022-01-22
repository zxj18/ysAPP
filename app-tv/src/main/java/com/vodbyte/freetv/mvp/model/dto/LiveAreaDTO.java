package com.vodbyte.freetv.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.freetv.bean.LiveAreaBean;

import java.io.Serializable;
import java.util.List;

@Keep
public class LiveAreaDTO implements Serializable {

    private Integer code;
    private String msg;
    private List<LiveAreaBean> data;

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

    public List<LiveAreaBean> getData() {
        return data;
    }

    public void setData(List<LiveAreaBean> data) {
        this.data = data;
    }

}
