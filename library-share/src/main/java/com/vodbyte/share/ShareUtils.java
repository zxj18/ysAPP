package com.vodbyte.share;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.vodbyte.share.info.IShareInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareUtils {

    public enum SHARE_TYPE {
        SHARE_TYPE_TIME_LINE,
        SHARE_TYPE_QQ,
        SHARE_TYPE_WEI_BO,
        SHARE_TYPE_QZONE,
        SHARE_TYPE_WECHAT
    }

    @JavascriptInterface
    public static void shareMoreImageToApp(final Context context, ArrayList<String> imageUrls,final String content, SHARE_TYPE share_type) {

        final Intent intent = new Intent();
        final ComponentName componentName;

        switch (share_type) {
            case SHARE_TYPE_QQ:
                componentName = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");

                if (!uninstallSoftware(context, "com.tencent.mobileqq")) {
                    Toast.makeText(context, "QQ未安装,请先安装!", Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
            case SHARE_TYPE_WECHAT:
                componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
                if (!uninstallSoftware(context, "com.tencent.mm")) {
                    Toast.makeText(context, "微信未安装,请先安装!", Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
            case SHARE_TYPE_TIME_LINE:
                componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                intent.putExtra("Kdescription", content);//内容描述

                if (!uninstallSoftware(context, "com.tencent.mm")) {
                    Toast.makeText(context, "微信未安装,请先安装!", Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
            case SHARE_TYPE_QZONE:
                componentName = new ComponentName("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity");

                if (!uninstallSoftware(context, "com.qzone")) {
                    Toast.makeText(context, "QQ空间未安装,请先安装!", Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
            case SHARE_TYPE_WEI_BO:
                componentName = new ComponentName("com.sina.weibo", "com.sina.weibo.composerinde.ComposerDispatchActivity");

                if (!uninstallSoftware(context, "com.sina.weibo")) {
                    Toast.makeText(context, "微博未安装,请先安装!", Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
            default:
                return;
        }

        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        final ArrayList<Uri> ImagePathsList = new ArrayList<>();

        new ImageDownloadUtils(context, imageUrls, new ImageDownloadUtils.DownloadImageStateListener() {
            @Override
            public void onFinish(List<String> list) {

                for (String filePath : list) {
                    if (FileUtils.isFileExists(filePath)) {
                        if (ImagePathsList.size() < 8) {
                            File file = new File(filePath);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                try {
                                    ImagePathsList.add(Uri.fromFile(file));
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            } else {
                                ImagePathsList.add(Uri.fromFile(file));
                            }
                        }
                    } else {
                        break;
                    }
                }

                if (ImagePathsList.size() == 0) {
                    Toast.makeText(context, "图片不存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if (!TextUtils.isEmpty(content)) {
                        intent.putExtra(Intent.EXTRA_TEXT, content);
                    }

                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, ImagePathsList);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed() {
                //LoadingUtils.dismiss();
//                 Toast.makeText(context,"下载失败请退出重新尝试",Toast.LENGTH_SHORT).show();
            }
        }).startDownload(true);
    }

    private static boolean uninstallSoftware(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            if (packageInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}