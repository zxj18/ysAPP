package com.vodbyte.freetv.bean;

import android.view.View;

import androidx.annotation.Keep;

import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.freetv.bean.rvc_cell.CidLabelCell;
import com.vodbyte.freetv.listener.OnVodCidClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class ClassifyTitleBean implements Serializable {
    private Integer id;
    private String name;
    private ExtendData extend;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExtendData getExtend() {
        return extend;
    }

    public void setExtend(ExtendData extend) {
        this.extend = extend;
    }

    public List<BaseRvCell> getVodCidCellList(final List<String> titleList, final OnVodCidClickListener listener) {

        List<BaseRvCell> cidCellList = new ArrayList<>();

        if ( extend != null){

            for (final String title : titleList) {
                if (!title.isEmpty()) {
                    CidLabelCell cidLabelCell = new CidLabelCell(title);
                    cidLabelCell.setListener(new OnClickViewRvListener() {
                        @Override
                        public void onClick(View view, int position) {
                            if (listener != null){
                                listener.onViewClick(view,position);
                            }
                        }

                        @Override
                        public <C> void onClickItem(C data, int position) {
                            if (listener != null){
                                listener.onDataClick((String) data,position);
                            }
                        }
                    });
                    cidCellList.add(cidLabelCell);
                }
            }

        }
        return cidCellList;
    }

    @Keep
    public static class ExtendData implements Serializable {
        List<String> class_name;
        List<String> area;
        List<String> lang;
        List<String> year;

        public List<String> getClassName() {
            if (!class_name.contains("全部:")) {
                class_name.add(0,"全部:");
            }
            return class_name;
        }

        public void setClassName(List<String> className) {
            this.class_name = className;
        }

        public List<String> getArea() {
            if (!area.contains("全部:")) {
                area.add(0,"全部:");
            }
            return area;
        }

        public void setArea(List<String> area) {
            this.area = area;
        }

        public List<String> getLang() {
            if (!lang.contains("全部:")) {
                lang.add(0,"全部:");
            }
            return lang;
        }

        public void setLang(List<String> lang) {
            this.lang = lang;
        }

        public List<String> getYear() {
            if (!year.contains("全部:")) {
                year.add(0,"全部:");
            }
            return year;
        }

        public void setYear(List<String> year) {
            this.year = year;
        }

    }
}