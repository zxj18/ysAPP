package com.vodbyte.movie.bean.rv_cell;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.ItemBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.config.ItemType;
import com.vodbyte.movie.listener.OnVodClickListener;
import com.vodbyte.movie.utils.StartActUtil;

public class ItemCell extends BaseRvCell<ItemBean> {

    private String TAG = "ItemCell";

    public ItemCell(ItemBean itemBean) {
        super(itemBean);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_BOOK_LIST;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRvViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, int position) {
        TextView tvTitle = holder.getTextView(R.id.tv_title_cell_item);
        RecyclerView rvBookList = (RecyclerView) holder.getView(R.id.rv_book_cell_item);

        Log.d(TAG, "onBindViewHolder->  mContext="+holder.getContext());
//        Glide.with(holder.getContext()).load(mData.getIconResId()).into(ivIcon);
        tvTitle.setText(mData.getTitle());
//        tvIntro.setText(mData.getIntro());
        if (rvBookList.getAdapter() == null){
            rvBookList.setLayoutManager(new LinearLayoutManager(
                    holder.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false));
            rvBookList.setAdapter(new RvSimpleAdapter());
        }
        RvSimpleAdapter simpleAdapter = (RvSimpleAdapter) rvBookList.getAdapter();
        simpleAdapter.clear();
        simpleAdapter.addAll(mData.getVodCellList(new OnVodClickListener() {
            @Override
            public void onClick( VodBean vodBean) {
                StartActUtil.toPlayDetail(holder.getContext(),vodBean);
            }
        }));
    }
}
