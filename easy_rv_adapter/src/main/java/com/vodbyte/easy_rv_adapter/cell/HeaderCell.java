package com.vodbyte.easy_rv_adapter.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.vodbyte.easy_rv_adapter.R;
import com.vodbyte.easy_rv_adapter.Utils;
import com.vodbyte.easy_rv_adapter.base.BaseRvStateCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.easy_rv_adapter.config.ItemType;

public class HeaderCell extends BaseRvStateCell {
    public static final int mDefaultHeight = 80;//dp
    public HeaderCell(Object o) {
        super(o);
    }

    @Override
    public int getItemType() {
        return ItemType.HEADER_TYPE;
    }


    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, int position) {

    }

    @Override
    protected View getDefaultView(Context context) {
        // 设置LoadMore View显示的默认高度
        setHeight(Utils.dpToPx(context,mDefaultHeight));
        return LayoutInflater.from(context).inflate(R.layout.rv_load_more_layout,null);
    }
}
