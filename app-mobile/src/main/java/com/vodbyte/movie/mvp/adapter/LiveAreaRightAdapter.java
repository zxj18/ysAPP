package com.vodbyte.movie.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.LiveAreaBean;

import java.util.List;

public class LiveAreaRightAdapter extends BaseAdapter {
    private List<LiveAreaBean.LiveBean> liveAreaList = null;
    private final Context mContext;

    public LiveAreaRightAdapter(Context mContext, List<LiveAreaBean.LiveBean> list) {
        this.mContext = mContext;
        this.liveAreaList = list;
    }

    @Override
    public int getCount() {
        return this.liveAreaList.size();
    }

    @Override
    public Object getItem(int position) {
        return liveAreaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final LiveAreaBean.LiveBean mLiveBean = liveAreaList.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.live_right_item_title, null);
            viewHolder.tvAreaView = (TextView) convertView.findViewById(R.id.tv_area_view);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvAreaView.setText(mLiveBean.getTitle());

        return convertView;
    }

    final static class ViewHolder {
        TextView tvAreaView;
    }
}

