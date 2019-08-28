package com.liuwa.shopping.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.liuwa.shopping.R;


public class LKAlertDialog extends Dialog {
	
	private Context context;
	private String title;
	private String message;
	private boolean cancelable;
	private String positiveButtonText;
	private String negativeButtonText;
	private View contentView;
	private OnClickListener positiveButtonClickListener;
	private OnClickListener negativeButtonClickListener;

	public LKAlertDialog(Context context) {
		super(context, R.style.Dialog);
		this.context = context;

	}

	public LKAlertDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	/**
	 * Set the Dialog message from resource
	 *
	 * @param title
	 * @return
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Set the positive button resource and it's listener
	 *
	 * @param positiveButtonText
	 * @return
	 */
	public LKAlertDialog setPositiveButton(int positiveButtonText,
			OnClickListener listener) {
		this.positiveButtonText = (String) context.getText(positiveButtonText);
		this.positiveButtonClickListener = listener;
		return this;
	}

	public LKAlertDialog setPositiveButton(String positiveButtonText,
			OnClickListener listener) {
		this.positiveButtonText = positiveButtonText;
		this.positiveButtonClickListener = listener;
		return this;
	}

	public LKAlertDialog setNegativeButton(int negativeButtonText,
			OnClickListener listener) {
		this.negativeButtonText = (String) context.getText(negativeButtonText);
		this.negativeButtonClickListener = listener;
		return this;
	}

	public LKAlertDialog setNegativeButton(String negativeButtonText,
			OnClickListener listener) {
		this.negativeButtonText = negativeButtonText;
		this.negativeButtonClickListener = listener;
		return this;
	}

	public LKAlertDialog createShow() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// instantiate the dialog with the custom Theme
		View layout = inflater.inflate(R.layout.alert_dialog_layout, null);
		this.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		// set the dialog title
		
		 TextView textView=(TextView) layout.findViewById(R.id.title);
		if(title==null)
		{
			textView.setVisibility(View.GONE);
		}
		else
		{
			textView.setVisibility(View.VISIBLE);
			textView.setText(title);
		}
		// set the confirm button
		if (positiveButtonText != null) {
			((Button) layout.findViewById(R.id.positiveButton))
					.setText(positiveButtonText);
			if (positiveButtonClickListener != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								positiveButtonClickListener.onClick(LKAlertDialog.this,DialogInterface.BUTTON_POSITIVE);
							}
						});
			}
		} else {
			// if no confirm button just set the visibility to GONE
			layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
		}
		// set the cancel button
		if (negativeButtonText != null) {
			((Button) layout.findViewById(R.id.negativeButton))
					.setText(negativeButtonText);
			if (negativeButtonClickListener != null) {
				((Button) layout.findViewById(R.id.negativeButton))
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								negativeButtonClickListener.onClick(LKAlertDialog.this,DialogInterface.BUTTON_NEGATIVE);
							}
						});
			}
		} else {
			// if no confirm button just set the visibility to GONE
			layout.findViewById(R.id.negativeButton).setVisibility(
					View.GONE);
		}
		// set the content message
		if (message != null) {
			((TextView) layout.findViewById(R.id.message)).setText(message);
		} else if (contentView != null) {
			// if no message set
			// add the contentView to the dialog body

		}
		
		this.setContentView(layout);
		
		this.setCancelable(cancelable);
		
		return this;
	}
}
