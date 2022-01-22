package com.vodbyte.movie.bean.rv_cell;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fiio.sdk.callBack.BannerCallBack;
import com.fiio.sdk.view.BannerAd;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.ConfigBean;
import com.vodbyte.movie.config.ItemType;
import com.vodbyte.movie.utils.ScreenUtil;

/**
 * 广告
 */
public class VodBannerAdCell extends BaseRvCell {

    private String TAG = "VodBannerAdCell";
    LinearLayout layoutAdView;
    BannerAd bannerAd;
    public OnSubSetClickViewListener mSubSetListener;

    public VodBannerAdCell() {
        super(null);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_VOD_BANNER_AD_LABEL;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRvViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vod_banner_ad_cell,parent,false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, int position) {
        layoutAdView = (LinearLayout)holder.getView(R.id.layout_banner_ad_view);
        try {
            ConfigBean configBean = ConfigBean.getConfig(holder.getContext());
            if (configBean != null) {
                if (configBean.getAd().isOpen_ad() && !configBean.getAd().getAd_banner_id().isEmpty()) {
                    bannerAd = new BannerAd((Activity) holder.getContext(), configBean.getAd().getAd_banner_id(), new BannerCallBack() {
                        @Override
                        public void onAdLoaded(View view) {
                            layoutAdView.removeAllViews();
                            layoutAdView.addView(view);
                        }

                        @Override
                        public void onAdShow() {

                        }

                        @Override
                        public void onAdClose() {

                        }

                        @Override
                        public void onAdClick() {

                        }

                        @Override
                        public void onAdFail(String s) {

                        }
                    });
                    bannerAd.loadAd();
                }else {
                    layoutAdView.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){

        }
    }

    /**
     * 添加监听事件
     * @param mListener
     */
    public void setListener(OnSubSetClickViewListener mListener) {
        this.mSubSetListener = mListener;
    }

    public interface OnSubSetClickViewListener {
        void onClickItem(int vodIndex,int subIndex);
    }

}
