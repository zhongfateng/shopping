package com.liuwa.shopping.util;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;

import com.liuwa.shopping.R;

import java.io.File;
import java.text.DecimalFormat;

public class VersionUpdataHelper {
    private static final String TAG = "VersionUpdataHelper";

    public static final String DOWNLOAD_FILE_NAME = "new_version.apk";

    private Context mContext;
    private DownloadManager mDownloadManager;
    private String mUrl;
    private long mDownloadId;
    private CompleteReceiver mReceiver;
    private int mCancleDownload;
    private CustomDialog mDialog;
    private CustomDialog.Builder mBuilder;
    private boolean mCancelable;

    public VersionUpdataHelper(Context context, String url){
        this(context, url, true);
    }

    public VersionUpdataHelper(Context context, String url, boolean cancelable) {
        mContext = context;
        mUrl = url;
        mCancelable = cancelable;
        showNewVersionDialog();
    }

    private void showNewVersionDialog() {
        mBuilder = new CustomDialog.Builder(mContext);
        mBuilder.setMessage("发现新版本，是否下载并更新...")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startDownload(mUrl);
                        initDownloadingDialog();
                        mDialog.show();
                        mCancleDownload = 0;
                    }
                });
        if (mCancelable) {
            mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        mDialog = mBuilder.create();
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void initDownloadingDialog() {
        mCancleDownload = 0;
        mBuilder = new CustomDialog.Builder(mContext);
        mBuilder.setMessage("软件更新中...");
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDownloadManager.remove(mDownloadId);
                dialog.dismiss();
                mCancleDownload = 1;
                unregisterUpdataReceiver();
            }
        });
        mDialog = mBuilder.create();
        mReceiver = new CompleteReceiver();
        mContext.registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void unregisterUpdataReceiver() {
        mContext.unregisterReceiver(mReceiver);
    }

    private void startDownload(String url) {
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri resource = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(resource);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);
        request.setShowRunningNotification(true);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, DOWNLOAD_FILE_NAME);
        mDownloadId = mDownloadManager.enqueue(request);
        mContext.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"),
                true, new DownloadObserver(handler, mContext, mDownloadId));
    }

    //启动安装
    private void openFile(long downloadId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Intent updateApk = new Intent(Intent.ACTION_VIEW);
            Uri downloadFileUri = mDownloadManager.getUriForDownloadedFile(downloadId);
            updateApk.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            updateApk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(updateApk);
        } else {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_DOWNLOADS + "/", DOWNLOAD_FILE_NAME);
            openFile(file, mContext);
        }
    }

    public void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String mimeType = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), mimeType);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMIMEType(File file) {
        String type = "";
        String name = file.getName();
        String fileName = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileName);
        return type;
    }

    public class CompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (mDownloadId == completeDownloadId && mCancleDownload == 0) {
                mDialog.dismiss();
                openFile(completeDownloadId);
                unregisterUpdataReceiver();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            float mDownloadSoFar = (float) msg.arg1 / (1024 * 1024);
            float mDownloadAll = (float) msg.arg2 / (1024 * 1024);

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            if (mDialog.isShowing()) {
                TextView mDownloadDialogMessageCancelTv = (TextView) mDialog.findViewById(R.id.tv_dialog_message);
                mDownloadDialogMessageCancelTv.setText("已下载" + decimalFormat.format(mDownloadSoFar) + "M，共" + decimalFormat.format(mDownloadAll) + "M");
            }
        }
    };

    public class DownloadObserver extends ContentObserver {
        private long downid;
        private Handler handler;
        private Context context;

        public DownloadObserver(Handler handler, Context context, long downid) {
            super(handler);
            this.handler = handler;
            this.downid = downid;
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(downid);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = downloadManager.query(query);
            while (cursor.moveToNext()) {
                int mDownload_so_far = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int mDownload_all = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                if (mDownload_so_far < 0) {
                    mDownload_so_far = 0;
                }
                Message message = Message.obtain();
                message.arg1 = mDownload_so_far;
                message.arg2 = mDownload_all;
                message.obj = downid;
                handler.sendMessage(message);
            }
        }
    }

    public static class CustomDialog extends Dialog {

        private TextView mMessageTv;
        private Button mPositiveBtn;
        private Button mNegativeBtn;
        private View mButtonDividerView;

        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public CustomDialog(Context context) {
            super(context);
        }

        public CustomDialog(Context context, int theme) {
            super(context, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_custome_layout);
            mMessageTv = (TextView) findViewById(R.id.tv_dialog_message);
            mPositiveBtn = (Button) findViewById(R.id.btn_dialog_positive);
            mNegativeBtn = (Button) findViewById(R.id.btn_dialog_negative);
            mButtonDividerView = findViewById(R.id.view_dialog_button_divider);

            if (message != null) {
                mMessageTv.setText(message);
            }
            if (positiveButtonText != null) {
                mPositiveBtn.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    mPositiveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(CustomDialog.this, Dialog.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                mPositiveBtn.setVisibility(View.GONE);
                mButtonDividerView.setVisibility(View.GONE);
            }

            if (negativeButtonText != null) {
                mNegativeBtn.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    mNegativeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(CustomDialog.this, Dialog.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                mNegativeBtn.setVisibility(View.GONE);
                mButtonDividerView.setVisibility(View.GONE);
            }

        }

        private void setMessage(String msg) {
            message = msg;
        }

        private void setPositiveButtonText(String text) {
            positiveButtonText = text;
        }

        private void setNegativeButtonText(String text) {
            negativeButtonText = text;
        }

        private void setOnNegativeListener(DialogInterface.OnClickListener listener) {
            negativeButtonClickListener = listener;
        }

        private void setOnPositiveListener(DialogInterface.OnClickListener listener) {
            positiveButtonClickListener = listener;
        }

        public static class Builder {
            private Context context;
            private String message;
            private String positiveButtonText;
            private String negativeButtonText;
            private OnClickListener positiveButtonClickListener;
            private OnClickListener negativeButtonClickListener;

            public Builder(Context context) {
                this.context = context;
            }

            public Builder setMessage(String message) {
                this.message = message;
                return this;
            }

            public Builder setMessage(int message) {
                this.message = context.getString(message);
                return this;
            }

            public Builder setPositiveButton(int positiveButtonText,
                                             OnClickListener listener) {
                return setPositiveButton(context.getString(positiveButtonText), listener);
            }

            public Builder setPositiveButton(String positiveButtonText,
                                             OnClickListener listener) {
                this.positiveButtonText = positiveButtonText;
                this.positiveButtonClickListener = listener;
                return this;
            }

            public Builder setNegativeButton(int negativeButtonText,
                                             OnClickListener listener) {
                return setNegativeButton(context.getString(negativeButtonText), listener);
            }

            public Builder setNegativeButton(String negativeButtonText,
                                             OnClickListener listener) {
                this.negativeButtonText = negativeButtonText;
                this.negativeButtonClickListener = listener;
                return this;
            }

            public CustomDialog create() {
                CustomDialog dialog = new CustomDialog(context);
                dialog.setCancelable(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setMessage(message);
                dialog.setNegativeButtonText(negativeButtonText);
                dialog.setPositiveButtonText(positiveButtonText);
                dialog.setOnNegativeListener(negativeButtonClickListener);
                dialog.setOnPositiveListener(positiveButtonClickListener);
                return dialog;
            }
        }
    }
}
