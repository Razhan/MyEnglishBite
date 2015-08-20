package com.ef.bite.ui.popup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class BaseDialogFragment extends DialogFragment {
	
	private OnDismissListener mListener;
	protected AlertDialog.Builder mBuilder;
	protected Activity mActivity;
	protected LayoutInflater mInflater ;

    public BaseDialogFragment(){}

	public BaseDialogFragment(Activity activity){
		mActivity = activity;
		mBuilder = new AlertDialog.Builder(mActivity);
		mInflater = mActivity.getLayoutInflater();
	}

	public void setOnDismissListener(BaseDialogFragment.OnDismissListener onDismissListener){
		mListener = onDismissListener;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog){
		super.onDismiss(dialog);
		if(mListener!=null)
			mListener.onDismiss(dialog);
	}
	
	public interface OnDismissListener{
		void onDismiss (DialogInterface dialog);
	}
}
