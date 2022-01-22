package com.vodbyte.movie.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.movie.bean.RankBean;

import java.util.List;

@Keep
public class RankDTO {

    List<String> kinkList;
    List<RankBean> comicRankList;

    public RankDTO(List<String> kinkList, List<RankBean> comicRankList) {
        this.kinkList = kinkList;
        this.comicRankList = comicRankList;
    }

    public List<String> getKinkList() {
        return kinkList;
    }

    public void setKinkList(List<String> kinkList) {
        this.kinkList = kinkList;
    }

    public List<RankBean> getComicRankList() {
        return comicRankList;
    }

    public void setComicRankList(List<RankBean> comicRankList) {
        this.comicRankList = comicRankList;
    }
}
