package com.vodbyte.share.info;

/**
 * 分享图片和链接
 */
public class SimpleShareImage implements IShareInfo {
    private String mTitle;
    private String mContent;
    private String mShareUrl;
    private String mImageUrl;

    public SimpleShareImage(String content, String imageUrl) {
        this.mContent = content;
        this.mImageUrl = imageUrl;
    }

    @Override
    public String getShareTitle() {
        return mTitle;
    }
    @Override
    public String getShareContent() {
        return mContent;
    }
    @Override
    public String getShareImgUrl() {
        return mImageUrl;
    }
    @Override
    public String getShareUrl() {
        return mShareUrl;
    }

}
