package com.ef.bite.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DialogHelper
{
	public static void MessageBox(Context context, String title,
			String message, String buttonText, final Clicking click)
	{
		new AlertDialog.Builder(context)
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton(buttonText, new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					if (click != null)
					{
						click.Click();
					}
					dialog.dismiss();
				}
			})
			.show();
	}
	
	public static void ConfirmBox(Context context, String title, String message, String positiveButnText, final Clicking positiveBtnclick, 
			String negativeBtnText, final Clicking negativeBtnClick)
	{
		new AlertDialog.Builder(context).setTitle(title).setMessage(message)
		.setPositiveButton(positiveButnText, new  DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(positiveBtnclick != null)
				{
					positiveBtnclick.Click();
				}
				//new RedeemPrizesTask().execute();
			}
		}).setNegativeButton(negativeBtnText, new  DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(negativeBtnClick != null)
				{
					negativeBtnClick.Click();
				}
			}
		}).show();
	}
	
	public interface Clicking
	{
		public void Click();
	}
	

	
}
