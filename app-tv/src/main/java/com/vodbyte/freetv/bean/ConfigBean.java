package com.vodbyte.freetv.bean;

import android.content.Context;
import androidx.annotation.Keep;

import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.utils.GsonUtil;
import com.vodbyte.freetv.utils.PrefsUtils;

import java.io.Serializable;
import java.util.List;

@Keep
public class ConfigBean {

    private List<Affiche> affiche_list;
    private int open_tv_ad;
    private String tv_ad_url;
    private String tv_ad_jump_url;
    private String share_url;
    private String search_qrcode_url;
    private String error_qrcode_url;
    private String play_note_info;
    private int open_tv_video_decode;

    public String getError_qrcode_url() {
        if (!error_qrcode_url.startsWith("http")) {
            error_qrcode_url = String.format("%s/%s", Constant.BASE_API_URL,error_qrcode_url);
        }
        return error_qrcode_url;
    }

    public void setError_qrcode_url(String error_qrcode_url) {
        this.error_qrcode_url = error_qrcode_url;
    }

    public String getSearch_qrcode_url() {
        if (!search_qrcode_url.startsWith("http")) {
            search_qrcode_url = String.format("%s/%s", Constant.BASE_API_URL,search_qrcode_url);
        }
        return search_qrcode_url;
    }

    public void setSearch_qrcode_url(String search_qrcode_url) {
        this.search_qrcode_url = search_qrcode_url;
    }

    public String getTv_ad_jump_url() {
        return tv_ad_jump_url;
    }

    public void setTv_ad_jump_url(String tv_ad_jump_url) {
        this.tv_ad_jump_url = tv_ad_jump_url;
    }

    public int getOpen_tv_ad() {
        return open_tv_ad;
    }

    public void setOpen_tv_ad(int open_tv_ad) {
        this.open_tv_ad = open_tv_ad;
    }

    public String getTv_ad_url() {
        return tv_ad_url;
    }

    public void setTv_ad_url(String tv_ad_url) {
        this.tv_ad_url = tv_ad_url;
    }

    public String getPlay_note_info() {
        return play_note_info;
    }

    public void setPlay_note_info(String play_note_info) {
        this.play_note_info = play_note_info;
    }


    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getOpen_tv_video_decode() {
        return open_tv_video_decode;
    }

    public void setOpen_tv_video_decode(int open_tv_video_decode) {
        this.open_tv_video_decode = open_tv_video_decode;
    }

    public List<Affiche> getAffiche_list() {
        return affiche_list;
    }

    public void setAffiche_list(List<Affiche> affiche_list) {
        this.affiche_list = affiche_list;
    }

    public String toJsonString()
    {
        return GsonUtil.gsonToJson(this);
    }

    public static ConfigBean getConfig(Context context)
    {
        return GsonUtil.gsonToBean(PrefsUtils.with(context).read("config"),ConfigBean.class);
    }

    @Keep
    public static class Affiche implements Serializable {
        private String title;
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
