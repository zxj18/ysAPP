package com.vodbyte.freetv.bean;

import androidx.annotation.Keep;

import com.vodbyte.freetv.utils.GsonUtil;

import java.io.Serializable;
import java.util.List;

@Keep
public  class LiveAreaBean implements Serializable {
    private  Integer id;
    private  String title;
    private  List<LiveBean> data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LiveBean> getData() {
        return data;
    }

    public void setData(List<LiveBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LiveAreaBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", data=" + data +
                '}';
    }

    @Keep
    public static class LiveBean implements Serializable {
        String title;
        String image;
        List<LiveItemBean> content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<LiveItemBean> getContent() {
            return content;
        }

        public void setContent(List<LiveItemBean> content) {
            this.content = content;
        }

        public String toJsonString()
        {
            return GsonUtil.gsonToJson(this);
        }

        @Keep
        public static class LiveItemBean implements Serializable {
            String title;
            String url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}