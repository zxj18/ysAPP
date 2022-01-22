package com.vodbyte.freetv.bean;

import android.util.Log;
import android.view.View;

import androidx.annotation.Keep;

import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.bean.rvc_cell.SubSourceCell;
import com.vodbyte.freetv.listener.OnVodCidClickListener;
import com.vodbyte.freetv.utils.GsonUtil;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频
 */
@Keep
public class VodBean extends LitePalSupport implements Serializable {
    @Column(unique = true)
    private long vid;
    private int cid;
    private String vod_name;
    private String vod_sub;
    private String vod_en;
    private String vod_letter;
    private String vod_class;
    private String vod_remarks;
    private String vod_pic;
    private String vod_actor;
    private String vod_serial;
    private String vod_director;
    private String vod_blurb;
    private String vod_area;
    private String vod_lang;
    private String vod_year;
    private String vod_score;
    private String vod_content;
    List<VodPlayBean> vod_play_list;
    private int vod_skip_time;

    @Column(ignore = true)
    private boolean isCollected;

    public long getVid() {
        return vid;
    }

    public void setVid(long vid) {
        this.vid = vid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getVod_serial() {
        return vod_serial;
    }

    public void setVod_serial(String vod_serial) {
        this.vod_serial = vod_serial;
    }

    public String getVod_name() {
        return vod_name;
    }

    public void setVod_name(String vod_name) {
        this.vod_name = vod_name;
    }

    public String getVod_sub() {
        return vod_sub;
    }

    public void setVod_sub(String vod_sub) {
        this.vod_sub = vod_sub;
    }

    public String getVod_en() {
        return vod_en;
    }

    public void setVod_en(String vod_en) {
        this.vod_en = vod_en;
    }

    public String getVod_letter() {
        return vod_letter;
    }

    public void setVod_letter(String vod_letter) {
        this.vod_letter = vod_letter;
    }

    public String getVod_class() {
        return vod_class;
    }

    public void setVod_class(String vod_class) {
        this.vod_class = vod_class;
    }

    public String getVod_remarks() {
        return vod_remarks;
    }

    public void setVod_remarks(String vod_remarks) {
        this.vod_remarks = vod_remarks;
    }

    public String getVod_pic() {
        if (!vod_pic.startsWith("http")) {
            vod_pic = String.format("%s/%s", Constant.BASE_API_URL,vod_pic);
        }
        return vod_pic;
    }

    public void setVod_pic(String vod_pic) {
        this.vod_pic = vod_pic;
    }

    public String getVod_actor() {
        return vod_actor;
    }

    public void setVod_actor(String vod_actor) {
        this.vod_actor = vod_actor;
    }

    public String getVod_director() {
        return vod_director;
    }

    public void setVod_director(String vod_director) {
        this.vod_director = vod_director;
    }

    public String getVod_blurb() {
        return vod_blurb;
    }

    public void setVod_blurb(String vod_blurb) {
        this.vod_blurb = vod_blurb;
    }

    public String getVod_area() {
        return vod_area;
    }

    public void setVod_area(String vod_area) {
        this.vod_area = vod_area;
    }

    public String getVod_lang() {
        return vod_lang;
    }

    public void setVod_lang(String vod_lang) {
        this.vod_lang = vod_lang;
    }

    public String getVod_year() {
        return vod_year;
    }

    public void setVod_year(String vod_year) {
        this.vod_year = vod_year;
    }

    public String getVod_score() {
        return vod_score;
    }

    public void setVod_score(String vod_score) {
        this.vod_score = vod_score;
    }

    public String getVod_content() {
        return vod_content;
    }

    public void setVod_content(String vod_content) {
        this.vod_content = vod_content;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public List<VodPlayBean> getVod_play_list() {
        return vod_play_list;
    }

    public void setVod_play_list(List<VodPlayBean> vod_play_list) {
        this.vod_play_list = vod_play_list;
    }

    public int getVod_skip_time() {
        return vod_skip_time;
    }

    public void setVod_skip_time(int vod_skip_time) {
        this.vod_skip_time = vod_skip_time;
    }

    public long getPrimaryKey(){
        return getBaseObjId();
    }

    public List<BaseRvCell> getVodSubSourceCell(final OnVodCidClickListener listener) {

        List<BaseRvCell> cidCellList = new ArrayList<>();

        if ( vod_play_list != null){

            for (final VodPlayBean vodPlayBean : vod_play_list) {
                SubSourceCell sourceCell = new SubSourceCell(vodPlayBean);
                sourceCell.setListener(new OnClickViewRvListener() {
                    @Override
                    public void onClick(View view, int position) { }

                    @Override
                    public <C> void onClickItem(C data, int position) {
                        if (listener != null){
                            listener.onDataClick((String) data,position);
                        }
                    }
                });
                cidCellList.add(sourceCell);
            }

        }
        return cidCellList;
    }

    @Keep
    public static class VodPlayBean implements Serializable {
        private Integer sid;
        private String from;
        private String note;
        private String qrcode_url;
        private Integer limit;
        private List<UrlBean> urls;

        public String getQrcode_url() {
            if (!qrcode_url.startsWith("http")) {
                qrcode_url = String.format("%s/%s", Constant.BASE_API_URL,qrcode_url);
            }
            return qrcode_url;
        }

        public void setQrcode_url(String qrcode_url) {
            this.qrcode_url = qrcode_url;
        }

        public Integer getSid() {
            return sid;
        }

        public void setSid(Integer sid) {
            this.sid = sid;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public List<UrlBean> getUrls() {
            return urls;
        }

        public void setUrls(List<UrlBean> urls) {
            this.urls = urls;
        }

        @Override
        public String toString() {
            return "VodPlayBean{" +
                    "sid=" + sid +
                    ", from='" + from + '\'' +
                    ", note='" + note + '\'' +
                    ", limit=" + limit +
                    ", urls=" + urls +
                    '}';
        }

        @Keep
        public static class UrlBean implements Serializable {
            private Integer nid;
            private String name;
            private String url;

            public Integer getNid() {
                return nid;
            }

            public void setNid(Integer nid) {
                this.nid = nid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            @Override
            public String toString() {
                return "UrlBean{" +
                        "nid=" + nid +
                        ", name='" + name + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }
    }

    public String toJsonString()
    {
        return GsonUtil.gsonToJson(this);
    }

    @Override
    public String toString() {
        return "VodBean{" +
                "id=" + vid +
                ", vod_name='" + vod_name + '\'' +
                ", vod_sub='" + vod_sub + '\'' +
                ", vod_en='" + vod_en + '\'' +
                ", vod_letter='" + vod_letter + '\'' +
                ", vod_class='" + vod_class + '\'' +
                ", vod_pic='" + vod_pic + '\'' +
                ", vod_actor='" + vod_actor + '\'' +
                ", vod_director='" + vod_director + '\'' +
                ", vod_blurb='" + vod_blurb + '\'' +
                ", vod_area='" + vod_area + '\'' +
                ", vod_lang='" + vod_lang + '\'' +
                ", vod_year='" + vod_year + '\'' +
                ", vod_score='" + vod_score + '\'' +
                ", vod_content='" + vod_content + '\'' +
                ", vod_play_list=" + vod_play_list +
                '}';
    }
}