package com.vodbyte.freetv.mvp.model.dto;

import androidx.annotation.Keep;


import com.vodbyte.freetv.bean.VodBean;

import java.util.List;

@Keep
public class VodListDTO {

    private int status;
    private String msg;
    private List<VodBean> data;

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

    public List<VodBean> getData() {
        return data;
    }

    public void setData(List<VodBean> data) {
        this.data = data;
    }
}
