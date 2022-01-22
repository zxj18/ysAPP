package com.vodbyte.freetv.mvp.adapter;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.vodbyte.freetv.R;

import java.util.ArrayList;
import java.util.List;

public class RightTitleAdapter extends RecyclerView.Adapter<RightTitleAdapter.ViewHolder> {

    private Context context;
    private List<String> dataList = new ArrayList<>();
    private OnClickListener listener;

    public RightTitleAdapter(Context context, OnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void refresh(List<String> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dkplayer_layout_right_title_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_item_title.setText(dataList.get(position));
        holder.tv_item_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        holder.tv_item_title.setOnClickListener(v -> {
            listener.onTitleFocus(holder.getAdapterPosition());
        });

        holder.tv_item_title.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                holder.tv_item_title.setBackgroundColor(Color.parseColor("#39C5BB"));
                holder.tv_item_title.setTextColor(Color.parseColor("#000000"));
            }else {
                holder.tv_item_title.setBackgroundColor(Color.parseColor("#9939C5BB"));
                holder.tv_item_title.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnClickListener {
        void onTitleFocus(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_title;
        public ViewHolder(@NonNull View view) {
            super(view);
            tv_item_title = view.findViewById(R.id.tv_live_item_title);
            tv_item_title.setFocusable(true);
        }
    }
}
