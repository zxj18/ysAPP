package com.vodbyte.movie.mvp.model.vo;

import androidx.annotation.Keep;

import com.vodbyte.movie.bean.ChapterBean;

import java.util.List;

@Keep
public class BookTabDirVO {

    private String updateTime;
    private List<ChapterBean> chapterList;

    public String getUpdateTime() {
        return updateTime == null ? "" : updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? "" : updateTime;
    }

    public List<ChapterBean> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<ChapterBean> chapterList) {
        this.chapterList = chapterList;
    }
}
