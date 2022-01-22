package com.vodbyte.freetv.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.freetv.bean.VodBean;

@Keep
public class VodDetailDTO {

    private int status;
    private String msg;
    private VodBean data;

    public boolean isSuccess(){
        return "ok".equals(msg) || status == 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public VodBean getData() {
        return data;
    }

    public void setData(VodBean data) {
        this.data = data;
    }
}
