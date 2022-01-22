package com.vodbyte.freetv.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.freetv.bean.VodBean;

import java.util.List;

@Keep
public class SearchDTO {

    private int code;
    private String msg;
    private List<VodBean> data;

    public boolean isSuccess(){
        return "ok".equals(msg);
    }

    public void setStatus(int status) {
        this.code = status;
    }
    public int getStatus() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public List<VodBean> getData() {
        return data;
    }

    public void setData(List<VodBean> data) {
        this.data = data;
    }
}
