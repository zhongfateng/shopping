package com.liuwa.shopping.activity;

/**
 * Created by ZFT on 2019/9/29.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


        import android.content.Context;
        import android.os.Environment;
        import android.text.TextUtils;

        import com.liuwa.shopping.util.UpdateManager;

        import java.io.File;

        import luo.library.base.base.BaseConfig;

public class BaseAndroid {
    public static BaseConfig baseConfig;

    public BaseAndroid() {
    }

    public static void init(BaseConfig config) {
        baseConfig = config;
    }

    public static BaseConfig getBaseConfig() {
        if(baseConfig == null) {
            throw new IllegalArgumentException("请先调用init方法");
        } else {
            return baseConfig;
        }
    }

    public static void checkUpdate(Context context, int versionCode, String url, String updateMessage, boolean isForced) {
        if(versionCode > UpdateManager.getInstance().getVersionCode(context)) {
            int type = 0;
            if(UpdateManager.getInstance().isWifi(context)) {
                type = 1;
            }

            if(isForced) {
                type = 2;
            }

            String downLoadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
            File dir = new File(downLoadPath);
            if(!dir.exists()) {
                dir.mkdir();
            }

            String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            if(fileName == null && TextUtils.isEmpty(fileName) && !fileName.contains(".apk")) {
                fileName = context.getPackageName() + ".apk";
            }

            File file = new File(downLoadPath + fileName);
            UpdateManager.getInstance().setType(type).setUrl(url).setUpdateMessage(updateMessage).setFileName(fileName).setIsDownload(file.exists());
            if(type == 1 && !file.exists()) {
                UpdateManager.getInstance().downloadFile(context);
            } else {
                UpdateManager.getInstance().showDialog(context);
            }
        }

    }
}

