package com.vodbyte.movie.bean;

import androidx.annotation.Keep;

import com.vodbyte.movie.config.Constant;

/**
 * 首页轮播图实体
 */
@Keep
public class BannerBean {

    private String title;
    private String image_url;
    private Integer vod_id;
    private VodBean vod;

    @Override
    public String toString() {
        return "BannerBean{" +
                "title='" + title + '\'' +
                ", imageUrl='" + image_url + '\'' +
                ", vod_id=" + vod_id +
                '}';
    }

    public BannerBean(String title, String imageUrl, int vodId) {
        this.title = title;
        this.image_url = imageUrl;
        this.vod_id = vodId;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public String getImageUrl() {
        if (!image_url.startsWith("http")) {
            image_url = String.format("%s/%s", Constant.PIC_DOMAIN_URL,image_url);
        }
        return image_url == null ? "" : image_url;
    }

    public void setImageUrl(String imageUrl) {
        this.image_url = imageUrl == null ? "" : imageUrl;
    }

    public Integer getVodId() {
        return vod_id;
    }

    public void setVodId(Integer vodId) {
        this.vod_id = vodId;
    }

    public VodBean getVod() {
        return vod;
    }

    public void setVod(VodBean vod) {
        this.vod = vod;
    }
}
