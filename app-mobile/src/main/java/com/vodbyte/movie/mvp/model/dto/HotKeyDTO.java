package com.vodbyte.movie.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.HotKeyBean;
import com.vodbyte.movie.bean.VodBean;

import java.util.ArrayList;
import java.util.List;

@Keep
public class HotKeyDTO {

    private int status;
    private String msg;
    private List<String> data;

    private static final int[] mColorResId = new int[]{
            R.drawable.bg_hot_key_purple,R.drawable.bg_hot_key_light_green,
            R.drawable.bg_hot_key_dark_green,R.drawable.bg_hot_key_red,
            R.drawable.bg_hot_key_orange,R.drawable.bg_hot_key_blue
    };

    public boolean isSuccess(){
        return "ok".equals(msg);
    }

    public List<HotKeyBean> toHotKeyBeanList(){
        if (!isSuccess()){
            return null;
        }
        List<HotKeyBean> list = new ArrayList<>();
        int  i = 0;
        for (String keyword : data) {
            HotKeyBean keyBean = new HotKeyBean();
            keyBean.setKey(keyword.trim());
            keyBean.setColorResId(mColorResId[i % mColorResId.length]);
            if (!keyword.trim().equals("")) {
                list.add(keyBean);
                i++;
            }
        }
        return list;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
