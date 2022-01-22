package com.vodbyte.movie.app;

import android.content.Context;

import com.vodbyte.share.info.IShareInfo;
import com.vodbyte.share.info.SimpleShareText;
import com.vodbyte.share.templet.AbsWarpTemplateShare;

public class DefaultShareImpl extends AbsWarpTemplateShare {

    private SimpleShareText mSimpleShareText;

    public DefaultShareImpl(Context context,SimpleShareText simpleShareText) {
        super(context);
        mSimpleShareText = simpleShareText;
    }

    @Override
    public IShareInfo warpWechatInfo() {
        return mSimpleShareText;
    }

    @Override
    public IShareInfo warpSinaInfo() {
        return mSimpleShareText;
    }

    @Override
    public IShareInfo warpQQInfo() {
        return mSimpleShareText;
    }

    @Override
    public IShareInfo warpMessageInfo() {
        return mSimpleShareText;
    }

    @Override
    public IShareInfo warpSaveInfo() {
        return mSimpleShareText;
    }

    @Override
    public IShareInfo warpImageInfo() {
        return mSimpleShareText;
    }
}
