package com.vodbyte.freetv.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.freetv.bean.ConfigBean;

import java.io.Serializable;

/**
 * 系统配置信息
 */
@Keep
public class ConfigDTO implements Serializable {

    private Integer code;
    private String msg;
    private ConfigBean data;

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

    public ConfigBean getData() {
        return data;
    }

    public void setData(ConfigBean data) {
        this.data = data;
    }
}
