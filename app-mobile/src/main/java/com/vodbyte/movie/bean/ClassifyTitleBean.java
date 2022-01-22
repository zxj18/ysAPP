package com.vodbyte.movie.bean;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.Keep;

import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.movie.bean.rv_cell.CidLabelCell;
import com.vodbyte.movie.bean.rv_cell.VodCell;
import com.vodbyte.movie.listener.OnVodCidClickListener;
import com.vodbyte.movie.listener.OnVodClickListener;

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

    public List<BaseRvCell> getVodCidCellList(final List<String> titleList,final OnVodCidClickListener listener) {

        List<BaseRvCell> cidCellList = new ArrayList<>();

        if ( extend != null){

            for (final String title : titleList) {
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
        return cidCellList;
    }

    @Keep
    public static class ExtendData implements Serializable {
        List<String> class_name;
        List<String> area;
        List<String> lang;
        List<String> year;

        public List<String> getClassName() {
            class_name.add(0,"类型:");
            return class_name;
        }

        public void setClassName(List<String> className) {
            this.class_name = className;
        }

        public List<String> getArea() {
            area.add(0,"地区:");
            return area;
        }

        public void setArea(List<String> area) {
            this.area = area;
        }

        public List<String> getLang() {
            lang.add(0,"语言:");
            return lang;
        }

        public void setLang(List<String> lang) {
            this.lang = lang;
        }

        public List<String> getYear() {
            year.add(0,"年代:");
            return year;
        }

        public void setYear(List<String> year) {
            this.year = year;
        }

    }
}