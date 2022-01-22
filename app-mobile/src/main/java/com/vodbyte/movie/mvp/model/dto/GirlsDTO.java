package com.vodbyte.movie.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.movie.bean.GirlBean;
import com.vodbyte.movie.bean.VodBean;

import java.util.List;

@Keep
public class GirlsDTO {

    private int status;
    private String msg;
    private List<GirlBean> data;

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

    public List<GirlBean> getData() {
        return data;
    }

    public void setData(List<GirlBean> data) {
        this.data = data;
    }
}
