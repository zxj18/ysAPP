package com.vodbyte.movie.bean;

import android.content.Context;

import androidx.annotation.Keep;

import com.vodbyte.movie.utils.GsonUtil;
import com.vodbyte.movie.utils.PrefsUtils;

import java.io.Serializable;
import java.util.List;

@Keep
public class ConfigBean {

    private int home_style;
    private List<Affiche> affiche_list;
    private Ad ad;
    private String share_url;
    private String p2p_token;
    private String play_note_info;
    private String pic_domain;

    public String getPic_domain() {
        return pic_domain;
    }

    public void setPic_domain(String pic_domain) {
        this.pic_domain = pic_domain;
    }

    public int getHome_style() {
        return home_style;
    }

    public String getPlay_note_info() {
        return play_note_info;
    }

    public void setPlay_note_info(String play_note_info) {
        this.play_note_info = play_note_info;
    }

    public void setHome_style(int home_style) {
        this.home_style = home_style;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public List<Affiche> getAffiche_list() {
        return affiche_list;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public String getP2p_token() {
        return p2p_token;
    }

    public void setP2p_token(String p2p_token) {
        this.p2p_token = p2p_token;
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
    public static class Ad implements Serializable {
        private boolean open_ad;
        private String ad_appid;
        private String ad_appkey;
        private String ad_splash_id;
        private String ad_banner_id;
        private String ad_flow_id;
        private String ad_half_screen_id;
        private String ad_reward_video_id;
        private int ad_reward_video_time;

        public boolean isOpen_ad() {
            return open_ad;
        }

        public void setOpen_ad(boolean open_ad) {
            this.open_ad = open_ad;
        }

        public String getAd_appid() {
            return ad_appid;
        }

        public void setAd_appid(String ad_appid) {
            this.ad_appid = ad_appid;
        }

        public String getAd_appkey() {
            return ad_appkey;
        }

        public void setAd_appkey(String ad_appkey) {
            this.ad_appkey = ad_appkey;
        }

        public String getAd_splash_id() {
            return ad_splash_id;
        }

        public void setAd_splash_id(String ad_splash_id) {
            this.ad_splash_id = ad_splash_id;
        }

        public String getAd_banner_id() {
            return ad_banner_id;
        }

        public void setAd_banner_id(String ad_banner_id) {
            this.ad_banner_id = ad_banner_id;
        }

        public String getAd_flow_id() {
            return ad_flow_id;
        }

        public void setAd_flow_id(String ad_flow_id) {
            this.ad_flow_id = ad_flow_id;
        }

        public String getAd_half_screen_id() {
            return ad_half_screen_id;
        }

        public void setAd_half_screen_id(String ad_half_screen_id) {
            this.ad_half_screen_id = ad_half_screen_id;
        }

        public String getAd_reward_video_id() {
            return ad_reward_video_id;
        }

        public void setAd_reward_video_id(String ad_reward_video_id) {
            this.ad_reward_video_id = ad_reward_video_id;
        }

        public int getAd_reward_video_time() {
            return ad_reward_video_time;
        }

        public void setAd_reward_video_time(int ad_reward_video_time) {
            this.ad_reward_video_time = ad_reward_video_time;
        }

        @Override
        public String toString() {
            return "Ad{" +
                    "open_ad=" + open_ad +
                    ", ad_appid='" + ad_appid + '\'' +
                    ", ad_appkey='" + ad_appkey + '\'' +
                    ", ad_splash_id='" + ad_splash_id + '\'' +
                    ", ad_banner_id='" + ad_banner_id + '\'' +
                    ", ad_flow_id='" + ad_flow_id + '\'' +
                    ", ad_half_screen_id='" + ad_half_screen_id + '\'' +
                    ", ad_reward_video_id='" + ad_reward_video_id + '\'' +
                    ", ad_reward_video_time=" + ad_reward_video_time +
                    '}';
        }
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
