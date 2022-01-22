package com.vodbyte.freetv.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.freetv.bean.HomePageDataBean;

@Keep
public class HomePageDataDTO {

    private Integer code;
    private String msg;
    private HomePageDataBean data;

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

    public HomePageDataBean getData() {
        return data;
    }

    public void setData(HomePageDataBean data) {
        this.data = data;
    }
}
