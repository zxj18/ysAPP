package com.vodbyte.share.templet;

import android.content.Context;

import com.vodbyte.share.ShareSetting;
import com.vodbyte.share.info.IShareInfo;
import com.vodbyte.share.way.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装模板的抽象实现
 * Created by GuDong on 8/20/15.
 */
public abstract class AbsWarpTemplateShare<T> implements IWarpTemplateShare {
    protected Context mContext;
    /**
     * 包装的源信息实体
     */
    protected T mDataSource;
    protected List<IShareInfo> dataList;

    public AbsWarpTemplateShare() {
        dataList = new ArrayList<>();
    }


    public AbsWarpTemplateShare(Context context) {
        this.mContext = context;
        dataList = new ArrayList<>();

    }

    public AbsWarpTemplateShare(Context context, T dataSource) {
        this(context);
        this.mDataSource = dataSource;
    }

    /**
     * 不同分享内容的集合
     *
     * @return
     */
    public List<IShareInfo> getListInfo() {
        if (ShareSetting.getInstance().getShareMedia().size() == 0) {
//            ShareSetting.getInstance().addShareMedia(SHARE_MEDIA.WEICHAT, SHARE_MEDIA.WEICHATCIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QQZONE, SHARE_MEDIA.MESSAGE);
            return dataList;
        }
        for (SHARE_MEDIA item : ShareSetting.getInstance().getShareMedia()) {
            switch (item) {
                case WEICHAT:
                    dataList.add(warpWechatInfo());
                    break;
                case WEICHATCIRCLE:
                    dataList.add(warpWechatInfo());
                    break;
                case SINA:
                    dataList.add(warpSinaInfo());
                    break;
                case QQ:
                    dataList.add(warpQQInfo());
                    break;
                case QQZONE:
                    dataList.add(warpQQInfo());
                    break;
                case COPYLINK:
                    dataList.add(warpMessageInfo());
                    break;
                case DOWNLOAD:
                    dataList.add(warpSaveInfo());
                    break;
                case IMAGES:
                    dataList.add(warpImageInfo());
                    break;
                default:
                    break;
            }
        }
        return dataList;
    }
}
