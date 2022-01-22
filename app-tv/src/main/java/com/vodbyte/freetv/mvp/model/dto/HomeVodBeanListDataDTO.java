package com.vodbyte.freetv.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.freetv.bean.VodBean;

import java.util.List;

@Keep
public class HomeVodBeanListDataDTO {

    private Integer code;
    private String msg;
    private HomeVodBeanListData data;

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

    public HomeVodBeanListData getData() {
        return data;
    }

    public void setData(HomeVodBeanListData data) {
        this.data = data;
    }

    @Keep
    public static class HomeVodBeanListData {
        private List<VodBean> item_list;
        public List<VodBean> getItem_list() {
            return item_list;
        }
        public void setItem_list(List<VodBean> item_list) {
            this.item_list = item_list;
        }
    }
}
