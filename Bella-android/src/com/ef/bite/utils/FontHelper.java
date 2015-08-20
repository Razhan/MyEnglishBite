package com.ef.bite.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontHelper {

	public final static String FONT_OpenSans = "fonts/OpenSans.ttf";
	
	public final static String FONT_OpenSans_Italic = "fonts/OpenSans-Italic.ttf";

	public final static String FONT_Museo300 = "fonts/MuseoSlab-300.otf";
	
	public final static String FONT_Museo300_Italic = "fonts/MuseoSlab-300Italic.otf";
	
	public final static String FONT_Museo500 = "fonts/MuseoSlab-500.otf";
	
	public final static String FONT_Museo500_Italic = "fonts/MuseoSlab-500Italic.otf";
	
	/**
	 * 
	 * @param context
	 * @param root
	 * @param fontName
	 */
	public static void applyFont(final Context context, final View root,
			final String fontName) {
		try {
			if (root instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) root;
				for (int i = 0; i < viewGroup.getChildCount(); i++)
					applyFont(context, viewGroup.getChildAt(i), fontName);
			} else if (root instanceof TextView)
				((TextView) root).setTypeface(Typeface.createFromAsset(
						context.getAssets(), fontName));
			else if (root instanceof Button)
				((Button) root).setTypeface(Typeface.createFromAsset(
						context.getAssets(), fontName));
			else if (root instanceof EditText)
				((EditText) root).setTypeface(Typeface.createFromAsset(
						context.getAssets(), fontName));
		} catch (Exception e) {
			Log.e("FontHelper", String.format(
					"Error occured when trying to apply %s font for %s view",
					fontName, root), e);
			e.printStackTrace();

		}
	}
	
	/**
	 * 获得字体Typeface
	 * @param context
	 * @param fontName		指定字体名称
	 * @return
	 */
	public static Typeface getFont(Context context, String fontName){
		try{
			return Typeface.createFromAsset(context.getAssets(), fontName);
		}catch (Exception e) {
			Log.e("FontHelper", String.format(
					"Error occured when trying to apply %s font for %s view",
					fontName), e);
			e.printStackTrace();
			return null;
		}
	}
}
