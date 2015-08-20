package com.ef.bite.utils;

import java.util.ArrayList;

import cn.sharesdk.framework.statistics.NewAppReceiver;

import com.ef.bite.R;
import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

/**
 * 将
 * 
 * @author Allen
 * 
 */
public class HighLightStringHelper {

	public final static int HIGH_LIGHT_COLOR = R.color.bella_color_orange_light;
	public final static int BOLD_COLOR = R.color.bella_color_black_dark;
	public final static String HIGH_LIGHT_PREFIX = "<h>";
	public final static String HIGH_LIGHT_SUFFIX = "</h>";

	/**
	 * 获得红色高亮的文本格式，用<h></h>表示高亮部分 <h>Can I get</h> some butter for my bread
	 * 
	 * @param normalText
	 * @return
	 */
	public static SpannableStringBuilder getHighLightString(Context context,
			String normalText) {
		if (normalText == null)
			return null;
		try {
			List<Highlight> highlightList = new ArrayList<Highlight>();
			int startPos = normalText.indexOf(HIGH_LIGHT_PREFIX);
			while (startPos >= 0) {
				normalText = normalText.replaceFirst(HIGH_LIGHT_PREFIX, "");
				int endPos = normalText.indexOf(HIGH_LIGHT_SUFFIX);
				if (endPos >= 0) {
					normalText = normalText.replaceFirst(HIGH_LIGHT_SUFFIX, "");
					// 找到第一个<h> </h>的span
					Highlight highlight = new Highlight();
					highlight.start = startPos;
					highlight.end = endPos;
					highlightList.add(highlight);
					startPos = normalText.indexOf(HIGH_LIGHT_PREFIX);
				} else
					break;
			}

			SpannableStringBuilder styled = new SpannableStringBuilder(
					normalText);
			for (int index = 0; index < highlightList.size(); index++) {
				styled.setSpan(new ForegroundColorSpan(context.getResources()
						.getColor(HIGH_LIGHT_COLOR)),
						highlightList.get(index).start, highlightList
								.get(index).end,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			return styled;
		} catch (Exception ex) {
			ex.printStackTrace();
			SpannableStringBuilder styled = new SpannableStringBuilder(
					normalText);
			return styled;
		}
	}

	static class Highlight {
		int start;
		int end;
	}

	/**
	 * 在文本中对指定关键字进行高亮
	 * 
	 * @param context
	 * @param normalText
	 * @param key
	 * @return
	 */
	public static SpannableStringBuilder getHighLightString(Context context,
			String normalText, String key) {
		if (normalText == null)
			return null;
		try {
			SpannableStringBuilder styled = new SpannableStringBuilder(
					normalText);
			if (key != null && !key.isEmpty()) {
				int startPos = normalText.indexOf(key);
				if (startPos >= 0)
					styled.setSpan(new ForegroundColorSpan(context
							.getResources().getColor(HIGH_LIGHT_COLOR)),
							startPos, startPos + key.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			return styled;
		} catch (Exception ex) {
			ex.printStackTrace();
			SpannableStringBuilder styled = new SpannableStringBuilder(
					normalText);
			return styled;
		}
	}

	/**
	 * 获得加黑加粗的文本格式，用<h></h>表示加粗部分 <h>Can I get</h> some butter for my bread
	 * 
	 * @param normalText
	 * @return
	 */
	public static SpannableStringBuilder getBoldString(Context context,
			String normalText) {
		if (normalText == null)
			return null;
		try {
			List<Highlight> highlightList = new ArrayList<Highlight>();
			int startPos = normalText.indexOf(HIGH_LIGHT_PREFIX);
			while (startPos >= 0) {
				normalText = normalText.replaceFirst(HIGH_LIGHT_PREFIX, "");
				int endPos = normalText.indexOf(HIGH_LIGHT_SUFFIX);
				if (endPos >= 0) {
					normalText = normalText.replaceFirst(HIGH_LIGHT_SUFFIX, "");
					// 找到第一个<h> </h>的span
					Highlight highlight = new Highlight();
					highlight.start = startPos;
					highlight.end = endPos;
					highlightList.add(highlight);
					startPos = normalText.indexOf(HIGH_LIGHT_PREFIX);
				} else
					break;
			}
			SpannableStringBuilder styled = new SpannableStringBuilder(
					normalText);
			for (int index = 0; index < highlightList.size(); index++) {
				styled.setSpan(new ForegroundColorSpan(context.getResources()
						.getColor(BOLD_COLOR)), highlightList.get(index).start,
						highlightList.get(index).end,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				styled.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
						highlightList.get(index).start,
						highlightList.get(index).end,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			return styled;
		} catch (Exception ex) {
			ex.printStackTrace();
			SpannableStringBuilder styled = new SpannableStringBuilder(
					normalText);
			return styled;
		}
	}

	/**
	 * 设置文本对指定关键字进行加黑加粗
	 * 
	 * @param context
	 * @param normalText
	 * @param key
	 * @return
	 */
	public static SpannableStringBuilder getBoldString(Context context,
			String normalText, String key) {
		if (normalText == null)
			return null;
		try {
			SpannableStringBuilder styled = new SpannableStringBuilder(
					normalText);
			if (key != null && !key.isEmpty()) {
				int startPos = normalText.indexOf(key);
				if (startPos >= 0) {
					styled.setSpan(new ForegroundColorSpan(context
							.getResources().getColor(BOLD_COLOR)), startPos,
							startPos + key.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					styled.setSpan(
							new StyleSpan(android.graphics.Typeface.BOLD),
							startPos, startPos + key.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			return styled;
		} catch (Exception ex) {
			ex.printStackTrace();
			SpannableStringBuilder styled = new SpannableStringBuilder(
					normalText);
			return styled;
		}
	}

	/**
	 * 获得高亮的可点击的SpannableStringBuilder
	 * 
	 * @param context
	 * @param normalText
	 * @return
	 */
	public static SpannableStringBuilder getClickableHighLightString(
			final Context context, String normalText,
			final View.OnClickListener click) {
		if (normalText == null)
			return null;
		try {
			List<Highlight> highlightList = new ArrayList<Highlight>();
			int startPos = normalText.indexOf(HIGH_LIGHT_PREFIX);
			while (startPos >= 0) {
				normalText = normalText.replaceFirst(HIGH_LIGHT_PREFIX, "");
				int endPos = normalText.indexOf(HIGH_LIGHT_SUFFIX);
				if (endPos >= 0) {
					normalText = normalText.replaceFirst(HIGH_LIGHT_SUFFIX, "");
					// 找到第一个<h> </h>的span
					Highlight highlight = new Highlight();
					highlight.start = startPos;
					highlight.end = endPos;
					highlightList.add(highlight);
					startPos = normalText.indexOf(HIGH_LIGHT_PREFIX);
				} else
					break;
			}
			SpannableStringBuilder styled = new SpannableStringBuilder(
					normalText);
			for (int index = 0; index < highlightList.size(); index++) {
				// styled.setSpan(new
				// ForegroundColorSpan(context.getResources().getColor(HIGH_LIGHT_COLOR)),highlightList.get(index).start,
				// highlightList.get(index).end,
				// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				styled.setSpan(
						new ClickableSpan() {

							@Override
							public void onClick(View widget) {
								if (click != null)
									click.onClick(widget);
							}

							@Override
							public void updateDrawState(TextPaint ds) {
								super.updateDrawState(ds);
								ds.setUnderlineText(false);
								ds.setColor(context.getResources().getColor(
										HIGH_LIGHT_COLOR));
							}

						}, highlightList.get(index).start,
						highlightList.get(index).end,
						Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
			return styled;
		} catch (Exception ex) {
			ex.printStackTrace();
			SpannableStringBuilder styled = new SpannableStringBuilder(
					normalText);
			return styled;
		}
	}
}
