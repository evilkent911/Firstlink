package com.trinerva.icrm.utility;

import asia.firstlink.icrm.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class GuiUtility {
	public static void alert(Context context, String strTitle, String strMsg, String strButton) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(strTitle).setMessage(strMsg).setCancelable(false).setPositiveButton(strButton, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void alert(Context context, String strTitle, String strMsg, int iGravity, String strButton, DialogInterface.OnClickListener oPositionHandler, String strNegativeButton, DialogInterface.OnClickListener oNegativeHandler) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(strTitle);
		builder.setMessage(strMsg);
		builder.setPositiveButton(strButton, oPositionHandler);
		if (!strNegativeButton.equals("")) {
			builder.setCancelable(true);
			if (oNegativeHandler != null) {
				builder.setNegativeButton(strNegativeButton, oNegativeHandler);
			}
		}
		AlertDialog alert = builder.create();
		alert.show();
		TextView messageView = (TextView) alert.findViewById(android.R.id.message);
		messageView.setGravity(iGravity);
	}
	
	public static Dialog getLoadingDialog(Context context, boolean bCancelable, String strText) {
		Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(bCancelable);
		dialog.setContentView(R.layout.dialog);
		if (strText != null) {
			TextView textLabel = (TextView) dialog.findViewById(R.id.text_label);
			textLabel.setText(strText);
		}
		dialog.show();
		return dialog;
	}
	
	public static void makeToast(Context context, String strMessage, int iDuration) {
		Toast.makeText(context, strMessage, iDuration).show();
	}
}
