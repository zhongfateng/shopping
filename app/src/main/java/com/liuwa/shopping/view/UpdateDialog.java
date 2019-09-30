package com.liuwa.shopping.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.liuwa.shopping.R;
import android.view.View.OnClickListener;

/**
 * Created by ZFT on 2019/9/29.
 */

public class UpdateDialog extends Dialog{
    private TextView tvTitle;
    private TextView tvMsg;
    private TextView tvLeft;
    private TextView tvRight;
    private View.OnClickListener onDefaultClickListener;
    private View.OnClickListener onLeftListener;
    private View.OnClickListener onRightListener;
    private String mTitle;
    private String mMessage;
    private String leftText;
    private String rightText;

    private UpdateDialog(Context context) {
        super(context, R.style.Dialog);
        this.onDefaultClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDialog.this.cancel();
            }
        };
        this.onLeftListener = this.onDefaultClickListener;
        this.onRightListener = this.onDefaultClickListener;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_update_dialog_layout);
        this.tvTitle = (TextView)this.findViewById(R.id.tv_title);
        this.tvMsg = (TextView)this.findViewById(R.id.tv_message);
        this.tvLeft = (TextView)this.findViewById(R.id.tv_left);
        this.tvRight = (TextView)this.findViewById(R.id.tv_right);
    }

    public void show() {
        super.show();
        this.show(this);
    }

    private void show(UpdateDialog mDialog) {
        if(!TextUtils.isEmpty(mDialog.mTitle)) {
            mDialog.tvTitle.setText(mDialog.mTitle);
        }

        if(!TextUtils.isEmpty(mDialog.mMessage)) {
            mDialog.tvMsg.setText(mDialog.mMessage);
        }

        mDialog.tvRight.setOnClickListener((View.OnClickListener) mDialog.onRightListener);
        if(!TextUtils.isEmpty(mDialog.rightText)) {
            mDialog.tvRight.setText(mDialog.rightText);
        }

        mDialog.tvLeft.setOnClickListener((View.OnClickListener) mDialog.onLeftListener);
        if(!TextUtils.isEmpty(mDialog.leftText)) {
            mDialog.tvLeft.setText(mDialog.leftText);
        }

    }

    public static class Builder {
        private UpdateDialog mDialog;

        public Builder(Context context) {
            this.mDialog = new UpdateDialog(context);
        }

        public UpdateDialog.Builder setTitle(String title) {
            this.mDialog.mTitle = title;
            return this;
        }

        public UpdateDialog.Builder setMessage(String msg) {
            this.mDialog.mMessage = msg;
            return this;
        }

        public UpdateDialog.Builder setLeftClick(View.OnClickListener onClickListener) {
            this.mDialog.onLeftListener = onClickListener;
            return this;
        }

        public UpdateDialog.Builder setLeftClick(String btnText, View.OnClickListener onClickListener) {
            this.mDialog.leftText = btnText;
            this.mDialog.onLeftListener = onClickListener;
            return this;
        }

        public UpdateDialog.Builder setRightClick(View.OnClickListener onClickListener) {
            this.mDialog.onRightListener = onClickListener;
            return this;
        }

        public UpdateDialog.Builder setRightClick(String btnText, View.OnClickListener onClickListener) {
            this.mDialog.rightText = btnText;
            this.mDialog.onRightListener = onClickListener;
            return this;
        }

        public UpdateDialog.Builder setCancelable(boolean cancelable) {
            this.mDialog.setCancelable(cancelable);
            return this;
        }

        public UpdateDialog.Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.mDialog.setOnCancelListener(onCancelListener);
            return this;
        }

        public UpdateDialog.Builder setOnDismissListener(OnDismissListener onDismissListener) {
            this.mDialog.setOnDismissListener(onDismissListener);
            return this;
        }

        public UpdateDialog create() {
            return this.mDialog;
        }
    }
}
