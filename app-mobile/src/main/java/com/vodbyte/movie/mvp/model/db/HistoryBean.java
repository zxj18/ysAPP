package com.vodbyte.movie.mvp.model.db;

import androidx.annotation.Keep;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

@Keep
public class HistoryBean extends LitePalSupport {
    @Column(unique = true)
    private long vodPrimaryKey;
    private Date time;

    public long getvodPrimaryKey() {
        return vodPrimaryKey;
    }

    public void setvodPrimaryKey(long vodPrimaryKey) {
        this.vodPrimaryKey = vodPrimaryKey;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
