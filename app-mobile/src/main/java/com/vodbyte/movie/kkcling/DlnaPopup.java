package com.vodbyte.movie.kkcling;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.vodbyte.movie.R;
import com.vodbyte.videoplayer.player.VideoViewManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.support.model.MediaInfo;
import org.fourthline.cling.support.model.PositionInfo;

import java.util.List;


public class DlnaPopup extends BottomPopupView {

    BaseAdapter mAdapter;
    private DLNAPlayer mPlayer;
    private Device mDevice;
    private ControlPoint mControlPoint;
    private String mTitle;
    private String mUrl;
    private Context mContext;
    private SmartRefreshLayout mRefreshLayout;

    public DlnaPopup(@NonNull Context context, String title, String url) {
        this(context);
        mTitle = title;
        mUrl = url;
        mContext = context;
    }

    public DlnaPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.activity_dlna;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mRefreshLayout = findViewById(R.id.refreshLayout_dlna_listview);
        ListView listView = findViewById(R.id.dlna_list_view);
        mAdapter = new DlnaPopup.DlnaListViewItemAdapter(mContext, android.R.layout.simple_list_item_single_choice, DLNAManager.getInstance(mContext).getDeviceList());
        listView.setAdapter(mAdapter);

        mPlayer = new DLNAPlayer();
        DLNAManager.getInstance(mContext).setOnDeviceRefreshListener(new DLNAManager.DeviceRefreshListener() {
            @Override
            public void onDeviceRefresh() {
                if (mControlPoint == null) {
                    mControlPoint = DLNAManager.getInstance(mContext).getControlPoint();
                }
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.finishRefresh();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDevice = (Device) mAdapter.getItem(position);
                mPlayer.setUp(mDevice, mControlPoint);
                mPlayer.play(mTitle, mUrl);

                VideoViewManager.instance().get("PlayDetailActivity").pause();
                getHandler().postDelayed(() -> dismiss(),800);

            }
        });
        mPlayer.addListener(new DLNAPlayer.EventListener() {
            @Override
            public void onPlay() {
                log("<-onPlay->");
            }

            @Override
            public void onGetMediaInfo(MediaInfo mediaInfo) {
                log("onGetMediaInfo:" + mediaInfo.getMediaDuration() + "," + mediaInfo.getPlayMedium() + "," + mediaInfo.getRecordMedium() + "," + mediaInfo.getCurrentURI());
            }

            @Override
            public void onPlayerError() {
                log("onPlayError");
            }

            @Override
            public void onTimelineChanged(PositionInfo positionInfo) {
                log("onTimelineChanged:" + positionInfo.getTrackDuration() + "," + positionInfo.getAbsTime() + "," + positionInfo.getRelTime());
            }

            @Override
            public void onSeekCompleted() {
                log("onSeekCompleted");
            }

            @Override
            public void onPaused() {
                log("onPaused");
            }

            @Override
            public void onMuteStatusChanged(boolean isMute) {
                log("onMuteStatusChanged:" + isMute);
            }

            @Override
            public void onVolumeChanged(int volume) {
                log("onVolumeChanged:" + volume);
            }

            @Override
            public void onStop() {
                log("onStop");
            }
        });

        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> DLNAManager.getInstance(mContext).search());

    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
        DLNAManager.getInstance(mContext).search();
        mRefreshLayout.autoRefresh();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        mRefreshLayout.finishRefresh();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getScreenHeight(getContext()) * .6f);
    }


    class DlnaListViewItemAdapter extends ArrayAdapter<Device> {

        public DlnaListViewItemAdapter(Context context, int resource, List<Device> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DlnaPopup.DlnaListViewItemAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new DlnaPopup.DlnaListViewItemAdapter.ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_single_choice, parent, false);
                holder.textView = convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            } else {
                holder = (DlnaPopup.DlnaListViewItemAdapter.ViewHolder) convertView.getTag();
            }
            Device device = getItem(position);
            holder.textView.setText(device.getDetails().getFriendlyName());
            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }
    }

    private void log(String msg) {
        Log.i("==MainActivity==", msg);
    }
}


