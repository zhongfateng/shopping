package com.liuwa.shopping.util;

/**
 * Created by ZFT on 2019/9/29.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.BuildConfig;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import luo.library.base.base.BaseAndroid;
import luo.library.base.widget.BaseDialog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.io.File;
import luo.library.base.base.BaseAndroid;
import luo.library.base.widget.BaseDialog;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.ProgressCallback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

public class UpdateManager {
    private String downLoadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
    private int type = 0;
    private String url = "";
    private String updateMessage = "";
    private String fileName = null;
    private boolean isDownload = false;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private BaseDialog dialog;
    private ProgressDialog progressDialog;
    public static UpdateManager updateManager;

    public static UpdateManager getInstance() {
        if(updateManager == null) {
            updateManager = new UpdateManager();
        }

        return updateManager;
    }

    private UpdateManager() {
    }

    public void showDialog(final Context context) {
        String title = "";
        String left = "";
        boolean cancelable = true;
        if(this.type == 1 | this.isDownload) {
            title = "安装新版本";
            left = "立即安装";
        } else {
            title = "发现新版本";
            left = "立即更新";
        }

        if(this.type == 2) {
            cancelable = false;
        }

        this.dialog = (new luo.library.base.widget.BaseDialog.Builder(context)).setTitle(title).setMessage(this.updateMessage).setCancelable(cancelable).setLeftClick(left, new View.OnClickListener() {
            public void onClick(View view) {
                UpdateManager.this.dialog.dismiss();
                if(UpdateManager.this.type == 1 | UpdateManager.this.isDownload) {
                    UpdateManager.this.installApk(context, new File(UpdateManager.this.downLoadPath, UpdateManager.this.fileName));
                } else if(UpdateManager.this.url != null && !TextUtils.isEmpty(UpdateManager.this.url)) {
                    if(UpdateManager.this.type == 2) {
                        UpdateManager.this.createProgress(context);
                    } else {
                        UpdateManager.this.createNotification(context);
                    }

                  UpdateManager.this.downloadFile(context);
                } else {
                    Toast.makeText(context, "下载地址错误", 0).show();
                }

            }
        }).setRightClick("取消", new View.OnClickListener() {
            public void onClick(View view) {
                UpdateManager.this.dialog.dismiss();
                if(UpdateManager.this.type == 2) {
                    System.exit(0);
                }

            }
        }).create();
        this.dialog.show();
    }

    public void downloadFile(final Context context) {
        RequestParams params = new RequestParams(this.url);
        params.setSaveFilePath(this.downLoadPath + this.fileName);
        x.http().request(HttpMethod.GET, params, new Callback.ProgressCallback<File>() {
            public void onSuccess(File result) {
            }

            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(context, ex.getMessage(), 0).show();
            }

            public void onCancelled(CancelledException cex) {
            }

            public void onFinished() {
            }

            public void onWaiting() {
            }

            public void onStarted() {
            }

            public void onLoading(long total, long current, boolean isDownloading) {
                if(UpdateManager.this.type == 0) {
                    UpdateManager.this.notifyNotification(current, total);
                } else if(UpdateManager.this.type == 2) {
                    UpdateManager.this.progressDialog.setProgress((int)(current * 100L / total));
                }

                if(total == current) {
                    if(UpdateManager.this.type == 0) {
                       UpdateManager.this.mBuilder.setContentText("下载完成");
                        UpdateManager.this.mNotifyManager.notify(10086, UpdateManager.this.mBuilder.build());
                    } else if(UpdateManager.this.type == 2) {
                        UpdateManager.this.progressDialog.setMessage("下载完成");
                    }

                    if(UpdateManager.this.type == 1) {
                        UpdateManager.this.showDialog(context);
                    } else {
                        UpdateManager.this.installApk(context, new File(UpdateManager.this.downLoadPath, UpdateManager.this.fileName));
                    }
                }

            }
        });
    }

    private void createProgress(Context context) {
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setMax(100);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("正在下载...");
        this.progressDialog.setProgressStyle(1);
        this.progressDialog.show();
    }

    private void createNotification(Context context) {
        this.mNotifyManager = (NotificationManager)context.getSystemService("notification");
        this.mBuilder = new NotificationCompat.Builder(context);
        this.mBuilder.setSmallIcon(BaseAndroid.getBaseConfig().getAppLogo());
        this.mBuilder.setContentTitle("版本更新");
        this.mBuilder.setContentText("正在下载...");
        this.mBuilder.setProgress(0, 0, false);
        Notification notification = this.mBuilder.build();
        notification.flags = 16;
        this.mNotifyManager.notify(10086, notification);
    }

    private void notifyNotification(long percent, long length) {
        this.mBuilder.setProgress((int)length, (int)percent, false);
        this.mNotifyManager.notify(10086, this.mBuilder.build());
    }

    private void installApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
//判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.liuwa.shopping.provider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);

//        Intent intent = new Intent();
//        intent.addFlags(268435456);
//        intent.setAction("android.intent.action.VIEW");
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        context.startActivity(intent);
    }

    public int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception var5) {
            var5.printStackTrace();
            return 0;
        }
    }

    public boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService("connectivity");
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == 1;
    }

    public UpdateManager setUrl(String url) {
        this.url = url;
        return this;
    }

    public UpdateManager setType(int type) {
        this.type = type;
        return this;
    }

    public UpdateManager setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
        return this;
    }

    public UpdateManager setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public UpdateManager setIsDownload(boolean download) {
        this.isDownload = download;
        return this;
    }
}

