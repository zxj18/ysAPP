package com.vodbyte.movie.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.movie.bean.ClassifyTitleBean;
import com.vodbyte.movie.bean.ConfigBean;

import java.io.Serializable;
import java.util.List;

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
