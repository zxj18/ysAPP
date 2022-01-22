package com.vodbyte.freetv.contract.collect;

import com.vodbyte.freetv.bean.VodBean;

import java.io.Serializable;

public class VideoCollect implements Serializable {

    private static final long serialVersionUID = 6834993428543033756L;

    private VodBean video;

    private String videoEngine;

    public VodBean getVideo() {
        return video;
    }

    public void setVideo(VodBean video) {
        this.video = video;
    }

    public String getVideoEngine() {
        return videoEngine;
    }

    public void setVideoEngine(String videoEngine) {
        this.videoEngine = videoEngine;
    }
}
