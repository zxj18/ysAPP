package com.vodbyte.movie.bean.rv_cell;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.utils.ScreenUtil;
import com.vodbyte.movie.utils.StartActUtil;

import java.util.ArrayList;
import java.util.List;

public class BookListCell extends BaseRvCell<List<VodBean>> {

    private int mType;
    private String mName;

    public static final int TYPE_TAB_SET_SUB_LIST_RECOMMEND = 16;

    public BookListCell(List<VodBean> list, int type, String name) {
        super(list);
        mType = type;
        mName = name;
    }


    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return mType;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_vod_list,parent,false);
        {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            int width = ScreenUtil.getScreenWidth(parent.getContext()) / 3;
            layoutParams.height = width + (width / 2);
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.topMargin = ScreenUtil.dip2px(parent.getContext(),5);
            view.setLayoutParams(layoutParams);
        }
        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, int position) {
        TextView mTvTitle = holder.getTextView(R.id.tv_title_cell_book_list);
        mTvTitle.setText(mName);

        RecyclerView rv = (RecyclerView) holder.getView(R.id.rv_vod_cell_view_list);
        rv.setLayoutManager(new GridLayoutManager(
                holder.getContext(),3,
                GridLayoutManager.VERTICAL,
                false));

        List<BaseRvCell> cellList = new ArrayList<>();
        for (final VodBean bean : mData) {

            VodCell vodCell = new VodCell(bean);
            vodCell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    StartActUtil.toPlayDetail(holder.getContext(),mData.get(position));
                }

            });
            cellList.add(vodCell);
        }
        RvSimpleAdapter adapter = new RvSimpleAdapter();
        rv.setAdapter(adapter);
        adapter.addAll(cellList);

    }
}
