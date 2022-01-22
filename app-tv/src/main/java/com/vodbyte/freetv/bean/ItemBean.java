package com.vodbyte.freetv.bean;

import android.view.View;

import androidx.annotation.Keep;

import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.freetv.bean.rvc_cell.VodCell;
import com.vodbyte.freetv.listener.OnVodClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页分类视频列表实体
 */
@Keep
public class ItemBean {

    private String title;//标题
    private List<VodBean> vod_list;//视频列表

    private List<BaseRvCell> vodCellList;

    @Override
    public String toString() {
        return "ItemBean{" +
                "title='" + title + '\'' +
                ", vod_list=" + vod_list +
                '}';
    }

    public List<BaseRvCell> getVodCellList(final OnVodClickListener listener) {
        if (vodCellList == null && vod_list != null){
            vodCellList = new ArrayList<>();

            for (final VodBean vodBean : vod_list) {
                VodCell vodCell = new VodCell(vodBean);

                vodCell.setListener(new OnClickViewRvListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }

                    @Override
                    public <C> void onClickItem(C data, int position) {
                        if (listener != null){
                            listener.onClick(vodBean);
                        }
                    }
                });
                vodCellList.add(vodCell);
            }
        }
        return vodCellList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<VodBean> getVod_list() {
        return vod_list;
    }

    public void setVod_list(List<VodBean> vod_list) {
        this.vod_list = vod_list;
    }
}
